package com.bs.lang.proto.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		BsObject toString = args[0].invoke("toString");
		if (toString.isError()) {
			return toString;
		}
		writer.print(Bs.asString(toString));
		return self;
	}

	@BsRuntimeMessage(name = "println", arity = 1, aliases = { "<<" })
	public BsObject println(BsObject self, BsObject... args) {
		PrintStream writer = self.value();
		BsObject toString = args[0].invoke("toString");
		if (toString.isError()) {
			return toString;
		}
		writer.println(Bs.asString(toString));
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
			BsObject toString = args[0].invoke("toString");
			if (toString.isError()) {
				return toString;
			}
			str[n] = Bs.asString(toString);
		}

		writer.format(Bs.asString(args[0]), str);
		return self;
	}

	@BsRuntimeMessage(name = "init", arity = 1)
	public BsObject init(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.File)) {
			return BsError.typeError("init", args[0], BsConst.File);
		}

		File file = args[0].value();
		try {
			self.value(new PrintStream(new BufferedOutputStream(
					new FileOutputStream(file))));
		} catch (FileNotFoundException e) {
			return BsError.IOError(e.getMessage());
		}

		return self;
	}
}
