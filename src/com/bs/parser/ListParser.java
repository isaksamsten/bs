package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.ListNode;
import com.bs.util.Message;
import com.bs.util.MessageType;

public class ListParser extends BsParser<ListNode> {

	public ListParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public ListNode parse(Token start) {
		ListNode node = null;
		if (start.type() == TokenType.LEFT_BRACKET) {
			node = nodeFactory().list(start);

			Token next = tokenizer().next();
			if (next.type() != TokenType.RIGHT_BRACKET) {
				ExpressionsParser parser = new ExpressionsParser(this);
				ExpressionsNode expressions = parser.parse(next);

				if (expressions != null
						&& tokenizer().current().type() == TokenType.RIGHT_BRACKET) {
					node.expressions(expressions);

					tokenizer().next();
				} else {
					messageHandler().error(tokenizer().current(),
							MessageType.SYNTAX_ERROR,
							Message.UNEXPECTED_END_OF_LIST,
							tokenizer().current().text());
				}
			} else {
				tokenizer().next(); // consume ]
			}
		} else {
			messageHandler().error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_LIST,
					tokenizer().current().text());
		}

		return node;
	}
}
