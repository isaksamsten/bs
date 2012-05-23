package com.bs.lang.message;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.BsObject;
import com.bs.lang.builtin.BsError;
import com.bs.parser.tree.Node;

public class BsMessage implements BsCode {

	private BsMessageData data;
	private BsObject binder;

	public BsMessage(BsMessageData data, BsObject binder) {
		this.data = data;
		this.binder = binder;
	}

	public BsMessageData getData() {
		return data;
	}

	public BsObject getBinder() {
		return binder;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		if (data.arity > 0 && data.arity != args.length) {
			return BsError.typeError(getBinder(), data.name, args.length,
					data.arity);
		}
		return execute(args);
	}

	protected BsObject execute(BsObject... args) {
		BsObject obj = getData().code.invoke(getBinder(), args);
		return obj;
	}

	@Override
	public void cloneStack() {
		data.code.cloneStack();
	}

	@Override
	public int getArity() {
		return data.arity;
	}

	@Override
	public Stack getStack() {
		return data.code.getStack();
	}

	@Override
	public void addArgument(String str) {
		data.code.addArgument(str);
	}

	@Override
	public void setStack(Stack s) {
		data.code.setStack(s);
	}

	@Override
	public boolean isInternal() {
		return data.code.isInternal();
	}

	@Override
	public Node getCode() {
		return data.code.getCode();
	}

	@Override
	public Node getLastEvaluatedCode() {
		return data.code.getLastEvaluatedCode();
	}

}
