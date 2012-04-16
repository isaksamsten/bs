package com.bs.lang.proto;

import java.util.List;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsList extends BsObject {

	public BsList() {
		super(BsConst.Enumerable, "List", BsList.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		List<BsObject> data = self.value();
		BsObject block = args[0];

		BsObject last = BsConst.False;
		for (BsObject obj : data) {
			last = block.invoke("call", obj);
		}

		return last;
	}
}
