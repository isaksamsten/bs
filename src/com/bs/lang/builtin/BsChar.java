package com.bs.lang.builtin;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsProto;
import com.bs.lang.annot.BsRuntimeMessage;

@BsProto(name = "Char")
public class BsChar extends BsAbstractProto {

	public static BsObject clone(char c) {
		return BsObject.value(BsConst.Char, c);
	}

	public BsChar() {
		super(BsConst.Comparable, "Char", BsChar.class);
	}

	@BsRuntimeMessage(name = "next", arity = 0)
	public BsObject next(BsObject self, BsObject... args) {
		char c = self.value();
		return clone((char) (c + 1));
	}

	@BsRuntimeMessage(name = "prev", arity = 0)
	public BsObject prev(BsObject self, BsObject... args) {
		char c = self.value();
		return clone((char) (c - 1));
	}

	@BsRuntimeMessage(name = "--", arity = 1)
	public BsObject range(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Char)) {
			return BsError.typeError("--", args[0], BsConst.Number);
		}

		return BsRange.clone(self, args[0]);
	}

	@BsRuntimeMessage(name = "compareTo", arity = 1, aliases = { "<=>" })
	public BsObject compareTo(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Char)) {
			return BsError.typeError("compareTo", args[0], BsConst.String);
		}

		return BsNumber.clone(Character.compare(Bs.asCharacter(self),
				Bs.asCharacter(args[0])));
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone(Bs.asCharacter(self) + "");
	}
}
