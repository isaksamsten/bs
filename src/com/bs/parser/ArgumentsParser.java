package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ArgumentsNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageType;

public class ArgumentsParser extends BsParser<ArgumentsNode> {

	public ArgumentsParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public ArgumentsNode parse(Token start) {
		ArgumentsNode node = null;
		if (start.type() == TokenType.IDENTIFIER) {
			node = nodeFactory().arguments(start);
			node.add(nodeFactory().identifier(start));

			Token next = tokenizer().next();
			while (next.type() == TokenType.COMMA) {
				next = tokenizer().next();
				node.add(nodeFactory().identifier(next));
				next = tokenizer().next();
			}

		} else {
			MessageHandler.error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_ARGUMENT,
					tokenizer().current().text());
		}

		return node;
	}

}
