package com.bs.lang.proto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsList extends BsObject {

	public BsList() {
		super(BsConst.Enumerable, "List", BsList.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		List<BsObject> data = self.value();
		BsObject block = args[0];

		BsObject last = BsConst.False;
		for (BsObject obj : data) {
			last = block.invoke("call", obj);
			if (Bs.asBoolean(block.slot(BsBlock.HAS_RETURNED))) {
				break;
			}
		}

		return last;
	}

	@BsRuntimeMessage(name = "<<", arity = 1)
	public BsObject add(BsObject self, BsObject... args) {
		List<BsObject> value = self.value();
		value.add(args[0]);
		return self;
	}

	public static BsObject create() {
		return BsObject.value(BsConst.List, new ArrayList<BsObject>());
	}

	public static BsObject create(BsObject... args) {
		return BsObject.value(BsConst.List, Arrays.asList(args));
	}
}
