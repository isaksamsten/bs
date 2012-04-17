package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.IdentifierNode;
import com.bs.parser.tree.IdentifierNode.State;
import com.bs.parser.tree.MessageNode;
import com.bs.util.Message;
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
			IdentifierNode identifier = nodeFactory().identifier(start);
			identifier.state(State.MESSAGE);
			node.identifier(identifier);

			next = tokenizer().next();
			if (StatementParser.START.contains(next.type())) {
				ExpressionsParser parser = new ExpressionsParser(this);
				ExpressionsNode expressions = parser.parse(tokenizer()
						.current());
				if (expressions != null) {
					node.expressions(expressions);
				}
			}

			next = tokenizer().current();
			if (next.type() != TokenType.RIGHT_PAREN) {
				messageHandler().error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_MESSAGE,
						tokenizer().current().text());
			} else {
				tokenizer().next();
			}
		} else if (StatementParser.START.contains(next.type())) {
			node = nodeFactory().message(start);
			IdentifierNode identifier = nodeFactory().identifier(start);
			identifier.state(State.MESSAGE);
			node.identifier(identifier);

			ExpressionsParser parser = new ExpressionsParser(this);
			ExpressionsNode expressions = parser.parse(next);
			if (expressions != null) {
				node.expressions(expressions);
				
				/*
				 * Handle breaking when dealing with parenthesis less calls
				 */
				if(tokenizer().current().type() == TokenType.SEMI_COLON) {
					tokenizer().next();
				}
			} else {
				messageHandler().error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_MESSAGE,
						tokenizer().current().text());
			}
		} else {

		}

		return node;
	}

}
