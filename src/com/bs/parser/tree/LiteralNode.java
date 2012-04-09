package com.bs.parser.tree;

import java.util.List;

import com.bs.parser.token.Token;

public abstract class LiteralNode extends AbstractNode {

	public LiteralNode(Token token) {
		super(token);
	}

	public LiteralNode(int line, int position) {
		super(line, position);
	}

	@Override
	public List<Node> childrens() {
		return null;
	}
}
