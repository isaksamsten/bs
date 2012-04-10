package com.bs.parser;

import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.Token;
import com.bs.parser.tree.NodeFactory;
import com.bs.parser.tree.StatementNode;
import com.bs.parser.tree.StatementsNode;

public class StatementsParser extends BsParser<StatementsNode> {

	public StatementsParser(BsParser<?> parser) {
		super(parser);
	}

	public StatementsParser(Tokenizer tokenizer, NodeFactory nodeFactory) {
		super(tokenizer, nodeFactory);
	}

	@Override
	public StatementsNode parse(Token start) {
		StatementsNode statements = nodeFactory().statements(start);
		StatementParser parser = new StatementParser(this);
		StatementNode node = parser.parse(start);
		if (node != null) {
			statements.add(node);

			while ((StatementParser.START
					.contains(tokenizer().current().type()))
					&& (node = parser.parse(tokenizer().current())) != null) {
				statements.add(node);
			}
		}

		return statements;

	}
}
