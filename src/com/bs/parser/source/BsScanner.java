package com.bs.parser.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.bs.util.MessageHandler;

public class BsScanner implements Scanner {

	private BufferedReader in;
	private ArrayList<String> data;
	private int line = -1;
	private int index = -1;

	public BsScanner(Reader stream) {
		in = new BufferedReader(stream);
		data = new ArrayList<String>();
		readLine();
		next();
	}

	private void readLine() {
		try {
			String str = in.readLine();
			index = -1;
			line++;
			data.add(str);
		} catch (IOException e) {
			MessageHandler.fatal(e);
		}

	}

	@Override
	public int line() {
		return line + 1;
	}

	@Override
	public int position() {
		return index;
	}

	@Override
	public char current() {
		if (currentLine() == null) {
			return EOF;
		} else if (index == -1 || index == currentLine().length()) {
			return EOL;
		} else if (index > currentLine().length()) {
			readLine();
			return next();
		} else {
			return currentLine().charAt(index);
		}
	}

	@Override
	public char peek() {
		return peek(1);
	}

	@Override
	public char peek(int n) {
		char c = current();
		if (c == EOF) {
			return c;
		}
		int next = index + n;
		return next < currentLine().length() ? currentLine().charAt(next) : EOL;
	}

	@Override
	public char next() {
		index++;
		return current();
	}

	@Override
	public boolean hasNext() {
		return peek() != EOF;
	}

	@Override
	public boolean hasNextOnLine() {
		return false;
	}

	@Override
	public String line(int n) {
		return data.get(n);
	}

	@Override
	public String currentLine() {
		return line(line);
	}

}
