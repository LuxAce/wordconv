package com.ieli.wordconv.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.ieli.wordconv.service.IWordParser;
import com.ieli.wordconv.util.StaticData;

public class WordParserImpl implements IWordParser {

	public List<String> getXMLFromWord(String inputFile) throws InvalidFormatException, IOException {

		FileInputStream fis = new FileInputStream(inputFile);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

		List<String> xmlElements = new ArrayList<String>();
		xmlElements.add("<?xml version=\"1.0\" standalone=\"yes\"?>" + StaticData.OS_NEW_LINE);
		xmlElements.add("<books>" + StaticData.OS_NEW_LINE);

		String style = "";
		String text = "";

		for (XWPFParagraph par : paragraphList) {

			boolean noImages = false;
			if (par.getStyle() != null) {

				style = par.getStyle();

			} else {
				style = "normal";
			}
			text = par.getText();

			List<XWPFRun> runs = par.getRuns();
			if (!runs.isEmpty()) {
				for (XWPFRun run : runs) {

					List<XWPFPicture> pics = run.getEmbeddedPictures();
					for (XWPFPicture pic : pics) {
						style = par.getStyle();
						text = pic.getCTPicture().getNvPicPr().getCNvPr().getDescr().substring(text.lastIndexOf(':'));
						if (text.trim().equals("")) {
							text = pic.getCTPicture().getNvPicPr().getCNvPr().getName();
						}
						text = "images/" + text.substring(text.lastIndexOf('.')) +  ".tif";
						noImages = true;
					}
				}
			}

			if (!(text.trim().equals("") && !noImages)) {
				xmlElements.add("<" + style + ">" + StaticData.OS_NEW_LINE + text + StaticData.OS_NEW_LINE + "<" + style
						+ "/>" + StaticData.OS_NEW_LINE);
			}
		}
		xmlElements.add("</books>");
		xdoc.close();
		return xmlElements;
	}

}
