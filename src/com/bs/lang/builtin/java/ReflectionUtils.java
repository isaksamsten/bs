package com.bs.lang.builtin.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.builtin.BsList;
import com.bs.lang.builtin.BsNumber;
import com.bs.lang.builtin.BsString;

public final class ReflectionUtils {

	/**
	 * Find method name of class cls that adhere to the parameters in
	 * parameters.
	 * 
	 * 
	 * {@link ReflectionUtils#isAllAssignableFrom(Type[], BsObject[])}
	 * 
	 * @param cls
	 * @param name
	 * @return
	 */
	public static Method findMethod(Class<?> cls, String name,
			Class<?>[] parameters) {
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			if (!method.getName().equals(name)) {
				continue;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != parameters.length) {
				continue;
			}
			boolean matches = true;
			for (int i = 0; i < parameterTypes.length; i++) {
				if (!convertClass(parameterTypes[i]).isAssignableFrom(
						parameters[i])) {
					matches = false;
					break;
				}
			}
			if (matches) {
				return method;
			}
		}
		return null;
	}

	public static Method findStaticMethod(Class<?> cls, String name,
			Class<?>[] parameters) {
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			if (!method.getName().equals(name)
					|| !Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != parameters.length) {
				continue;
			}
			boolean matches = true;
			for (int i = 0; i < parameterTypes.length; i++) {
				if (!convertClass(parameterTypes[i]).isAssignableFrom(
						parameters[i])) {
					matches = false;
					break;
				}
			}
			if (matches) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Convert primitive.class to Object.class, i.e. int.class => Integer.class
	 * 
	 * @param cls
	 * @return
	 */
	public static Class<?> convertClass(Class<?> cls) {
		if (cls == int.class) {
			return Integer.class;
		} else if (cls == long.class) {
			return Long.class;
		} else if (cls == double.class) {
			return Double.class;
		} else if (cls == float.class) {
			return Float.class;
		} else if (cls == boolean.class) {
			return Boolean.class;
		} else if (cls == char.class) {
			return Character.class;
		} else if (cls == byte.class) {
			return Byte.class;
		} else {
			return cls;
		}
	}

	/**
	 * Create a BsObject from an Object. If value is Number, String, Boolean,
	 * Character, List or Array, will a corresponding BsObject be returned.
	 * 
	 * Else; is an instance of proto (JavaInstance, JavaClass) returned with the
	 * corresponding object of reflection.
	 * 
	 * @param proto
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static BsObject createBsObject(BsObject proto, Object value) {
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
		} else if (value instanceof List) {
			List<BsObject> obj = new ArrayList<BsObject>();
			List<Object> list = (List<Object>) value;
			for (Object o : list) {
				if (o == null) {
					obj.add(BsConst.Nil);
				} else {
					obj.add(createBsObject(o));
				}
			}
			return BsList.create(obj);
		} else if (value.getClass().isArray()) {
			List<BsObject> obj = new ArrayList<BsObject>();
			Object[] list = (Object[]) value;
			for (Object o : list) {
				if (o == null) {
					obj.add(BsConst.Nil);
				} else {
					obj.add(createBsObject(o));
				}
			}
			return BsList.create(obj);
		} else {
			Class<?> cls = value.getClass();
			if (value instanceof Class) {
				cls = (Class<?>) value;
			}

			return BsObject.value(proto, new BsJavaData(cls, value));
		}
	}

	/**
	 * Same as:
	 * {@link ReflectionUtils#createBsObject(BsConst.JavaInstance, Object)}
	 * 
	 * @param value
	 * @return
	 */
	public static BsObject createBsObject(Object value) {
		return createBsObject(BsConst.JavaInstance, value);
	}

	/**
	 * Find a constructor that corresponds to the arguments defined by args.
	 * 
	 * {@link ReflectionUtils#isAllAssignableFrom(Type[], BsObject[])}
	 * 
	 * @param cls
	 * @param args
	 * @return
	 */
	public static Constructor<?> findConstructor(Class<?> cls, BsObject[] args) {
		Constructor<?>[] ctors = cls.getConstructors();
		Constructor<?> ctor = null;
		for (int n = 0; n < ctors.length; n++) {
			Type[] types = ctors[n].getGenericParameterTypes();
			if (types.length == args.length) {
				if (!ReflectionUtils.isAllAssignableFrom(types, args))
					continue;

				ctor = ctors[n];
				break;
			}
		}
		return ctor;
	}

	public static Object[] createJavaObjects(BsObject[] args, int offset) {
		Object[] arguments = new Object[args.length - offset];
		for (int n = offset; n < args.length; n++) {
			Object value = args[n].value();
			if (value != null) {
				arguments[n - offset] = ReflectionUtils.getValue(value);
			} else {
				return null;
			}
		}

		return arguments;
	}

	public static Object[] createJavaObjects(BsObject[] o) {
		return createJavaObjects(o, 0);
	}

	public static BsObject[] createBsObjects(Object[] args) {
		if (args == null) {
			return new BsObject[0];
		}

		BsObject[] obj = new BsObject[args.length];
		for (int n = 0; n < args.length; n++) {
			obj[n] = createBsObject(args[n]);
		}

		return obj;
	}

	/**
	 * Get the value of an object.
	 * 
	 * @param obj
	 * @return
	 */
	public static Object getValue(Object obj) {
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

	/**
	 * 
	 * @param obj
	 * @return
	 */
	protected static Class<?> getClass(Object obj) {
		if (obj instanceof BsJavaData) {
			return ((BsJavaData) obj).cls;
		} else {
			return obj.getClass();
		}
	}

	/**
	 * Check if the two arrays are "assignable" to each other.
	 * 
	 * @param types
	 * @param args
	 * @return
	 */
	protected static boolean isAllAssignableFrom(Type[] types, BsObject[] args) {
		for (int i = 0; i < args.length; i++) {
			Object object = args[i].value();
			if (object != null) {
				Class<?> cls = getClass(object);
				Class<?> cls2 = convertClass((Class<?>) types[i]);
				if (!cls2.isAssignableFrom(cls))
					return false;
			}
		}
		return true;
	}
}
