package com.bs.lang;

public abstract class BsAbstractProto extends BsObject {

	public BsAbstractProto(BsObject prototype, String name, Class<?> me) {
		super(prototype, name, me);
		initRuntimeMethods();
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
		int max = args.length;
		if (max < check.length)
			max = check.length;

		for (int n = 0; n < max; n++) {
			if (!args[n].instanceOf(check[n])) {
				return n;
			}
		}

		return -1;
	}
}
