package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ArgumentsNode extends AbstractListNode<IdentifierNode> {

	public ArgumentsNode(Token token) {
		super(token);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretArguments(this);
	}

	@Override
	public String toTree() {
		return "Arguments(values=" + super.toTree() + ")";
	}
}
