package jerry.test.com.mvp.future.impl;

import java.util.concurrent.Future;

import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.common.util.concurrent.XSync;

/**
 * 
 * @author Jingqi Xu
 * @param <T>
 */
public abstract class AbstractFuture<T> implements Future<T> {
	//
	protected volatile boolean cancelled;
	protected final XSync sync = new XSync(1);
	
	/**
	 *
	 */
	@Override
	public boolean isDone() {
		return sync.getCount() != 1;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	@Override
	public boolean cancel(boolean interrupt) {
		this.cancelled = true; return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return Strings.build(this).append("cancelled", cancelled).toString();
	}
}
