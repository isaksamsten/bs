package com.bs.lang;

import com.bs.interpreter.BsCompiler;
import com.bs.interpreter.BsInterpreter;
import com.bs.parser.tree.Node;

public final class Bs {

	private static BsObject builtin;

	static {
		builtin = BsModule.create();
		builtin.var(BsConst.Proto);
		builtin.var(BsConst.String);
		builtin.var(BsConst.List);
		builtin.var(BsConst.Number);
		builtin.var(BsConst.Module);
		builtin.var(BsConst.Enumerable);
		builtin.var(BsConst.True);
		builtin.var(BsConst.False);
		builtin.var(BsConst.Error);
	}

	/**
	 * Check is obj is the same object (reference) as BsConst.True
	 * 
	 * @param obj
	 *            - any BsObject or null
	 * @return
	 */
	public static boolean isTrue(BsObject obj) {
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

	public static String asString(BsObject obj) {
		return obj.instanceOf(BsConst.String) ? (String) obj.value() : null;
	}

	public static BsObject negate(BsObject invoke) {
		return isTrue(invoke) ? BsConst.False : BsConst.True;
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

}
