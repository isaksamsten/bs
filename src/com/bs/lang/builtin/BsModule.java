package com.bs.lang.builtin;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.builtin.java.BsJavaData;
import com.bs.lang.lib.Loadable;

public class BsModule extends BsAbstractProto {

	public static final String FILE_NAME = "fileName";
	public static final String LOAD_PATH = "loadPath";

	public static BsObject create(String string) {
		BsObject obj = BsObject.value(BsConst.Module, null);
		obj.setSlot(FILE_NAME, BsString.clone(string));
		return obj;
	}

	public BsModule() {
		super(BsConst.Proto, "Module", BsModule.class);
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsObject slot = self.getSlot(name);
		if (slot == null) {
			return BsError.nameError(name);
		}

		return slot;
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		if (self == BsConst.Module) {
			return BsString.clone("Module");
		}
		return BsString.clone("Module: <"
				+ Bs.asString(self.getSlot(FILE_NAME)) + ">");
	}

	@BsRuntimeMessage(name = "import", arity = 1, types = { BsString.class })
	public BsObject static_(BsObject self, BsObject... args) {
		BsObject javaImport = BsConst.Java.invoke("import", args);
		if (javaImport.isError()) {
			return javaImport;
		}

		BsJavaData data = javaImport.value();
		String name = data.cls.getName();
		self.setSlot(name, javaImport);

		return javaImport;
	}

	@BsRuntimeMessage(name = "load", arity = 1)
	public BsObject load(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("load", args[0], BsConst.String);
		}

		String moduleName = Bs.asString(args[0]);
		Loadable loadable = Bs.findModule(moduleName);
		if (loadable != null) {
			BsObject module = loadable.getModule();
			self.setSlot(loadable.getName(), module);
			return module;
		}

		List<BsObject> loadPath = BsConst.Module.getSlot(LOAD_PATH).value();
		File file = Bs.findModule(loadPath, args[0]);
		if (file == null) {
			return BsError
					.raise("Can't find module '%s'", Bs.asString(args[0]));
		}

		moduleName = FilenameUtils.removeExtension(moduleName);

		Stack stack = new BsStack();
		stack.push(BsModule.create(moduleName));
		BsObject error = Bs.eval(file, stack);
		if (error.isError()) {
			return error;
		}

		BsObject module = (BsObject) stack.root();
		self.setSlot(moduleName, module);
		return module;
	}
}
