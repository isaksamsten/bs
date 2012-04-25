package com.bs.lang.proto.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsNumber;
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

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		String name = Bs.asString(args[0]);
		BsJavaData data = self.value();

		Class<?>[] classes = new Class<?>[args.length - 1];
		Object[] arguments = new Object[args.length - 1];
		for (int n = 1; n < args.length; n++) {
			Object value = args[n].value();
			if (value != null) {
				classes[n] = getClass(value);
				arguments[n] = value;
			} else {
				return BsError
						.javaError("Unsupported type in invokation (is it really a Java type?)");
			}
		}

		try {
			Method method = data.cls.getMethod(name, classes);
			Object ret = method.invoke(data.instance, arguments);

			return getBsObject(ret.getClass(), ret);
		} catch (Exception e) {
			return BsError.javaError(e.getMessage());
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
				return BsError.javaError("No such Constructor");
			}

			Type[] types = ctor.getGenericParameterTypes();
			if (types.length == 0) {
				BsJavaData data = new BsJavaData(cls, ctor.newInstance());
				BsObject obj = BsObject.value(BsConst.Java, data);
				return obj;
			} else {
				Object[] arguments = new Object[types.length];
				for (int n = 1; n < args.length; n++) {
					Object obj = getValue(args[n].value());
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
			return BsError.javaError(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone("Java" + self.value());
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	protected Class<?> getClass(Object obj) {
		if (obj instanceof BsJavaData) {
			return ((BsJavaData) obj).cls;
		} else {
			return obj.getClass();
		}
	}

	protected Object getValue(Object obj) {
		if (obj instanceof BsJavaData) {
			return ((BsJavaData) obj).instance;
		} else if (obj == BsConst.True) {
			return true;
		} else if (obj == BsConst.False) {
			return false;
		} else if (obj == BsConst.Nil) {
			return null;
		} else {
			return obj;
		}
	}

	protected BsObject getBsObject(Class<?> cls, Object value) {
		if (value instanceof Number) {
			return BsNumber.clone((Number) value);
		} else if (value instanceof String) {
			return BsString.clone((String) value);
		} else if (value instanceof Boolean) {
			if ((Boolean) value) {
				return BsConst.True;
			} else {
				return BsConst.False;
			}
		} else if (value == null) {
			return BsConst.Nil;
		} else {
			return BsObject.value(BsConst.Java, new BsJavaData(cls, value));
		}
	}

	/**
	 * @param types
	 * @param args
	 * @return
	 */
	protected boolean isCorrectType(Type[] types, BsObject[] args) {
		for (int i = 1; i < args.length; i++) {
			Object object = args[i].value();
			if (object != null) {
				Class<?> cls = getClass(object);
				if (!((Class<?>) types[i - 1]).isAssignableFrom(cls))
					return false;
			}
		}
		return true;
	}
}
