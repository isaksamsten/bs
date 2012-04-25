package com.bs.lang.proto;

import java.io.InputStreamReader;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.io.BsReader;
import com.bs.lang.proto.io.BsWriter;

public class BsSystem extends BsObject {

	public BsSystem() {
		super(BsConst.Proto, "System", BsSystem.class);
		initRuntimeMethods();

		slot("in", BsReader.create(new InputStreamReader(System.in)));
		slot("out", BsWriter.create(System.out));
	}

	@BsRuntimeMessage(name = "puts", arity = -1)
	public BsObject puts(BsObject self, BsObject... args) {
		BsObject ret = slot("out").invoke("println", args);
		if (ret.isError()) {
			return ret;
		}
		return self;
	}

	@BsRuntimeMessage(name = "read", arity = 0)
	public BsObject read(BsObject self, BsObject... args) {
		return slot("in").invoke("readLine");
	}

	@BsRuntimeMessage(name = "clone", arity = 0)
	public BsObject clone(BsObject self, BsObject... args) {
		return BsError.raise("Can't clone '%s'", "System");
	}
}
