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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bs.interpreter.BsCompiler;
import com.bs.interpreter.BsInterpreter;
import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.builtin.BsBool;
import com.bs.lang.builtin.BsEnumerable;
import com.bs.lang.builtin.BsError;
import com.bs.lang.builtin.BsFalse;
import com.bs.lang.builtin.BsFuture;
import com.bs.lang.builtin.BsList;
import com.bs.lang.builtin.BsModule;
import com.bs.lang.builtin.BsNil;
import com.bs.lang.builtin.BsNumber;
import com.bs.lang.builtin.BsProto;
import com.bs.lang.builtin.BsString;
import com.bs.lang.builtin.BsSymbol;
import com.bs.lang.builtin.BsSystem;
import com.bs.lang.builtin.BsThread;
import com.bs.lang.builtin.BsTrue;
import com.bs.lang.builtin.io.BsFile;
import com.bs.lang.builtin.io.BsIO;
import com.bs.lang.builtin.io.BsReader;
import com.bs.lang.builtin.io.BsWriter;
import com.bs.lang.builtin.java.BsJava;
import com.bs.lang.builtin.java.BsJavaClass;
import com.bs.lang.builtin.java.BsJavaInstance;
import com.bs.lang.lib.Loadable;
import com.bs.lang.lib.ast.AstLoadable;
import com.bs.parser.tree.Node;

public final class Bs {

	private static BsObject builtin;
	private static Map<Class<?>, BsObject> prototypes = new HashMap<Class<?>, BsObject>();
	private static Map<String, Loadable> loadable = new HashMap<String, Loadable>();

	public static void init() {
		builtin = BsModule.create("builtin");
		builtin.setSlot(Proto);
		builtin.setSlot(Nil);
		builtin.setSlot(BsConst.System);

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

		addModule(new AstLoadable());
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

	public static void addPrototype(Class<?> me, BsObject proto) {
		prototypes.put(me, proto);
	}

	public static BsObject findPrototype(Class<?> cls) {
		return prototypes.get(cls);
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
	 * Check if args..n instanceOf check..n
	 * 
	 * @param args
	 * @param check
	 * @return
	 */
	public static int checkTypes(BsObject[] args, BsObject... check) {
		int max = args.length;
		if (max < check.length)
			max = check.length;

		for (int n = 0; n < max; n++) {
			if (!args[n].instanceOf(check[n])) {
				return n;
			}
		}

		return -1;
	}

	/**
	 * Check if args..n instanceOf check..n
	 * 
	 * @param args
	 * @param check
	 * @return
	 */
	public static int checkTypes(BsObject[] args, Class<?>... check) {
		int max = args.length;
		if (max < check.length)
			max = check.length;

		for (int n = 0; n < max; n++) {
			if (!args[n].instanceOf(check[n])) {
				return n;
			}
		}

		return -1;
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

	public static synchronized void addModule(Loadable load) {
		loadable.put(load.getName(), load);
	}

	public static synchronized Loadable findModule(String name) {
		if (loadable.containsKey(name)) {
			return loadable.get(name);
		} else {
			return null;
		}
	}

	public static File findModule(List<BsObject> loadPath, BsObject fileName) {
		for (BsObject obj : loadPath) {
			String path = Bs.asString(obj);
			String file = Bs.asString(fileName);
			if (path == null || file == null) {
				return null;
			}
			if (!FilenameUtils.getExtension(file).equals("bs")) {
				file += ".bs";
			}
			String f = FilenameUtils.concat(path, file);
			File filePath = new File(f);
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
