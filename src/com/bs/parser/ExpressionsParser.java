package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.ExpressionsNode;

public class ExpressionsParser extends BsParser<ExpressionsNode> {

	public ExpressionsParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public ExpressionsNode parse(Token start) {
		ExpressionsNode node = nodeFactory().expressions(start);
		ExpressionParser parser = new ExpressionParser(this);
		ExpressionNode expression = parser.parse(start);

		if (node != null) {
			node.add(expression);

			Token next = tokenizer().current();
			while (next.type() == TokenType.COMMA) {
				next = tokenizer().next();
				expression = parser.parse(next);
				node.add(expression);

				next = tokenizer().current();
			}
		}

		return node;
	}
}
