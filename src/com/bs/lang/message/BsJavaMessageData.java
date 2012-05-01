package com.bs.lang.message;

import com.bs.lang.BsObject;

public class BsJavaMessageData extends BsMessageData {

	public BsJavaMessageData(String name, int arity, BsCode code) {
		super(name, arity, code);
	}

	@Override
	public BsMessage getMessage(BsObject binder) {
		return new BsJavaMessage(this, binder);
	}

}
