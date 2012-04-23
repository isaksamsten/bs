package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ExpressionsNode extends AbstractListNode<ExpressionNode> {

	public ExpressionsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretExpressions(this);
	}

	@Override
	public String toTree() {
		return "ExpressionsNode(values=[" + super.toTree() + "])";
	}

}
