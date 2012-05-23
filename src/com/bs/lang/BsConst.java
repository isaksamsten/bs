package com.bs.lang;

import com.bs.lang.builtin.BsBlock;
import com.bs.lang.builtin.BsBool;
import com.bs.lang.builtin.BsChar;
import com.bs.lang.builtin.BsComparable;
import com.bs.lang.builtin.BsEnumerable;
import com.bs.lang.builtin.BsError;
import com.bs.lang.builtin.BsFalse;
import com.bs.lang.builtin.BsFuture;
import com.bs.lang.builtin.BsList;
import com.bs.lang.builtin.BsModule;
import com.bs.lang.builtin.BsNil;
import com.bs.lang.builtin.BsNumber;
import com.bs.lang.builtin.BsProto;
import com.bs.lang.builtin.BsRange;
import com.bs.lang.builtin.BsString;
import com.bs.lang.builtin.BsSymbol;
import com.bs.lang.builtin.BsSystem;
import com.bs.lang.builtin.BsThread;
import com.bs.lang.builtin.BsTrue;
import com.bs.lang.builtin.io.BsFile;
import com.bs.lang.builtin.io.BsIO;
import com.bs.lang.builtin.io.BsReader;
import com.bs.lang.builtin.io.BsWriter;
import com.bs.lang.builtin.java.BsJava;
import com.bs.lang.builtin.java.BsJavaClass;
import com.bs.lang.builtin.java.BsJavaInstance;

public final class BsConst {

	public static final BsObject Proto = new BsProto();
	public static final BsObject Module = new BsModule();
	public static final BsObject Comparable = new BsComparable();

	public static final BsObject Nil = new BsNil();
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
	public static final BsObject IOError = BsError.clone("IOError");
	public static final BsObject JavaError = BsError.clone("JavaError");
	public static final BsObject CloneError = BsError.clone("CloneError");
	public static final BsObject SubTypeError = BsError.clone("SubTypeError");

	public static final BsObject Symbol = new BsSymbol();
	public static final BsObject Char = new BsChar();

	public static final BsObject IO = new BsIO();
	public static final BsObject Reader = new BsReader();
	public static final BsObject Writer = new BsWriter();
	public static final BsObject File = new BsFile();

	public static final BsObject System = new BsSystem();
	public static final BsObject Java = new BsJava();
	public static final BsObject JavaInstance = new BsJavaInstance();
	public static final BsObject JavaClass = new BsJavaClass();
	public static final BsObject Future = new BsFuture();
	public static final BsObject Thread = new BsThread();
}
