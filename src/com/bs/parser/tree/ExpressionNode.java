package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ExpressionNode extends StatementNode {

	private LiteralNode literal;

	public ExpressionNode(int line, int position) {
		super(line, position);
	}

	public ExpressionNode(Token token) {
		super(token);
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(literal);
	}

	public LiteralNode literal() {
		return literal;
	}

	public void literal(LiteralNode node) {
		this.literal = node;
	}

	@Override
	public String toTree() {
		return "Expr(literal=" + literal.toTree() + ")";
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitExpression(this);
	}

}
