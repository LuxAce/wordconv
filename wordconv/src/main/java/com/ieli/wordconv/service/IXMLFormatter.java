package com.ieli.wordconv.service;

import java.util.List;

import com.ieli.wordconv.model.BookXML;

public interface IXMLFormatter {

	List<BookXML> formatXMLList(List<BookXML> booksXMLList); 
}
