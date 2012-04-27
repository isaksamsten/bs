package com.bs.lang.proto.java;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;

public class BsJavaClass extends BsAbstractProto {

	public BsJavaClass() {
		super(BsConst.JavaInstance, "JavaClass", BsJavaClass.class);
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsJavaData data = self.value();

		Object[] arguments = ReflectionUtils.getValue(args, 1);
		if (arguments == null) {
			return BsError
					.javaError("Unsupported type in invokation (is it really a Java type?)");
		}
		try {
			Object ret = MethodUtils.invokeStaticMethod(data.cls, name,
					arguments);
			if (ret == null) {
				return self;
			}
			return ReflectionUtils.createBsObject(ret);
		} catch (Exception e) {
			return BsError.javaError(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "new", arity = -1)
	public BsObject new_(BsObject self, BsObject... args) {
		BsJavaData data = self.value();
		try {
			Object[] arguments = ReflectionUtils.getValue(args, 0);
			if (arguments == null) {
				return BsError
						.javaError("Unsupported type in invokation (is it really a Java type?)");
			}

			Object new_ = ConstructorUtils.invokeConstructor(data.cls,
					arguments);

			return ReflectionUtils.createBsObject(new_);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return self;
	}
}
