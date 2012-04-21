package com.bs.parser.tree;

import java.util.List;

import com.bs.interpreter.Interpreter;

public interface Node {

	Object visit(Interpreter visitor);

	Node parent();

	String code();

	int line();

	int position();

	List<Node> childrens();

	String toTree();
}
