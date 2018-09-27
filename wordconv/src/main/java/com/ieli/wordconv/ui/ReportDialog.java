package com.ieli.wordconv.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.EditorKit;

import org.apache.log4j.Logger;

import com.ieli.wordconv.util.ScreenConfig;
import com.ieli.wordconv.util.StackTraceHandler;

import net.miginfocom.swing.MigLayout;

public class ReportDialog extends JDialog {

	final static Logger logger = Logger.getLogger(ReportDialog.class);

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JEditorPane infoTextPane;
	private EditorKit editor;

	/**
	 * Create the dialog.
	 */
	public ReportDialog() {

		setTitle("Export Report");
		ScreenConfig.setDialogSizeCustom(this, 400, 400);
		ScreenConfig.setDialogPositionCenter(this, null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

		JScrollPane infoScrollPane = new JScrollPane();
		contentPanel.add(infoScrollPane, "cell 0 0,grow");

		infoTextPane = new JTextPane();
		infoTextPane.setContentType("text/html");
		infoScrollPane.setViewportView(infoTextPane);
		editor = infoTextPane.getEditorKit();

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infoTextPane.setText("");
			}
		});
		buttonPane.add(btnClear);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	}

	public void appendText(String data) {

		try {
			StringReader reader = new StringReader(data);
			editor.read(reader, infoTextPane.getDocument(), infoTextPane.getDocument().getLength());

		} catch (Exception e) {
			logger.error(StackTraceHandler.getErrString(e));
		}
	}
}
