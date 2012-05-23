package com.bs.lang.builtin.io;

import java.io.Closeable;
import java.io.IOException;

import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.builtin.BsError;

public class BsIO extends BsObject {

	public BsIO() {
		super(BsConst.Proto, "IO", BsIO.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "close", arity = 0)
	public BsObject close(BsObject self, BsObject... args) {
		Closeable reader = self.value();
		try {
			reader.close();
		} catch (IOException e) {
			return BsError.IOError(e.getMessage());
		}
		return self;
	}
}
