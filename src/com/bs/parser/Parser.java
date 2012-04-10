package com.bs.parser;

import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.Token;
import com.bs.parser.tree.Node;
import com.bs.parser.tree.NodeFactory;

public interface Parser<T extends Node> {

	NodeFactory nodeFactory();

	Tokenizer tokenizer();

	T parse();

	T parse(Token start);
}
