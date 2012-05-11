package com.bs.lang;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.bs.interpreter.stack.StackFrame;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsCodeData;
import com.bs.lang.message.BsJavaCode;
import com.bs.lang.message.BsMessage;
import com.bs.lang.message.BsMessageCode;
import com.bs.lang.message.BsMessageData;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsString;

/**
 * 
 * @author isak
 * 
 */
public class BsObject implements StackFrame {

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

	private boolean isBreaking;

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
		this.isBreaking = false;
		id = ID++;
	}

	/**
	 * Initialize any runtime methods defined by the {@link BsRuntimeMessage}
	 * annotation and add them to the list of methods for this prototype
	 */
	protected void initRuntimeMethods() {
		Method[] methods = klass.getMethods();
		for (Method m : methods) {
			BsRuntimeMessage brm = m.getAnnotation(BsRuntimeMessage.class);
			if (brm != null) {
				BsJavaCode proxy = new BsJavaCode(this, m);

				addMessage(brm.name(), brm.arity(), proxy);
				for (String alias : brm.aliases()) {
					addMessage(alias, brm.arity(), proxy);
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

	public BsObject getPrototype() {
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

	public boolean isBreakingContext() {
		return isReturning() || isBreaking() || isError();
	}

	public boolean isError() {
		BsObject proto = getPrototype();
		return proto != null && proto.instanceOf(BsConst.Error)
				&& !Bs.asBoolean(getSlot(BsError.IGNORED));
	}

	public void setBreaking(boolean b) {
		this.isBreaking = b;
	}

	/**
	 * If we should break from the currently executing context. Stops when the
	 * context is exited.
	 * 
	 * @return
	 */
	public boolean isBreaking() {
		return this.isBreaking;
	}

	/**
	 * Set if this object should be returned from the currently executing
	 * method. Stop when method has returned.
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

	/**
	 * Get this, or supertype method
	 * 
	 * @param name
	 * @return
	 */
	public BsMessage getMessage(String name) {
		BsMessageData data = getMessageData(name);
		if (data != null) {
			return data.getMessage(this);
		} else {
			return null;
		}
	}

	public BsMessageData getMessageData(String name) {
		BsMessageData data = messages.get(name);
		if (data != null) {
			return data;
		}

		if (prototype != null) {
			return prototype.getMessageData(name);
		} else {
			return null;
		}
	}

	public boolean respondTo(String name) {
		return messages.containsKey(name);
	}

	public void addMessage(String name, BsCodeData code) {
		messages.put(name, new BsMessageData(name, code.arity,
				new BsMessageCode(code)));
	}

	public void addMessage(String name, int arity, BsJavaCode code) {
		messages.put(name, new BsMessageData(name, arity, code));
	}

	public BsObject invoke(String message, BsObject... args) {
		BsMessage msg = getMessage(message);
		BsObject obj = null;
		if (msg != null) {
			obj = msg.invoke(this, args);
		} else if ((msg = getMessage(Bs.METHOD_MISSING)) != null) {
			args = ArrayUtils.add(args, 0, BsString.clone(message));
			obj = msg.invoke(this, args);
		}

		if (obj != null) {
			return obj;
		}

		return BsError.nameError(message, this);
	}

	@Override
	public BsObject getSlot(String key) {
		BsObject obj = slots.get(key);
		return obj;
	}

	@Override
	public void setSlot(String key, BsObject value) {
		slots.put(key, value);
	}

	public void setSlot(BsObject value) {
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

	@Override
	public boolean hasSlot(String key) {
		return slots.containsKey(key);
	}

	@Override
	public BsObject removeSlot(String key) {
		return slots.remove(key);
	}

	public boolean isNil() {
		return instanceOf(BsConst.Nil);
	}

	@Override
	public boolean searchParent() {
		return true;
	}

	public void setPrototype(BsObject bsObject) {
		this.prototype = bsObject;
	}

}
