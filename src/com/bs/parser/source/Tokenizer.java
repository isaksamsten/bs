package com.bs.parser.source;

import com.bs.parser.token.Token;

public interface Tokenizer {

	Token current();

	Token peek();

	Token next();
}
