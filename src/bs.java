import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import com.bs.parser.source.Scanner;
import com.bs.parser.source.BsScanner;
import com.bs.parser.source.BsTokenizer;
import com.bs.parser.source.Tokenizer;
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
		Tokenizer tz = new BsTokenizer(sc, new TokenFactory() {

			@Override
			public Token special(String value, int line, int position) {
				System.out.println("SPECIAL " + value + " " + line + ":"
						+ position);
				return new Token(value, value, TokenType.getSpecial(value),
						line, position);
			}

			@Override
			public Token number(String value, int line, int position) {
				System.out.println("NUMBER " + value + " " + line + ":"
						+ position);
				return new Token(value, Integer.parseInt(value),
						TokenType.NUMBER, line, position);
			}

			@Override
			public Token identifier(String value, int line, int position) {
				System.out.println("IDENTIFIER " + value + " " + line + ":"
						+ position);
				return new Token(value, value, TokenType.IDENTIFIER, line,
						position);
			}

			@Override
			public Token guess(String value, int line, int position) {
				System.out.println("GUESS " + value + " " + line + ":"
						+ position);
				return null;
			}

			@Override
			public Token error(int line, int position) {
				return new Token("<error>", null, TokenType.ERROR, line,
						position);
			}

			@Override
			public Token eof() {
				return new Token(null, null, TokenType.EOF, -1, -1);
			}
		}, '#');

		Token t = null;
		while ((t = tz.next()).type() != TokenType.EOF) {

		}

	}
}
