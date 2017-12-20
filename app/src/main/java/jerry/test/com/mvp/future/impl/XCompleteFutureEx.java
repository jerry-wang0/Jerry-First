package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.util.concurrent.future.CompleteFutureEx;

/**
 * 
 * @author Jingqi Xu
 */
@SuppressWarnings("unchecked")
public class XCompleteFutureEx extends AbstractFuture<Void> implements CompleteFutureEx {
	//
	protected volatile Object cookie;
	protected volatile Throwable throwable;
	protected volatile Listener<Void> listener;

	/**
	 * 
	 */
	public XCompleteFutureEx() {
	}
	
	public XCompleteFutureEx(boolean completed) {
		if(completed) complete();
	}
	
	public XCompleteFutureEx(Throwable throwable) {
		complete(throwable);
	}
	
	/**
	 * 
	 */
	@Override
	public <V> V getCookie() {
		return (V)cookie;
	}
	
	@Override
	public CompleteFutureEx setCookie(Object cookie) {
		this.cookie = cookie; return this;
	}
	
	/**
	 * 
	 */
	public synchronized void complete() {
		this.sync.releaseShared(1);
		Listener<Void> r = this.listener; if(r != null) r.onComplete(this);
	}
	
	public synchronized void complete(Throwable throwable) {
		this.throwable = throwable; this.sync.releaseShared(1);
		Listener<Void> r = this.listener; if(r != null) r.onComplete(this);
	}
	
	@Override
	public synchronized Listener<Void> setListener(Listener<Void> listener) {
		Listener<Void> r = this.listener; this.listener = listener;
		if(this.isDone() && listener != null) listener.onComplete(this); return r;
	}
	
	/**
	 * 
	 */
	@Override
	public Void get() throws InterruptedException, ExecutionException {
		this.sync.acquireSharedInterruptibly(1);
		if(throwable == null) return null; else throw new ExecutionException(throwable);
	}
	
	@Override
	public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		timeout = unit.toNanos(timeout);
		if(!this.sync.tryAcquireSharedNanos(1, timeout)) throw new TimeoutException();
		if(throwable == null) return null; else throw new ExecutionException(throwable);
	}
}
