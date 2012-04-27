package com.bs.lang.proto;

import java.io.File;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsModule extends BsAbstractProto {

	private static final String FILE_NAME = "fileName";

	public static BsObject create(String string) {
		BsObject obj = BsObject.value(BsConst.Module, null);
		obj.setSlot(FILE_NAME, BsString.clone(string));

		return obj;
	}

	public BsModule() {
		super(BsConst.Proto, "Module", BsModule.class);
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsObject slot = self.getSlot(name);
		if (slot == null) {
			return BsError.nameError(name);
		}

		return slot;
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		if (self == BsConst.Module) {
			return BsString.clone("Module");
		}
		return BsString.clone("Module: <"
				+ Bs.asString(self.getSlot(FILE_NAME)) + ">");
	}

	@BsRuntimeMessage(name = "load", arity = 1)
	public BsObject load(BsObject self, BsObject... args) {
		Stack stack = new BsStack();
		stack.push(BsModule.create(Bs.asString(args[0])));
		BsObject error = Bs.eval(new File(Bs.asString(args[0])), stack);
		if (error.isError()) {
			return error;
		}
		return stack.root();
	}

}
