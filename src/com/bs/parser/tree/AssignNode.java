package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class AssignNode extends StatementNode {

	private IdentifierNode identifier;
	private ExpressionNode expression;

	public AssignNode(Token token) {
		super(token);
	}

	public IdentifierNode identifier() {
		return identifier;
	}

	public void identifier(IdentifierNode identifier) {
		this.identifier = identifier;
	}

	public ExpressionNode expression() {
		return expression;
	}

	public void expression(ExpressionNode expression) {
		this.expression = expression;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretAssign(this);
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(identifier, expression);
	}

	@Override
	public String toTree() {
		return "Assign(identifier=" + identifier.toTree() + " expression="
				+ expression.toTree() + ")";
	}

}
