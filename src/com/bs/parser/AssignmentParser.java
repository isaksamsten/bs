package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;

public class AssignmentParser extends BsParser<AssignNode> {

	public AssignmentParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public AssignNode parse(Token start) {
		AssignNode node = null;

		ExpressionParser exprParser = new ExpressionParser(this);
		ExpressionNode exprNode = exprParser.parse();
		if (exprNode != null) {
			node = nodeFactory().assignment(start);
			node.expression(exprNode);
			node.identifier(nodeFactory().variable(start));
		} else {
			MessageHandler.error(start, Message.UNEXPECTED_ASSIGNMENT,
					start.text());
		}

		return node;
	}

}
