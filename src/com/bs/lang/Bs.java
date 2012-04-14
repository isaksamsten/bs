package com.bs.lang;

public final class Bs {

	/**
	 * Check is obj is the same object (reference) as BsConst.True
	 * 
	 * @param obj
	 *            - any BsObject or null
	 * @return
	 */
	public static boolean isTrue(BsObject obj) {
		return obj != null && obj == BsConst.True;
	}

	/**
	 * Return the value of a BsNumber
	 * 
	 * @param obj
	 * @return
	 */
	public static Number getNumber(BsObject obj) {
		return obj.instanceOf(BsConst.Number) ? (Number) obj.value() : null;
	}

	public static String getString(BsObject obj) {
		return obj.instanceOf(BsConst.String) ? (String) obj.value() : null;
	}
}
