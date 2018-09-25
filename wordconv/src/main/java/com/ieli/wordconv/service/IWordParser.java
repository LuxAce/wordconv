package com.ieli.wordconv.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public interface IWordParser {

	List<String> getXMLFromWord(String inputFile) throws InvalidFormatException, IOException;
}
