package jerry.test.com.mvp.future.impl;

import cn.nextop.erebor.mid.common.util.concurrent.future.RunnableCompleteFutureEx;

/**
 * 
 * @author Jingqi Xu
 */
public class XRunnableCompleteFutureEx extends XCompleteFutureEx implements RunnableCompleteFutureEx {
	//
	protected final Runnable runnable;

	/**
	 * 
	 */
	public XRunnableCompleteFutureEx(Runnable runnable) {
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
