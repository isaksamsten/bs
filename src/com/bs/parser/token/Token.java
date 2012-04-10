package com.bs.parser.token;

public class Token {

	private Object value;
	private TokenType type;
	private String text;
	int line, position;
	private String currentLine;

	public int line() {
		return line;
	}

	public int position() {
		return position;
	}

	public Token(String text, Object value, TokenType type, int line,
			int position) {
		this.value = value;
		this.text = text;
		this.type = type;
		this.line = line;
		this.position = position;
	}

	public TokenType type() {
		return this.type;
	}

	public String text() {
		return this.text;
	}

	public void text(String t) {
		this.text = t;
	}

	public void value(Object t) {
		this.value = t;
	}

	public Object value() {
		return this.value;
	}

	public void currentLine(String currentLine) {
		this.currentLine = currentLine;
	}

	public String currentLine() {
		return currentLine;
	}
}
