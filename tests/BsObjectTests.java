import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsNumber;
import com.bs.lang.proto.BsString;
import com.bs.lang.proto.BsSymbol;

public class BsObjectTests {

	@Before
	public void setUp() {
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
	public void testJavaIterop() {
		BsObject java = BsConst.Java.invoke("new",
				BsString.clone("java.lang.String"), BsString.clone("Value"));

		BsObject ret = java.invoke("toString");
		System.out.println(ret);

		java = BsConst.Java.invoke("new", BsString.clone("java.io.File"),
				BsString.clone("test.bs"));

		System.out.println(java.invoke("exists").value());

		System.out.println(java.value());
	}

	@Test
	public void testList() {
		BsObject list = Bs.eval("[10, 20, 30, dsadsa].");
		assertEquals(true, list.instanceOf(BsConst.NameError));

		list = Bs.eval("[1,2,3] << [4,5,6] size?;; ++ [10, 20]; size?.");
		assertEquals(6, Bs.asNumber(list));

		BsObject list2 = Bs
				.eval("[1,2,3] <<([4,5,6] size?()) ++([10, 20]) size?().");

		assertEquals(Bs.asNumber(list2), Bs.asNumber(list));
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
		assertEquals(BsConst.True, one.invoke("<", two));
	}

	@Test
	public void testMethods() {
		BsObject obj = BsConst.Proto.invoke("clone");
		BsObject block = Bs.compile("self return 10.");

		BsConst.Proto.invoke("<<=", BsSymbol.get("test2"), block);
		obj.invoke("<<=", BsSymbol.get("test"), block);

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
	public void testSyntax() {
		BsObject obj = Bs.eval("10");
		assertEquals(true, obj.instanceOf(BsConst.SyntaxError));

		obj = Bs.eval("10 + 10");
		assertEquals(true, obj.instanceOf(BsConst.SyntaxError));

		obj = Bs.eval("10 +(10.");
		assertEquals(true, obj.instanceOf(BsConst.SyntaxError));

		obj = Bs.eval("10 := 10.");
		assertEquals(true, obj.instanceOf(BsConst.SyntaxError));

		obj = Bs.eval("10 + 10; * 20.");
		assertEquals(400, Bs.asNumber(obj));

		obj = Bs.eval("10 +(10) *(20).");
		assertEquals(400, Bs.asNumber(obj));

		obj = Bs.eval("10 + 10 * 20.");
		assertEquals(210, Bs.asNumber(obj));
	}

	@Test
	public void testCompare() {
		assertEquals(true,
				Bs.asBoolean(BsConst.Proto.invoke("=", BsConst.Proto)));
	}

	@Test
	public void testBoolean() {
		assertEquals(BsConst.True, Bs.eval("True & True."));
		assertEquals(BsConst.True, Bs.eval("True \\/ True."));
		assertEquals(BsConst.True, Bs.eval("True \\/ False."));
		assertEquals(BsConst.True, Bs.eval("True \\/ False."));
		assertEquals(BsConst.False, Bs.eval("True & False."));
		assertEquals(BsConst.False, Bs.eval("False & False."));
		assertEquals(Bs.eval("True /\\ True."), Bs.eval("True & True."));
		assertEquals(Bs.eval("True and True."), Bs.eval("True & True."));
		assertEquals(Bs.eval("True and True."), Bs.eval("True /\\ True."));
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
		assertEquals(true, obj.isReturning() && obj.isBreak());

		obj = Bs.eval("Proto clone()");
		BsObject method = Bs.compile("Proto return 10.");
		obj.invoke("<<=", BsSymbol.get("getTen"), method);

		assertEquals(10, Bs.asNumber(obj.invoke("getTen")));

		obj = Bs.compile("10. return(30). 20.");
		assertEquals(true, obj.instanceOf(BsConst.Block));
		assertEquals(30, Bs.asNumber(obj.invoke("call")));
		assertEquals(true, Bs.asBoolean(obj.invoke("hasReturned")));

		obj = Bs.eval("10. return(30). 20.");
		assertEquals(30, Bs.asNumber(obj));
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
