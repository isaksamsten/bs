package com.bs.parser.token;

public class DefaultTokenFactory implements TokenFactory {
	@Override
	public Token special(String value, int line, int position) {
		// System.out.println("SPECIAL " + value + " " + line + ":" + position);
		return new Token(value, value, TokenType.getSpecial(value), line,
				position);
	}

	@Override
	public Token number(String value, int line, int position) {
		Number number = null;
		try {
			number = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			number = Double.parseDouble(value);
		}

		return new Token(value, number, TokenType.NUMBER, line, position);
	}

	@Override
	public Token identifier(String value, int line, int position) {
		// System.out.println("IDENTIFIER " + value + " " + line + ":" +
		// position);
		return new Token(value, value, TokenType.IDENTIFIER, line, position);
	}

	@Override
	public Token guess(String value, int line, int position) {
		// System.out.println("GUESS " + value + " " + line + ":" + position);
		return null;
	}

	@Override
	public Token error(int line, int position) {
		return new Token("<error>", null, TokenType.ERROR, line, position);
	}

	@Override
	public Token eof() {
		return new Token("EOF", "EOF", TokenType.EOF, -1, -1);
	}

	@Override
	public Token string(String value, int line, int position) {
		// System.out.println("STRING " + value + " " + line + ":" + position);
		return new Token(value, value, TokenType.STRING, line, position);
	}
}