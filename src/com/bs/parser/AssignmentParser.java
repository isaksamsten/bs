package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.IdentifierNode;
import com.bs.parser.tree.IdentifierNode.State;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageType;

public class AssignmentParser extends BsParser<AssignNode> {

	public AssignmentParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public AssignNode parse(Token start) {
		AssignNode node = null;
		Token next = tokenizer().next();
		if (next.type() == TokenType.COLON_EQUAL) {
			ExpressionParser exprParser = new ExpressionParser(this);
			ExpressionNode exprNode = exprParser.parse();
			if (exprNode != null) {
				node = nodeFactory().assignment(start);
				node.expression(exprNode);

				IdentifierNode identifier = nodeFactory().identifier(start);
				identifier.state(State.STORE);
				node.identifier(identifier);
			} else {
				MessageHandler.error(tokenizer().current(),
						MessageType.SYNTAX_ERROR,
						Message.UNEXPECTED_ASSIGNMENT, tokenizer().current()
								.text());
			}
		} else {
			MessageHandler.error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_ASSIGNMENT,
					tokenizer().current().text());
		}

		return node;
	}

}
