package com.ieli.wordconv.model;

import java.io.Serializable;

public class BookXML implements Serializable {

	private static final long serialVersionUID = 1L;
	private String tagStart;
	private String content;
	private String tagEnd;

	public BookXML() {

	}

	public BookXML(String tagStart, String content, String tagEnd) {
		super();
		this.tagStart = tagStart;
		this.content = content;
		this.tagEnd = tagEnd;
	}

	public String getTagStart() {
		return tagStart;
	}

	public void setTagStart(String tagStart) {
		this.tagStart = tagStart;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTagEnd() {
		return tagEnd;
	}

	public void setTagEnd(String tagEnd) {
		this.tagEnd = tagEnd;
	}

	@Override
	public String toString() {
		return tagStart + content + tagEnd;
	}
}
