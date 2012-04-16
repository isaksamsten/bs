package com.bs.lang;

import com.bs.lang.proto.BsBlock;
import com.bs.lang.proto.BsBool;
import com.bs.lang.proto.BsEnumerable;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsFalse;
import com.bs.lang.proto.BsList;
import com.bs.lang.proto.BsModule;
import com.bs.lang.proto.BsNumber;
import com.bs.lang.proto.BsProto;
import com.bs.lang.proto.BsRange;
import com.bs.lang.proto.BsString;
import com.bs.lang.proto.BsSystem;
import com.bs.lang.proto.BsTrue;

public final class BsConst {

	public static final BsObject Proto = new BsProto();
	public static final BsObject Module = new BsModule();

	public static final BsObject System = new BsSystem();

	public static final BsObject Bool = new BsBool();
	public static final BsObject True = new BsTrue();
	public static final BsObject False = new BsFalse();
	public static final BsObject String = new BsString();
	public static final BsObject Number = new BsNumber();
	public static final BsObject Block = new BsBlock();

	public static final BsObject Enumerable = new BsEnumerable();
	public static final BsObject List = new BsList();
	public static final BsObject Range = new BsRange();

	public static final BsObject Error = new BsError();
	public static final BsObject TypeError = BsError.clone("TypeError");
	public static final BsObject NameError = BsError.clone("NameError");
	public static final BsObject SyntaxError = BsError.clone("SyntaxError");

}
