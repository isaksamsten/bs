package com.bs.interpreter.stack;

import com.bs.lang.BsObject;

public interface StackFrame {
	public void setSlot(String key, BsObject value);

	public BsObject getSlot(String key);

	public boolean hasSlot(String key);

	public boolean searchParent();

	BsObject removeSlot(String key);
}
