package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class CallNode extends ExpressionNode {

	private MessagesNode messages;

	public CallNode(int line, int position) {
		super(line, position);
	}

	public CallNode(Token token) {
		super(token);
	}

	public MessagesNode messages() {
		return this.messages;
	}

	public void messages(MessagesNode messages) {
		this.messages = messages;
	}

	@Override
	public String toTree() {
		return "Call(literal=" + left().toTree() + " messages="
				+ messages.toTree() + ")";
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(left());
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitCall(this);
	}

}
