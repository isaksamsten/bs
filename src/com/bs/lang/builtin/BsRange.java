package com.bs.lang.builtin;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsRange extends BsAbstractProto {

	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String STEP = "step";

	public static BsObject clone(Number min, Number max) {
		return clone(BsNumber.clone(min), BsNumber.clone(max));
	}

	public static BsObject clone(BsObject min, BsObject max) {
		BsObject obj = new BsObject(BsConst.Range);
		obj.setSlot(MIN, min);
		obj.setSlot(MAX, max);
		return obj;
	}

	public BsRange() {
		super(BsConst.Enumerable, "Range", BsRange.class);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone(Bs.asString(self.getSlot(MIN).invoke("toString"))
				+ "--" + Bs.asString(self.getSlot(MAX).invoke("toString")));
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("each", args[0], BsConst.Proto);
		}

		BsObject value = BsConst.Nil;
		BsObject min = self.getSlot(MIN);
		BsObject max = self.getSlot(MAX);
		BsObject block = args[0];

		while (Bs.asBoolean(min.invoke("<=", max))) {
			value = block.invoke("call", min);
			if (value.isBreakingContext()) {
				break;
			}

			min = min.invoke("next");
			if (min.isError()) {
				value = min;
				break;
			}

		}

		return value;
	}

	@BsRuntimeMessage(name = "init", arity = 2)
	public BsObject init(BsObject self, BsObject... args) {
		self.setSlot(MIN, args[0]);
		self.setSlot(MAX, args[1]);
		return self;
	}
}
