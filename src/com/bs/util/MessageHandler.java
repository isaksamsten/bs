package com.bs.util;

import java.util.LinkedList;
import java.util.List;

import com.bs.parser.source.Scanner;
import com.bs.parser.token.Token;

public final class MessageHandler {
	private static final int MAX_ERRORS = 15;

	private static List<Message> messages = new LinkedList<Message>();
	private static int errors = 0;

	public static void add(Message message) {
		messages.add(message);
	}

	/**
	 * 
	 * @param message
	 * @param scanner
	 * @param args
	 */
	public static void error(Scanner scanner, String message, Object... args) {
		error(scanner.currentLine(), scanner.line(), scanner.position(),
				message, args);
	}

	/**
	 * 
	 * @param message
	 * @param token
	 * @param args
	 */
	public static void error(Token token, String message, Object... args) {
		error(null, token.line(), token.position(), message, args);
	}

	/**
	 * 
	 * @param currentLine
	 *            - String
	 * @param line
	 *            - int
	 * 
	 * @param pos
	 *            - int
	 * @param message
	 *            - String interpolated with args
	 * @param args
	 *            - Object[] of arguments to message
	 */
	public static void error(String currentLine, int line, int pos,
			String message, Object... args) {
		if (errors <= MAX_ERRORS) {
			for (Message m : messages) {
				m.error(currentLine, line, pos, message, args);
			}
			errors++;
		} else {
			fatal(currentLine, line, pos, Message.TOO_MANY_ERRORS);
		}
	}

	public static void fatal(String currentLine, int line, int pos,
			String message, Object... args) {
		for (Message m : messages) {
			m.fatal(currentLine, line, pos, message, args);
		}
	}

	public static void fatal(Throwable t) {
		for (Message m : messages) {
			m.fatal(t);
		}
	}

}
