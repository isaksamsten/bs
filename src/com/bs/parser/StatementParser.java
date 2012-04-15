package com.bs.parser;

import java.util.EnumSet;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.StatementNode;
import com.bs.util.Message;
import com.bs.util.MessageType;

public class StatementParser extends BsParser<StatementNode> {

	public static final EnumSet<TokenType> START = EnumSet.of(
			TokenType.IDENTIFIER, TokenType.NUMBER, TokenType.STRING,
			TokenType.LEFT_BRACE, TokenType.LEFT_PAREN, TokenType.LEFT_BRACKET);

	public static final EnumSet<TokenType> END = EnumSet.of(TokenType.DOT);

	public StatementParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public StatementNode parse(Token start) {
		StatementNode node = null;
		if (START.contains(start.type())) {
			Token next = tokenizer().peek();

			if (start.type() == TokenType.IDENTIFIER
					&& next.type() == TokenType.COLON_EQUAL) {
				AssignmentParser assignmentParser = new AssignmentParser(this);
				node = assignmentParser.parse(start);
			} else {
				ExpressionParser expressionParser = new ExpressionParser(this);
				node = expressionParser.parse(start);
			}

			start = tokenizer().current();
			if (!END.contains(start.type())) {
				messageHandler().error(tokenizer().current(),
						MessageType.SYNTAX_ERROR,
						Message.UNEXPECTED_END_OF_STATEMENT,
						tokenizer().current().text());

				node = null;
			} else {
				tokenizer().next(); // consume ending dot
			}
		} else {
			messageHandler().error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_STATEMENT,
					tokenizer().current().text());
		}

		return node;
	}
}
