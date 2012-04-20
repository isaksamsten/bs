package com.bs.lang;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.proto.BsError;

public class BsMessageProxy implements BsCode {
	private BsCodeData data;

	public BsMessageProxy(BsCodeData data) {
		this.data = data;
	}

	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		if (data.arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.slot(data.arguments.get(n), args[n]);
			}

			Stack stack = data.stack;
			stack.push(self);
			BsObject ret = Bs.eval(data.code, stack);
			stack.pop();

			return ret;
		} else {
			return BsError.raise("Invalid arity");
		}
	}

	@Override
	public BsMessage getMessage(String name, int arity, BsObject binder) {
		return new BsBoundMessage(name, arity, this, binder);
	}

}
