package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsError extends BsObject {

	public static BsObject raise(BsObject self, String message, Object... args) {
		BsObject obj = BsObject.clone(self);
		obj.slot("message",
				BsObject.value(BsConst.String, String.format(message, args)));
		return obj;
	}

	public static BsObject raise(String msg, Object... args) {
		return raise(BsConst.Error, msg, args);
	}

	public static BsObject nameError(String message, BsObject obj) {
		return raise(BsConst.NameError, "No method '%s' for '%s'", message, obj);
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

	public static BsObject typeError(BsObject self, String method, int got,
			int expected) {
		return raise(BsConst.TypeError,
				"%s %s() takes %d arguments (%d given)", self.prototype()
						.name(), method, expected, got);
	}

	public static final String IGNORE = "ignore";
	public static final String MESSAGE = "message";
	public static final String CAUGHT = "caught";

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
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone(self.prototype().name() + ": ").invoke("+",
				self.slot(MESSAGE));
	}

	@BsRuntimeMessage(name = "getMessage", arity = 0)
	public BsObject getMessage(BsObject self, BsObject... args) {
		return self.slot(MESSAGE);
	}

	@BsRuntimeMessage(name = "setMessage", arity = 1)
	public BsObject setMessage(BsObject self, BsObject... args) {
		self.slot(MESSAGE, args[0]);
		return self;
	}

	@BsRuntimeMessage(name = "catch", arity = 2)
	public BsObject catchit(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return typeError("catch", args[0], BsConst.Symbol);
		}

		if (!args[1].instanceOf(BsConst.Block)) {
			return typeError("catch", args[1], BsConst.Block);
		}

		String type = self.prototype().name();
		String toCatch = Bs.asString(args[0]);
		if (type.equals(toCatch) && !Bs.asBoolean(args[1].slot(CAUGHT))) {
			int arity = Bs.asNumber(args[1].slot(BsBlock.ARITY)).intValue();
			args[1].slot(CAUGHT, BsConst.True);
			if (arity == 1) {
				return args[1].invoke("call", self);
			}
			return args[1].invoke("call");
		}
		return BsConst.False;
	}

	@BsRuntimeMessage(name = "pass", arity = 0)
	public BsObject pass(BsObject self, BsObject... args) {
		self.slot(IGNORE, BsConst.False);
		return self;
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
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return BsError.typeError("clone", args[0].prototype(),
					BsConst.Symbol);
		}

		return clone(Bs.asString(args[0]));
	}

}
