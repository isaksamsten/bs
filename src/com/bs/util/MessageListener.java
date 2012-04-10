package com.bs.util;

public interface MessageListener {

	String TOO_MANY_ERRORS = "To many errors encountered";
	String UNEXPECTED_TOKEN = "Unexpected token '%s'";
	String UNEXPECTED_STATEMENT = "Unexpected statement '%s'";
	String UNEXPECTED_ASSIGNMENT = "Unexpected assignment '%s'";

	void error(Message message);

	void fatal(Message message);

	void fatal(Throwable t);
}
