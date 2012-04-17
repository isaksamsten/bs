package com.bs.parser.token;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
	IDENTIFIER, NUMBER, STRING, SYMBOL, EOF, ERROR,

	/*
	 * Assignment
	 */
	COLON(":"), COLON_EQUAL(":="), DOT("."),

	/*
	 * Delimiter
	 */
	COMMA(","), SEMI_COLON(";"),

	/*
	 * Block
	 */
	PIPE("|"),

	/*
	 * Calls
	 */
	LEFT_PAREN("("), RIGHT_PAREN(")"),

	/*
	 * Lists
	 */
	LEFT_BRACKET("["), RIGHT_BRACKET("]"),

	/*
	 * Blocks
	 */
	LEFT_BRACE("{"), RIGHT_BRACE("}");

	private static Map<String, TokenType> special = new HashMap<String, TokenType>();

	public static boolean isSpecial(String val) {
		return special.get(val) != null;
	}

	public static boolean isSpecial(char start) {
		return isSpecial(String.valueOf(start));
	}

	public static TokenType getSpecial(String val) {
		return special.get(val);
	}

	static {
		TokenType[] types = TokenType.values();
		for (int n = COLON.ordinal(); n <= RIGHT_BRACE.ordinal(); n++) {
			special.put(types[n].text(), types[n]);
		}
	}

	private String text;

	private TokenType(String text) {
		this.text = text;
	}

	private TokenType() {
		this.text = name().toLowerCase();
	}

	public String text() {
		return text;
	}
}
