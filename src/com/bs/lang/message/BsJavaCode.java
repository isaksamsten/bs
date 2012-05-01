package com.bs.lang.message;

import java.lang.reflect.Method;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsError;

public class BsJavaCode implements BsCode {

	private Method method;
	private Object invoker;
	private int arity;

	public BsJavaCode(Object invoker, Method m) {
		method = m;
		arity = m.getParameterTypes().length;
		this.invoker = invoker;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		try {
			return (BsObject) method.invoke(invoker, self, args);
		} catch (Exception e) {
			e.printStackTrace();
			return BsError.raise("Java invokation failed. This is a bug.");
		}
	}

	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public Stack getStack() {
		return null;
	}

	@Override
	public void addArgument(String str) {

	}

	@Override
	public void setStack(Stack s) {
	}

	@Override
	public boolean isInternal() {
		return true;
	}
}
