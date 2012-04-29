package com.bs.interpreter.stack;

import com.bs.lang.BsObject;

public class BreakFrame implements StackFrame {

	public static final StackFrame BREAK = new BreakFrame();

	private BreakFrame() {
	}

	@Override
	public void setSlot(String key, BsObject value) {

	}

	@Override
	public BsObject getSlot(String key) {
		return null;
	}

	@Override
	public boolean hasSlot(String key) {
		return false;
	}

	@Override
	public boolean searchParent() {
		return false;
	}

	@Override
	public BsObject removeSlot(String key) {
		return null;
	}

}
