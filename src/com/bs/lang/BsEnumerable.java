package com.bs.lang;

public class BsEnumerable extends BsObject {

	public BsEnumerable() {
		super(BsConst.Proto, "Enumerable", BsEnumerable.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		return BsError.raise("Subclass implementation");
	}
}
