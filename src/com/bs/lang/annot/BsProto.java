package com.bs.lang.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BsProto {

	/**
	 * The name of the prototype
	 * 
	 * @return
	 */
	String name();
}
