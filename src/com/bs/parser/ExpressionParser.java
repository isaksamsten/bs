package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.tree.ExpressionNode;

public class ExpressionParser extends BsParser<ExpressionNode> {

	public ExpressionParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public ExpressionNode parse(Token start) {
		// TODO Auto-generated method stub
		return super.parse(start);
	}

}
