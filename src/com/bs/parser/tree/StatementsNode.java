package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class StatementsNode extends AbstractListNode<StatementNode> {

	public StatementsNode(int line, int position) {
		super(line, position);
	}

	public StatementsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitStatements(this);
	}

}
