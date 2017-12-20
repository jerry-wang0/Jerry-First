package jerry.test.com.mvp.future.impl;

import cn.nextop.erebor.mid.common.util.concurrent.future.RunnableCompleteFuture;

/**
 * 
 * @author Jingqi Xu
 */
public class XRunnableCompleteFuture extends XCompleteFuture implements RunnableCompleteFuture {
	//
	protected final Runnable runnable;

	/**
	 * 
	 */
	public XRunnableCompleteFuture(Runnable runnable) {
		this.runnable = runnable;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try { this.runnable.run(); complete(); } catch(Throwable e) { complete(e); }
	}
}
