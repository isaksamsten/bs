package com.bs.lang;

public class BsModule extends BsObject {

	public static BsObject create() {
		return BsObject.value(Bs.Module, null);
	}

	public BsModule() {
		super(Bs.Proto, "Module", BsModule.class);
	}

}
