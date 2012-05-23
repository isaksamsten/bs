package com.bs.lang.builtin;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsTrue extends BsObject {

	public BsTrue() {
		super(BsConst.Bool, "True", BsTrue.class);
		initRuntimeMethods();
	}
}
