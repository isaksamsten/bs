package com.bs.lang;

public class BsFalse extends BsObject {
	public BsFalse() {
		super(BsConst.Proto, "False", BsFalse.class);
	}

	@BsRuntimeMessage(name = "isTrue", arity = -1)
	public BsObject isTrue(BsObject self, BsObject... args) {
		System.out.println("isFalse");
		return self;
	}

	@BsRuntimeMessage(name = "isFalse", arity = -1)
	public BsObject isFalse(BsObject self, BsObject... args) {
		System.out.println("isFalse");
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsError.raise("Cant clone");
	}

}
