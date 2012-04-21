package com.bs.lang;

import com.bs.lang.proto.BsBlock;

public class BsMessageCode implements BsCode {
	private BsCodeData data;

	public BsMessageCode(BsCodeData data) {
		this.data = data;
	}

	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		return BsBlock.execute(data, self, args);
	}

	@Override
	public BsMessage getMessage(String name, int arity, BsObject binder) {
		return new BsBoundMessage(name, arity, this, binder);
	}

}
