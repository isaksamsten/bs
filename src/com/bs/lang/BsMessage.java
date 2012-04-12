package com.bs.lang;

public class BsMessage extends BsObject {
	private int arity;

	private BsCode code;

	/**
	 * 
	 * @param arity
	 *            -1 unkown
	 */
	public BsMessage(String name, int arity, BsCode code) {
		super(Bs.Proto, name, BsMessage.class);
		this.arity = arity;
		this.code = code;
	}

	public BsMessage(String name, BsCode code) {
		this(name, -1, code);
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
			throw BsError.INVALID_ARITY;
		}

		return code.execute(self, args);
	}
}
