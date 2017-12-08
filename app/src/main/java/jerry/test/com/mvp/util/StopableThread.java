package jerry.test.com.mvp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import cn.nextop.erebor.mid.common.util.Concurrents;

/**
 * 
 * @author Jingqi Xu
 */
public class StopableThread extends Thread {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(StopableThread.class);
	
	//
	private final Runnable runnable;
	private volatile boolean stopped = false;
	
	/**
	 * 
	 */
	public StopableThread(Runnable runnable) {
		this.runnable = runnable;
	}
	
	public StopableThread(String name, Runnable runnable) {
		this.runnable = runnable; this.setName(name);
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		while(!this.stopped) {
			try {
				this.runnable.run();
			} catch(Throwable t) {
				LOGGER.error("unhandled exception in thread: " + getId() + ":" + getName(), t);
				break;
			}
		}
	}
	
	/**
	 * 
	 */
	public long stop(long timeout, TimeUnit unit) {
		this.stopped = true; this.interrupt();
		return Concurrents.timedJoinQuietly(this, timeout, unit);
	}
	
	/**
	 * 
	 */
	public static long stop(StopableThread thread, long timeout, TimeUnit unit) {
		return thread == null ? timeout : thread.stop(timeout, unit);
	}
	
	public static final StopableThread start(final String name, Runnable runnable) {
		return start(name, runnable, true, false);
	}
	
	public static final StopableThread start(String name, Runnable runnable, boolean start) {
		return start(name, runnable, start, false);
	}
	
	public static final StopableThread start(String name, Runnable runnable, boolean start, boolean daemon) {
		final StopableThread r = new StopableThread(name, runnable); r.setDaemon(daemon); if(start) r.start(); return r;
	}
}
