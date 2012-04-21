package com.bs.parser.tree;

import com.bs.parser.token.Token;

public abstract class StatementNode extends AbstractNode {

	public StatementNode(Token token) {
		super(token);
	}

}
