package com.bs.interpreter;

import java.util.List;

import com.bs.lang.BsObject;
import com.bs.parser.tree.MessageNode;
import com.bs.parser.tree.MessagesNode;
import com.bs.parser.tree.Node;

public class BsCallInterpreter extends BsInterpreter {

	private BsObject receiver;

	public BsCallInterpreter(BsInterpreter inter, BsObject receiver) {
		super(inter);
		this.receiver = receiver;
	}

	@Override
	public Object visitMessages(MessagesNode node) {
		for (Node n : node.childrens()) {
			receiver = (BsObject) visit(n);
			if(receiver.isBreak()) {
				return receiver;
			}
		}

		return receiver;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitMessage(MessageNode node) {
		Object exprs = visit(node.expressions());
		if (exprs instanceof BsObject && ((BsObject) exprs).isBreak()) {
			return exprs;
		}

		String message = (String) visit(node.identifier());
		List<BsObject> list = (List<BsObject>) exprs;
		BsObject[] arguments = new BsObject[0];
		if (list != null) {
			arguments = list.toArray(new BsObject[0]);
		}

		return receiver.invoke(message, arguments);
	}

}
