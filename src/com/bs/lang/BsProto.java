package com.bs.lang;

public class BsProto extends BsObject {

	public BsProto() {
		super(null, "Proto", BsProto.class);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		Object value = self.name();
		if (self.value() != null) {
			value = self.value();
		}
		return BsObject.value(Bs.String, value.toString());
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return new BsObject(self);
	}

}
