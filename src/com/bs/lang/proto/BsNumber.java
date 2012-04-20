package com.bs.lang.proto;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsNumber extends BsObject {

	public static BsObject clone(Number number) {
		return BsObject.value(BsConst.Number, number);
	}

	public BsNumber() {
		super(BsConst.Proto, "Number", BsNumber.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "+", arity = 1)
	public BsObject add(BsObject self, BsObject... args) {
		Number lhs = self.value();
		Number rhs = args[0].value();

		Number result = add(lhs, rhs);
		return clone(result);
	}

	@BsRuntimeMessage(name = "-", arity = 1)
	public BsObject subtract(BsObject self, BsObject... args) {
		Number lhs = self.value();
		Number rhs = args[0].value();

		Number result = subtract(lhs, rhs);
		return clone(result);
	}

	@BsRuntimeMessage(name = "*", arity = 1)
	public BsObject multiply(BsObject self, BsObject... args) {
		Number lhs = self.value();
		Number rhs = args[0].value();

		Number result = multiply(lhs, rhs);
		return clone(result);
	}

	@BsRuntimeMessage(name = "/", arity = 1)
	public BsObject divide(BsObject self, BsObject... args) {
		Number lhs = self.value();
		Number rhs = args[0].value();

		Number result = divide(lhs, rhs);
		return clone(result);
	}

	@BsRuntimeMessage(name = "%", arity = 1)
	public BsObject modulo(BsObject self, BsObject... args) {
		Number lhs = self.value();
		Number rhs = args[0].value();

		Number result = modulo(lhs, rhs);
		return clone(result);
	}

	@BsRuntimeMessage(name = "--", arity = 1)
	public BsObject range(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Number)) {
			return BsError.typeError("--", args[0], BsConst.Number);
		}

		return BsRange.clone(Bs.asNumber(self), Bs.asNumber(args[0]));
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	protected Number add(Number lhs, Number rhs) {
		Number result = null;
		if (isFloat(lhs) || isFloat(rhs)) {
			result = lhs.doubleValue() + rhs.doubleValue();
		} else {
			result = lhs.intValue() + rhs.intValue();
		}
		return result;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	protected Number subtract(Number lhs, Number rhs) {
		Number result = null;
		if (isFloat(lhs) || isFloat(rhs)) {
			result = lhs.doubleValue() - rhs.doubleValue();
		} else {
			result = lhs.intValue() - rhs.intValue();
		}
		return result;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	protected Number multiply(Number lhs, Number rhs) {
		Number result = null;
		if (isFloat(lhs) || isFloat(rhs)) {
			result = lhs.doubleValue() * rhs.doubleValue();
		} else {
			result = lhs.intValue() * rhs.intValue();
		}
		return result;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	protected Number divide(Number lhs, Number rhs) {
		Number result = null;
		if (isFloat(lhs) || isFloat(rhs)) {
			result = lhs.doubleValue() / rhs.doubleValue();
		} else {
			result = lhs.intValue() / rhs.intValue();
		}
		return result;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	protected Number modulo(Number lhs, Number rhs) {
		int result = lhs.intValue() % rhs.intValue();
		return result;
	}

	private boolean isFloat(Number n) {
		return n != null && (n instanceof Float || n instanceof Double);
	}
}
