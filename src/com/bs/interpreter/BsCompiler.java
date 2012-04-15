package com.bs.interpreter;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import com.bs.lang.BsBlock;
import com.bs.lang.BsConst;
import com.bs.lang.BsError;
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

public class BsCompiler {

	private class BsCompilerMessage implements MessageListener {

		private BsObject error = null;

		@Override
		public void error(Message message) {
			StringBuilder builder = new StringBuilder();

			builder.append(String.format("   File \"%s\", line %d\n",
					"<stdin>", message.line()));
			builder.append("     \n" + message.currentLine());
			for (int n = 0; n < message.position() + 5; n++) {
				builder.append(" ");
			}
			builder.append("^\n");
			builder.append(String.format(message.type().toString() + ": "
					+ message.message() + "\n\n", message.args()));
			error = BsError.syntaxError(builder.toString());
		}

		@Override
		public void fatal(Message message) {

		}

		@Override
		public void fatal(Throwable t) {
			// TODO Auto-generated method stub

		}

		public boolean hasError() {
			return error != null;
		}

	}

	private MessageHandler handler = new MessageHandler();
	private BsCompilerMessage message = new BsCompilerMessage();

	public BsCompiler() {
		handler.add(message);
	}

	public BsObject compile(Reader reader) {
		Node node = parse(reader);
		if (message.hasError()) {
			return message.error;
		}

		BsObject code = BsBlock.create(new ArrayList<String>(), node);
		return code;
	}

	public BsObject compile(String str) {
		return compile(new StringReader(str));
	}

	public BsObject eval(String str) {
		BsObject compile = compile(str);
		if (!compile.instanceOf(BsConst.Error)) {
			return compile.invoke("call");
		}

		return message.error;
	}

	/**
	 * @param reader
	 * @return
	 */
	protected Node parse(Reader reader) {
		Scanner scanner = new BsScanner(reader);

		Tokenizer tokenizer = new BsTokenizer(scanner,
				new DefaultTokenFactory(), handler, '#');
		StatementsParser parser = new StatementsParser(tokenizer,
				new DefaultNodeFactory(), handler);
		Node node = parser.parse();
		return node;
	}

}
