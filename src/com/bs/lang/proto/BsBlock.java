package com.bs.lang.proto;

import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
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
	public static final String HAS_RETURNED = "hasReturned";

	public BsBlock() {
		super(BsConst.Proto, "Block", BsBlock.class);
	}

	public BsBlock(Class<?> c) {
		super(BsConst.Proto, "Block", c);
	}

	@BsRuntimeMessage(name = "arity", arity = 0)
	public BsObject arity(BsObject self, BsObject... args) {
		return self.slot(ARITY);
	}

	@BsRuntimeMessage(name = "hasReturned", arity = 0)
	public BsObject hasReturned(BsObject self, BsObject... args) {
		return self.slot(HAS_RETURNED);
	}

	@BsRuntimeMessage(name = "setReturned", arity = 1)
	public BsObject setReturned(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Bool)) {
			return BsError.typeError("setReturned", args[0], BsConst.Bool);
		}
		self.slot(HAS_RETURNED, args[0]);
		return self;
	}

	@BsRuntimeMessage(name = "call", arity = -1)
	public BsObject call(BsObject self, BsObject... args) {
		return execute(self, args);
	}

	@BsRuntimeMessage(name = "whileTrue", arity = 1)
	public BsObject whileTrue(BsObject self, BsObject... args) {
		BsObject w = self.invoke("call");

		BsObject last = BsConst.False;
		while (Bs.asBoolean(w) && !Bs.asBoolean(args[0].slot(HAS_RETURNED))) {
			last = args[0].invoke("call");
			w = self.invoke("call");
			if (last.isBreak()) {
				return last;
			}
		}

		return last;
	}

	public BsObject execute(BsObject self, BsObject... args) {
		BsCodeData data = self.value();

		if (data.arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.slot(data.arguments.get(n), args[n]);
			}

			Stack stack = data.stack;
			stack.push(self);
			BsObject ret = Bs.eval(data.code, stack);
			stack.pop();
			if (ret.isReturn()) {
				ret.setReturn(false);
				self.slot(HAS_RETURNED, BsConst.True);
			}

			return ret;
		} else {
			return BsError.raise("Invalid arity");
		}
	}
}
