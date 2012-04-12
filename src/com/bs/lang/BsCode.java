package com.bs.lang;

public interface BsCode {

	/**
	 * 
	 * @param self
	 * @param args
	 * @return
	 */
	BsObject execute(BsObject self, BsObject... args);

}
