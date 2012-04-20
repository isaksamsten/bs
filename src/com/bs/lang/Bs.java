package com.bs.lang;

import static com.bs.lang.BsConst.Bool;
import static com.bs.lang.BsConst.Enumerable;
import static com.bs.lang.BsConst.Error;
import static com.bs.lang.BsConst.False;
import static com.bs.lang.BsConst.List;
import static com.bs.lang.BsConst.Module;
import static com.bs.lang.BsConst.NameError;
import static com.bs.lang.BsConst.Number;
import static com.bs.lang.BsConst.Proto;
import static com.bs.lang.BsConst.String;
import static com.bs.lang.BsConst.SyntaxError;
import static com.bs.lang.BsConst.System;
import static com.bs.lang.BsConst.True;
import static com.bs.lang.BsConst.TypeError;
import static com.bs.lang.BsConst.Nil;
import static com.bs.lang.BsConst.Symbol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.bs.interpreter.BsCompiler;
import com.bs.interpreter.BsInterpreter;
import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsModule;
import com.bs.parser.tree.Node;

public final class Bs {

	private static BsObject builtin;

	static {
		builtin = BsModule.create("builtin");
		builtin.slot(Proto);
		builtin.slot(Nil);
		builtin.slot(System);

		/*
		 * Literal types
		 */
		builtin.slot(Symbol);
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

	/**
	 * 
	 * @param bool
	 * @return
	 */
	public static BsObject bool(boolean bool) {
		return bool ? True : False;
	}

	/**
	 * Return the value of a BsNumber
	 * 
	 * @param obj
	 * @return
	 */
	public static Number asNumber(BsObject obj) {
		return obj.instanceOf(Number) ? (Number) obj.value() : null;
	}

	/**
	 * Return an instance of BsConst.String to its java String value
	 * 
	 * @param obj
	 * @return
	 */
	public static String asString(BsObject obj) {
		return obj.instanceOf(String) || obj.instanceOf(Symbol) ? (String) obj
				.value() : null;
	}

	/**
	 * Negate a boolean
	 * 
	 * @param invoke
	 * @return
	 */
	public static BsObject negate(BsObject invoke) {
		return asBoolean(invoke) ? False : True;
	}

	/**
	 * Return true if obj is Nil
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNil(BsObject obj) {
		return obj == null || obj == Nil;
	}

	/**
	 * Return the global builtin module
	 * 
	 * @return
	 */
	public static BsObject builtin() {
		return builtin;
	}

	/**
	 * Compile a string of code in the default stack
	 * 
	 * @param code
	 * @return
	 */
	public static BsObject compile(String code) {
		BsCompiler compiler = new BsCompiler();
		return compiler.compile(code);
	}

	/**
	 * Evaluate a string of code in the default stack
	 * 
	 * @param code
	 * @return
	 */
	public static BsObject eval(String code) {
		BsCompiler compiler = new BsCompiler();
		return compiler.eval(code);
	}

	/**
	 * Compile a string of code that will evaluate in stack
	 * 
	 * @param code
	 * @param stack
	 * @return BsObject that is callable <code>obj.invoke("call")</code>
	 */
	public static BsObject compile(String code, Stack stack) {
		BsCompiler compiler = new BsCompiler();
		return compiler.compile(code, stack);
	}

	/**
	 * Evaluate a Node in the context of stack
	 * 
	 * @param node
	 * @param stack
	 * @return
	 */
	public static BsObject eval(Node node, Stack stack) {
		BsInterpreter i = new BsInterpreter(stack);
		return (BsObject) i.visit(node);
	}

	/**
	 * Evaluate a file in the context of stack
	 * 
	 * @param file
	 * @param stack
	 * @return
	 */
	public static BsObject eval(File file, Stack stack) {
		BsCompiler compiler = new BsCompiler();
		Node node;
		try {
			node = compiler.parse(new FileReader(file), stack);
			if (compiler.hasError()) {
				return compiler.error();
			} else {
				return eval(node, stack);
			}
		} catch (FileNotFoundException e) {
			return BsError.raise(e.getMessage());
		}

	}

	/**
	 * Evaluate a node in the default stack
	 * 
	 * @param code
	 * @return
	 */
	public static BsObject eval(Node code) {
		return eval(code, BsStack.getDefault());
	}

}