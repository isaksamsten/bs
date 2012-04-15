package com.bs.lang;

import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.parser.tree.Node;

public class BsBlock extends BsObject implements BsCode {

	public static BsObject create(List<String> args, Node statements) {
		return BsObject.value(BsConst.Block, new Object[] { args, statements });
	}

	public BsBlock() {
		super(BsConst.Proto, "Block", BsBlock.class);
	}

	@BsRuntimeMessage(name = "call", arity = -1)
	public BsObject call(BsObject self, BsObject... args) {
		return execute(self, args);
	}

	@BsRuntimeMessage(name = "whileTrue", arity = 1)
	public BsObject whileTrue(BsObject self, BsObject... args) {
		BsObject w = self.invoke("call");

		BsObject last = BsConst.False;
		while (Bs.isTrue(w)) {
			last = args[0].invoke("call");
			w = self.invoke("call");
		}

		return last;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		Object[] data = self.value();
		List<String> arguments = (List<String>) data[0];
		Node node = (Node) data[1];

		if (arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.var(arguments.get(n), args[n]);
			}

			BsStack.instance().push(self);
			BsObject ret = Bs.eval(node);
			BsStack.instance().pop();

			return ret;
		} else {
			return BsError.raise("Invalid arity");
		}
	}
}
