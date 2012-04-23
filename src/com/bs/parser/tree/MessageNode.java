package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class MessageNode extends AbstractNode {

	private IdentifierNode identifier;
	private ExpressionsNode expressions;

	public MessageNode(Token token) {
		super(token);
	}

	public IdentifierNode identifier() {
		return identifier;
	}

	public void identifier(IdentifierNode node) {
		this.identifier = node;
	}

	public ExpressionsNode expressions() {
		return expressions;
	}

	public void expressions(ExpressionsNode node) {
		expressions = node;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretMessage(this);
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(identifier, expressions);
	}

	@Override
	public String toTree() {
		return "Message(identifier=" + identifier.toTree() + " expressions="
				+ (expressions != null ? expressions.toTree() : "[]") + ")";
	}

}
