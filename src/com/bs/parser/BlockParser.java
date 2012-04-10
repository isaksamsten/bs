package com.bs.parser;

import com.bs.parser.token.Token;
import com.bs.parser.token.TokenType;
import com.bs.parser.tree.ArgumentsNode;
import com.bs.parser.tree.BlockNode;
import com.bs.parser.tree.StatementsNode;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageType;

public class BlockParser extends BsParser<BlockNode> {

	public BlockParser(BsParser<?> parser) {
		super(parser);
	}

	@Override
	public BlockNode parse(Token start) {
		BlockNode node = null;
		if (start.type() == TokenType.LEFT_BRACKET) {
			node = nodeFactory().block(start);
			Token next = tokenizer().next();
			if (next.type() == TokenType.PIPE) {
				ArgumentsParser args = new ArgumentsParser(this);
				ArgumentsNode arguments = args.parse();

				node.arguments(arguments);
				if (tokenizer().current().type() != TokenType.PIPE) {
					MessageHandler.error(tokenizer().current(),
							MessageType.SYNTAX_ERROR, Message.UNEXPECTED_BLOCK,
							tokenizer().current().text());
				} else {
					tokenizer().next();
				}
			}

			StatementsParser parser = new StatementsParser(this);
			StatementsNode statements = parser.parse(tokenizer().current());
			if (statements != null
					&& tokenizer().current().type() == TokenType.RIGHT_BRACKET) {
				node.statements(statements);
				tokenizer().next(); // consume right bracket
			} else {
				MessageHandler.error(tokenizer().current(),
						MessageType.SYNTAX_ERROR, Message.UNEXPECTED_BLOCK,
						tokenizer().current().text());
			}
		} else {
			MessageHandler.error(tokenizer().current(),
					MessageType.SYNTAX_ERROR, Message.UNEXPECTED_BLOCK,
					tokenizer().current().text());
		}
		return node;
	}
}
