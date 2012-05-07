package com.bs.lang.message;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsObject;
import com.bs.parser.tree.Node;

public interface BsCode {

	/**
	 * 
	 * @param self
	 * @param args
	 * @return
	 */
	BsObject invoke(BsObject self, BsObject... args);

	boolean isInternal();
	
	Node getCode();
	
	Node getLastEvaluatedCode();

	int getArity();

	Stack getStack();

	void setStack(Stack s);

	void addArgument(String str);

	void cloneStack();
}
