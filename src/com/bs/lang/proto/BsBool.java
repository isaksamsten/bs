package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsBool extends BsObject {

	public BsBool() {
		super(BsConst.Proto, "Bool", BsBool.class);
	}

	@BsRuntimeMessage(name = "isTrue", arity = -1)
	public BsObject isTrue(BsObject self, BsObject... args) {
		if (args.length > 0 && Bs.asBoolean(self)) {
			return args[0].invoke("call");
		} else if (args.length > 1) {
			return args[1].invoke("call");
		} else {
			return self;
		}
	}

	@BsRuntimeMessage(name = "isFalse", arity = -1)
	public BsObject isFalse(BsObject self, BsObject... args) {
		if (args.length > 0 && !Bs.asBoolean(self)) {
			return args[0].invoke("call");
		} else if (args.length > 1) {
			return args[1].invoke("call");
		} else {
			return self;
		}
	}

	@BsRuntimeMessage(name = "negate", arity = -1)
	public BsObject negate(BsObject self, BsObject... args) {
		return Bs.negate(self);
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsError.raise("Cant clone");
	}

}
