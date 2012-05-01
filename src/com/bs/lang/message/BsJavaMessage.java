package com.bs.lang.message;

import com.bs.lang.BsObject;

public class BsJavaMessage extends BsMessage {

	public BsJavaMessage(BsMessageData data, BsObject binder) {
		super(data, binder);
	}

	@Override
	protected BsObject execute(BsObject... args) {
		return getData().code.invoke(getBinder(), args);
	}

}
