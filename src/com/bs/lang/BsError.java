package com.bs.lang;


public class BsError extends BsObject {

	public static BsObject raise(String message, Object... args) {
		BsObject obj = BsObject.clone(BsConst.Error);
		obj.var("message",
				BsObject.value(BsConst.String, String.format(message, args)));
		return obj;
	}

	public static BsObject nameError(String name) {
		return raise("name '%s' not defined", name);
	}

	public BsError() {
		super(BsConst.Proto, "Error", BsError.class);
	}

	@BsRuntimeMessage(name = "getMessage", arity = 0)
	public BsObject getMessage(BsObject self, BsObject... args) {
		return self.var("message");
	}

	@BsRuntimeMessage(name = "setMessage", arity = 1)
	public BsObject setMessage(BsObject self, BsObject... args) {
		self.var("message", args[0]);
		return self;
	}
}
