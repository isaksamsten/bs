package com.bs.lang.builtin;

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

	@BsRuntimeMessage(name = "call", arity = -1, aliases = { "=>" })
	public BsObject call(BsObject self, BsObject... args) {
		return ((BsCode) self.value()).invoke(self, args);
	}

	@BsRuntimeMessage(name = "callFuture", arity = -1, aliases = { "==>" })
	public BsObject callFuture(BsObject self, BsObject... args) {
		return BsFuture.create((BsCode) self.value(), self, args);
	}

	@BsRuntimeMessage(name = "callAsync", arity = -1, aliases = { "===>" })
	public BsObject callAsync(final BsObject self, final BsObject... args) {
		final BsCode code = self.value();
		BsObject obj = BsThread.create(code, self, args);
		Thread t = obj.value();
		t.start();
		return obj;
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
		if (w.isBreakingContext()) {
			return w;
		}

		BsObject last = BsConst.Nil;
		while (Bs.asBoolean(w)) {
			last = args[0].invoke("call");
			if (last.isBreakingContext()) {
				return last;
			}

			w = self.invoke("call");
			if (w.isBreakingContext()) {
				return w;
			}
		}

		return last;
	}
}
