package com.bs.lang.message;

import java.lang.reflect.Method;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsException;
import com.bs.lang.BsObject;
import com.bs.lang.builtin.BsError;
import com.bs.parser.tree.Node;

public class BsJavaCode implements BsCode {

	private String methodName;
	private Method method;
	private Object invoker;
	private int arity;
	private Class<?>[] classes;

	public BsJavaCode(Object invoker, Method m, String methodName,
			Class<?>[] classes) {
		method = m;
		arity = m.getParameterTypes().length - 1;
		this.classes = classes;
		this.invoker = invoker;
		this.methodName = methodName;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		try {
			if (classes.length > 0) {
				int c = Bs.checkTypes(args, classes);
				if (c >= 0) {
					return BsError.typeError(methodName, args[0],
							Bs.findPrototype(classes[c]));
				}
			}
			return (BsObject) method.invoke(invoker, self, args);
		} catch (Exception e) {
			if (e.getCause() instanceof BsException) {
				throw (BsException) e.getCause();
			}
			e.printStackTrace();
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
