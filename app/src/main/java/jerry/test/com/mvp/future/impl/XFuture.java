package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author Jingqi Xu
 * @param <T>
 */
public final class XFuture<T> extends AbstractFuture<T> {
	//
	private volatile T result;
	private volatile Object cookie;
	private volatile Throwable throwable;

	/**
	 * 
	 */
	public <V> V getCookie() {
		return (V)cookie;
	}
	
	public void setCookie(Object cookie) {
		this.cookie = cookie;
	}

	public void setResult(T result) {
		this.result = result;
		this.sync.releaseShared(1);
	}
	
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
		this.sync.releaseShared(1);
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
	public T get(long timeout, TimeUnit unit)  throws InterruptedException, ExecutionException, TimeoutException {
		timeout = unit.toNanos(timeout);
		if(!this.sync.tryAcquireSharedNanos(1, timeout)) throw new TimeoutException();
		if(throwable == null) return result; else throw new ExecutionException(throwable);
	}
}
