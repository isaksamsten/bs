package com.bs.parser.tree;

import com.bs.parser.token.Token;

public abstract class AbstractNode implements Node {

	private int line, position;

	public AbstractNode(Token token) {
		this(token.line(), token.position());
	}

	public AbstractNode(int line, int position) {
		this.line = line;
		this.position = position;
	}

	@Override
	public Node parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int line() {
		return line;
	}

	@Override
	public int position() {
		return position;
	}
}
