package com.bs.lang.proto;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsEnumerable extends BsObject {

	public BsEnumerable() {
		super(BsConst.Proto, "Enumerable", BsEnumerable.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		return BsError.raise("Subclass implementation");
	}
}
