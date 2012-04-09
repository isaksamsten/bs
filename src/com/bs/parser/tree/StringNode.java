package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

/**
 * Node representing a String
 * 
 * @author Isak Karlsson
 * 
 */
public class StringNode extends LiteralNode {

	private String string;

	public StringNode(int line, int position) {
		super(line, position);
		// TODO Auto-generated constructor stub
	}

	public StringNode(Token token) {
		super(token);
		// TODO Auto-generated constructor stub
	}

	public void string(String string) {
		this.string = string;
	}

	public String string() {
		return this.string;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitString(this);
	}

	@Override
	public String toTree() {
		return "String(value=" + string + ")";
	}

}
