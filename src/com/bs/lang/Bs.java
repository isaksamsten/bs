package com.bs.lang;

import com.bs.interpreter.BsBlockInterpreter;
import com.bs.interpreter.BsCompiler;
import com.bs.interpreter.BsInterpreter;
import com.bs.lang.proto.BsModule;
import com.bs.parser.tree.Node;

import static com.bs.lang.BsConst.*;

public final class Bs {

	private static BsObject builtin;

	static {
		builtin = BsModule.create();
		builtin.slot(Proto);
		builtin.slot(System);

		/*
		 * Literal types
		 */
		builtin.slot(String);
		builtin.slot(List);
		builtin.slot(Number);
		builtin.slot(Bool);
		builtin.slot(True);
		builtin.slot(False);

		builtin.slot(Module);
		builtin.slot(Enumerable);

		/*
		 * Errors
		 */
		builtin.slot(Error);
		builtin.slot(SyntaxError);
		builtin.slot(NameError);
		builtin.slot(TypeError);
	}

	/**
	 * Check is obj is the same object (reference) as BsConst.True
	 * 
	 * @param obj
	 *            - any BsObject or null
	 * @return
	 */
	public static boolean asBoolean(BsObject obj) {
		return obj != null && obj == BsConst.True;
	}

	public static BsObject bool(boolean bool) {
		return bool ? BsConst.True : BsConst.False;
	}

	/**
	 * Return the value of a BsNumber
	 * 
	 * @param obj
	 * @return
	 */
	public static Number asNumber(BsObject obj) {
		return obj.instanceOf(BsConst.Number) ? (Number) obj.value() : null;
	}

	/**
	 * Return an instance of BsConst.String to its java String value
	 * 
	 * @param obj
	 * @return
	 */
	public static String asString(BsObject obj) {
		return obj.instanceOf(BsConst.String) ? (String) obj.value() : null;
	}

	/**
	 * Negate a boolean
	 * 
	 * @param invoke
	 * @return
	 */
	public static BsObject negate(BsObject invoke) {
		return asBoolean(invoke) ? BsConst.False : BsConst.True;
	}

	public static BsObject builtin() {
		return builtin;
	}

	public static BsObject compile(String code) {
		BsCompiler compiler = new BsCompiler();
		return compiler.compile(code);
	}

	public static BsObject eval(String code) {
		BsCompiler compiler = new BsCompiler();
		return compiler.eval(code);
	}

	public static BsObject eval(Node node) {
		BsInterpreter i = new BsInterpreter();
		return (BsObject) i.visit(node);
	}

	public static BsObject blockEval(Node node) {
		BsBlockInterpreter i = new BsBlockInterpreter();
		return (BsObject) i.visit(node);
	}

}
