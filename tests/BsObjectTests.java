import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsNumber;
import com.bs.lang.BsObject;
import com.bs.lang.BsString;

public class BsObjectTests {
	
	

	@Test
	public void testIsTrue() {
		assertEquals(true, Bs.isTrue(BsConst.True));
		assertEquals(false, Bs.isTrue(null));
		assertEquals(false, Bs.isTrue(BsConst.False));
		assertEquals(false, Bs.isTrue(BsConst.Proto));
		assertEquals(true, Bs.isTrue(BsConst.False.invoke("negate")));
		assertEquals(false, Bs.isTrue(BsConst.True.invoke("negate")));
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
	public void testString() {
		BsObject str = BsString.clone("Hello");
		assertEquals(5, Bs.asNumber(str.invoke("length")));
		assertEquals("Hello", Bs.asString(str));
	}

	@Test
	public void testEval() {
		BsObject obj = Bs.eval("10 + 10 + 10.");
		assertEquals(30, Bs.asNumber(obj));

		BsObject block = BsConst.Proto.invoke("compile",
				BsString.clone("10+10+10."));

		obj = block.invoke("call");
		assertEquals(30, Bs.asNumber(obj));

		block = Bs.eval("Proto compile(\"10+10+10.\")");
		obj = block.invoke("call");
		assertEquals(30, Bs.asNumber(obj));
	}
}
