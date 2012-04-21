package com.bs.lang.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BsRuntimeMessage {

	int VARIABLE = -1;

	/**
	 * The name of the message
	 * 
	 * @return
	 */
	String name();

	/**
	 * The arity of the message
	 * 
	 * 1...n
	 * 
	 * or
	 * 
	 * -1 for variabel
	 * 
	 * @return
	 */
	int arity();

	/**
	 * Arbitary list of aliases for this message
	 * 
	 * @return
	 */
	String[] aliases() default {};
}
