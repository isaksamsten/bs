import static org.junit.Assert.*;

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
	}

	@Test
	public void testInstanceOf() {
		BsObject basic = BsObject.clone(BsConst.Proto);
		BsObject list = BsConst.List.invoke("clone");

		assertEquals(true, basic.instanceOf(BsConst.Proto));
		assertEquals(true, list.instanceOf(BsConst.List));
		assertEquals(true, list.instanceOf(BsConst.Proto));
		assertEquals(false, list.instanceOf(BsConst.True));
	}

	@Test
	public void testNumber() {
		BsObject one = BsNumber.clone(10);
		BsObject two = BsNumber.clone(20);
		assertEquals(30, Bs.getNumber(one.invoke("+", two)));
		assertEquals(-10, Bs.getNumber(one.invoke("-", two)));
		assertEquals(200, Bs.getNumber(one.invoke("*", two)));
		assertEquals(10, Bs.getNumber(one.invoke("%", two)));
		assertEquals(0, Bs.getNumber(one.invoke("/", two)));
	}

	@Test
	public void testString() {
		BsObject str = BsString.clone("Hello");
		assertEquals(5, Bs.getNumber(str.invoke("length")));
		assertEquals("Hello", Bs.getString(str));
	}
}
