package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.MessagesNode;

public class CallParser extends BsParser<CallNode> {

	public CallParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public CallNode parse(Token start) {
		CallNode node = nodeFactory().call(start);
		node.literal(nodeFactory().literal(start));

		MessagesParser parser = new MessagesParser(this);
		MessagesNode messages = parser.parse(start);
		if (messages != null) {
			node.messages(messages);
		} else {

		}

		return node;
	}

}
