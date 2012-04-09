package com.bs.parser.source;

public interface Scanner {

	char EOL = '\n';
	char EOF = (char) 0;

	int line();

	String line(int n);

	String currentLine();

	int position();

	char current();

	char peek();

	char peek(int n);

	char next();

	boolean hasNext();

	boolean hasNextOnLine();
}
