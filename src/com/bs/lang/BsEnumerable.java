package com.bs.lang;

public class BsEnumerable extends BsObject {

	public BsEnumerable() {
		super(Bs.Proto, "Enumerable", BsEnumerable.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		throw BsError.SUBCLASS_RESPONSIBILITY;
	}
}
