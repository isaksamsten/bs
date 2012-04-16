package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsSystem extends BsObject {

	public BsSystem() {
		super(BsConst.Proto, "System", BsSystem.class);
	}

	@BsRuntimeMessage(name = "puts", arity = -1)
	public BsObject puts(BsObject self, BsObject... args) {
		StringBuilder s = new StringBuilder();
		for (int n = 0; n < args.length; n++) {
			s.append(args[n].invoke("toString"));
		}

		System.out.println(s.toString());

		return self;
	}
}
