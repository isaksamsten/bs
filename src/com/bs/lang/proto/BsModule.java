package com.bs.lang.proto;

import java.io.File;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsModule extends BsObject {

	public static BsObject create(String string) {
		BsObject obj = BsObject.value(BsConst.Module, null);
		obj.slot("setName", BsString.clone(string));

		return obj;
	}

	public BsModule() {
		super(BsConst.Proto, "Module", BsModule.class);
	}

	@BsRuntimeMessage(name = "load", arity = 1)
	public BsObject load(BsObject self, BsObject... args) {
		Stack stack = new BsStack();
		stack.push(BsModule.create(Bs.asString(args[0])));
		BsObject error = Bs.eval(new File(Bs.asString(args[0])), stack);
		if (error.isError()) {
			return error;
		}
		stack.root().invoke("setName", args[0]);
		return self;
	}

}
