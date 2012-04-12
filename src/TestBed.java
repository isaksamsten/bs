import com.bs.lang.Bs;
import com.bs.lang.BsNumber;
import com.bs.lang.BsObject;

public class TestBed {
	public static void main(String[] args) {
		BsObject obj = BsObject.value(Bs.Number, 14);
		BsObject len = BsObject.value(Bs.String, "Isak").invoke("length");

		System.out
				.println(obj.invoke("-", len.invoke("+", BsNumber.clone(10))));
	}
}
