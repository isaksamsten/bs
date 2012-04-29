package com.bs.lang;


public class BsBoundMessage extends BsMessage {

	private BsObject binder;

	public BsBoundMessage(String name, int arity, BsCode code, BsObject binder) {
		super(name, arity, code);
		this.binder = binder;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		BsObject obj = super.invoke(binder, args);

		if (obj.isReturning()) {
			obj.setReturning(false);
		}

		return obj;
	}
}
