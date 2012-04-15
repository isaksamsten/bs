package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.util.Message;
import com.bs.util.MessageType;

public class CallParser extends BsParser<CallNode> {

	public CallParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public CallNode parse(Token start) {
		CallNode node = nodeFactory().call(start);

		if (start.type() == TokenType.LEFT_PAREN) {
			ExpressionParser parser = new ExpressionParser(this);
			ExpressionNode expression = parser.parse();

			if (expression != null
					&& tokenizer().current().type() == TokenType.RIGHT_PAREN) {
				node.left(expression);
				tokenizer().next();
			} else {
				messageHandler().error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_CALL,
						tokenizer().current().text());
			}
		} else {
			node.left(nodeFactory().literal(start));
		}

		MessagesParser parser = new MessagesParser(this);
		MessagesNode messages = parser.parse(start);
		if (messages != null) {
			node.messages(messages);
		} else {
			messageHandler().error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_CALL,
					tokenizer().current().text());
		}

		return node;
	}

}
