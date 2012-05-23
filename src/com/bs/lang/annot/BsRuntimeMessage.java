package com.bs.lang.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.bs.lang.Bs;

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

	/**
	 * * Required argument types (if different leave empty)
	 * 
	 * To get a correct error message must the Class<?> been added to the list
	 * {@link Bs#addPrototype(Class, com.bs.lang.BsObject)} and referenced to
	 * the required prototype. All builtin types are referenced there
	 * 
	 * @return
	 */
	Class<?>[] types() default {};
}
