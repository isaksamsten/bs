import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsError;
import com.bs.lang.proto.BsModule;
import com.bs.parser.StatementsParser;
import com.bs.parser.source.BsScanner;
import com.bs.parser.source.BsTokenizer;
import com.bs.parser.source.Scanner;
import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.DefaultTokenFactory;
import com.bs.parser.tree.DefaultNodeFactory;
import com.bs.parser.tree.Node;
import com.bs.util.MessageHandler;
import com.bs.util.PrintStreamMessageListener;

public class bs {

	public static void main(String[] args) throws FileNotFoundException {
		args = new String[] { "Main.bs" };
		MessageHandler handler = new MessageHandler();
		handler.add(new PrintStreamMessageListener(System.out));

		BsObject module = BsModule.create(args[0]);
		Stack stack = BsStack.getDefault();
		stack.push(module);

		Scanner sc = new BsScanner(new FileReader(new File(args[0])));
		Tokenizer tz = new BsTokenizer(sc, new DefaultTokenFactory(), handler,
				'#');
		StatementsParser parser = new StatementsParser(tz,
				new DefaultNodeFactory(), handler);

		Node code = parser.parse();

		if (handler.errors() == 0) {
			BsObject value = Bs.eval(code, BsStack.getDefault());
			if (value.isError()) {
				System.out.println("Traceback (most recent call first):\n  "
						+ value);
				List<BsObject> stackTrace = value.slot(BsError.STACK_TRACE)
						.value();
				for (BsObject str : stackTrace) {
					System.out.println("\t" + Bs.asString(str));
				}
			}
		}
	}
}
