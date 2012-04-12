package com.bs.interpreter.stack;

import java.util.ArrayList;

import com.bs.lang.BsObject;

public class BsStack implements Stack {

	private static final BsStack instance = new BsStack();

	public static BsStack instance() {
		return instance;
	}

	private ArrayList<BsObject> stack = new ArrayList<BsObject>();
	private int current = -1;

	private BsStack() {
	}

	@Override
	public BsObject[] stack() {
		return stack.toArray(new BsObject[0]);
	}

	@Override
	public BsObject local() {
		return stack.get(current);
	}

	@Override
	public BsObject push(BsObject obj) {
		stack.add(obj);
		current++;
		return obj;
	}

	@Override
	public BsObject pop() {
		current--;
		return stack.remove(current + 1);
	}

	@Override
	public BsObject lookup(String key) {
		BsObject found = null;
		for (int i = current; i >= 0 && found == null; i--) {
			found = stack.get(i).var(key);
		}
		return found;
	}

}
