package com.ieli.wordconv.service;

import java.util.List;

import com.ieli.wordconv.model.BookXML;

public interface IXMLModifier {

	List<BookXML> modifyXMLList(List<BookXML> booksXMLList, List<String> styles, List<String> types);
}
