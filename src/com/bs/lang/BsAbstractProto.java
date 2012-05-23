package com.bs.lang;

public abstract class BsAbstractProto extends BsObject {

	public BsAbstractProto(BsObject prototype, String name, Class<?> me) {
		super(prototype, name, me);
		initRuntimeMethods();
		Bs.addPrototype(me, this);
	}

	protected int findError(BsObject... args) {
		for (int n = 0; n < args.length; n++) {
			if (args[n].isError()) {
				return n;
			}
		}

		return -1;
	}

	public int checkTypes(BsObject[] args, BsObject... check) {
		return Bs.checkTypes(args, check);
	}
}
