package com.bs.lang.proto;

import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.lang.Bs;
import com.bs.lang.BsCodeData;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.parser.tree.Node;

public class BsBlock extends BsObject {

	public static BsObject create(List<String> args, Node statements) {
		BsObject obj = BsObject.value(BsConst.Block, new BsCodeData(args,
				statements));
		obj.slot(ARITY, BsNumber.clone(args.size()));
		return obj;
	}

	public static final String ARITY = "arity";

	public BsBlock() {
		super(BsConst.Proto, "Block", BsBlock.class);
	}

	@BsRuntimeMessage(name = "arity", arity = 0)
	public BsObject arity(BsObject self, BsObject... args) {
		return slot(ARITY);
	}

	@BsRuntimeMessage(name = "call", arity = -1)
	public BsObject call(BsObject self, BsObject... args) {
		return execute(self, args);
	}

	@BsRuntimeMessage(name = "whileTrue", arity = 1)
	public BsObject whileTrue(BsObject self, BsObject... args) {
		BsObject w = self.invoke("call");

		BsObject last = BsConst.False;
		while (Bs.asBoolean(w)) {
			last = args[0].invoke("call");
			w = self.invoke("call");
		}

		return last;
	}

	public BsObject execute(BsObject self, BsObject... args) {
		BsCodeData data = self.value();

		if (data.arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.slot(data.arguments.get(n), args[n]);
			}

			BsStack.getDefault().push(self);
			BsObject ret = Bs.blockEval(data.code);
			BsStack.getDefault().pop();

			return ret;
		} else {
			return BsError.raise("Invalid arity");
		}
	}
}
