package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class StatementsNode extends AbstractListNode<StatementNode> {

	public StatementsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitStatements(this);
	}

	@Override
	public String toTree() {
		return "Statements(values=[" + super.toTree() + "])";
	}

}
