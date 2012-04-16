package com.bs.lang.proto;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsModule extends BsObject {

	public static BsObject create() {
		return BsObject.value(BsConst.Module, null);
	}

	public BsModule() {
		super(BsConst.Proto, "Module", BsModule.class);
	}

}
