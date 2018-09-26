package com.ieli.wordconv.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.ieli.wordconv.model.BookXML;
import com.ieli.wordconv.service.IWordParser;
import com.ieli.wordconv.util.StaticData;

public class WordParserImpl implements IWordParser {

	public List<BookXML> getXMLFromWord(String inputFile) throws InvalidFormatException, IOException {

		FileInputStream fis = new FileInputStream(inputFile);
		XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
		List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

		List<BookXML> xmlElements = new ArrayList<BookXML>();
		xmlElements.add(new BookXML("",
				"<?xml version=\"1.0\"  encoding=\"UTF-8\" standalone=\"yes\"?>" + StaticData.OS_NEW_LINE, ""));
		xmlElements.add(new BookXML("<book>" + StaticData.OS_NEW_LINE, "", ""));

		String style = "";
		String text = "";

		for (XWPFParagraph par : paragraphList) {

			boolean noImages = false;
			if (par.getStyle() != null) {

				style = par.getStyle();

			} else {
				style = "normal";
			}
			text = par.getParagraphText();

			List<XWPFRun> runs = par.getRuns();
			if (!runs.isEmpty()) {
				for (XWPFRun run : runs) {

					List<XWPFPicture> pics = run.getEmbeddedPictures();
					for (XWPFPicture pic : pics) {
						style = par.getStyle();
						if (style == null) {
							style = "normal";
						}
						String desc = pic.getCTPicture().getNvPicPr().getCNvPr().getDescr();
						if (!desc.equals("")) {
							text = desc.substring(desc.lastIndexOf(':')).replace(":", "");
						}
						if (text.trim().equals("")) {
							text = pic.getCTPicture().getNvPicPr().getCNvPr().getName();
						}
						text = "<Img href=\"images/" + FilenameUtils.removeExtension(text) + ".tif\" />";
						noImages = true;
					}
				}
			}

			if (!(text.trim().equals("") && !noImages)) {
				xmlElements.add(new BookXML("<" + style + ">" + StaticData.OS_NEW_LINE, text + StaticData.OS_NEW_LINE,
						"</" + style + ">" + StaticData.OS_NEW_LINE));
			}
		}
		xmlElements.add(new BookXML("", "", "</book>"));

		xdoc.close();

		return xmlElements;
	}

}
