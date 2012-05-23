package com.bs.lang.builtin.java;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.builtin.BsError;

public class BsJava extends BsAbstractProto {

	public BsJava() {
		super(BsConst.Proto, "Java", BsJava.class);
	}

	@BsRuntimeMessage(name = "import", arity = 1, aliases = { "-" })
	public BsObject static_(BsObject self, BsObject... args) {
		try {
			Class<?> cls = Class.forName(Bs.asString(args[0]));
			return ReflectionUtils.createBsObject(BsConst.JavaClass, cls);
		} catch (ClassNotFoundException e) {
			return BsError.javaError("class not found: '%s'", e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "new", arity = -1)
	public BsObject new_(BsObject self, BsObject... args) {
		try {
			Class<?> cls = Class.forName(Bs.asString(args[0]));

			Object[] arguments = ReflectionUtils.createJavaObjects(args, 1);
			if (arguments == null) {
				return BsError
						.javaError("Unsupported type in invokation (is it really a Java type?)");
			}

			Object obj = ConstructorUtils.invokeConstructor(cls, arguments);
			BsJavaData data = new BsJavaData(cls, obj);
			return BsObject.value(BsConst.JavaInstance, data);
		} catch (ClassNotFoundException e) {
			return BsError.javaError("class not found: '%s'", e.getMessage());
		} catch (Exception e) {
			return BsError.javaError(e.getMessage());
		}
	}

}
