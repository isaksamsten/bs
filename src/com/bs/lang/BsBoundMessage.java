package com.bs.lang;

import org.apache.commons.lang3.ArrayUtils;

public class BsBoundMessage extends BsMessage {

	private BsObject binder;

	public BsBoundMessage(String name, int arity, BsCode code, BsObject binder) {
		super(name, arity, code);
		this.binder = binder;
	}
	
	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		args = ArrayUtils.add(args, binder);
		return super.invoke(self, args);
	}

}
