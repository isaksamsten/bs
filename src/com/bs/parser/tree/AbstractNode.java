package com.bs.parser.tree;

import com.bs.parser.token.Token;

public abstract class AbstractNode implements Node {

	private int line, position;
	private String code;

	public AbstractNode(Token token) {
		this(token.currentLine(), token.line(), token.position());
	}

	public AbstractNode(String code, int line, int position) {
		this.code = code;
		this.line = line;
		this.position = position;
	}

	@Override
	public Node parent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String code() {
		return code;
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
