import java.io.StringReader;

import com.bs.interpreter.BsInterpreter;
import com.bs.interpreter.Interpreter;
import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsObject;
import com.bs.parser.StatementsParser;
import com.bs.parser.source.BsScanner;
import com.bs.parser.source.BsTokenizer;
import com.bs.parser.source.Scanner;
import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.DefaultTokenFactory;
import com.bs.parser.tree.DefaultNodeFactory;
import com.bs.parser.tree.Node;
import com.bs.util.Message;
import com.bs.util.MessageHandler;
import com.bs.util.MessageListener;

public class bs {

	public static void main(String[] args) {
		MessageHandler handler = new MessageHandler();
		handler.add(new MessageListener() {

			@Override
			public void fatal(Throwable t) {
				System.out.println(t.getMessage());
				System.exit(-1);
			}

			@Override
			public void fatal(Message message) {
				System.out.format(message + "\n", message.args());
				System.exit(-1);
			}

			@Override
			public void error(Message message) {
				System.out.format("   File \"%s\", line %d\n", "<stdin>",
						message.line());
				System.out.println("     " + message.currentLine());
				for (int n = 0; n < message.position() + 4; n++) {
					System.out.print(" ");
				}
				System.out.println("^");
				System.out.format(
						message.type().toString() + ": " + message.message()
								+ "\n\n", message.args());
			}
		});

		BsObject module = Bs.builtin();

		Stack stack = BsStack.getDefault();
		stack.push(module);

		Scanner sc = new BsScanner(
				new StringReader(
						"x := 1--10." +
						"(1--10) each { | y |" +
						"  System puts y." +
						"}." +
						"x := True." +
						"{x.} whileTrue {" +
						"  System puts \"While true\"." +
						"  x := False." +
						"}." +
						"left := Proto try { [10, 10 + 10] each {| x | " +
						"     System puts xy * 10." +
						"  }." +
						"}. " +
						"left catch \"NameError\", { | e |" +
						"  System puts \"Caught NameError: \" + e getMessage()." +
						"}."));
		Tokenizer tz = new BsTokenizer(sc, new DefaultTokenFactory(), handler,
				'#');
		StatementsParser parser = new StatementsParser(tz,
				new DefaultNodeFactory(), handler);

		Node n = parser.parse();
		System.out.println(n.toTree());

		Interpreter interpreter = new BsInterpreter(stack);
		BsObject value = (BsObject) interpreter.visit(n);

		System.out.println(value.isError() + " ->" + value);

		System.out.println(module.slot("right"));
		System.out.println(module.slot("left"));
	}
}
