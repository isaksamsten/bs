package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ExpressionsNode extends AbstractListNode<ExpressionNode> {

	public ExpressionsNode(int line, int position) {
		super(line, position);
	}

	public ExpressionsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitExpressions(this);
	}

	@Override
	public String toTree() {
		return "ExpressionsNode(values=[" + super.toTree() + "])";
	}

}
