package com.ieli.wordconv.service.impl;

import java.util.List;

import com.ieli.wordconv.model.BookXML;
import com.ieli.wordconv.service.IXMLModifier;
import com.ieli.wordconv.util.StaticData;

public class XMLModifierImpl implements IXMLModifier {

	public List<BookXML> modifyXMLList(List<BookXML> booksXMLList, List<String> styles, List<String> types) {

		// Every time you see the style you should add above it a new tag type
		// Loop styles
		for (int j = 0; j < styles.size(); j++) {

			for (int i = 0; i < booksXMLList.size(); i++) {

				BookXML currBookXML = booksXMLList.get(i);

				// Get current book xml start tag
				String bookStartTag = currBookXML.getTagStart().trim().replaceAll("<", "").replaceAll(">", "")
						.replaceAll("/", "");

				// Check if current book xml tag equals a style in the styles
				// list
				if (bookStartTag.equals(styles.get(j))) {

					// If yes, create a new style open tag above it
					BookXML openTagBookXML = new BookXML("<" + types.get(j) + ">" + StaticData.OS_NEW_LINE, "", "");
					booksXMLList.add(booksXMLList.indexOf(currBookXML), openTagBookXML);

					boolean notFound = false;
					// If there is more than one style
					// then we will have to add the end style tag after the next
					// style found in the list
					if (j < styles.size() - 1) {

						// Loop the xml books tag again, and start from current
						// xml book tag
						for (int s = booksXMLList.indexOf(currBookXML); s < booksXMLList.size(); s++) {

							String innerBookStartTag = booksXMLList.get(s).getTagStart().trim().replaceAll("<", "")
									.replaceAll(">", "").replaceAll("/", "");

							if (j < styles.size() - 1) {

								if (innerBookStartTag.equals(styles.get(j + 1))) {

									BookXML newEndBookXML = new BookXML(
											"</" + types.get(j) + ">" + StaticData.OS_NEW_LINE, "", "");
									booksXMLList.add(booksXMLList.indexOf(booksXMLList.get(s)) + 1, newEndBookXML);
									i = booksXMLList.indexOf(newEndBookXML);
									notFound = false;
									break;

								} else {
									notFound = true;
								}
							}

							if ((s == booksXMLList.size() - 1) && notFound) {

								BookXML newEndBookXML = new BookXML("</" + types.get(j) + ">" + StaticData.OS_NEW_LINE,
										"", "");
								booksXMLList.add(booksXMLList.indexOf(currBookXML) + 1, newEndBookXML);
								i = booksXMLList.indexOf(newEndBookXML);
								break;

							}
						}

					} else {

						// And an end style tag right after the found book xml
						// element
						// The new tag will wrapp only one style found element
						BookXML newEndBookXML = new BookXML("</" + types.get(j) + ">" + StaticData.OS_NEW_LINE, "", "");
						booksXMLList.add(booksXMLList.indexOf(currBookXML) + 1, newEndBookXML);
						i = booksXMLList.indexOf(newEndBookXML);
					}

				}
			}

		}

		return booksXMLList;
	}

}
