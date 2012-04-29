package com.bs.lang;

import com.bs.interpreter.stack.BreakFrame;
import com.bs.lang.proto.BsBlock;

public class BsMessageCode implements BsCode {
	private BsCodeData data;

	public BsMessageCode(BsCodeData data) {
		this.data = data;
	}

	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		BsObject scope = BsObject.clone(BsConst.Block); // external scope
		scope.setSlot("self", self);

		data.stack.push(BreakFrame.BREAK);
		data.stack.push(self);
		BsObject obj = BsBlock.execute(data, scope, args);
		data.stack.pop();
		data.stack.pop();
		return obj;
	}

	@Override
	public BsMessage getMessage(String name, int arity, BsObject binder) {
		return new BsBoundMessage(name, arity, this, binder);
	}

}
