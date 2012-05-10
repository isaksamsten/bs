import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.bs.interpreter.stack.BsStack;
import com.bs.interpreter.stack.Stack;
import com.bs.lang.Bs;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.proto.BsModule;
import com.bs.lang.proto.BsString;
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

	private static final String EVAL = "eval";

	private static final String HELP = "help";

	private static final String LOAD_PATH = "loadPath";

	private static Option help = new Option("h", HELP, false,
			"Print this message");

	// @formatter:off

	@SuppressWarnings("static-access")
	private static Option loadPath = OptionBuilder.withArgName(LOAD_PATH)
			.isRequired(false).hasArg()
			.withDescription("Append <loadPath> to the load path")
			.withLongOpt(LOAD_PATH).create('l');

	@SuppressWarnings("static-access")
	private static Option eval = OptionBuilder.withArgName(EVAL)
			.isRequired(false).hasArg()
			.withDescription("Evaluate code and exit").withLongOpt(EVAL)
			.create('e');

	// @formatter:on

	public static void main(String[] args) throws FileNotFoundException {
		Options options = new Options();
		options.addOption(help);
		options.addOption(loadPath);
		options.addOption(eval);

		try {
			Bs.init();

			CommandLineParser argParser = new PosixParser();
			CommandLine line = argParser.parse(options, args);

			if (line.hasOption(HELP)) {
				help(options);
			}

			if (line.hasOption(LOAD_PATH)) {
				loadPath(line.getOptionValue(LOAD_PATH));
			}

			if (line.hasOption(EVAL)) {
				eval(line.getOptionValue(EVAL));
			}

			List<?> rest = line.getArgList();

			if (rest.size() > 0) {
				eval(rest);
			} else {
				repl();
			}

		} catch (ParseException e) {
			help(options);
		}

	}

	/**
	 * 
	 */
	protected static void repl() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		BsObject module = BsModule.create("<stdin>");
		Stack stack = BsStack.getDefault();
		stack.push(module);
		while (true) {
			String code = read(scanner, ">> ");
			BsObject obj = Bs.evalRepl(code, stack);
			if (obj == null) {
				continue;
			}
			if (obj.isError()) {
				Bs.breakError(obj, false);
			} else {
				System.out.println(obj);
			}
		}
	}

	/**
	 * @param rest
	 * @throws FileNotFoundException
	 */
	protected static void eval(List<?> rest) throws FileNotFoundException {
		MessageHandler handler = new MessageHandler();
		handler.add(new PrintStreamMessageListener(System.out));

		String file = (String) rest.get(0);
		BsObject module = BsModule.create(file);
		Stack stack = BsStack.getDefault();
		stack.push(module);

		Scanner sc = new BsScanner(new FileReader(new File(file)));
		Tokenizer tz = new BsTokenizer(sc, new DefaultTokenFactory(), handler,
				'#');
		StatementsParser parser = new StatementsParser(tz,
				new DefaultNodeFactory(), handler);

		Node code = parser.parse();

		if (handler.errors() == 0) {
			BsObject value = Bs.eval(code, BsStack.getDefault());
			if (value == null) {
				System.out.println("Wtf!?");
			}
			if (value.isError()) {
				Bs.breakError(value);
			}
		}
	}

	/**
	 * @param code
	 */
	protected static void eval(String code) {
		BsObject obj = Bs.eval(code);
		if (obj.isError()) {
			Bs.breakError(obj);
		}

		System.exit(1);
	}

	/**
	 * @param path
	 */
	protected static void loadPath(String path) {
		List<BsObject> loadPath = BsConst.Module.getSlot(BsModule.LOAD_PATH)
				.value();

		loadPath.add(BsString.clone(path));
	}

	/**
	 * @param options
	 */
	protected static void help(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("bs", options);
		System.exit(0);
	}

	private static String read(java.util.Scanner scanner, String promt) {
		System.out.print(promt);
		return scanner.nextLine();
	}
}
