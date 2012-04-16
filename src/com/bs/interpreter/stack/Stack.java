package com.bs.interpreter.stack;

import com.bs.lang.BsObject;

public interface Stack {

	BsObject[] stack();

	BsObject local();

	BsObject enter(String key, BsObject value);

	BsObject push(BsObject obj);

	BsObject pop();

	BsObject lookup(String key);
}
