package com.bs.lang.proto.java;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsJava extends BsObject {

	public BsJava() {
		super(BsConst.Proto, "Java", BsJava.class);
	}

	@BsRuntimeMessage(name = "messageMissing", arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "new", arity = 1)
	public BsObject new_(BsObject self, BsObject... args) {
		return self;
	}
}
