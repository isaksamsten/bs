package com.bs.lang.message;

import com.bs.interpreter.stack.BreakFrame;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsMessageCode extends BsBlockCode {
	public BsMessageCode(BsCodeData data) {
		super(data);
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		BsObject scope = BsObject.clone(BsConst.Block); // local scope
		scope.setSlot("self", self);

		data.stack.push(BreakFrame.BREAK);
		data.stack.push(self);
		BsObject obj = super.invoke(scope, args);
		data.stack.pop();
		data.stack.pop();
		return obj;
	}
}
