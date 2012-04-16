import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.bs.interpreter.stack.BsStack;
import com.bs.lang.Bs;
import com.bs.lang.BsCodeData;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsNumber;
import com.bs.lang.proto.BsString;

public class BsObjectTests {

	@Before
	public void setUp() {
		BsStack.getDefault().push(Bs.builtin());
	}

	@Test
	public void testIsTrue() {
		assertEquals(true, Bs.asBoolean(BsConst.True));
		assertEquals(false, Bs.asBoolean(null));
		assertEquals(false, Bs.asBoolean(BsConst.False));
		assertEquals(false, Bs.asBoolean(BsConst.Proto));
		assertEquals(true, Bs.asBoolean(BsConst.False.invoke("negate")));
		assertEquals(false, Bs.asBoolean(BsConst.True.invoke("negate")));
	}

	@Test
	public void testInstanceOf() {
		BsObject basic = BsObject.clone(BsConst.Proto);
		BsObject list = BsConst.List.invoke("clone");

		assertEquals(true, BsConst.True.instanceOf(BsConst.Bool));
		assertEquals(true, basic.instanceOf(BsConst.Proto));
		assertEquals(true, list.instanceOf(BsConst.List));
		assertEquals(true, list.instanceOf(BsConst.Proto));
		assertEquals(false, list.instanceOf(BsConst.True));
	}

	@Test
	public void testNumber() {
		BsObject one = BsNumber.clone(10);
		BsObject two = BsNumber.clone(20);
		assertEquals(30, Bs.asNumber(one.invoke("+", two)));
		assertEquals(-10, Bs.asNumber(one.invoke("-", two)));
		assertEquals(200, Bs.asNumber(one.invoke("*", two)));
		assertEquals(10, Bs.asNumber(one.invoke("%", two)));
		assertEquals(0, Bs.asNumber(one.invoke("/", two)));
	}

	@Test
	public void testMethods() {
		BsObject obj = BsConst.Proto.invoke("clone");
		BsObject block = Bs.compile("self return 10.");

		BsCodeData data = block.value();
		data.arguments.add("self"); // a method always take one arg

		BsConst.Proto.invoke("<-", BsString.clone("test2"), block);
		obj.invoke("<-", BsString.clone("test"), block);

		obj = obj.invoke("test");
		assertEquals(10, Bs.asNumber(obj));

		obj = obj.invoke("test2");
		assertEquals(10, Bs.asNumber(obj));

		obj = BsConst.Proto.invoke("test2");
		assertEquals(10, Bs.asNumber(obj));

		obj = BsConst.Proto.invoke("test");
		assertEquals(true, obj.instanceOf(BsConst.NameError));
	}

	@Test
	public void testCompare() {
		assertEquals(true,
				Bs.asBoolean(BsConst.Proto.invoke("=", BsConst.Proto)));
	}

	@Test
	public void testString() {
		BsObject str = BsString.clone("Hello");
		assertEquals(5, Bs.asNumber(str.invoke("length")));
		assertEquals("Hello world",
				Bs.asString(str.invoke("+", BsString.clone(" world"))));
		assertEquals("Hello", Bs.asString(str));
	}

	@Test
	public void testReturn() {
		BsObject obj = BsConst.Proto.invoke("return",
				BsString.clone("Hello world"));
		assertEquals(true, obj.isReturn() && obj.isBreak());

		obj = Bs.eval("10. Proto return 30. 20.");
		assertEquals(30, Bs.asNumber(obj));
		assertEquals(true, obj.isReturn() && obj.isBreak());
	}

	@Test
	public void testEval() {
		BsObject obj = Bs.eval("10 + 10 + 10.");
		assertEquals(30, Bs.asNumber(obj));

		obj = Bs.eval("10 + 10");
		assertEquals(true, obj.instanceOf(BsConst.SyntaxError));

		BsObject block = BsConst.Proto.invoke("compile",
				BsString.clone("10+10+10."));

		obj = block.invoke("call");
		assertEquals(30, Bs.asNumber(obj));

		block = Bs.eval("Proto compile(\"10+10+10.\").");
		obj = block.invoke("call");
		assertEquals(30, Bs.asNumber(obj));

		obj = Bs.eval("Proto eval \"10+10+10.\".");
		assertEquals(30, Bs.asNumber(obj));
	}
}
