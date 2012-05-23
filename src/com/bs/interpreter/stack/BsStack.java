package com.bs.interpreter.stack;

import java.util.ArrayList;

import com.bs.lang.Bs;
import com.bs.lang.BsObject;
import com.bs.lang.builtin.BsError;

public class BsStack implements Stack {

	private static BsStack instance;

	private static final int MAX_STACK_DEPTH = 100;

	public static BsStack getDefault() {
		if (instance == null)
			instance = new BsStack();

		return instance;
	}

	private ArrayList<StackFrame> stack = new ArrayList<StackFrame>(100);
	private BsObject global = Bs.builtin();
	private int current = -1;

	public BsStack() {
	}

	@Override
	public BsObject[] stack() {
		return stack.toArray(new BsObject[0]);
	}

	@Override
	public StackFrame local() {
		return stack.get(current);
	}

	@Override
	public StackFrame push(StackFrame obj) {
		stack.add(obj);
		current++;
		return obj;
	}

	@Override
	public StackFrame pop() {
		current--;
		return stack.remove(current + 1);
	}

	@Override
	public BsObject lookup(String key) {
		BsObject found = null;
		for (int i = current; i >= 0 && found == null; i--) {
			if (!stack.get(i).searchParent())
				break;

			found = stack.get(i).getSlot(key);
		}
		if (found == null) {
			found = global.getSlot(key);
		}
		if (found == null) {
			found = BsError.nameError(key);
		}

		return found;
	}

	@Override
	public BsObject enter(String key, BsObject value) {
		int idx = foundAt(key);
		StackFrame c = null;
		if (idx >= 0) {
			c = stack.get(idx);
		} else {
			c = local();
		}

		c.setSlot(key, value);
		return value;
	}

	protected int foundAt(String key) {
		for (int i = current; i >= 0; i--) {
			if (!stack.get(i).searchParent()) {
				break;
			}
			if (stack.get(i).hasSlot(key)) {
				return i;
			}

		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized Stack clone() {
		BsStack stack = new BsStack();
		stack.current = current;
		stack.global = global;
		stack.stack = (ArrayList<StackFrame>) this.stack.clone();

		return stack;
	}

	@Override
	public BsObject global() {
		return global;
	}

	@Override
	public StackFrame root() {
		return stack.get(0);
	}

	@Override
	public BsObject enterGlobal(String key, BsObject value) {
		global.setSlot(key, value);
		return value;
	}

	@Override
	public int depth() {
		return current;
	}

}
