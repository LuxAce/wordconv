package com.ieli.wordconv.util;

public class StackTraceHandler {

	public static String getErrString(Exception e) {

		String errMsg = "";

		for (StackTraceElement el : e.getStackTrace()) {
			errMsg += el.toString() + "\n";
		}

		return errMsg;
	}
}
