package com.bs.parser.tree;

import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class NumberNode extends AbstractNode implements LiteralNode {

	private Number number;

	public NumberNode(Token token, Number number) {
		super(token);
		this.number = number;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitNumber(this);
	}

	/**
	 * Return the number represented by this node
	 * 
	 * @return a number
	 */
	public Number number() {
		return number;
	}

	public void number(Number number) {
		this.number = number;
	}

	@Override
	public String toTree() {
		return "Number(" + number + ")";
	}

	@Override
	public List<Node> childrens() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object value() {
		return number;
	}

}
