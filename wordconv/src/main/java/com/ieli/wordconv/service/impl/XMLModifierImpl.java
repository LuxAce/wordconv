package com.ieli.wordconv.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ieli.wordconv.model.BookXML;
import com.ieli.wordconv.service.IXMLModifier;
import com.ieli.wordconv.util.StaticData;

public class XMLModifierImpl implements IXMLModifier {

	/**
	 * Modification Logic: 1- If there is only one Trigger tag, then open a
	 * wrapper tag above it and close it at the end of the document.
	 * 
	 * 2- If there are multiple tags: Find the first style, and open a type tag
	 * above it, loop till you find the second style, if found close the wrapper
	 * tag and open the new type tag and so on, till there are no other styles.
	 */
	public List<BookXML> modifyXMLList(List<BookXML> booksXMLList, List<String> styles, List<String> types) {

		if (!styles.isEmpty()) {

			if (styles.size() == 1) {

				addSingleStyle(booksXMLList, styles, types);

			} else {

				addMultiStyles(booksXMLList, styles, types);
			}

		}

		return booksXMLList;
	}

	private void addSingleStyle(List<BookXML> booksXMLList, List<String> styles, List<String> types) {
		for (int i = 0; i < booksXMLList.size(); i++) {

			BookXML currBookXML = booksXMLList.get(i);

			// Get current book xml start tag
			String bookStartTag = currBookXML.getTagStart().trim().replaceAll("<", "").replaceAll(">", "")
					.replaceAll("/", "");

			if (bookStartTag.equals(styles.get(0))) {

				BookXML openTagBookXML = new BookXML("<" + types.get(0) + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(booksXMLList.indexOf(currBookXML), openTagBookXML);

				BookXML newEndBookXML = new BookXML("</" + types.get(0) + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(newEndBookXML);
				break;
			}
		}
	}

	private void addMultiStyles(List<BookXML> booksXMLList, List<String> styles, List<String> types) {

		List<String> foundTags = new ArrayList<String>();

		for (int j = 0; j < styles.size(); j++) {

			for (int i = 0; i < booksXMLList.size(); i++) {

				BookXML currBookXML = booksXMLList.get(i);

				String bookStartTag = currBookXML.getTagStart().trim().replaceAll("<", "").replaceAll(">", "")
						.replaceAll("/", "");

				if (bookStartTag.equals(styles.get(j))) {
					if (j == 0) {
						foundTags.add(i + ":" + bookStartTag + ":" + types.get(j) + ":" + types.get(j));

					} else {
						foundTags.add(i + ":" + bookStartTag + ":" + types.get(j) + ":" + types.get(j - 1));
					}
				}
			}
		}

		List<String> triggerTags = new ArrayList<String>();

		for (int i = 0; i < foundTags.size();) {

			String currEl = foundTags.get(i);
			String nextEl = null;
			if (i < foundTags.size() - 1) {

				nextEl = foundTags.get(i + 1);

				if (currEl.split(":")[1].equals(nextEl.split(":")[1])) {
					triggerTags.add(currEl);
					i = getLastSimilarElement(currEl, foundTags) + 1;
				} else {
					triggerTags.add(currEl);
					i++;
				}
			} else {
				break;
			}

		}

		/**
		 * 2:T-booktitle-image:template-booktitle:template-booktitle
		 * 4:T-copyright:template-copyright:template-booktitle
		 * 6:T-Chap-Num:template-chapt-number:template-copyright
		 * 8:1-column-flow:template-1-col:template-chapt-number
		 * 85:R-1p-Title:template-recipe-1p:template-1-col
		 * 
		 * 
		 */
		int index = 0;
		int incrementIndex = 0;
		for (String s : triggerTags) {

			if (index == 0) {
				String startTag = s.split(":")[2];
				int boookIndex = Integer.valueOf(s.split(":")[0]);
				BookXML firstBookXML = new BookXML("<" + startTag + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(boookIndex, firstBookXML);
				incrementIndex++;
			} else {

				String endTag = s.split(":")[3];
				int boookIndex = Integer.valueOf(s.split(":")[0]);
				BookXML endBookXML = new BookXML("</" + endTag + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(boookIndex + incrementIndex, endBookXML);
				incrementIndex++;

				String newStartTag = s.split(":")[2];
				BookXML startBookXML = new BookXML("<" + newStartTag + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(boookIndex + incrementIndex, startBookXML);
				incrementIndex++;

			}

			index++;

			if (index == triggerTags.size()) {
				String lastTag = s.split(":")[2];
				BookXML lastBookXML = new BookXML("</" + lastTag + ">" + StaticData.OS_NEW_LINE, "", "");
				booksXMLList.add(booksXMLList.size(), lastBookXML);
			}
		}

	}

	private int getLastSimilarElement(String el, List<String> data) {
		int lastIndex = 0;

		int mainIndex = 0;

		for (String s : data) {

			if (s.split(":")[1].equals(el.split(":")[1])) {
				lastIndex = mainIndex;
			}

			mainIndex++;
		}

		return lastIndex;
	}

}
