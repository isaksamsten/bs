package com.bs.lang.builtin.java;

public class BsJavaData {
	public Class<?> cls;
	public Object instance;

	public BsJavaData(Class<?> cls, Object instance) {
		this.cls = cls;
		this.instance = instance;
	}

	@Override
	public String toString() {
		return "Instance of " + cls.getCanonicalName() + " with value "
				+ instance;
	}

}