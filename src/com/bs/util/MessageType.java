package com.bs.util;

public enum MessageType {
	SYNTAX_ERROR("SyntaxError"), NAME_ERROR("NameError"), ATTRIBUTE_ERROR(
			"AttributeError");

	private String text;

	private MessageType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
