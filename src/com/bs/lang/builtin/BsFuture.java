package com.bs.lang.builtin;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.ArrayUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsCode;

public class BsFuture extends BsAbstractProto {

	private static final ExecutorService executor = Executors
			.newFixedThreadPool(10);

	public static BsObject create(FutureTask<BsObject> future) {
		execute(future);
		return BsObject.value(BsConst.Future, future);
	}

	public static void execute(FutureTask<BsObject> obj) {
		executor.execute(obj);
	}

	public static BsObject create(final BsCode code, final BsObject self,
			final BsObject[] args) {
		code.setStack(code.getStack().clone());
		return create(new FutureTask<BsObject>(new Callable<BsObject>() {

			@Override
			public BsObject call() throws Exception {
				return code.invoke(self, args);
			}
		}));
	}

	public BsFuture() {
		super(BsConst.Proto, "Future", BsFuture.class);
	}

	@BsRuntimeMessage(name = "done?", arity = 0)
	public BsObject isDone(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		return Bs.bool(future.isDone());
	}

	@BsRuntimeMessage(name = "cancelled?", arity = 0)
	public BsObject isCancelled(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		return Bs.bool(future.isCancelled());
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		try {
			BsObject obj = future.get();
			if (obj.isError()) {
				return obj;
			}
			return obj.invoke(Bs.asString(args[0]),
					ArrayUtils.subarray(args, 1, args.length));
		} catch (Exception e) {
			return BsError.raise(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "return", arity = 0)
	public BsObject get(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		try {
			return future.get();
		} catch (Exception e) {
			return BsError.raise(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "toString", arity = 0)
	public BsObject toString(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		StringBuilder builder = new StringBuilder();
		builder.append("Future <");
		if (future.isDone()) {
			try {
				builder.append("done"
						+ Bs.asString(future.get().invoke("toString")));
				builder.append(">");
			} catch (Exception e) {
				return BsError.raise(e.getMessage());
			}
		} else {
			builder.append("working>");
		}

		return BsString.clone(builder.toString());
	}

}
