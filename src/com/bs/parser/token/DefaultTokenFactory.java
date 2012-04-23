package com.bs.parser.token;

public class DefaultTokenFactory implements TokenFactory {
	@Override
	public Token special(String value, int line, int position) {
		return new Token(value, value, TokenType.getSpecial(value), line,
				position);
	}

	@Override
	public Token number(String value, Number number, int line, int position) {
		return new Token(value, number, TokenType.NUMBER, line, position);
	}

	@Override
	public Token identifier(String value, int line, int position) {
		return new Token(value, value, TokenType.IDENTIFIER, line, position);
	}

	@Override
	public Token guess(String value, int line, int position) {
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
		return new Token(value, value, TokenType.STRING, line, position);
	}

	@Override
	public Token symbol(String value, int line, int position) {
		return new Token(value, value, TokenType.SYMBOL, line, position);
	}

	@Override
	public Token character(String value, char at, int line, int position) {
		return new Token(value, at, TokenType.CHARACTER, line, position);
	}
}