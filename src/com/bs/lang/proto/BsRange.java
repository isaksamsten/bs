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
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("each", args[0], BsConst.Proto);
		}

		BsObject value = BsConst.Nil;
		BsObject min = self.slot(MIN);
		BsObject max = self.slot(MAX);
		BsObject block = args[0];

		while (Bs.asBoolean(min.invoke("<=", max))) {
			value = block.invoke("call", min);
			if (value.isBreak()) {
				break;
			}

			min = min.invoke("next");
			if (min.isError()) {
				value = min;
				break;
			}

		}

		// BsObject value = BsConst.False;
		// int min = Bs.asNumber(self.slot(MIN)).intValue(), max = Bs.asNumber(
		// self.slot(MAX)).intValue();
		//
		// for (int n = min; n < max
		// && !Bs.asBoolean(args[0].slot(BsBlock.HAS_RETURNED)); n++) {
		// value = args[0].invoke("call", BsNumber.clone(n));
		// if (value.isError()) {
		// return value;
		// }
		// }

		return value;
	}

	@BsRuntimeMessage(name = "init", arity = 2)
	public BsObject init(BsObject self, BsObject... args) {
		self.slot(MIN, args[0]);
		self.slot(MAX, args[1]);
		return self;
	}
}
