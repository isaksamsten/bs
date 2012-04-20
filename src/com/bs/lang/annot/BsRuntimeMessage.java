package com.bs.lang.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BsRuntimeMessage {
	String name();

	int arity();

	String[] aliases() default {};
}