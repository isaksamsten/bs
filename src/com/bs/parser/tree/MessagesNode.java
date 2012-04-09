package com.bs.parser.tree;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class MessagesNode extends AbstractListNode<MessageNode> {

	public MessagesNode(int line, int position) {
		super(line, position);
	}

	public MessagesNode(Token token) {
		super(token);
	}

	@Override
	public String toTree() {
		return "Messages(messages=[" + super.toTree() + "])";
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitMessages(this);
	}
}
