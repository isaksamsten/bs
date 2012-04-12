package com.bs.parser.tree;

import java.util.Arrays;
import java.util.List;

import com.bs.interpreter.Interpreter;
import com.bs.parser.token.Token;

public class ListNode extends ExpressionNode {

	private ExpressionsNode expressions;

	public ListNode(Token token) {
		super(token);
	}

	public ListNode(int line, int position) {
		super(line, position);
	}

	public ExpressionsNode expressions() {
		return expressions;
	}

	public void expressions(ExpressionsNode expressions) {
		this.expressions = expressions;
	}

	@Override
	public String toTree() {
		return "List(values=["
				+ (expressions != null ? expressions.toTree() : "") + "])";
	}

	@Override
	public List<Node> childrens() {
		return Arrays.<Node> asList(expressions);
	}

	@Override
	public Object visit(Interpreter visitor) {
		return visitor.visitList(this);
	}

}
