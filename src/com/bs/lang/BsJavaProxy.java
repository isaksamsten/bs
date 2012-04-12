package com.bs.lang;

import java.lang.reflect.Method;

public class BsJavaProxy implements BsCode {

	private Method method;
	private Object invoker;

	public BsJavaProxy(Object invoker, Method m) {
		method = m;
		this.invoker = invoker;
	}

	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		try {
			return (BsObject) method.invoke(invoker, self, args);
		} catch (Exception e) {
			e.printStackTrace();
			throw BsError.NO_SUCH_METHOD;
		}
	}
}
