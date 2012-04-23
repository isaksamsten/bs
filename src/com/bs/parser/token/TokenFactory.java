package com.bs.parser.token;

public interface TokenFactory {

	Token number(String value, Number number, int line, int position);

	Token identifier(String value, int line, int position);

	Token special(String value, int line, int position);

	Token string(String value, int line, int position);

	Token guess(String value, int line, int position);

	Token error(int line, int position);

	Token eof();

	Token symbol(String string, int line, int position);

	Token character(String value, char charAt, int line, int position);
}
