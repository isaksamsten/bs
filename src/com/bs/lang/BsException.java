package com.bs.lang;

import com.bs.lang.proto.BsError;

public class BsException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637955054735556298L;

	private BsObject error;

	public BsException(BsObject error) {
		this.error = error;
	}

	public BsObject getError() {
		return error;
	}

	@Override
	public String toString() {
		return error.getSlot(BsError.MESSAGE).value();
	}
	
}
