package com.bs.lang;

import com.bs.lang.proto.BsError;

public class BsMessage {
	private int arity;

	private BsCode code;

	/**
	 * 
	 * @param arity
	 *            -1 unkown
	 */
	public BsMessage(String name, int arity, BsCode code) {
		this.arity = arity;
		this.code = code;
	}

	public BsMessage(String name, BsCode code) {
		this(name, -1, code);
	}

	public boolean isJavaCode() {
		return code instanceof BsJavaProxy;
	}

	/**
	 * Executes using the supplied BsCode object
	 * 
	 * @param self
	 * @param args
	 * @return
	 */
	public BsObject invoke(BsObject self, BsObject... args) {
		if (arity > 0 && arity != args.length) {
			return BsError.raise("Invalid arity");
		}

		return code.execute(self, args);
	}
}
