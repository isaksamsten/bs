package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsRange extends BsObject {

	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String STEP = "step";

	public static BsObject clone(Number min, Number max) {
		return clone(BsNumber.clone(min), BsNumber.clone(max));
	}

	public static BsObject clone(BsObject min, BsObject max) {
		BsObject obj = new BsObject(BsConst.Range);
		obj.slot(MIN, min);
		obj.slot(MAX, max);
		return obj;
	}

	public BsRange() {
		super(BsConst.Enumerable, "Range", BsRange.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("each", args[0], BsConst.Proto);
		}

		BsObject value = BsConst.False;
		int min = Bs.asNumber(self.slot(MIN)).intValue(), max = Bs.asNumber(
				self.slot(MAX)).intValue();

		for (int n = min; n < max; n++) {
			value = args[0].invoke("call", BsNumber.clone(n));
			if(value.isBreak()) {
				return value;
			}
		}

		return value;
	}
}
