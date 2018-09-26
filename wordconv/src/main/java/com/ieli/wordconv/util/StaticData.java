package com.ieli.wordconv.util;

import java.util.ArrayList;
import java.util.List;

public class StaticData {

	public static final List<String> IMAGE_STYLES = new ArrayList<String>();

	static {
		IMAGE_STYLES.add("Image");
		IMAGE_STYLES.add("Image1");
		IMAGE_STYLES.add("Image2");
		IMAGE_STYLES.add("Image3");
		IMAGE_STYLES.add("Image4");
		IMAGE_STYLES.add("Image5");
		IMAGE_STYLES.add("Image6");
	}

	public static final String OS_NEW_LINE = System.getProperty("line.separator");
	public static final String OS_FILE_SEP = System.getProperty("file.separator");
}
