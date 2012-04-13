package com.bs.lang;

public class BsTrue extends BsObject {

	public BsTrue() {
		super(Bs.Proto, "True", BsTrue.class);
	}

	@BsRuntimeMessage(name = "isTrue", arity = -1)
	public BsObject isTrue(BsObject self, BsObject... args) {
		System.out.println("isTrue");
		return self;
	}

	@BsRuntimeMessage(name = "isFalse", arity = -1)
	public BsObject isFalse(BsObject self, BsObject... args) {
		System.out.println("isFalse");
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		throw BsError.CANT_CLONE;
	}
}
