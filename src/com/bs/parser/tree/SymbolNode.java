package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class SymbolNode extends StringNode {

	public SymbolNode(Token token) {
		super(token);
		string((String) token.value());
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitSymbol(this);
	}
}
