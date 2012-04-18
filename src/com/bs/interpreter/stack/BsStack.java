package com.bs.interpreter.stack;

import java.util.ArrayList;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsStack implements Stack {

	private static final BsStack instance = new BsStack();

	public static BsStack getDefault() {
		return instance;
	}

	private ArrayList<BsObject> stack = new ArrayList<BsObject>();
	private BsObject global = Bs.builtin();
	private int current = -1;

	public BsStack() {
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
		for (int i = current; i >= 0 && Bs.isNil(found); i--) {
			found = stack.get(i).slot(key);
		}
		if (Bs.isNil(found)) {
			found = global.slot(key);
		}

		return found;
	}

	@Override
	public BsObject enter(String key, BsObject value) {
		int idx = foundAt(key);
		BsObject c = null;
		if (idx > 0) {
			c = stack.get(idx);
		} else {
			c = local();
		}

		c.slot(key, value);
		return value;
	}

	protected int foundAt(String key) {
		for (int i = current; i >= 0; i--) {
			if (stack.get(i).hasSlot(key)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public BsObject global() {
		return global;
	}

	@Override
	public BsObject root() {
		return stack.get(0);
	}

	@Override
	public BsObject enterGlobal(String key, BsObject value) {
		global.slot(key, value);
		return value;
	}

}
