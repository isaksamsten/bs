package com.bs.lang.proto;

import java.util.HashMap;
import java.util.Map;

import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsSymbol extends BsAbstractProto {

	private static final Map<String, BsObject> symbols = new HashMap<String, BsObject>();

	public BsSymbol() {
		super(BsConst.Comparable, "Symbol", BsSymbol.class);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone((String) self.value());
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
