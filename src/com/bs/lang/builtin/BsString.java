package com.bs.lang.builtin;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsString extends BsAbstractProto {

	public static BsObject clone(String str) {
		if (str == null) {
			return BsConst.Nil;
		}
		return BsObject.value(BsConst.String, str);
	}

	public BsString() {
		super(BsConst.Comparable, "String", BsString.class);
	}

	@BsRuntimeMessage(name = "length?", arity = 0)
	public BsObject length(BsObject self, BsObject... args) {
		return BsObject.value(BsConst.Number, self.value().toString().length());
	}

	@BsRuntimeMessage(name = "+", arity = 1, types = { BsString.class })
	public BsObject concat(BsObject self, BsObject... args) {
		String me = Bs.asString(self);
		String other = Bs.asString(args[0]);
		return BsString.clone(me + other);
	}

	@BsRuntimeMessage(name = "toInt", arity = 0)
	public BsObject toInt(BsObject self, BsObject... args) {
		try {
			return BsNumber.clone(Integer.parseInt((String) self.value()));
		} catch (Exception e) {
			return BsError.typeError("Can't convert '%s' to int", self.value());
		}
	}

	@BsRuntimeMessage(name = "toDouble", arity = 0)
	public BsObject toDouble(BsObject self, BsObject... args) {
		try {
			return BsNumber.clone(Double.parseDouble((String) self.value()));
		} catch (Exception e) {
			return BsError.typeError("Can't convert '%s' to int", self.value());
		}
	}

	@BsRuntimeMessage(name = "compareTo", arity = 1, aliases = { "<=>" }, types = { BsString.class })
	public BsObject compareTo(BsObject self, BsObject... args) {
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
