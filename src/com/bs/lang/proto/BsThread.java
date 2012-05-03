package com.bs.lang.proto;

import org.apache.commons.lang3.ArrayUtils;

import com.bs.lang.Bs;
import com.bs.lang.BsAbstractProto;
import com.bs.lang.BsConst;
import com.bs.lang.BsObject;
import com.bs.lang.annot.BsRuntimeMessage;
import com.bs.lang.message.BsCode;

public class BsThread extends BsAbstractProto {

	public static BsObject create(Thread thread) {
		return BsObject.value(BsConst.Thread, thread);
	}

	public static BsObject create(final BsCode code, final BsObject self,
			final BsObject... args) {
		code.cloneStack();
		return create(new Thread(new Runnable() {

			@Override
			public void run() {
				BsObject ret = code.invoke(self, args);
				if (ret.isError()) {
					Bs.breakError(ret);
				}

			}
		}, "BsThread"));
	}

	public BsThread() {
		super(BsConst.Proto, "Thread", BsThread.class);
	}

	@BsRuntimeMessage(name = "join", arity = 0)
	public BsObject join(BsObject self, BsObject... args) {
		Thread thread = self.value();
		try {
			thread.join();
		} catch (InterruptedException e) {
			return BsError.raise(e.getMessage());
		}
		return self;
	}

	@BsRuntimeMessage(name = "start", arity = 0)
	public BsObject start(BsObject self, BsObject... args) {
		Thread thread = self.value();
		thread.start();
		return self;
	}

	@BsRuntimeMessage(name = "sleep", arity = 0)
	public BsObject sleep(BsObject self, BsObject... args) {
		try {
			Thread.sleep(Bs.asNumber(args[0]).intValue());
		} catch (InterruptedException e) {
			return BsError.raise(e.getMessage());
		}
		return self;
	}

	@BsRuntimeMessage(name = "clone", arity = BsRuntimeMessage.VARIABLE)
	public BsObject clone(BsObject self, final BsObject... args) {
		if (!args[0].instanceOf(BsConst.Block)) {
			return BsError.typeError("clone", args[0], BsConst.Block);
		}

		final BsCode code = args[0].value();
		BsObject obj = create(code, args[0],
				ArrayUtils.subarray(args, 1, args.length));
		return obj;
	}
}
