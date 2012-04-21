package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ExpressionNode extends StatementNode implements LiteralNode {

	private LiteralNode left;

	public ExpressionNode(Token token) {
		super(token);
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(left);
	}

	public LiteralNode left() {
		return left;
	}

	public void left(LiteralNode node) {
		this.left = node;
	}

	@Override
	public String toTree() {
		return "Expr(literal=" + left.toTree() + ")";
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitExpression(this);
	}

	@Override
	public Object value() {
		return null;
	}

}
