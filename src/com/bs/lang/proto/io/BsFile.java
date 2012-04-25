package com.bs.lang.proto.io;

import java.io.File;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsProto;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.proto.BsError;

@BsProto(name = "File")
public class BsFile extends BsObject {

	public BsFile() {
		super(BsConst.Proto, "File", BsFile.class);
		initRuntimeMethods();
	}

	@BsRuntimeMessage(name = "exist?", arity = 0, aliases = "?")
	public BsObject exist(BsObject self, BsObject... args) {
		File file = self.value();
		return Bs.bool(file.exists());
	}

	@BsRuntimeMessage(name = "init", arity = 1)
	public BsObject init(BsObject self, BsObject... args) {
		if (!args[0].instanceOf(BsConst.String)) {
			return BsError.typeError("init", args[0], BsConst.String);
		}

		String filename = Bs.asString(args[0]);
		File file = new File(filename);
		self.value(file);
		return self;
	}

}
