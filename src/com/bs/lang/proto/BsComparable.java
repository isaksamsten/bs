package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsComparable extends BsObject {

	public BsComparable() {
		super(BsConst.Proto, "Comparable", BsComparable.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "equal", arity = 1, aliases = { "=" })
	public BsObject equal(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() == 0);
		} else {
			return BsError.typeError("lessThan", other[0], BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "notEqual", arity = 1, aliases = { "!=" })
	public BsObject notEqual(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() != 0);
		} else {
			return BsError.typeError("lessThan", other[0], BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "lessThan", arity = 1, aliases = { "<" })
	public BsObject lessThan(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() < 0);
		} else {
			return BsError.typeError("lessThan", other[0], BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "greaterThan", arity = 1, aliases = { ">" })
	public BsObject greaterThan(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() > 0);
		} else {
			return BsError.typeError("greaterThan", other[0], BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "greaterThanEqual", arity = 1, aliases = { ">=" })
	public BsObject greaterThanEqual(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() >= 0);
		} else {
			return BsError.typeError("greaterThanEqual", other[0],
					BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "lessThanEqual", arity = 1, aliases = { "<=" })
	public BsObject lessThanEqual(BsObject self, BsObject... other) {
		BsObject cmp = self.invoke("compareTo", other[0]);
		if (cmp.instanceOf(BsConst.Number)) {
			return Bs.bool(Bs.asNumber(cmp).intValue() <= 0);
		} else {
			return BsError.typeError("lessThanEqual", other[0], BsConst.Number);
		}
	}

	@BsRuntimeMessage(name = "compareTo", arity = 1, aliases = { "<=>" })
	public BsObject compareTo(BsObject self, BsObject... other) {
		return BsError.subClassResponsibility();
	}

}
