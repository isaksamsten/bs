package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.tree.StatementsNode;

public class StatementsParser extends BsParser<StatementsNode> {

	public StatementsParser(BsParser<?> parser) {
		super(parser);
	}
	
	@Override
	public StatementsNode parse(Token start) {
		// TODO Auto-generated method stub
		return super.parse(start);
	}

}
