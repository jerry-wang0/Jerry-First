package jerry.test.com.mvp.future.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.nextop.erebor.mid.common.util.Strings;

/**
 * 
 * @author Jingqi Xu
 */
public class XRunnableFuture<V> implements RunnableFuture<V> {
	//
	protected final Object task;
	protected final FutureTask<V> future;
	protected volatile long timestamp = System.currentTimeMillis();

	/**
	 * 
	 */
	public XRunnableFuture(Callable<V> callable) {
		this.task = callable;
		this.future = new FutureTask<V>(callable);
	}

	public XRunnableFuture(Runnable runnable, V result) {
		this.task = runnable;
		this.future = new FutureTask<V>(runnable, result);
	}

	/**
	 * 
	 */
	public Object getTask() {
		return task;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return Strings.build(this)
		.append("task", task)
		.append("timestamp", timestamp).toString();
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		future.run();
	}
	
	@Override
	public boolean isDone() {
		return future.isDone();
	}
	
	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean cancel(boolean interrupt) {
		return future.cancel(interrupt);
	}
	
	@Override
	public V get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return future.get(timeout, unit);
	}
}
