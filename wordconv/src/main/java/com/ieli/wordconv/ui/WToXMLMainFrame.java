package com.ieli.wordconv.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.ieli.wordconv.model.BookXML;
import com.ieli.wordconv.service.IWordParser;
import com.ieli.wordconv.service.IXMLFormatter;
import com.ieli.wordconv.service.impl.WordParserImpl;
import com.ieli.wordconv.service.impl.XMLFormatterImpl;
import com.ieli.wordconv.util.CustomTableModel;
import com.ieli.wordconv.util.OSDetector;
import com.ieli.wordconv.util.ScreenConfig;
import com.ieli.wordconv.util.StackTraceHandler;
import com.ieli.wordconv.util.StaticData;

import net.miginfocom.swing.MigLayout;

public class WToXMLMainFrame extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(WToXMLMainFrame.class);
	private JTextField dirFileTxtFld;
	private JTable startingStyleTbl;
	private JTable pageTypesTbl;
	private JProgressBar progressBar;
	private Task task;

	public String userDir = null;

	private JButton btnLoadFile;

	private List<File> finalFiles;

	IWordParser iWordParser = new WordParserImpl();
	IXMLFormatter ixmlFormatter = new XMLFormatterImpl();

	private Properties prop;

	private ReportDialog reportDialog;

	/**
	 * Create the frame.
	 */
	public WToXMLMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Word To XML");

		ScreenConfig.setFrameSizeCustom(this, 200, 200);
		ScreenConfig.setFramePositionCenter(this, null);

		reportDialog = new ReportDialog();

		JPanel mainPnl = new JPanel();
		getContentPane().add(mainPnl, BorderLayout.CENTER);
		mainPnl.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));

		JPanel propertiesPnl = new JPanel();
		mainPnl.add(propertiesPnl, "cell 0 0,grow");
		propertiesPnl.setLayout(new MigLayout("", "[grow][grow]", "[grow]"));

		JScrollPane startingStylePane = new JScrollPane();
		propertiesPnl.add(startingStylePane, "cell 0 0,grow");

		startingStyleTbl = new JTable();
		startingStylePane.setViewportView(startingStyleTbl);

		JScrollPane pageTypesPane = new JScrollPane();
		propertiesPnl.add(pageTypesPane, "cell 1 0,grow");

		pageTypesTbl = new JTable();
		pageTypesPane.setViewportView(pageTypesTbl);

		loadTableData();

		JPanel loadPnl = new JPanel();
		mainPnl.add(loadPnl, "cell 0 1,grow");
		loadPnl.setLayout(new MigLayout("", "[grow]", "[][][][grow]"));

		JPanel loadGeneratePnl = new JPanel();
		loadGeneratePnl.setBorder(
				new TitledBorder(null, "Load & Generate Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		loadPnl.add(loadGeneratePnl, "cell 0 0,grow");
		loadGeneratePnl.setLayout(new MigLayout("", "[][][]", "[]"));

		btnLoadFile = new JButton("Load File(s)");
		btnLoadFile.setToolTipText("Select a file, multiple files or a directory");
		btnLoadFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				FileDialog filesChooser = new FileDialog(WToXMLMainFrame.this, "Select file(s)", FileDialog.LOAD);
				filesChooser.setMultipleMode(true);
				if (userDir == null) {
					userDir = System.getProperty("user.home");
				}
				filesChooser.setDirectory(userDir);
				filesChooser.setVisible(true);
				if (filesChooser.getFiles() != null) {

					finalFiles = new ArrayList<File>();

					File[] files = filesChooser.getFiles();

					if (files.length == 1) {
						finalFiles.add(files[0]);
						userDir = filesChooser.getFiles()[0].getAbsolutePath();
						dirFileTxtFld.setText(userDir);
					} else {
						String fileNames = "";
						for (File file : files) {
							fileNames += file.getName() + "/";
							finalFiles.add(file);
						}
						userDir = filesChooser.getFiles()[0].getParent();
						dirFileTxtFld.setText(userDir + "(" + fileNames + ")");
					}

					btnLoadFile.setEnabled(false);
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					task = new Task(finalFiles);
					task.addPropertyChangeListener(WToXMLMainFrame.this);
					task.execute();

				}

			}
		});
		loadGeneratePnl.add(btnLoadFile, "cell 0 0");

		JPanel outputDirPnl = new JPanel();
		outputDirPnl
				.setBorder(new TitledBorder(null, "Input File(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		loadPnl.add(outputDirPnl, "cell 0 1,grow");
		outputDirPnl.setLayout(new MigLayout("", "[grow,fill]", "[]"));

		dirFileTxtFld = new JTextField();
		outputDirPnl.add(dirFileTxtFld, "cell 0 0");
		dirFileTxtFld.setEnabled(false);
		dirFileTxtFld.setColumns(10);

		JPanel outputPnl = new JPanel();
		outputPnl.setBorder(new TitledBorder(null, "Output Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		loadPnl.add(outputPnl, "cell 0 2,grow");
		outputPnl.setLayout(new MigLayout("", "[][][]", "[][]"));

		JButton btnOpenDestination = new JButton("Open Destination");
		btnOpenDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (finalFiles != null) {
					if (!finalFiles.isEmpty()) {
						openDestination(finalFiles.get(0));
					}
				} else {
					JOptionPane.showMessageDialog(WToXMLMainFrame.this, "Please load files first!!!");
				}
			}
		});
		outputPnl.add(btnOpenDestination, "cell 0 0");

		JButton btnShowReport = new JButton("Show Report");
		btnShowReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				reportDialog.setVisible(true);
			}
		});
		outputPnl.add(btnShowReport, "cell 1 0");

		JPanel exitPnl = new JPanel();
		loadPnl.add(exitPnl, "cell 0 3,grow");
		exitPnl.setLayout(new BorderLayout());

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(500, 20));
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		exitPnl.add(progressBar, BorderLayout.WEST);
		exitPnl.add(btnExit, BorderLayout.EAST);
	}

	public static boolean openDestination(File file) {
		try {
			if (OSDetector.isWindows()) {
				Runtime.getRuntime().exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", file.getParent() });
				return true;
			} else if (OSDetector.isLinux() || OSDetector.isMac()) {
				Runtime.getRuntime().exec(new String[] { "/usr/bin/open", file.getParent() });
				return true;
			} else {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error(StackTraceHandler.getErrString(e));
			return false;
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	private void loadTableData() {
		prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("./config.properties");

			// load a properties file
			prop.load(input);

			Set<Object> propNames = prop.keySet();

			Object[][] stylesData = { { "" } };

			if (propNames.size() != 0) {
				stylesData = new Object[1][propNames.size()];

				int rowIndex = 0;
				for (Object propName : propNames) {
					stylesData[rowIndex] = new Object[] { propName.toString() };
					rowIndex++;
				}

			}

			Object[][] typesData = { { "" } };

			if (propNames.size() != 0) {
				typesData = new Object[1][propNames.size()];

				int rowIndex = 0;
				for (Object propName : propNames) {
					typesData[rowIndex] = new Object[] { prop.get(propName) };
					rowIndex++;
				}

			}

			startingStyleTbl.setModel(new CustomTableModel(stylesData, new String[] { "Starting Style" }));
			pageTypesTbl.setModel(new CustomTableModel(typesData, new String[] { "Page Type" }));

		} catch (IOException ex) {
			logger.error(StackTraceHandler.getErrString(ex));
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(StackTraceHandler.getErrString(e));
				}
			}
		}

	}

	class Task extends SwingWorker<Void, Void> {

		private List<File> files;

		public Task(List<File> files) {
			this.files = files;
		}

		@Override
		public Void doInBackground() {
			setProgress(0);
			generateXMLFiles(files);
			setProgress(0);
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			btnLoadFile.setEnabled(true);
			files = null;
			dirFileTxtFld.setText("");
			setCursor(null);
		}
	}

	public void generateXMLFiles(List<File> files) {

		for (File file : files) {
			progressBar.setString("Generating XML for file: " + file.getName());
			reportDialog.appendText("<br /><h3><u>Parsing file: " + file.getAbsolutePath() + "</u></h3>");
			//
			try {
				// String output =
				// iWordParser.getFileOutputData(file.getAbsolutePath(),
				// new ArrayList<Object>(prop.keySet()), new
				// ArrayList<Object>(prop.values()), progressBar,
				// reportDialog);
				//

				List<BookXML> xmlElements = iWordParser.getXMLFromWord(file.getAbsolutePath());
				List<BookXML> formattedXMLElements = ixmlFormatter.formatXMLList(xmlElements);

				FileDialog saveFileChooser = new FileDialog(WToXMLMainFrame.this, "Save your new XML file(s)",
						FileDialog.SAVE);
				saveFileChooser.setDirectory(userDir);
				saveFileChooser.setFile(file.getAbsolutePath().replaceAll(".docx", ".xml"));
				saveFileChooser.setVisible(true);
				if (saveFileChooser.getDirectory() != null && saveFileChooser.getFile() != null) {

					FileWriter writer = new FileWriter(
							saveFileChooser.getDirectory() + StaticData.OS_FILE_SEP + saveFileChooser.getFile());
					formattedXMLElements.add(new BookXML("", "", "</book>"));
					for (BookXML bookXML : formattedXMLElements) {
						writer.write(bookXML.toString());
					}
					writer.close();

				}

			} catch (InvalidFormatException e) {
				logger.error(StackTraceHandler.getErrString(e));
			} catch (IOException e) {
				logger.error(StackTraceHandler.getErrString(e));
			}

			reportDialog.appendText(
					"<br /><h3 style='color:green;'>Done parsing file: " + file.getAbsolutePath() + "</u></h3>");
			reportDialog.appendText("<br /><hr>");
		}

		progressBar.setString("Export finished. Thank you");
	}

}
