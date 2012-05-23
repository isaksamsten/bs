package com.bs.lang.builtin;

import java.io.InputStreamReader;

import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.builtin.io.BsReader;
import com.bs.lang.builtin.io.BsWriter;

public class BsSystem extends BsAbstractProto {

	public BsSystem() {
		super(BsConst.Proto, "System", BsSystem.class);

		setSlot("in", BsReader.create(new InputStreamReader(System.in)));
		setSlot("out", BsWriter.create(System.out));
	}

	@BsRuntimeMessage(name = "puts", arity = -1, aliases = { "<<" })
	public BsObject puts(BsObject self, BsObject... args) {
		BsObject ret = getSlot("out").invoke("println", args);
		if (ret.isError()) {
			return ret;
		}
		return self;
	}

	@BsRuntimeMessage(name = "read", arity = 0)
	public BsObject read(BsObject self, BsObject... args) {
		return getSlot("in").invoke("readLine");
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsError.raise("Can't clone '%s'", "System");
	}
}
