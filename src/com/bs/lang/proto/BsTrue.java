package com.bs.lang.proto;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsTrue extends BsObject {

	public BsTrue() {
		super(BsConst.Bool, "True", BsTrue.class);
		initRuntimeMethods();
	}
}
