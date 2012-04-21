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

	/**
	 * Should it be a global (i.e. reside in builtins)?
	 * 
	 * @return
	 */
	boolean global() default true;
}
