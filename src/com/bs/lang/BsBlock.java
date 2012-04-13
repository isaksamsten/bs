package com.bs.lang;

import java.util.List;

import com.bs.interpreter.BsInterpreter;
import com.bs.interpreter.stack.BsStack;
import com.bs.parser.tree.StatementsNode;

public class BsBlock extends BsObject {

	public static BsObject create(List<String> args, StatementsNode statements) {
		return BsObject.value(Bs.Block, new Object[] { args, statements });
	}

	public BsBlock() {
		super(Bs.Proto, "Block", BsBlock.class);
	}

	@SuppressWarnings("unchecked")
	@BsRuntimeMessage(name = "call", arity = -1)
	public BsObject call(BsObject self, BsObject... args) {
		Object[] data = self.value();
		List<String> arguments = (List<String>) data[0];
		StatementsNode node = (StatementsNode) data[1];

		if (arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.var(arguments.get(n), args[n]);
			}

			BsStack.instance().push(self);
			BsInterpreter interpreter = new BsInterpreter();
			BsObject ret = (BsObject) interpreter.visit(node);
			BsStack.instance().pop();

			return ret;
		} else {
			throw BsError.INVALID_ARITY;
		}
	}
}
