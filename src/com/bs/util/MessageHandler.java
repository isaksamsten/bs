package com.bs.util;

import java.util.LinkedList;
import java.util.List;

import com.bs.lang.BsObject;
import com.bs.parser.source.Scanner;
import com.bs.parser.token.Token;

public final class MessageHandler {
	private static final int MAX_ERRORS = 15;

	private static List<MessageListener> messages = new LinkedList<MessageListener>();
	private static int errors = 0;

	public static void add(MessageListener message) {
		messages.add(message);
	}

	/**
	 * Return the number of encountered errors.
	 * 
	 * @return
	 */
	public static int errors() {
		return errors;
	}

	/**
	 * Reset the error count
	 */
	public static void reset() {
		errors = 0;
	}

	/**
	 * 
	 * @param message
	 * @param scanner
	 * @param args
	 */
	public static void error(Scanner scanner, MessageType type, String message,
			Object... args) {
		error(scanner.currentLine(), type, scanner.line(), scanner.position(),
				message, args);
	}

	/**
	 * 
	 * @param message
	 * @param token
	 * @param args
	 */
	public static void error(Token token, MessageType type, String message,
			Object... args) {
		error(token.currentLine(), type, token.line(), token.position(),
				message, args);
	}

	public static void error(BsObject object, MessageType type, int line,
			int position, String name) {
		error("", MessageType.NAME_ERROR, line, position,
				"name '%s' not defined in '%s'", name, object.name());
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
	public static void error(String currentLine, MessageType type, int line,
			int pos, String message, Object... args) {
		if (errors <= MAX_ERRORS) {
			for (MessageListener m : messages) {
				m.error(new Message(currentLine, type, line, pos, message, args));
			}
			errors++;
		} else {
			fatal(currentLine, type, line, pos, Message.TOO_MANY_ERRORS);
		}
	}

	public static void fatal(String currentLine, MessageType type, int line,
			int pos, String message, Object... args) {
		for (MessageListener m : messages) {
			m.fatal(new Message(currentLine, type, line, pos, message, args));
		}
	}

	public static void fatal(Throwable t) {
		for (MessageListener m : messages) {
			m.fatal(t);
		}
	}

}
