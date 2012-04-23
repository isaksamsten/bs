package com.bs.util;

public class Message {
	public static final String TOO_MANY_ERRORS = "To many errors encountered";
	public static final String UNEXPECTED_TOKEN = "Unexpected token '%s'";
	public static final String UNEXPECTED_CHAR = "Unexpected char '%s' (this is not a character?)";
	public static final String UNEXPECTED_NUMBER = "Unexpected number '%s' (this is not a number?)";
	public static final String UNEXPECTED_STATEMENT = "Unexpected statement '%s'";
	public static final String UNEXPECTED_ASSIGNMENT = "Unexpected assignment '%s'";
	public static final String UNEXPECTED_EXPRESSION = "Unexpected expression '%s'";
	public static final String UNEXPECTED_END_OF_STATEMENT = "Unexpected end of statement '%s' (are you missing a dot?)";
	public static final String UNEXPECTED_BLOCK = "Unexpected block '%s'";
	public static final String UNEXPECTED_MESSAGE = "Unexpected message '%s'";
	public static final String UNEXPECTED_ARGUMENT = "Unexpected argument '%s' (are you missing a comma?)";
	public static final String UNEXPECTED_CALL = "Unexpected call '%s' (are you missing a message?)";
	public static final String UNEXPECTED_LIST = "Unexpected list '%s'";
	public static final String UNEXPECTED_END_OF_LIST = "Unexpected end of list '%s' (are you missing ]?)";

	private int line, position;
	private String currentLine, message;
	private MessageType type;
	private Object[] args;

	public Message(String currentLine, MessageType type, int line,
			int position, String message, Object[] args) {
		this.currentLine = currentLine;
		this.type = type;
		this.line = line;
		this.position = position;
		this.message = message;
		this.args = args;
	}

	public int line() {
		return line;
	}

	public int position() {
		return position;
	}

	public String currentLine() {
		return currentLine;
	}

	public String message() {
		return message;
	}

	public MessageType type() {
		return type;
	}

	public Object[] args() {
		return args;
	}

}
