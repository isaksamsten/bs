package com.bs.lang.message;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsBlock;
import com.bs.lang.proto.BsError;

public class BsBlockCode implements BsCode {

	public BsCodeData data;

	public BsBlockCode(BsCodeData data) {
		this.data = data;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		if (data.arguments.size() == args.length) {
			for (int n = 0; n < args.length; n++) {
				self.setSlot(data.arguments.get(n), args[n]);
			}

			Stack stack = data.stack;
			stack.push(self);
			BsObject ret = Bs.eval(data.code, stack);
			stack.pop();
			if (ret.isBreak()) {
				self.setSlot(BsBlock.HAS_RETURNED, BsConst.True);
			}

			return ret;
		} else {
			return BsError.raise("Invalid arity");
		}
	}

	@Override
	public int getArity() {
		return data.arguments.size();
	}

	@Override
	public Stack getStack() {
		return data.stack;
	}

	@Override
	public void addArgument(String str) {
		data.arguments.add(str);
	}

	@Override
	public void setStack(Stack s) {
		data.stack = s;
	}

	@Override
	public boolean isInternal() {
		return false;
	}
}
