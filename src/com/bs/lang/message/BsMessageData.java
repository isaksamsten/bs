package com.bs.lang.message;

import com.bs.lang.BsObject;

public class BsMessageData {
	public BsCode code;
	public int arity;
	public String name;

	public BsMessageData(String name, int arity, BsCode code) {
		this.code = code;
		this.arity = arity;
		this.name = name;
	}

	public BsMessage getMessage(BsObject binder) {
		return new BsMessage(this, binder);
	}
}
