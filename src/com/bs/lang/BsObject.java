package com.bs.lang;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;

/**
 * 
 * @author isak
 * 
 */
public class BsObject {

	/**
	 * 
	 * @author isak
	 * 
	 */
	private class BsMessageData {
		public BsCode code;
		public int arity;
		public String name;

		public BsMessageData(BsCode code, int arity, String name) {
			this.code = code;
			this.arity = arity;
			this.name = name;
		}

		public BsMessage getMessage(BsObject binder) {
			return code.getMessage(name, arity, binder);
		}

	}

	/**
	 * Create a bs object with proto as Prototype and value as a java value
	 * 
	 * @param proto
	 * @param value
	 * @return
	 */
	public static BsObject value(BsObject proto, Object value) {
		BsObject obj = new BsObject(proto);
		obj.value(value);
		return obj;
	}

	/**
	 * Create a new BsObject with a prototype
	 * 
	 * @param proto
	 * @return
	 */
	public static BsObject clone(BsObject proto) {
		BsObject obj = new BsObject(proto);
		return obj;
	}

	/**
	 * Id incremented for each object in the system.
	 */
	private static long ID = 0;

	/**
	 * This objects id
	 */
	private long id;
	private BsObject prototype;

	private String name;
	private Object value;

	private Map<String, BsMessageData> messages = new HashMap<String, BsMessageData>();
	private Map<String, BsObject> slots = new HashMap<String, BsObject>();

	private Class<?> klass;

	private boolean isReturning;

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
		this.isReturning = false;
		id = ID++;
	}

	protected void initRuntimeMethods() {

		Method[] methods = klass.getMethods();
		for (Method m : methods) {
			BsRuntimeMessage brm = m.getAnnotation(BsRuntimeMessage.class);
			if (brm != null) {
				BsJavaCode proxy = new BsJavaCode(this, m);

				messages.put(brm.name(), new BsMessageData(proxy, brm.arity(),
						brm.name()));
				for (String alias : brm.aliases()) {
					messages.put(alias, new BsMessageData(proxy, brm.arity(),
							alias));
				}
			}
		}
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
		return isError() || isReturning();
	}

	public boolean isError() {
		BsObject proto = prototype();
		return proto != null && proto.instanceOf(BsConst.Error)
				&& !Bs.asBoolean(slot(BsError.IGNORED));
	}

	/**
	 * Set if this object should be returned from the currently executing
	 * context
	 * 
	 * @param b
	 */
	public void setReturning(boolean b) {
		this.isReturning = b;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReturning() {
		return this.isReturning;
	}

	@SuppressWarnings("unchecked")
	public <T> T value() {
		return (T) safeValue();
	}

	public BsMessage message(String name) {
		BsMessageData data = messages.get(name);
		if (data != null) {
			return data.getMessage(this);
		}

		if (prototype != null) {
			return prototype.message(name);
		} else {
			return null;
		}
	}

	public boolean respondTo(String name) {
		return messages.containsKey(name);
	}

	public void message(String name, BsCodeData data) {
		messages.put(name, new BsMessageData(new BsMessageCode(data),
				data.arguments.size(), name));
	}

	public BsObject invoke(String message, BsObject... args) {
		BsMessage msg = message(message);
		if (msg != null) {
			return msg.invoke(this, args);
		} else {
			return BsError.nameError(message, this.prototype());
		}
	}

	public BsObject slot(String key) {
		BsObject obj = slots.get(key);
		if (obj == null) {
			return BsConst.Nil;
		}
		return obj;
	}

	public void slot(String key, BsObject value) {
		slots.put(key, value);
	}

	public void slot(BsObject value) {
		if (value.name() != null)
			slots.put(value.name(), value);
	}

	@Override
	public String toString() {
		BsObject str = invoke("toString");
		if (str.value() != null) {
			return (String) str.value();
		}
		return (name != null ? name : "anonymous") + "@" + id;
	}

	public boolean hasSlot(String key) {
		return slots.containsKey(key);
	}

	public boolean isNil() {
		return instanceOf(BsConst.Nil);
	}

}
