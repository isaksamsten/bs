package com.bs.lang.proto;

import java.util.concurrent.ExecutionException;
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

	public BsFuture() {
		super(BsConst.Proto, "Future", BsFuture.class);
	}

	@BsRuntimeMessage(name = "isDone", arity = 0)
	public BsObject isDone(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		return Bs.bool(future.isDone());
	}

	@BsRuntimeMessage(name = "isCancelled", arity = 0)
	public BsObject isCancelled(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		return Bs.bool(future.isCancelled());
	}

	@BsRuntimeMessage(name = Bs.METHOD_MISSING, arity = -1)
	public BsObject methodMissing(BsObject self, BsObject... args) {
		Future<BsObject> future = self.value();
		try {
			BsObject obj = future.get();
			return obj.invoke(Bs.asString(args[0]),
					ArrayUtils.subarray(args, 1, args.length));
		} catch (Exception e) {
			return BsError.raise(e.getMessage());
		}
	}

	@BsRuntimeMessage(name = "get", arity = 0)
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
				builder.append("done>"
						+ Bs.asString(future.get().invoke("toString")));
			} catch (Exception e) {
				return BsError.raise(e.getMessage());
			}
		} else {
			builder.append("working>");
		}

		return BsString.clone(builder.toString());
	}

}
