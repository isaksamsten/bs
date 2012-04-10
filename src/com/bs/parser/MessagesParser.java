package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.MessageNode;
import com.bs.parser.tree.MessagesNode;

public class MessagesParser extends BsParser<MessagesNode> {

	public MessagesParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public MessagesNode parse(Token start) {
		MessagesNode node = nodeFactory().messages(start);
		MessageParser parser = new MessageParser(this);
		MessageNode message = parser.parse(tokenizer().current());

		if (message != null) {
			node.add(message);

			while (tokenizer().current().type() == TokenType.IDENTIFIER
					&& (message = parser.parse(tokenizer().current())) != null) {
				node.add(message);
			}

		} else {

		}

		return node;
	}
}
