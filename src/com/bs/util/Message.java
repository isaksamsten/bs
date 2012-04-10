package com.bs.util;

public class Message {
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
