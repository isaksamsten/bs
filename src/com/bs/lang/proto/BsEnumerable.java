package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsCodeData;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsEnumerable extends BsObject {

	public BsEnumerable() {
		super(BsConst.Proto, "Enumerable", BsEnumerable.class);
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		return BsError.raise("Subclass implementation");
	}

	@BsRuntimeMessage(name = "map", arity = 1)
	public BsObject map(BsObject self, final BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("map", args[0], BsConst.Block);
		}

		BsCodeData map = args[0].value();
		BsObject list = BsList.create();

		BsObject each = Bs.compile("list << block call e.", map.stack);
		each.slot("list", list);
		each.slot("block", args[0]);
		BsCodeData data = each.value();
		data.arguments.add("e");
		data.stack = map.stack;

		BsObject ret = self.invoke("each", each);
		if (ret.isError()) {
			return ret;
		}

		return list;
	}
}
