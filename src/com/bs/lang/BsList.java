package com.bs.lang;

import java.util.List;

public class BsList extends BsObject {

	public BsList() {
		super(Bs.Enumerable, "List", BsList.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		List<BsObject> data = self.value();
		BsObject block = args[0];

		BsObject last = Bs.False;
		for (BsObject obj : data) {
			last = block.invoke("call", obj);
		}

		return last;
	}
}
