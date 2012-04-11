package com.bs.parser.tree;

import com.bs.parser.token.Token;

public class DefaultNodeFactory implements NodeFactory {

	@Override
	public IdentifierNode identifier(Token token) {
		IdentifierNode node = new IdentifierNode(token);
		node.variable(token.text());
		return node;
	}

	@Override
	public NumberNode number(Token token) {
		return new NumberNode(token, (Number) token.value());
	}

	@Override
	public StringNode string(Token token) {
		StringNode node = new StringNode(token);
		node.string((String) token.value());
		return node;
	}

	@Override
	public MessageNode message(Token token) {
		return new MessageNode(token);
	}

	@Override
	public MessagesNode messages(Token token) {
		return new MessagesNode(token);
	}

	@Override
	public ExpressionNode expression(Token token) {
		return new ExpressionNode(token);
	}

	@Override
	public CallNode call(Token token) {
		return new CallNode(token);
	}

	@Override
	public BlockNode block(Token token) {
		return new BlockNode(token);
	}

	@Override
	public ExpressionsNode expressions(Token token) {
		return new ExpressionsNode(token);
	}

	@Override
	public AssignNode assignment(Token token) {
		return new AssignNode(token);
	}

	@Override
	public StatementsNode statements(Token token) {
		return new StatementsNode(token);
	}

	@Override
	public ArgumentsNode arguments(Token token) {
		return new ArgumentsNode(token);
	}

	@Override
	public LiteralNode literal(Token start) {
		switch (start.type()) {
		case NUMBER:
			return number(start);
		case STRING:
			return string(start);
		case IDENTIFIER:
			return identifier(start);
		default:
			return null;
		}
	}

	@Override
	public ListNode list(Token token) {
		return new ListNode(token);
	}

}
