package com.bs.lang.builtin;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;

public class BsNumber extends BsAbstractProto {

	public static BsObject clone(Number number) {
		if (number == null) {
			return BsConst.Nil;
		}
		return BsObject.value(BsConst.Number, number);
	}

	public BsNumber() {
		super(BsConst.Comparable, "Number", BsNumber.class);
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

	@BsRuntimeMessage(name = "next", arity = 0)
	public BsObject next(BsObject self, BsObject... args) {
		return clone(Bs.asNumber(self).intValue() + 1);
	}

	@BsRuntimeMessage(name = "prev", arity = 0)
	public BsObject prev(BsObject self, BsObject... args) {
		return clone(Bs.asNumber(self).intValue() + 1);
	}

	@BsRuntimeMessage(name = "--", arity = 1)
	public BsObject range(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Number)) {
			return BsError.typeError("--", args[0], BsConst.Number);
		}

		return BsRange.clone(Bs.asNumber(self), Bs.asNumber(args[0]));
	}

	@BsRuntimeMessage(name = "compareTo", arity = 1, aliases = { "<=>" })
	public BsObject compareTo(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.Number)) {
			return BsError.typeError("compareTo", args[0], BsConst.String);
		}

		return subtract(self, args);
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		return BsString.clone(Bs.asNumber(self).toString());
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
			if (isLong(lhs) || isLong(rhs)) {
				result = lhs.longValue() + lhs.longValue();
			} else {
				result = lhs.intValue() + rhs.intValue();
			}
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
			if (isLong(lhs) || isLong(rhs)) {
				result = lhs.longValue() - lhs.longValue();
			} else {
				result = lhs.intValue() - rhs.intValue();
			}
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
			if (isLong(lhs) || isLong(rhs)) {
				result = lhs.longValue() * lhs.longValue();
			} else {
				result = lhs.intValue() * rhs.intValue();
			}
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
			if (isLong(lhs) || isLong(rhs)) {
				result = lhs.longValue() / lhs.longValue();
			} else {
				result = lhs.intValue() / rhs.intValue();
			}
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

	private boolean isLong(Number n) {
		return n != null && n instanceof Long;
	}

	private boolean isFloat(Number n) {
		return n != null && (n instanceof Float || n instanceof Double);
	}
}
