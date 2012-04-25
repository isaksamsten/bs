package com.bs.lang.proto.io;

import java.io.OutputStream;
import java.io.PrintStream;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;

public class BsWriter extends BsObject {

	public static BsObject create(OutputStream writer) {
		PrintStream w = new PrintStream(writer);
		return BsObject.value(BsConst.Writer, w);
	}

	public static BsObject create(PrintStream writer) {
		return BsObject.value(BsConst.Writer, writer);
	}

	public BsWriter() {
		super(BsConst.IO, "Writer", BsWriter.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "print", arity = 1)
	public BsObject print(BsObject self, BsObject... args) {
		PrintStream writer = self.value();
		writer.print(Bs.asString(args[0].invoke("toString")));
		return self;
	}

	@BsRuntimeMessage(name = "println", arity = 1)
	public BsObject println(BsObject self, BsObject... args) {
		PrintStream writer = self.value();
		writer.println(Bs.asString(args[0].invoke("toString")));
		return self;
	}

	@BsRuntimeMessage(name = "format", arity = -1)
	public BsObject format(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("format", args[0], BsConst.String);
		}

		PrintStream writer = self.value();

		Object[] str = new Object[args.length];
		for (int n = 1; n < str.length; n++) {
			str[n] = Bs.asString(args[n].invoke("toString"));
		}

		writer.format(Bs.asString(args[0]), str);
		return self;
	}
}
