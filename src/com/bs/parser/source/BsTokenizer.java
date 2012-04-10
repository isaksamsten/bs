package com.bs.parser.source;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenFactory;
import com.bs.parser.token.TokenType;
import com.bs.util.Message;
import com.bs.util.MessageHandler;

public class BsTokenizer implements Tokenizer {

	private Token next, current;
	private Scanner scanner;
	private TokenFactory factory;

	private char comment;

	public BsTokenizer(Scanner s, TokenFactory factory, char comment) {
		this.scanner = s;
		this.comment = comment;
		this.factory = factory;
	}

	public Scanner scanner() {
		return this.scanner;
	}

	@Override
	public Token current() {
		return current;
	}

	@Override
	public Token peek() {
		if (next == null) {
			next = extract();
		}

		return next;
	}

	@Override
	public Token next() {
		if (next != null) {
			current = next;
			next = null;
		} else {
			current = extract();
		}

		return current();
	}

	protected Token extract() {
		consumeWhitespace();
		char current = scanner.current();
		if (current == Scanner.EOF) {
			return factory.eof();
		} else if (validIdentifierStart(current)) {
			return extractIdentifier();
		} else if (Character.isDigit(current)) {
			return extractNumber();
		} else if (TokenType.isSpecial(current)) {
			return extractSpecial();
		} else {
			MessageHandler.error(scanner(), Message.UNEXPECTED_TOKEN,
					String.valueOf(current));
			scanner().next(); // skip.. (and recover?)
			return factory.error(scanner().line(), scanner().position());
		}
	}

	protected Token extractSpecial() {
		StringBuilder builder = new StringBuilder();
		char c = scanner().current();
		char n = scanner().peek();

		builder.append(c);
		if (c == ':' && n == '=') {
			scanner().next();
			builder.append(n);
		}

		scanner().next();
		return factory.special(builder.toString(), scanner.line(),
				scanner.position());
	}

	private Token extractNumber() {
		StringBuilder builder = new StringBuilder();
		char c = scanner().current();
		boolean hasDot = false;
		while (Character.isDigit(c) || (c == '.' && !hasDot)) {
			if (c == '.') {
				hasDot = true;
			}

			builder.append(c);
			c = scanner().next();
		}

		return factory.number(builder.toString(), scanner().line(), scanner()
				.position());
	}

	private Token extractIdentifier() {
		StringBuilder builder = new StringBuilder();
		char c = scanner().current();
		while (validIdentifierStart(c)) {
			builder.append(c);

			c = scanner().next();
		}

		return factory.identifier(builder.toString(), scanner().line(),
				scanner().position());
	}

	protected boolean validIdentifierStart(char current) {
		if (Character.isLetter(current)) {
			return true;
		} else {
			switch (current) {
			case '$':
			case '%':
			case '*':
			case '+':
			case '-':
			case '/':
			case '<':
			case '>':
				return true;
			default:
				return false;
			}
		}
	}

	protected void consumeWhitespace() {
		char current = scanner.current();
		while (Character.isWhitespace(current) || current == this.comment) {
			if (current == this.comment) {
				do {
					current = scanner().next();
				} while (current != Scanner.EOL && current != Scanner.EOF);
				if (current == Scanner.EOL) {
					current = scanner().next();
				}

			} else {
				current = scanner().next();
			}
		}
	}
}
