package com.bs.lang.proto.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsException;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsCode;
import com.bs.lang.proto.BsError;

public class BsJavaClass extends BsAbstractProto {

	public BsJavaClass() {
		super(BsConst.JavaInstance, "JavaClass", BsJavaClass.class);
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsJavaData data = self.value();

		Object[] arguments = ReflectionUtils.createJavaObjects(args, 1);
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

	/**
	 * TODO: Fix -> delegate to original?
	 * 
	 * @param self
	 * @param args
	 * @return
	 */
	@BsRuntimeMessage(name = "proxy", arity = -1)
	public BsObject proxy(BsObject self, final BsObject... args) {
		final BsJavaData data = self.value();
		if (!data.cls.isInterface()) {
			return BsError.javaError("Can't create a proxy from non interface");
		} else if (args.length % 2 != 0) {
			return BsError.raise("Method require an even number of arguments");
		}

		final Map<String, BsCode> toHandle = new HashMap<String, BsCode>();
		for (int n = 0; n < args.length; n += 2) {
			String methodName = Bs.asString(args[n]);
			BsCode code = args[n + 1].value();

			if (methodName == null || code == null)
				return BsError.raise("Unexpected parameter to 'proxy'");

			toHandle.put(methodName, code);
		}

		Object obj = data.cls.cast(Proxy.newProxyInstance(
				data.cls.getClassLoader(), new Class[] { data.cls },
				new InvocationHandler() {

					@Override
					public Object invoke(Object me, Method method, Object[] arg)
							throws Throwable {
						if (toHandle.containsKey(method.getName())) {
							BsObject v = toHandle.get(method.getName()).invoke(
									args[1],
									ReflectionUtils.createBsObjects(arg));
							if (v.isError()) {
								throw new BsException(v);
							}

							return ReflectionUtils.getValue(v.value());
						} else {
							throw new BsException(BsError.raise(
									"Method '%s' not defined for proxy",
									method.getName()));
						}
					}
				}));

		return ReflectionUtils.createBsObject(obj);
	}

	@BsRuntimeMessage(name = "new", arity = -1)
	public BsObject new_(BsObject self, BsObject... args) {
		BsJavaData data = self.value();
		if (data.cls.isInterface()
				|| Modifier.isAbstract(data.cls.getModifiers())) {
			return BsError
					.javaError("Can't instantiate an interface or abstract class");
		}
		try {
			Object[] arguments = ReflectionUtils.createJavaObjects(args);
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
