package com.bs.lang;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;

public class BsObject {

	public static BsObject value(BsObject proto, Object value) {
		BsObject obj = new BsObject(proto);
		obj.value(value);
		return obj;
	}

	public static BsObject clone(BsObject proto) {
		BsObject obj = new BsObject(proto);
		return obj;
	}

	private static long ID = 0;

	private long id;
	private BsObject prototype;

	private String name;
	private Object value;

	private Map<String, BsMessageData> messages = new HashMap<String, BsMessageData>();
	private Map<String, BsObject> instVars = new HashMap<String, BsObject>();

	private Class<?> klass;

	private boolean returnMe;

	public BsObject(BsObject prototype, String name) {
		this(prototype, name, BsObject.class);
	}

	public BsObject(BsObject prototype) {
		this(prototype, null);
	}

	protected BsObject(BsObject prototype, String name, Class<?> me) {
		this.name = name;
		this.prototype = prototype;
		this.klass = me;
		this.returnMe = false;
		id = ID++;
	}

	public long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public void value(Object value) {
		this.value = value;
	}

	public Object safeValue() {
		return value;
	}

	public BsObject prototype() {
		return prototype;
	}

	public boolean instanceOf(BsObject obj) {
		if (equals(obj)) {
			return true;
		} else if (prototype != null) {
			return prototype.instanceOf(obj);
		} else {
			return false;
		}
	}

	public boolean isBreak() {
		return isError() || isReturn();
	}

	public boolean isError() {
		BsObject proto = prototype();
		return proto != null && proto.instanceOf(BsConst.Error)
				&& !Bs.asBoolean(slot(BsError.IGNORE));
	}

	/**
	 * Set if this object should be returned from the currently executing
	 * statementslist
	 * 
	 * @param b
	 */
	public void setReturn(boolean b) {
		this.returnMe = b;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReturn() {
		return this.returnMe;
	}

	@SuppressWarnings("unchecked")
	public <T> T value() {
		return (T) safeValue();
	}

	public BsMessage message(String name) {
		BsMessage msg = null;
		BsMessageData data = messages.get(name);
		if (data != null) {
			msg = new BsBoundMessage(data.name, data.arity, data.code, this);
			return msg;
		}
		if ((msg = javaMethod(name)) != null) {
			return msg;
		}

		if (prototype != null) {
			return prototype.message(name);
		} else {
			return null;
		}
	}

	public void message(String asString, BsCodeData data) {
		messages.put(name, new BsMessageData(new BsMessageProxy(data),
				data.arguments.size(), name));
	}

	private BsMessage javaMethod(String name) {
		Method[] methods = klass.getMethods();
		for (Method m : methods) {
			BsRuntimeMessage brm = m.getAnnotation(BsRuntimeMessage.class);
			if (brm != null && brm.name().equals(name)) {
				return new BsMessage(brm.name(), brm.arity(), new BsJavaProxy(
						this, m));
			}
		}
		return null;
	}

	public BsObject invoke(String message, BsObject... args) {
		BsMessage msg = message(message);
		if (msg != null) {
			return msg.invoke(this, args);
		} else {
			return BsError.nameError(message, this);
		}
	}

	public BsObject slot(String key) {
		BsObject obj = instVars.get(key);
		if (obj == null) {
			return BsConst.Nil;
		}
		return obj;
	}

	public void slot(String key, BsObject value) {
		instVars.put(key, value);
	}

	public void slot(BsObject value) {
		if (value.name() != null)
			instVars.put(value.name(), value);
	}

	@Override
	public String toString() {
		BsObject str = invoke("toString");
		if (str.value() != null) {
			return (String) str.value();
		}
		return (name != null ? name : "anonymous") + "@" + id;
	}

}
