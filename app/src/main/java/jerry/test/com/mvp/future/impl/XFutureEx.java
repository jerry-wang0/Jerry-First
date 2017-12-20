package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.util.concurrent.future.FutureEx;

/**
 * 
 * @author Jingqi Xu
 * @param <T>
 */
public class XFutureEx<T> extends AbstractFuture<T> implements FutureEx<T> {
	//
	protected volatile T result;
	protected volatile Object cookie;
	protected volatile Throwable throwable;
	protected volatile Listener<T> listener;

	/**
	 * 
	 */
	@Override
	public <V> V getCookie() {
		return (V)cookie;
	}
	
	@Override
	public XFutureEx<T> setCookie(Object cookie) {
		this.cookie = cookie; return this;
	}
	
	public synchronized void setResult(T result) {
		this.result = result; this.sync.releaseShared(1);
		Listener<T> r = this.listener; if(r != null) r.onComplete(this);
	}
	
	public synchronized void setThrowable(Throwable throwable) {
		this.throwable = throwable; this.sync.releaseShared(1);
		Listener<T> r = this.listener; if(r != null) r.onComplete(this);
	}
	
	@Override
	public synchronized Listener<T> setListener(Listener<T> listener) {
		final Listener<T> r = this.listener; this.listener = listener;
		if(this.isDone() && listener != null) listener.onComplete(this); return r;
	}

	/**
	 * 
	 */
	@Override
	public T get() throws InterruptedException, ExecutionException {
		this.sync.acquireSharedInterruptibly(1);
		if(throwable == null) return result; else throw new ExecutionException(throwable);
	}
	
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		timeout = unit.toNanos(timeout);
		if(!this.sync.tryAcquireSharedNanos(1, timeout)) throw new TimeoutException();
		if(throwable == null) return result; else throw new ExecutionException(throwable);
	}

	/**
	 *
	 */
	public static final <T> FutureEx<T> valueOf(T t) { XFutureEx<T> r = new XFutureEx<>(); r.setResult(t); return r; }
}
