package com.bs.parser.tree;

import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

/**
 * Node representing a variable
 * 
 * @author Isak Karlsson
 * 
 */
public class IdentifierNode extends AbstractNode implements LiteralNode {

	/**
	 * The state of a variable
	 * 
	 * @author Isak Karlsson
	 * 
	 */
	public enum State {
		LOAD, STORE, MESSAGE;
	}

	private String variable;
	private IdentifierNode.State state = State.LOAD;

	public IdentifierNode(Token token) {
		super(token);
	}

	public void variable(String string) {
		this.variable = string;
	}

	public String variable() {
		return this.variable;
	}

	public void state(IdentifierNode.State state) {
		this.state = state;
	}

	public IdentifierNode.State state() {
		return state;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitVariable(this);
	}

	@Override
	public String toTree() {
		return "Identifier(value=" + variable + " state=" + state + ")";
	}

	@Override
	public List<Node> childrens() {
		return null;
	}

	@Override
	public Object value() {
		return variable;
	}
}
