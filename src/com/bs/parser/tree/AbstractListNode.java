package com.bs.parser.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bs.parser.token.Token;

public abstract class AbstractListNode<T extends Node> extends AbstractNode {

	private List<T> nodes = new ArrayList<T>();

	public AbstractListNode(Token token) {
		super(token);
	}

	public void add(T node) {
		nodes.add(node);
	}

	@Override
	public List<Node> childrens() {
		return Collections.<Node> unmodifiableList(nodes);
	}

	@Override
	public String toTree() {
		StringBuilder builder = new StringBuilder();
		for (T t : nodes) {
			builder.append(t.toTree());
			builder.append(", ");
		}
		if (nodes.size() > 1) {
			builder.replace(builder.length() - 2, builder.length(), "");
		}

		return builder.toString();
	}

}
