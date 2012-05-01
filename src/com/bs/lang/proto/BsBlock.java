package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsProto;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsBlockCode;
import com.bs.lang.message.BsCode;
import com.bs.lang.message.BsCodeData;

@BsProto(name = "Block")
public class BsBlock extends BsAbstractProto {

	public static BsObject create(BsCodeData data) {
		return create(new BsBlockCode(data));
	}

	public static BsObject create(BsCode code) {
		BsObject obj = BsObject.value(BsConst.Block, code);
		return obj;
	}

	public static final String ARITY = "arity";
	public static final String HAS_RETURNED = "hasReturned";

	public BsBlock() {
		super(BsConst.Proto, "Block", BsBlock.class);
	}

	public BsBlock(Class<?> c) {
		super(BsConst.Proto, "Block", c);
	}

	@BsRuntimeMessage(name = "getArity", arity = 0)
	public BsObject arity(BsObject self, BsObject... args) {
		return BsNumber.clone(((BsCode) self.value()).getArity());
	}

	@BsRuntimeMessage(name = "hasReturned", arity = 0)
	public BsObject hasReturned(BsObject self, BsObject... args) {
		return self.getSlot(HAS_RETURNED);
	}

	@BsRuntimeMessage(name = "setReturned", arity = 1)
	public BsObject setReturned(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Bool)) {
			return BsError.typeError("setReturned", args[0], BsConst.Bool);
		}
		self.setSlot(HAS_RETURNED, args[0]);
		return self;
	}

	@BsRuntimeMessage(name = "call", arity = -1, aliases = { "=>" })
	public BsObject call(BsObject self, BsObject... args) {
		return ((BsCode) self.value()).invoke(self, args);
	}

	@BsRuntimeMessage(name = "addArgument", arity = 1)
	public BsObject addArgument(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("addArgument", args[0], BsConst.String);
		}

		BsCode code = self.value();
		if (code.isInternal()) {
			return BsError
					.typeError("Impossible to add arguments to internal method");
		} else {
			code.addArgument(Bs.asString(args[0]));
		}

		return self;
	}

	@BsRuntimeMessage(name = "whileTrue", arity = 1)
	public BsObject whileTrue(BsObject self, BsObject... args) {
		BsObject w = self.invoke("call");
		if (w.isError()) {
			return w;
		}

		BsObject last = BsConst.Nil;
		while (Bs.asBoolean(w) && !Bs.asBoolean(args[0].getSlot(HAS_RETURNED))) {
			last = args[0].invoke("call");
			if (last.isBreak()) {
				return last;
			}

			w = self.invoke("call");
		}

		return last;
	}
}
