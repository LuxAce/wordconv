package com.ieli.wordconv;


import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ieli.wordconv.ui.WToXMLMainFrame;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;

/**
 * Hello world!
 *
 */
public class RunDoc2XML {
	public static void main(String[] args) {
		String appId = "WToXMLMainFrameUID";
		boolean alreadyRunning;
		try {
			JUnique.acquireLock(appId);
			alreadyRunning = false;
		} catch (AlreadyLockedException e) {
			alreadyRunning = true;
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		if (!alreadyRunning) {

			WToXMLMainFrame teMainFrame = new WToXMLMainFrame();
			teMainFrame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "The application is already running!!!");
		}

	}
}
