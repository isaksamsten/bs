package com.bs.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsBlock;
import com.bs.lang.BsConst;
import com.bs.lang.BsError;
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

	public BsInterpreter() {
		this(BsStack.instance());
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
			Object value = stack.lookup(node.variable());
			if (value == null) {
				return BsError.nameError(node.variable());
			} else {
				return value;
			}
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
		if (lhs.isError()) {
			return lhs;
		}

		Interpreter interpreter = new BsCallInterpreter(this, lhs);
		return interpreter.visit(node.messages());
	}

	@Override
	public Object visitExpressions(ExpressionsNode node) {
		List<BsObject> objects = new ArrayList<BsObject>();
		for (Node expr : node.childrens()) {
			BsObject obj = (BsObject) visit(expr);
			if (obj.isError()) {
				return obj;
			}
			objects.add(obj);
		}

		return objects;
	}

	@Override
	public Object visitMessage(MessageNode messageNode) {
		throw new UnsupportedOperationException("Handled in overriden method.");
	}

	@Override
	public Object visitMessages(MessagesNode messagesNode) {
		throw new UnsupportedOperationException("Handled in overriden method.");
	}

	@Override
	public Object visitStatements(StatementsNode statementsNode) {
		BsObject last = null;
		for (Node n : statementsNode.childrens()) {
			last = (BsObject) visit(n);
			if (last.isError()) {
				return last;
			}
		}
		return last;
	}

	@Override
	public Object visitAssign(AssignNode node) {
		String var = (String) visit(node.identifier());
		BsObject value = (BsObject) visit(node.expression());

		if (value.isError()) {
			return value;
		}
		stack.local().var(var, value);

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitBlock(BlockNode blockNode) {
		List<String> args = (List<String>) visit(blockNode.arguments());
		if (args == null) {
			args = new ArrayList<String>();
		}
		BsObject block = BsBlock.create(args, blockNode.statements());
		return block;
	}

	@Override
	public Object visitArguments(ArgumentsNode argumentsNode) {
		List<String> args = new ArrayList<String>();
		for (Node n : argumentsNode.childrens()) {
			String arg = (String) visit(n);
			args.add(arg);
		}

		return args;
	}

	@Override
	public Object visit(Node node) {
		if (node != null)
			return node.visit(this);

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitList(ListNode node) {
		List<BsObject> objects = (List<BsObject>) visit(node.expressions());
		BsObject list = BsObject.value(BsConst.List, objects);
		return list;
	}

}
