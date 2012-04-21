package com.bs.lang;

import com.bs.lang.proto.BsError;

/**
 * Instances of this class should not be 
 * @author isak
 *
 */
public class BsMessage {
	private int arity;

	private BsCode code;

	private String name;

	/**
	 * 
	 * @param arity
	 *            -1 variable
	 */
	public BsMessage(String name, int arity, BsCode code) {
		this.name = name;
		this.arity = arity;
		this.code = code;
	}

	public BsMessage(String name, BsCode code) {
		this(name, -1, code);
	}

	public boolean isJavaCode() {
		return code instanceof BsJavaCode;
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
			return BsError.typeError(self, name, args.length, arity);
		}

		return code.execute(self, args);
	}
}
