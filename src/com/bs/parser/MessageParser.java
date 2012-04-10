package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.MessageNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageType;

public class MessageParser extends BsParser<MessageNode> {

	public MessageParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public MessageNode parse(Token start) {
		MessageNode node = null;
		Token next = tokenizer().next();
		if (next.type() == TokenType.LEFT_PAREN) {
			node = nodeFactory().message(start);
			node.identifier(nodeFactory().identifier(start));

//			next = tokenizer().next();
//			if (StatementParser.START.contains(next.type())) {
//
//			}

			next = tokenizer().next();
			if (next.type() != TokenType.RIGHT_PAREN) {
				MessageHandler.error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_MESSAGE,
						tokenizer().current().text());
			} else {
				tokenizer().next();
			}
		}

		return node;
	}

}
