package com.bs.parser;

import java.util.EnumSet;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.IdentifierNode;
import com.bs.parser.tree.IdentifierNode.State;
import com.bs.parser.tree.MessageNode;
import com.bs.util.Message;
import com.bs.util.MessageType;

public class MessageParser extends BsParser<MessageNode> {

	public static EnumSet<TokenType> MESSAGE_END = EnumSet.of(TokenType.DOT,
			TokenType.SEMI_COLON, TokenType.RIGHT_PAREN, TokenType.LEFT_BRACE);

	public MessageParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public MessageNode parse(Token start) {
		MessageNode node = null;
		Token next = tokenizer().next();
		/*
		 * Calls with parenthesis with and without arguments
		 * 
		 * Call like message(). or message(1,2,3)
		 */
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

			/*
			 * Parenthesis less calls with arguments
			 * 
			 * Call like message 10, 20. or message 10; message 20.
			 */
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
				if (tokenizer().current().type() == TokenType.SEMI_COLON) {
					tokenizer().next();
				}
			} else {
				messageHandler().error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_MESSAGE,
						tokenizer().current().text());
			}

			/*
			 * Parenthesis less calls, without arguments
			 * 
			 * Call like message. message; message. etc.
			 */
		} else if (MESSAGE_END.contains(next.type())) {
			node = nodeFactory().message(start);
			IdentifierNode identifier = nodeFactory().identifier(start);
			identifier.state(State.MESSAGE);
			node.identifier(identifier);

			// Consume any semicolon
			if (next.type() == TokenType.SEMI_COLON) {
				tokenizer().next();
			}
		} else {

		}

		return node;
	}
}
