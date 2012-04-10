package com.bs.parser;

import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.Token;
import com.bs.parser.tree.Node;
import com.bs.parser.tree.NodeFactory;

public abstract class BsParser<T extends Node> implements Parser<T> {

	private Tokenizer tokenizer;
	private NodeFactory nodeFactory;

	public BsParser(Tokenizer tokenizer, NodeFactory nodeFactory) {
		this.tokenizer = tokenizer;
		this.nodeFactory = nodeFactory;
	}

	public BsParser(BsParser<?> parser) {
		this(parser.tokenizer, parser.nodeFactory);
	}

	@Override
	public NodeFactory nodeFactory() {
		return nodeFactory;
	}

	@Override
	public Tokenizer tokenizer() {
		return tokenizer;
	}

	/**
	 * Detault implementation, same as
	 * <code>return parse(tokenizer().next())</code>
	 */
	@Override
	public T parse() {
		return parse(tokenizer().next());
	}

	@Override
	public T parse(Token start) {
		return null;
	}

}
