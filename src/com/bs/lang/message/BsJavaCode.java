package com.bs.lang.message;

import java.lang.reflect.Method;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsError;
import com.bs.parser.tree.Node;

public class BsJavaCode implements BsCode {

	private Method method;
	private Object invoker;
	private int arity;

	public BsJavaCode(Object invoker, Method m) {
		method = m;
		arity = m.getParameterTypes().length - 1;
		this.invoker = invoker;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		try {
			return (BsObject) method.invoke(invoker, self, args);
		} catch (Exception e) {
			return BsError.javaError(e.getMessage());
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

	@Override
	public void cloneStack() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getCode() {
		return null;
	}

	@Override
	public Node getLastEvaluatedCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
