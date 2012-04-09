import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import com.bs.parser.BsParser;
import com.bs.parser.Parser;
import com.bs.parser.source.Scanner;
import com.bs.parser.source.BsScanner;
import com.bs.parser.source.BsTokenizer;
import com.bs.parser.source.Tokenizer;
import com.bs.parser.token.DefaultTokenFactory;
import com.bs.parser.token.Token;
import com.bs.parser.token.TokenFactory;
import com.bs.parser.token.TokenType;
import com.bs.util.Message;
import com.bs.util.MessageHandler;

public class bs {

	public static void main(String[] args) {
		MessageHandler.add(new Message() {

			@Override
			public void fatal(Throwable t) {
				System.out.println(t.getMessage());
				System.exit(-1);
			}

			@Override
			public void fatal(String message, Scanner scanner, Object... args) {
				System.out.format(message + "\n", args);
				System.exit(-1);
			}

			@Override
			public void error(String message, Scanner scanner, Object... args) {
				System.out.println(scanner.currentLine());
				System.out.format(message + " at line " + scanner.line() + ":"
						+ scanner.position() + "\n", args);
			}
		});

		Scanner sc = new BsScanner(new StringReader("hello ^= 1 2 *"));
		Tokenizer tz = new BsTokenizer(sc, new DefaultTokenFactory(), '#');
		Parser parser = new BsParser(tz, null);

		//
		// Token t = null;
		// while ((t = tz.next()).type() != TokenType.EOF) {
		//
		// }

	}
}
