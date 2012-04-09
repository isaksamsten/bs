package com.bs.parser.tree;

import com.bs.parser.token.Token;

public abstract class StatementNode extends AbstractNode {

	public StatementNode(int line, int position) {
		super(line, position);
	}

	public StatementNode(Token token) {
		super(token);
	}

}
