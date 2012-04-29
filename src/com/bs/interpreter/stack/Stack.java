package com.bs.interpreter.stack;

import com.bs.lang.BsObject;

public interface Stack {

	BsObject[] stack();

	StackFrame local();

	BsObject enter(String key, BsObject value);

	BsObject enterGlobal(String key, BsObject value);

	StackFrame push(StackFrame obj);

	StackFrame pop();

	BsObject global();

	StackFrame root();

	BsObject lookup(String key);

	int depth();
}
