package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsCode;

public class BsError extends BsAbstractProto {

	/**
	 * Raise an BsError of type 'type' with the message interpolated with args
	 * 
	 * @param type
	 * @param message
	 * @param args
	 * @return
	 */
	public static BsObject raise(BsObject type, String message, Object... args) {
		BsObject obj = BsObject.clone(type);
		obj.setSlot(MESSAGE,
				BsObject.value(BsConst.String, String.format(message, args)));
		return obj;
	}

	/**
	 * Raise a generic BsConst.Error with message
	 * 
	 * @param msg
	 * @param args
	 * @return
	 */
	public static BsObject raise(String msg, Object... args) {
		return raise(BsConst.Error, msg, args);
	}

	/**
	 * Raise a CloneError
	 * 
	 * @param name
	 * @return
	 */
	public static BsObject cloneError(String name) {
		return raise(BsConst.CloneError, "Can't clone '%s'", name);
	}

	/**
	 * Raise a NameError that is, that a method is note defined by obj
	 * 
	 * @param message
	 * @param obj
	 * @return
	 */
	public static BsObject nameError(String message, BsObject obj) {
		return raise(BsConst.NameError, "No method '%s' for '%s'", message,
				(obj.getPrototype() == null ? obj : obj.getPrototype()));
	}

	/**
	 * Raise a name error, that is, a name is not defined (eg. a variable)
	 * 
	 * @param name
	 * @return
	 */
	public static BsObject nameError(String name) {
		return raise(BsConst.NameError, "name '%s' not defined", name);
	}

	/**
	 * Raise a JavaError (more generic)
	 * 
	 * @param msg
	 * @param args
	 * @return
	 */
	public static BsObject javaError(String msg, Object... args) {
		return raise(BsConst.JavaError, "error in Java invokation '%s'", msg);
	}

	/**
	 * Raise a SyntaxError, when a eval() or compile() is used.
	 * 
	 * @param message
	 * @return
	 */
	public static BsObject syntaxError(String message) {
		return raise(BsConst.SyntaxError, message);
	}

	/**
	 * Raise TypeError when method is invoked with invalid arguments
	 * 
	 * @param method
	 * @param got
	 * @param expected
	 * @return
	 */
	public static BsObject typeError(String method, BsObject got,
			BsObject expected) {
		return raise(BsConst.TypeError, "%s argument must be %s, got %s",
				method, expected.name(), (got.getPrototype() == null ? got
						: got.getPrototype()).name());
	}

	/**
	 * Raise a TypeError when a method is invoked with invalid arity
	 * 
	 * @param self
	 * @param method
	 * @param got
	 * @param expected
	 * @return
	 */
	public static BsObject typeError(BsObject self, String method, int got,
			int expected) {
		return raise(BsConst.TypeError,
				"%s %s() takes %d arguments (%d given)",
				(self.getPrototype() == null ? self : self.getPrototype())
						.name(), method, expected, got);
	}

	/**
	 * Raise a IOError when
	 * 
	 * @param message
	 * @return
	 */
	public static BsObject IOError(String message) {
		return raise(BsConst.IOError, message);
	}

	/**
	 * Generic message to denot a method
	 * 
	 * @return
	 */
	public static BsObject subClassResponsibility() {
		return raise(BsConst.SubTypeError,
				"Method should be implemented by sub types");
	}

	public static final String IGNORED = "ignored";
	public static final String MESSAGE = "message";
	public static final String CAUGHT = "caught";
	public static final String STACK_TRACE = "stackTrace";

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
		return BsString.clone(self.getPrototype().name() + ": ").invoke("+",
				self.getSlot(MESSAGE));
	}

	@BsRuntimeMessage(name = "getMessage", arity = 0)
	public BsObject getMessage(BsObject self, BsObject... args) {
		return self.getSlot(MESSAGE);
	}

	@BsRuntimeMessage(name = "getStacktrace", arity = 0)
	public BsObject getStacktrace(BsObject self, BsObject... args) {
		return self.getSlot(STACK_TRACE);
	}

	@BsRuntimeMessage(name = "setMessage", arity = 1)
	public BsObject setMessage(BsObject self, BsObject... args) {
		self.setSlot(MESSAGE, args[0]);
		return self;
	}

	@BsRuntimeMessage(name = "catch", arity = 2)
	public BsObject catch_(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Error)) {
			return typeError("catch", args[0], BsConst.Symbol);
		}

		if (!args[1].instanceOf(BsConst.Block)) {
			return typeError("catch", args[1], BsConst.Block);
		}

		BsObject type = self.getPrototype();
		if (type.instanceOf(args[0]) && !Bs.asBoolean(args[1].getSlot(CAUGHT))) {
			int arity = ((BsCode) args[1].value()).getArity();
			args[1].setSlot(CAUGHT, BsConst.True);
			if (arity == 1) {
				return args[1].invoke("call", self);
			}
			return args[1].invoke("call");
		}
		return BsConst.False;
	}

	@BsRuntimeMessage(name = "pass", arity = 0)
	public BsObject pass(BsObject self, BsObject... args) {
		self.setSlot(IGNORED, BsConst.False);
		return self;
	}

	@BsRuntimeMessage(name = "raise", arity = 1)
	public BsObject raise(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("raise", args[0].getPrototype(),
					BsConst.String);
		}
		return BsError.raise(self, Bs.asString(args[0]));
	}

	@BsRuntimeMessage(name = "clone", arity = 1)
	public BsObject clone(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Symbol)) {
			return BsError.typeError("clone", args[0].getPrototype(),
					BsConst.Symbol);
		}

		return clone(Bs.asString(args[0]));
	}

	public static BsObject typeError(String string, Object... value) {
		return raise(BsConst.TypeError, string, value);
	}

}
