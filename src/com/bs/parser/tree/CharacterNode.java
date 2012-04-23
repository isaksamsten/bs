package com.bs.parser.tree;

import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class CharacterNode extends AbstractNode implements LiteralNode {

	private char value;

	public CharacterNode(Token token, char value) {
		super(token);
		this.value = value;
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.interpretCharacter(this);
	}

	@Override
	public List<Node> childrens() {
		return null;
	}

	@Override
	public String toTree() {
		return "Character(value=" + value + ")";
	}

	@Override
	public Object value() {
		return value;
	}

}
