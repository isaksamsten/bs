package com.bs.interpreter;

import java.util.List;

import com.bs.lang.Bs;
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
	public Object interpretMessages(MessagesNode node) {
		for (Node n : node.childrens()) {
			receiver = (BsObject) interpret(n);
			if (receiver.isBreak()) {
				Bs.updateError(receiver, node);
				return receiver;
			}
		}

		return receiver;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object interpretMessage(MessageNode node) {
		Object exprs = interpret(node.expressions());
		if (exprs instanceof BsObject && ((BsObject) exprs).isBreak()) {
			return exprs;
		}

		String message = (String) interpret(node.identifier());
		List<BsObject> list = (List<BsObject>) exprs;
		BsObject[] arguments = new BsObject[0];

		if (list != null) {
			arguments = new BsObject[list.size()];
			for (int n = 0; n < arguments.length; n++) {
				BsObject o = list.get(n);
				if (o.isBreak()) {
					Bs.updateError(o, node);
					return o;
				} else {
					arguments[n] = o;
				}
			}
		}

		return receiver.invoke(message, arguments);
	}
}
