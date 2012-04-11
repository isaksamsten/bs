package com.bs.parser;

import java.util.EnumSet;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ExpressionNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageType;

public class ExpressionParser extends BsParser<ExpressionNode> {

	public static final EnumSet<TokenType> START = EnumSet.of(
			TokenType.IDENTIFIER, TokenType.STRING, TokenType.NUMBER);

	public ExpressionParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public ExpressionNode parse(Token start) {
		ExpressionNode node = null;
		Token next = tokenizer().peek();
		if (START.contains(start.type()) && next.type() == TokenType.IDENTIFIER) {
			next = tokenizer().next();
			CallParser parser = new CallParser(this);
			node = parser.parse(start);
		} else if (start.type() == TokenType.IDENTIFIER) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().identifier(start));

			tokenizer().next();
		} else if (start.type() == TokenType.LEFT_PAREN) {
			CallParser parser = new CallParser(this);
			node = parser.parse(start);
		} else if (start.type() == TokenType.LEFT_BRACKET) {
			ListParser parser = new ListParser(this);
			node = parser.parse(start);
		} else if (start.type() == TokenType.LEFT_BRACE) {
			BlockParser parser = new BlockParser(this);
			node = parser.parse(start);
		} else if (start.type() == TokenType.NUMBER) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().number(start));

			tokenizer().next();
		} else if (start.type() == TokenType.STRING) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().string(start));

			tokenizer().next();
		} else {
			MessageHandler.error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_EXPRESSION,
					tokenizer().current().text());
		}

		return node;
	}
}
