package com.ieli.wordconv.util;

import java.util.ArrayList;
import java.util.List;

public class StaticData {

	public static final List<String> FILE_EXT = new ArrayList<String>();

	public static final List<String> IMAGE_STYLES = new ArrayList<String>();

	static {
		IMAGE_STYLES.add("Image");
		IMAGE_STYLES.add("Image1");
		IMAGE_STYLES.add("Image2");
		IMAGE_STYLES.add("Image3");
		IMAGE_STYLES.add("Image4");
		IMAGE_STYLES.add("Image5");
		IMAGE_STYLES.add("Image6");

		FILE_EXT.add("docx");
	}

	public static final String OS_NEW_LINE = System.getProperty("line.separator");
	public static final String OS_FILE_SEP = System.getProperty("file.separator");

	public static final String HTML_H4_OPEN = "<h4>";
	public static final String HTML_H4_CLOSE = "</h4>";
	
	public static final String HTML_ERROR_OPEN = "<h3 style=\"color:red;\">";
	public static final String HTML_ERROR_CLOSE = "</h3>";
	
	public static final String HTML_WARN_OPEN = "<h3 style=\"color:#ff9966;\">";
	public static final String HTML_WARN_CLOSE = "</h3>";

}
