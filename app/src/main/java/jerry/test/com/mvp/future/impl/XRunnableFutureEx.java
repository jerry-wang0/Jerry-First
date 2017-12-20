package jerry.test.com.mvp.future.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.concurrent.future.RunnableFutureEx;
import cn.nextop.erebor.mid.common.util.concurrent.future.RunnableFutureFactory;

/**
 * 
 * @author Jingqi Xu
 */
public class XRunnableFutureEx<T> extends XRunnableFuture<T> implements RunnableFutureEx<T> {
	//
	protected boolean done = false;
	protected volatile Object cookie;
	protected volatile Listener<T> listener;

	/**
	 *
	 */
	public XRunnableFutureEx(Callable<T> callable) {
		super(callable);
	}

	public XRunnableFutureEx(Runnable runnable, T result) {
		super(runnable, result);
	}

	/**
	 *
	 */
	@Override
	public <V> V getCookie() {
		return Objects.cast(cookie);
	}

	@Override
	public XRunnableFutureEx<T> setCookie(Object cookie) {
		this.cookie = cookie; return this;
	}

	/**
	 *
	 */
	@Override
	public void run() {
		try { future.run(); } finally { this.complete(); }
	}

	/**
	 *
	 */
	protected synchronized void complete() {
		this.done = true; if(listener != null) listener.onComplete(this);
	}

	@Override
	public synchronized Listener<T> setListener(Listener<T> listener) {
		final Listener<T> r = this.listener; this.listener = listener;
		if(done && listener != null) listener.onComplete(this); return r;
	}
	
	/**
	 * 
	 */
	public static class Factory implements RunnableFutureFactory {

		@Override
		public <T> RunnableFuture<T> create(Callable<T> callable) {
			return new XRunnableFutureEx<>(callable);
		}

		@Override
		public <T> RunnableFuture<T> create(Runnable runnable, T result) {
			return new XRunnableFutureEx<>(runnable, result);
		}
	}
}
