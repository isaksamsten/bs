package com.bs.lang;

import static com.bs.lang.BsConst.Bool;
import static com.bs.lang.BsConst.Enumerable;
import static com.bs.lang.BsConst.Error;
import static com.bs.lang.BsConst.False;
import static com.bs.lang.BsConst.List;
import static com.bs.lang.BsConst.Module;
import static com.bs.lang.BsConst.NameError;
import static com.bs.lang.BsConst.Nil;
import static com.bs.lang.BsConst.Number;
import static com.bs.lang.BsConst.Proto;
import static com.bs.lang.BsConst.Symbol;
import static com.bs.lang.BsConst.SyntaxError;
import static com.bs.lang.BsConst.True;
import static com.bs.lang.BsConst.TypeError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bs.interpreter.BsCompiler;
import com.bs.interpreter.BsInterpreter;
import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsList;
import com.bs.lang.proto.BsModule;
import com.bs.lang.proto.BsString;
import com.bs.parser.tree.Node;

public final class Bs {

	private static BsObject builtin;

	public static void init() {
		builtin = BsModule.create("builtin");
		builtin.setSlot(Proto);
		builtin.setSlot(Nil);
		builtin.setSlot(BsConst.System);
		builtin.setSlot(BsConst.Ast);

		/*
		 * Literal types
		 */
		builtin.setSlot(Symbol);
		builtin.setSlot(BsConst.String);
		builtin.setSlot(List);
		builtin.setSlot(Number);
		builtin.setSlot(Bool);
		builtin.setSlot(True);
		builtin.setSlot(False);

		builtin.setSlot(Module);
		builtin.setSlot(Enumerable);

		builtin.setSlot(BsConst.Thread);
		builtin.setSlot(BsConst.Future);

		/*
		 * Java interop
		 */
		builtin.setSlot(BsConst.Java);
		builtin.setSlot(BsConst.JavaInstance);
		builtin.setSlot(BsConst.JavaClass);

		/*
		 * IO
		 */
		builtin.setSlot(BsConst.IO);
		builtin.setSlot(BsConst.Reader);
		builtin.setSlot(BsConst.Writer);
		builtin.setSlot(BsConst.File);

		/*
		 * Errors
		 */
		builtin.setSlot(Error);
		builtin.setSlot(SyntaxError);
		builtin.setSlot(NameError);
		builtin.setSlot(TypeError);
		builtin.setSlot(BsConst.IOError);
		builtin.setSlot(BsConst.JavaError);
		builtin.setSlot(BsConst.CloneError);
		builtin.setSlot(BsConst.SubTypeError);

		// Fields in the Module proto.

		List<BsObject> path = new ArrayList<BsObject>();
		path.add(BsString.clone("."));
		Module.setSlot(BsModule.LOAD_PATH, BsList.create(path));
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

	public static Object to(BsObject obj, Class<?> cl) {
		return cl.cast(obj.value());
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
	 * 
	 * @param obj
	 * @return
	 */
	public static char asCharacter(BsObject obj) {
		return obj.instanceOf(BsConst.Char) ? (Character) obj.value() : null;
	}

	/**
	 * Return an instance of BsConst.String to its java String value
	 * 
	 * @param obj
	 * @return
	 */
	public static String asString(BsObject obj) {
		return obj.instanceOf(BsConst.String) || obj.instanceOf(Symbol) ? (String) obj
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
	 * Evaluate a string of code in the default stack
	 * 
	 * @param code
	 * @return
	 */
	public static BsObject eval(String code, Stack stack) {
		BsCompiler compiler = new BsCompiler();
		return compiler.eval(code, stack);
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
		return (BsObject) i.interpret(node);
	}

	public static Pair<Node, BsObject> evalYield(Node node, Stack stack) {
		BsInterpreter i = new BsInterpreter(stack);
		BsObject ret = (BsObject) i.interpret(node);
		Pair<Node, BsObject> pair = Pair.of(i.lastNode(), ret);

		return pair;
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
			node = compiler.parse(new FileReader(file));
			if (compiler.hasError()) {
				return compiler.error();
			} else {
				return eval(node, stack);
			}
		} catch (FileNotFoundException e) {
			return BsError.raise(e.getMessage());
		}

	}

	public static BsObject evalRepl(String code, Stack stack) {
		BsCompiler compile = new BsCompiler();
		Node node = compile.parse(new StringReader(code));
		if (compile.hasError()) {
			return compile.error();
		} else {
			return eval(node, stack);
		}
	}

	/**
	 * 
	 * @param o
	 * @param node
	 */
	public static void updateError(BsObject o, Node node) {
		if (o.isError()) {
			BsObject obj = o.getSlot(BsError.STACK_TRACE);
			BsObject str = BsString.clone(String.format(
					"'%s' at line %d position %d", node.code().trim(),
					node.line(), node.position()));
			if (obj == null || obj.isNil()) {
				List<BsObject> objs = new ArrayList<BsObject>();
				objs.add(str);
				obj = BsList.create(objs);
				o.setSlot(BsError.STACK_TRACE, obj);
			} else {
				ArrayList<BsObject> objs = obj.value();
				objs.add(str);
			}

		}
	}

	/**
	 * @param value
	 */
	public static void breakError(BsObject value, boolean exit) {
		System.out.println("Traceback (most recent call first):\n  " + value);
		BsObject obj = value.getSlot(BsError.STACK_TRACE);
		if (obj == null) {
			return;
		}
		List<BsObject> stackTrace = obj.value();
		if (stackTrace != null) {
			for (BsObject str : stackTrace) {
				System.out.println("\t" + Bs.asString(str));
			}
		}

		if (exit)
			System.exit(0);
	}

	public static void breakError(BsObject value) {
		breakError(value, true);
	}

	public static File findModule(List<BsObject> loadPath, BsObject fileName) {
		for (BsObject obj : loadPath) {
			String path = Bs.asString(obj);
			String file = Bs.asString(fileName);
			if (path == null || file == null) {
				return null;
			}
			File filePath = new File(FilenameUtils.getFullPath(FilenameUtils
					.concat(path, file)));
			if (filePath.exists()) {
				return filePath;
			}
		}

		return null;
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

	public static final String NEW = "new";
	public static final String CLONE = "clone";
	public static final String METHOD_MISSING = "methodMissing";
	public static final String GET_SLOT = "getSlot";
	public static final String SET_SLOT = "setSlot";
	public static final String HAS_SLOT = "hasSlot";

	public static BsObject safe(BsObject slot) {
		return slot == null ? BsConst.Nil : slot;
	}
}
