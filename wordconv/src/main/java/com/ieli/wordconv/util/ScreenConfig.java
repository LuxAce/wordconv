package com.ieli.wordconv.util;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class ScreenConfig {

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private static int screenWidth = screenSize.width;

	private static int screenHeight = screenSize.height;

	public static void setFrameSizeMax(JFrame frame) {
		frame.setPreferredSize(ScreenConfig.screenSize);
	}

	public static void setDialogSizeMax(JDialog dialog) {
		dialog.setPreferredSize(ScreenConfig.screenSize);
	}

	public static void setFrameSizeCustom(JFrame frame, int paddLeft, int paddTop) {
		frame.setSize(ScreenConfig.screenWidth - paddLeft, ScreenConfig.screenHeight - paddTop);
	}

	public static void setFrameSizeCustom(Applet applet, int paddLeft, int paddTop) {
		applet.setSize(ScreenConfig.screenWidth - paddLeft, ScreenConfig.screenHeight - paddTop);
	}

	public static void setFrameSizeNormal(JFrame frame) {
		frame.setSize(ScreenConfig.screenWidth - 150, ScreenConfig.screenHeight - 150);
	}

	public static void setFramePositionCenter(JFrame frame, Component c) {
		frame.setLocationRelativeTo(c);
	}

	public static void setDialogSizeCustom(JDialog dialog, int paddLeft, int paddTop) {
		dialog.setSize(ScreenConfig.screenWidth - paddLeft, ScreenConfig.screenHeight - paddTop);
	}

	public static void setDialogPositionCenter(JDialog dialog, Component c) {
		dialog.setLocationRelativeTo(c);
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenHeight(int screenHeight) {
		ScreenConfig.screenHeight = screenHeight;
	}

	public static Dimension getScreenSize() {
		return screenSize;
	}

	public static void setScreenSize(Dimension screenSize) {
		ScreenConfig.screenSize = screenSize;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static void setScreenWidth(int screenWidth) {
		ScreenConfig.screenWidth = screenWidth;
	}

}
