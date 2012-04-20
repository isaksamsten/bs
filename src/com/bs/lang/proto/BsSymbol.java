package com.bs.lang.proto;

import java.util.HashMap;
import java.util.Map;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;

public class BsSymbol extends BsObject {

	private static final Map<String, BsObject> symbols = new HashMap<String, BsObject>();

	public BsSymbol() {
		super(BsConst.Proto, "Symbol", BsSymbol.class);
		initRuntimeMethods();
	}

	/**
	 * Get a static instance of a symbol
	 * 
	 * @param value
	 * @return
	 */
	public static BsObject get(String value) {
		BsObject obj = symbols.get(value);
		if (obj != null) {
			return obj;
		} else {
			obj = BsObject.value(BsConst.Symbol, value);
			symbols.put(value, obj);
			return obj;
		}
	}
}
