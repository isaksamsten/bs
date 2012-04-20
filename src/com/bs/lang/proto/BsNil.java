package com.bs.lang.proto;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsNil extends BsObject {

	public BsNil() {
		super(BsConst.Proto, "Nil", BsNil.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone("Nil");
	}

	@BsRuntimeMessage(name = "ifNil", arity = 1)
	public BsObject ifNil(BsObject self, BsObject... args) {
		if (args[0].instanceOf(BsConst.Block)) {
			return args[0].invoke("call");
		}

		return BsError.typeError("ifNil", args[0], BsConst.Block);
	}
}
