package com.bs.lang.proto;

import org.apache.commons.lang3.ArrayUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsCodeData;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsProto extends BsObject {

	public BsProto() {
		super(null, "Proto", BsProto.class);
		initRuntimeMethods();
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
			ret.slot("ignore", BsConst.True);
		}

		return ret;
	}

	@BsRuntimeMessage(name = "<=>", arity = 1)
	public BsObject compareTo(BsObject self, BsObject... args) {
		if (self.equals(args[0])) {
			return BsNumber.clone(0);
		}

		return BsNumber.clone(-1);
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

	@BsRuntimeMessage(name = "getSlot", arity = 1, aliases = { "->" })
	public BsObject getSlot(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return BsError.typeError("getSlot", args[0].prototype(),
					BsConst.Symbol);
		}
		return self.slot(Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "setSlot", arity = 2, aliases = { "<-" })
	public BsObject setSlot(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return BsError.typeError("setSlot", args[0].prototype(),
					BsConst.Symbol);
		}
		self.slot(Bs.asString(args[0]), args[1]);
		return self;
	}

	@BsRuntimeMessage(name = "isNil?", arity = 0)
	public BsObject isNil(BsObject self, BsObject... args) {
		return BsConst.False;
	}

	@BsRuntimeMessage(name = "ifNil", arity = 1)
	public BsObject ifNil(BsObject self, BsObject... args) {
		if (args[0].instanceOf(BsConst.Block)) {
			return self;
		}

		return BsError.typeError("ifNil", args[0], BsConst.Block);
	}

	@BsRuntimeMessage(name = "ifNonNil", arity = 1)
	public BsObject ifNonNil(BsObject self, BsObject... args) {
		if (args[0].instanceOf(BsConst.Block)) {
			return args[0].invoke("call");
		}

		return BsError.typeError("ifNonNil", args[0], BsConst.Block);
	}

	@BsRuntimeMessage(name = "pass", arity = 0)
	public BsObject pass(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "setMethod", arity = 2, aliases = { "<<=" })
	public BsObject addMethod(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return BsError.typeError("method", args[0].prototype(),
					BsConst.String);
		}
		if (!args[1].instanceOf(BsConst.Block)) {
			return BsError.typeError("method", args[0].prototype(),
					BsConst.Block);
		}
		BsCodeData data = args[1].value();
		self.message(Bs.asString(args[0]), data);

		return self;
	}

	@BsRuntimeMessage(name = "return", arity = 1)
	public BsObject returnit(BsObject self, BsObject... args) {
		args[0].setReturn(true);
		return args[0];
	}

	@BsRuntimeMessage(name = "init", arity = 1)
	public BsObject init(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = -1)
	public BsObject clone(BsObject self, BsObject... args) {
		BsObject obj = null;
		if (args.length > 0 && args[0].instanceOf(BsConst.Symbol)) {
			obj = new BsObject(self, Bs.asString(args[0]));
		} else {
			obj = new BsObject(self);
		}

		args = ArrayUtils.add(args, 0, obj);
		obj.invoke("init", args);
		return obj;
	}
}
