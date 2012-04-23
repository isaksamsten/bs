package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class BlockNode extends ExpressionNode {

	private ArgumentsNode arguments;
	private StatementsNode statements;

	public BlockNode(Token token) {
		super(token);
	}

	public ArgumentsNode arguments() {
		return arguments;
	}

	public void arguments(ArgumentsNode arguments) {
		this.arguments = arguments;
	}

	public StatementsNode statements() {
		return statements;
	}

	public void statements(StatementsNode statements) {
		this.statements = statements;
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(arguments, statements);
	}

	@Override
	public String toTree() {
		return "Block(arguments="
				+ (arguments != null ? arguments.toTree() : "[]")
				+ " statements=" + statements.toTree() + ")";
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretBlock(this);
	}

}
