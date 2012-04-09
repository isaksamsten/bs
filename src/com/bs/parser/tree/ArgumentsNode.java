package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ArgumentsNode extends AbstractListNode<IdentifierNode> {

	public ArgumentsNode(int line, int position) {
		super(line, position);
	}

	public ArgumentsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitArguments(this);
	}

	@Override
	public String toTree() {
		return "Arguments(values=" + super.toTree() + ")";
	}
}
