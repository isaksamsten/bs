package com.bs.lang;

public class BsModule extends BsObject {

	public static BsObject create() {
		return BsObject.value(BsConst.Module, null);
	}

	public BsModule() {
		super(BsConst.Proto, "Module", BsModule.class);
	}

}
