package com.bs.lang;

import java.util.List;

import com.bs.parser.tree.Node;

public class BsCodeData {

	public List<String> arguments;
	public Node code;

	public BsCodeData(List<String> arguments, Node code) {
		this.arguments = arguments;
		this.code = code;
	}
}
