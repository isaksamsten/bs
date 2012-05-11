package com.bs.interpreter;

import java.util.ArrayList;
import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.message.BsCodeData;
import com.bs.lang.proto.BsBlock;
import com.bs.lang.proto.BsChar;
import com.bs.lang.proto.BsNumber;
import com.bs.lang.proto.BsString;
import com.bs.lang.proto.BsSymbol;
import com.bs.parser.tree.ArgumentsNode;
import com.bs.parser.tree.AssignNode;
import com.bs.parser.tree.BlockNode;
import com.bs.parser.tree.CallNode;
import com.bs.parser.tree.CharacterNode;
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
import com.bs.parser.tree.SymbolNode;

public class BsInterpreter implements Interpreter {

	private Stack stack;
	private Node lastNode;

	public BsInterpreter(Stack stack) {
		this.stack = stack;
	}

	public BsInterpreter() {
		this(BsStack.getDefault());
	}

	public BsInterpreter(BsInterpreter interpreter) {
		this(interpreter.stack);
	}

	@Override
	public Object interpretNumber(NumberNode numberNode) {
		return BsNumber.clone(numberNode.number());
	}

	@Override
	public Object interpretVariable(IdentifierNode node) {
		if (node.state() == State.LOAD) {
			BsObject value = stack.lookup(node.variable());
			return value;
		}

		return node.variable();
	}

	@Override
	public Object interpretString(StringNode stringNode) {
		return BsString.clone(stringNode.string());
	}

	@Override
	public Object interpretExpression(ExpressionNode node) {
		return interpret(node.left());
	}

	@Override
	public Object interpretCall(CallNode node) {
		BsObject lhs = (BsObject) interpret(node.left());

		/*
		 * Null left hand side == call without a receiver
		 */
		if (lhs != null && lhs.isBreakingContext()) {
			Bs.updateError(lhs, node);
			return lhs;
		}

		/*
		 * Thus, set lhs to the local object if lhs is null
		 */
		lhs = (BsObject) (lhs != null ? lhs : stack.local());
		Interpreter interpreter = new BsCallInterpreter(this, lhs);
		return interpreter.interpret(node.messages());
	}

	@Override
	public Object interpretExpressions(ExpressionsNode node) {
		List<BsObject> objects = new ArrayList<BsObject>();
		for (Node expr : node.childrens()) {
			BsObject obj = (BsObject) interpret(expr);
			if (obj.isBreakingContext()) {
				return obj;
			}
			objects.add(obj);
		}

		return objects;
	}

	@Override
	public Object interpretMessage(MessageNode messageNode) {
		throw new UnsupportedOperationException("Handled in overriden method.");
	}

	@Override
	public Object interpretMessages(MessagesNode messagesNode) {
		throw new UnsupportedOperationException("Handled in overriden method.");
	}

	@Override
	public Object interpretStatements(StatementsNode node) {
		BsObject last = null;
		for (Node n : node.childrens()) {
			last = (BsObject) interpret(n);
			if (last.isError()) {
				Bs.updateError(last, node);
				return last;
			}
			if (last.isReturning()) {
				return last;
			}
		}
		return last;
	}

	@Override
	public Object interpretAssign(AssignNode node) {
		String var = (String) interpret(node.identifier());
		BsObject value = (BsObject) interpret(node.expression());

		if (value.isError()) {
			Bs.updateError(value, node);
			return value;
		}
		// if (Character.isUpperCase(var.charAt(0))) {
		// stack.enterGlobal(var, value);
		// } else {
		stack.enter(var, value);
		// }

		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object interpretBlock(BlockNode node) {
		List<String> args = (List<String>) interpret(node.arguments());
		if (args == null) {
			args = new ArrayList<String>();
		}

		BsCodeData data = new BsCodeData(args, node.statements());
		data.stack = stack.clone();
		if (node.isVariable()) {
			data.arity = -1;
		}

		BsObject block = BsBlock.create(data);

		return block;
	}

	@Override
	public Object interpretArguments(ArgumentsNode argumentsNode) {
		List<String> args = new ArrayList<String>();
		for (Node n : argumentsNode.childrens()) {
			String arg = (String) interpret(n);
			args.add(arg);
		}

		return args;
	}

	@Override
	public Object interpret(Node node) {
		if (node != null) {
			lastNode = node;
			return node.visit(this);
		}

		return null;
	}

	@Override
	public Object interpretList(ListNode node) {
		Object objects = interpret(node.expressions());
		if (objects instanceof BsObject && ((BsObject) objects).isError()) {
			Bs.updateError((BsObject) objects, node);
			return objects;
		}
		if (objects == null) {
			objects = new ArrayList<BsObject>();
		}

		BsObject list = BsObject.value(BsConst.List, objects);
		return list;
	}

	@Override
	public Object interpretSymbol(SymbolNode node) {
		return BsSymbol.get(node.string());
	}

	@Override
	public Object interpretCharacter(CharacterNode node) {
		return BsChar.clone((Character) node.value());
	}

	public Node lastNode() {
		return lastNode;
	}
}
