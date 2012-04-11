import java.io.StringReader;

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
		MessageHandler.add(new MessageListener() {

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
				for (int n = 0; n < message.position() + 5; n++) {
					System.out.print(" ");
				}
				System.out.println("^");
				System.out.format(
						message.type().toString() + ": " + message.message()
								+ "\n\n", message.args());
			}
		});

		Scanner sc = new BsScanner(new StringReader("lol := 10 + [isak.]."));
		Tokenizer tz = new BsTokenizer(sc, new DefaultTokenFactory(), '#');
		StatementsParser parser = new StatementsParser(tz,
				new DefaultNodeFactory());

		Node n = parser.parse();
		System.out.println(n.toTree());
	}
}
