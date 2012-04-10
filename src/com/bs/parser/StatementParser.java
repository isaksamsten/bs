package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.StatementNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;

public class StatementParser extends BsParser<StatementNode> {

	public StatementParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public StatementNode parse(Token start) {
		StatementNode node = null;
		if (start.type() == TokenType.IDENTIFIER) {
			Token current = tokenizer().next();

			if (current.type() == TokenType.COLON_EQUAL) {
				AssignmentParser assignmentParser = new AssignmentParser(this);
				node = assignmentParser.parse(start);
			} else if (current.type() == TokenType.IDENTIFIER) {
				ExpressionParser expressionParser = new ExpressionParser(this);
				node = expressionParser.parse(start);
			} else {
				MessageHandler.error(current, Message.UNEXPECTED_STATEMENT,
						current.text());
			}
		} else {
			MessageHandler.error(start, Message.UNEXPECTED_STATEMENT,
					start.text());
		}

		return node;
	}
}
