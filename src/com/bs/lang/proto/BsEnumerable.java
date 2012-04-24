package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsCodeData;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsEnumerable extends BsObject {

	public BsEnumerable() {
		super(BsConst.Proto, "Enumerable", BsEnumerable.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "each", arity = 1)
	public BsObject each(BsObject self, BsObject... args) {
		return BsError.raise("Subclass implementation");
	}

	@BsRuntimeMessage(name = "map", arity = 1)
	public BsObject map(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("map", args[0], BsConst.Block);
		}

		return each("list << block call e.", self, args[0]);
	}

	@BsRuntimeMessage(name = "filter", arity = 1)
	public BsObject filter(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("filter", args[0], BsConst.Block);
		}

		return each("(block call e) ifTrue { list << e. }.", self, args[0]);
	}
	
	@BsRuntimeMessage(name = "any?", arity = 1)
	public BsObject any(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("any?", args[0], BsConst.Block);
		}

		return each("(block call e) ifTrue { return(True). }.", self, args[0]);
	}
	
	@BsRuntimeMessage(name = "all?", arity = 1)
	public BsObject all(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("any?", args[0], BsConst.Block);
		}

		return each("(block call e) ifFalse { return(False). }.", self, args[0]);
	}
	

	/**
	 * Iterate over subclass, executing code in context of each object
	 * 
	 * In code, e refer to current element, block to the block and list to a
	 * list for collecting return values
	 * 
	 * @param code
	 * @param self
	 * @param block
	 * @return
	 */
	protected BsObject each(String code, BsObject self, BsObject block) {
		BsCodeData mapData = block.value();
		BsObject list = BsList.create();

		BsObject each = Bs.compile(code, mapData.stack);
		each.slot("list", list);
		each.slot("block", block);
		BsCodeData data = each.value();
		data.arguments.add("e");
		data.stack = mapData.stack;

		BsObject ret = self.invoke("each", each);
		if (ret.isError()) {
			return ret;
		}

		return list;
	}
}
