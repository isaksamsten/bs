package com.bs.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsNumber;
import com.bs.lang.BsObject;
import com.bs.lang.BsString;
import com.bs.parser.tree.ArgumentsNode;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.BlockNode;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.ExpressionNode;
import com.bs.parser.tree.ExpressionsNode;
import com.bs.parser.tree.IdentifierNode;
import com.bs.parser.tree.IdentifierNode.State;
import com.bs.parser.tree.ListNode;
import com.bs.parser.tree.MessageNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.parser.tree.Node;
import com.bs.parser.tree.NumberNode;
import com.bs.parser.tree.StatementsNode;
import com.bs.parser.tree.StringNode;

public class BsInterpreter implements Interpreter {

	private Stack stack;

	public BsInterpreter(Stack stack) {
		this.stack = stack;
	}

	public BsInterpreter(BsInterpreter inter) {
		this(inter.stack);
	}

	@Override
	public Object visitNumber(NumberNode numberNode) {
		return BsNumber.clone(numberNode.number());
	}

	@Override
	public Object visitVariable(IdentifierNode node) {
		if (node.state() == State.LOAD) {
			return stack.lookup(node.variable());
		}

		return node.variable();
	}

	@Override
	public Object visitString(StringNode stringNode) {
		return BsString.clone(stringNode.string());
	}

	@Override
	public Object visitExpression(ExpressionNode node) {
		return visit(node.left());
	}

	@Override
	public Object visitCall(CallNode node) {
		BsObject lhs = (BsObject) visit(node.left());
		Interpreter interpreter = new BsCall(this, lhs);
		return interpreter.visit(node.messages());
	}

	@Override
	public Object visitExpressions(ExpressionsNode node) {
		List<BsObject> objects = new ArrayList<BsObject>();
		for (Node expr : node.childrens()) {
			objects.add((BsObject) visit(expr));
		}

		return objects;
	}

	@Override
	public Object visitMessage(MessageNode messageNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitMessages(MessagesNode messagesNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatements(StatementsNode statementsNode) {
		BsObject last = null;
		for (Node n : statementsNode.childrens()) {
			last = (BsObject) visit(n);
		}
		return last;
	}

	@Override
	public Object visitAssign(AssignNode node) {
		String var = (String) visit(node.identifier());
		BsObject value = (BsObject) visit(node.expression());

		stack.local().var(var, value);

		return value;
	}

	@Override
	public Object visitBlock(BlockNode blockNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitArguments(ArgumentsNode argumentsNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Node node) {
		if (node != null)
			return node.visit(this);

		return null;
	}

	@Override
	public Object visitList(ListNode listNode) {
		// TODO Auto-generated method stub
		return null;
	}

}
