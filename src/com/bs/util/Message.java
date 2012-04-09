package com.bs.util;

import com.bs.parser.source.Scanner;

public interface Message {

	String TOO_MANY_ERRORS = "To many errors encountered";
	String INVALID_TOKEN = "Invalid token at '%s'";

	void error(String message, Scanner scanner, Object... args);

	void fatal(String message, Scanner scanner, Object... args);

	void fatal(Throwable t);
}
