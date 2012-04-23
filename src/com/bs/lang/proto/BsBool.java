package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsProto;
import com.bs.lang.annot.BsRuntimeMessage;

@BsProto(name = "Bool")
public class BsBool extends BsObject {

	public BsBool() {
		super(BsConst.Proto, "Bool", BsBool.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "ifTrue", arity = -1)
	public BsObject isTrue(BsObject self, BsObject... args) {
		if (args.length > 0 && Bs.asBoolean(self)) {
			return args[0].invoke("call");
		} else if (args.length > 1) {
			return args[1].invoke("call");
		} else {
			return self;
		}
	}

	@BsRuntimeMessage(name = "ifFalse", arity = -1)
	public BsObject isFalse(BsObject self, BsObject... args) {
		if (args.length > 0 && !Bs.asBoolean(self)) {
			return args[0].invoke("call");
		} else if (args.length > 1) {
			return args[1].invoke("call");
		} else {
			return self;
		}
	}

	@BsRuntimeMessage(name = "and", arity = 1, aliases = { "&" })
	public BsObject and(BsObject self, BsObject... args) {
		return Bs.bool(Bs.asBoolean(self) && Bs.asBoolean(args[0]));
	}

	@BsRuntimeMessage(name = "or", arity = 1, aliases = { "/\\" })
	public BsObject or(BsObject self, BsObject... args) {
		return Bs.bool(Bs.asBoolean(self) || Bs.asBoolean(args[0]));
	}

	@BsRuntimeMessage(name = "negate", arity = 0)
	public BsObject negate(BsObject self, BsObject... args) {
		return Bs.negate(self);
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsError.raise("Can't clone");
	}

}
