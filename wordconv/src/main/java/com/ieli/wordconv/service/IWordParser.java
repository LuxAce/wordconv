package com.ieli.wordconv.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.ieli.wordconv.model.BookXML;

public interface IWordParser {

	List<BookXML> getXMLFromWord(String inputFile) throws InvalidFormatException, IOException;
}
