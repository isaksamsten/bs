package com.bs.lang;

class BsMessageData {
	public BsCode code;
	public int arity;
	public String name;

	public BsMessageData(BsCode code, int arity, String name) {
		this.code = code;
		this.arity = arity;
		this.name = name;
	}

}
