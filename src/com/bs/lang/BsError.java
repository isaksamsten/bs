package com.bs.lang;

public class BsError extends BsObject {

	public static BsObject raise(BsObject self, String message, Object... args) {
		BsObject obj = BsObject.clone(self);
		obj.var("message",
				BsObject.value(BsConst.String, String.format(message, args)));
		return obj;
	}

	public static BsObject raise(String msg, Object... args) {
		return raise(BsConst.Error, msg, args);
	}

	public static BsObject nameError(String name) {
		return raise(BsConst.NameError, "name '%s' not defined", name);
	}

	public static BsObject syntaxError(String message) {
		return raise(BsConst.SyntaxError, message);
	}

	public static BsObject typeError(String method, BsObject got,
			BsObject expected) {
		return raise(BsConst.TypeError, "%s argument must be %s, got %s",
				method, expected.name(), got.name());
	}

	/**
	 * Create a new Error type
	 * 
	 * @param name
	 * @return
	 */
	public static BsObject clone(String name) {
		return new BsObject(BsConst.Error, name);
	}

	public BsError() {
		super(BsConst.Proto, "Error", BsError.class);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone(self.prototype().name() + ": ").invoke("+",
				self.var("message"));
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

	@BsRuntimeMessage(name = "catch", arity = 2)
	public BsObject catchit(BsObject self, BsObject... args) {
		String type = name();
		String toCatch = Bs.asString(args[0]);
		if (type.equals(toCatch) && !Bs.isTrue(args[1].var("caught"))) {
			args[1].var("caught", BsConst.True);
			return args[1].invoke("call");
		}
		return BsConst.False;
	}

	@BsRuntimeMessage(name = "raise", arity = 1)
	public BsObject raise(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("raise", args[0].prototype(),
					BsConst.String);
		}
		return BsError.raise(self, Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "clone", arity = 1)
	public BsObject clone(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("clone", args[0].prototype(),
					BsConst.String);
		}

		return clone(Bs.asString(args[0]));
	}
}
