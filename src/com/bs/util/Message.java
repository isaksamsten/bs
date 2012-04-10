package com.bs.util;

public interface Message {

	String TOO_MANY_ERRORS = "To many errors encountered";
	String UNEXPECTED_TOKEN = "Unexpected token at '%s'";
	String UNEXPECTED_STATEMENT = "Unexpected statement at '%s'";
	String UNEXPECTED_ASSIGNMENT = "Unexpected assignment at '%s'";

	void error(String currentLine, int line, int pos, String message,
			Object... args);

	void fatal(String currentLine, int line, int pos, String message,
			Object... args);

	void fatal(Throwable t);
}
