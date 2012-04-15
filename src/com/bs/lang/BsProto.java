package com.bs.lang;

import java.util.ArrayList;

public class BsProto extends BsObject {

	public BsProto() {
		super(null, "Proto", BsProto.class);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		Object value = self.name();
		if (self.value() != null) {
			value = self.value();
		}
		return BsObject.value(BsConst.String, value != null ? value.toString()
				: value);
	}

	@BsRuntimeMessage(name = "try", arity = 1)
	public BsObject tryit(BsObject self, BsObject... args) {
		BsObject block = args[0];
		BsObject ret = block.invoke("call");
		if (ret.isError()) {
			ret.var("ignore", BsConst.True);
		}

		return ret;
	}

	@BsRuntimeMessage(name = "=", arity = 1)
	public BsObject equal(BsObject self, BsObject... args) {
		return Bs.bool(self.equals(args[0]));
	}

	@BsRuntimeMessage(name = "!=", arity = 1)
	public BsObject notEqual(BsObject self, BsObject... args) {
		return Bs.negate(self.invoke("=", args));
	}

	@BsRuntimeMessage(name = "catch", arity = 2)
	public BsObject catchit(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "eval", arity = 1)
	public BsObject eval(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("eval", args[0].prototype(),
					BsConst.String);
		}
		return Bs.eval(Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "compile", arity = 1)
	public BsObject compile(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("compile", args[0].prototype(),
					BsConst.String);
		}
		return Bs.compile(Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "getSlot", arity = 1)
	public BsObject getSlot(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("getSlot", args[0].prototype(),
					BsConst.String);
		}
		return self.var(Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "setSlot", arity = 1)
	public BsObject setSlot(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("getSlot", args[0].prototype(),
					BsConst.String);
		}
		return self.var(Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "init", arity = 0)
	public BsObject init(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = -1)
	public BsObject clone(BsObject self, BsObject... args) {
		BsObject obj = new BsObject(self);
		obj.invoke("init", args);
		return obj;
	}
}
