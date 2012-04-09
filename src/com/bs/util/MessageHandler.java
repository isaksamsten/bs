package com.bs.util;

import java.util.LinkedList;
import java.util.List;

import com.bs.parser.source.Scanner;

public final class MessageHandler {
	private static final int MAX_ERRORS = 15;

	private static List<Message> messages = new LinkedList<Message>();
	private static int errors = 0;

	public static void add(Message message) {
		messages.add(message);
	}

	public static void error(String message, Scanner scanner, Object... args) {
		if (errors <= MAX_ERRORS) {
			for (Message m : messages) {
				m.error(message, scanner, args);
			}
			errors++;
		} else {
			fatal(Message.TOO_MANY_ERRORS, scanner);
		}
	}

	public static void fatal(String message, Scanner scanner, Object... args) {
		for (Message m : messages) {
			m.fatal(message, scanner, args);
		}
	}

	public static void fatal(Throwable t) {
		for (Message m : messages) {
			m.fatal(t);
		}
	}

}
