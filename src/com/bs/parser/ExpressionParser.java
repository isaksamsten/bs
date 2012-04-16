package com.bs.parser;

import java.util.EnumSet;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.LiteralNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.util.Message;
import com.bs.util.MessageType;

public class ExpressionParser extends BsParser<ExpressionNode> {

	public static final EnumSet<TokenType> START = EnumSet.of(
			TokenType.IDENTIFIER, TokenType.STRING, TokenType.NUMBER,
			TokenType.SYMBOL);

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

			/*
			 * There is a message after the list literal
			 */
			if (START.contains(tokenizer().current().type())) {
				LiteralNode tmp = node;
				MessagesParser msgParser = new MessagesParser(this);
				MessagesNode msgs = msgParser.parse(tokenizer().current());

				if (msgs != null) {
					CallNode cnode = nodeFactory().call(start);
					cnode.left(tmp);
					cnode.messages(msgs);

					node = cnode;
				}

			}
		} else if (start.type() == TokenType.LEFT_BRACE) {
			BlockParser parser = new BlockParser(this);
			node = parser.parse(start);

			if (START.contains(tokenizer().current().type())) {
				LiteralNode tmp = node;
				MessagesParser msgParser = new MessagesParser(this);
				MessagesNode msgs = msgParser.parse(tokenizer().current());

				if (msgs != null) {
					CallNode cnode = nodeFactory().call(start);
					cnode.left(tmp);
					cnode.messages(msgs);

					node = cnode;
				}
			}
		} else if (start.type() == TokenType.NUMBER) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().number(start));

			tokenizer().next();
		} else if (start.type() == TokenType.STRING) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().string(start));

			tokenizer().next();
		} else if (start.type() == TokenType.SYMBOL) {
			node = nodeFactory().expression(start);
			node.left(nodeFactory().symbol(start));

			tokenizer().next();
		} else {
			messageHandler().error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_EXPRESSION,
					tokenizer().current().text());
		}

		return node;
	}
}
