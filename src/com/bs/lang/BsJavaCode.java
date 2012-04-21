package com.bs.lang;

import java.lang.reflect.Method;

import com.bs.lang.proto.BsError;

public class BsJavaCode implements BsCode {

	private Method method;
	private Object invoker;

	public BsJavaCode(Object invoker, Method m) {
		method = m;
		this.invoker = invoker;
	}

	@Override
	public BsObject execute(BsObject self, BsObject... args) {
		try {
			return (BsObject) method.invoke(invoker, self, args);
		} catch (Exception e) {
			e.printStackTrace();
			return BsError.raise("Java invokation failed. This is a bug.");
		}
	}

	@Override
	public BsMessage getMessage(String name, int arity, BsObject binder) {
		return new BsMessage(name, arity, this);
	}
}
