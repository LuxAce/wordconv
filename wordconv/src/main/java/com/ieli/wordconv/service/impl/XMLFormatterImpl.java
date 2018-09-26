package com.ieli.wordconv.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ieli.wordconv.model.BookXML;
import com.ieli.wordconv.service.IXMLFormatter;

public class XMLFormatterImpl implements IXMLFormatter {

	public List<BookXML> formatXMLList(List<BookXML> booksXMLList) {

		List<BookXML> formattedBookXMLList = new ArrayList<BookXML>();

		for (int i = 0; i < booksXMLList.size();) {

			BookXML currEl = booksXMLList.get(i);
			BookXML nextEl = null;
			if (i < booksXMLList.size() - 1) {
				nextEl = booksXMLList.get(i + 1);

				if (currEl.getTagStart().equals(nextEl.getTagStart())) {

					for (int j = booksXMLList.indexOf(currEl); j < booksXMLList.size(); j++) {

						if (j != booksXMLList.size() + 1) {
							BookXML nextInnerEl = booksXMLList.get(j + 1);
							if (!currEl.getTagStart().equals(nextInnerEl.getTagStart())) {
								i = booksXMLList.indexOf(nextInnerEl);
								break;
							}
							currEl.setContent(currEl.getContent() + nextInnerEl.getContent());
						}
					}

					formattedBookXMLList.add(currEl);
				} else {
					formattedBookXMLList.add(currEl);
					i++;
				}
			} else {
				break;
			}

		}

		return formattedBookXMLList;
	}

}
