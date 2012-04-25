package com.bs.lang.proto.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsString;

public class BsJava extends BsObject {

	private class BsJavaData {
		public Class<?> cls;
		public Object instance;

		public BsJavaData(Class<?> cls, Object instance) {
			this.cls = cls;
			this.instance = instance;
		}

		@Override
		public String toString() {
			return "Instance of " + cls.getCanonicalName() + " with value "
					+ instance;
		}

	}

	public BsJava() {
		super(null, "Java", BsJava.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "messageMissing", arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsJavaData data = self.value();

		Class<?>[] classes = new Class<?>[args.length - 1];
		Object[] arguments = new Object[args.length - 1];
		for (int n = 1; n < args.length; n++) {
			Object value = args[n].value();
			if (value != null) {
				if (value instanceof BsJavaData) {
					classes[n] = ((BsJavaData) value).cls;
				} else {
					classes[n] = value.getClass();
				}
				arguments[n] = value;
			} else {
				return BsError.raise("Fail to convert to java");
			}
		}

		try {
			Method method = data.cls.getMethod(name, classes);
			Object ret = method.invoke(data.instance, arguments);

			data = new BsJavaData(ret.getClass(), ret);
			return BsObject.value(BsConst.Java, data);
		} catch (Exception e) {
			return BsError.nameError(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "new", arity = -1)
	public BsObject new_(BsObject self, BsObject... args) {
		try {
			Class<?> cls = Class.forName(Bs.asString(args[0]));
			Constructor<?>[] ctors = cls.getConstructors();
			Constructor<?> ctor = null;
			for (int n = 0; n < ctors.length; n++) {
				Type[] types = ctors[n].getGenericParameterTypes();
				if (types.length == args.length - 1) {
					if (!isCorrectType(types, args))
						continue;

					ctor = ctors[n];
					break;
				}
			}

			if (ctor == null) {
				return BsError.nameError("new");
			}

			Type[] types = ctor.getGenericParameterTypes();
			if (types.length == 0) {
				BsJavaData data = new BsJavaData(cls, ctor.newInstance());
				BsObject obj = BsObject.value(BsConst.Java, data);
				return obj;
			} else {
				Object[] arguments = new Object[types.length];
				for (int n = 1; n < args.length; n++) {
					Object obj = args[n].value();
					if (obj == null) {
						return BsError.typeError("new", BsConst.Nil,
								BsConst.Java);
					} else {
						arguments[0] = obj;
					}
				}

				BsJavaData data = new BsJavaData(cls,
						ctor.newInstance(arguments));
				return BsObject.value(BsConst.Java, data);
			}

		} catch (Exception e) {
			return BsError.nameError(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone("Java" + self.value());
	}

	/**
	 * @param types
	 * @param args
	 * @return
	 */
	protected boolean isCorrectType(Type[] types, BsObject... args) {
		for (int i = 1; i < args.length; i++) {
			Object object = args[i].value();
			if (object != null && object.getClass() != types[i - 1]) {
				return false;
			}
		}
		return true;
	}
}
