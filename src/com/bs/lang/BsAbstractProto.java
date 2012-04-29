package com.bs.lang;

public abstract class BsAbstractProto extends BsObject {

	public BsAbstractProto(BsObject prototype, String name, Class<?> me) {
		super(prototype, name, me);
		initRuntimeMethods();
	}

	protected boolean isError(BsObject... args) {
		return false;
	}
}
