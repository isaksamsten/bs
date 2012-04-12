package com.bs.lang;

public class BsError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static BsError NO_SUCH_METHOD = new BsError("No such method");
	public static final BsError INVALID_ARITY = new BsError("Invalid arity");

	public BsError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BsError(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BsError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BsError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BsError(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
