package com.bs.lang.proto;

import java.util.ArrayList;
import java.util.List;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsList extends BsAbstractProto {

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
			if (last.isBreakingContext()) {
				break;
			}
		}

		return last;
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		ArrayList<BsObject> value = self.value();

		return BsString.clone(value.toString());
	}

	@BsRuntimeMessage(name = "size?", arity = 0)
	public BsObject size(BsObject self, BsObject... args) {
		ArrayList<BsObject> value = self.value();
		return BsNumber.clone(value.size());
	}

	@BsRuntimeMessage(name = "add", arity = 1, aliases = { "<<" })
	public BsObject add(BsObject self, BsObject... args) {
		ArrayList<BsObject> value = self.value();
		value.add(args[0]);
		return self;
	}

	@BsRuntimeMessage(name = "addAll", arity = 1, aliases = { "++" })
	public BsObject addAll(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.List)) {
			return BsError.typeError("addAll", args[0], BsConst.List);
		}

		List<BsObject> value = self.value();
		List<BsObject> other = args[0].value();
		value.addAll(other);
		return self;
	}

	@BsRuntimeMessage(name = "insert", arity = 2)
	public BsObject insert(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Number)) {
			return BsError.typeError("insert", args[0], BsConst.Number);
		}

		List<BsObject> value = self.value();
		value.add(Bs.asNumber(args[0]).intValue(), args[0]);
		return self;
	}

	public static BsObject create() {
		return BsObject.value(BsConst.List, new ArrayList<BsObject>());
	}

	public static BsObject create(List<BsObject> list) {
		return BsObject.value(BsConst.List, list);
	}
}
