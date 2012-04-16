package com.bs.lang;

import java.lang.reflect.Method;

import com.bs.lang.proto.BsError;

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
			return BsError.raise("Invalid java method invokation... Fail!!!");
		}
	}
}
