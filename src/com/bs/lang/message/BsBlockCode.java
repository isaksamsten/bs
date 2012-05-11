package com.bs.lang.message;

import java.util.ArrayList;
import java.util.List;

import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsList;
import com.bs.parser.tree.Node;

public class BsBlockCode implements BsCode {

	public BsCodeData data;

	public BsBlockCode(BsCodeData data) {
		this.data = data;
	}

	@Override
	public BsObject invoke(BsObject self, BsObject... args) {
		if (data.arity > 0 && data.arity != args.length) {
			return BsError.typeError(self, "call", args.length, data.arity);
		}

		/*
		 * This is not a variable call
		 */
		if (data.arity > 0) {
			for (int n = 0; n < args.length; n++) {
				self.setSlot(data.arguments.get(n), args[n]);
			}
		} else {
			/*
			 * If there are more arguments than arguments
			 * we fail
			 */
			if (data.arguments.size() > args.length) {
				return BsError.typeError(self, "call", args.length,
						data.arguments.size());
			}

			/*
			 * Initialize all non rest
			 */
			for (int n = 0; n < data.arguments.size(); n++) {
				self.setSlot(data.arguments.get(n), args[n]);
			}

			/*
			 * If there are any more - collect them as the rest
			 */
			if (data.arguments.size() < args.length) {
				List<BsObject> rest = new ArrayList<BsObject>();
				for (int n = data.arguments.size() - 1; n < args.length; n++) {
					rest.add(args[n]);
				}
				self.setSlot(data.arguments.get(data.arguments.size() - 1),
						BsList.create(rest));
			}

		}

		Stack stack = data.stack;
		stack.push(self);
		BsObject ret = Bs.eval(data.code, stack);
		stack.pop();
		return ret;
	}

	@Override
	public int getArity() {
		return data.arity;
	}

	@Override
	public Stack getStack() {
		return data.stack;
	}

	@Override
	public void addArgument(String str) {
		data.arguments.add(str);
	}

	@Override
	public void setStack(Stack s) {
		data.stack = s;
	}

	@Override
	public boolean isInternal() {
		return false;
	}

	@Override
	public void cloneStack() {
		data.stack = data.stack.clone();
	}

	@Override
	public Node getCode() {
		return data.code;
	}

	@Override
	public Node getLastEvaluatedCode() {
		return data.lastEval;
	}
}
