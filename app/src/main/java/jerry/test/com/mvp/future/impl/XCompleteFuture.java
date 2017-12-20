package jerry.test.com.mvp.future.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.util.concurrent.future.CompleteFuture;

/**
 * 
 * @author Jingqi Xu
 */
public class XCompleteFuture extends AbstractFuture<Void> implements CompleteFuture {
	//
	protected volatile Throwable throwable;

	/**
	 * 
	 */
	public XCompleteFuture() {
	}
	
	public XCompleteFuture(boolean completed) {
		if(completed) complete();
	}
	
	public XCompleteFuture(Throwable throwable) {
		complete(throwable);
	}
	
	/**
	 * 
	 */
	public synchronized void complete() {
		this.sync.releaseShared(1);
	}
	
	public synchronized void complete(Throwable throwable) {
		this.throwable = throwable; this.sync.releaseShared(1);
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
