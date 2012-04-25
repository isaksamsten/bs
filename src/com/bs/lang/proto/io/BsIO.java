package com.bs.lang.proto.io;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.sun.xml.internal.ws.Closeable;

public class BsIO extends BsObject {

	public BsIO() {
		super(BsConst.Proto, "IO", BsIO.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "close", arity = 0)
	public BsObject close(BsObject self, BsObject... args) {
		Closeable reader = self.value();
		reader.close();
		return self;
	}
}
