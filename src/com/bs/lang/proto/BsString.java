package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsString extends BsObject {

	public static BsObject clone(String str) {
		return BsObject.value(BsConst.String, str);
	}

	public BsString() {
		super(BsConst.Comparable, "String", BsString.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "length", arity = 0)
	public BsObject length(BsObject self, BsObject... args) {
		return BsObject.value(BsConst.Number, self.value().toString().length());
	}

	@BsRuntimeMessage(name = "+", arity = 1)
	public BsObject concat(BsObject self, BsObject... args) {
		String me = Bs.asString(self);
		String other = Bs.asString(args[0]);
		return BsString.clone(me + other);
	}

	@BsRuntimeMessage(name = "compareTo", arity = 1, aliases = { "<=>" })
	public BsObject compareTo(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("compareTo", args[0], BsConst.String);
		}

		return BsNumber
				.clone(Bs.asString(self).compareTo(Bs.asString(args[0])));
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = 1)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsObject.value(self, args[0].toString());
	}
}
