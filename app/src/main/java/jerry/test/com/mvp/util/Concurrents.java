package jerry.test.com.mvp.util;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Jingqi Xu
 */
public final class Concurrents {

	/**
	 * 
	 */
    public static long sub(long l1, long l2) {
		long r = (l1 < 0 ? 0 : l1) - (l2 < 0 ? 0 : l2);
		return r < 0 ? 0 : r;
	}
	
	/**
	 * 
	 */
	public static void delayQuietly(long timeout) {
		delayQuietly(timeout, TimeUnit.MILLISECONDS);
	}
	
	public static long delay(long timeout) throws InterruptedException {
		return delay(timeout, TimeUnit.MILLISECONDS);
	}

	public static void delayQuietly(final long timeout, final TimeUnit unit) {
		try {
			delay(timeout, unit);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static long delay(long timeout, TimeUnit unit) throws InterruptedException {
		if(timeout <= 0) return 0;
		final long now = System.nanoTime();
		final Object object = new Object();
		synchronized(object) { unit.timedWait(object, timeout); }
		return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
	}
	
	/**
	 * 
	 */
	public static long timedWait(Object obj, long timeout) throws InterruptedException {
		return timedWait(obj, timeout, TimeUnit.MILLISECONDS);
	}
	
	public static long timedWait(Object obj, long timeout, TimeUnit unit) throws InterruptedException {
		if(timeout <= 0) return 0;
		final long now = System.nanoTime();
		synchronized(obj) { unit.timedWait(obj, timeout); }
		return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
	}
	
	/**
	 * 
	 */
	public static long timedJoinQuietly(Thread thread, long timeout, TimeUnit unit) {
		final long now = System.nanoTime();
		try {
			return timedJoin(thread, timeout, unit);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
		}
	}

	public static long timedJoin(Thread thread, long timeout) throws InterruptedException {
		return timedJoin(thread, timeout, TimeUnit.MILLISECONDS);
	}
	
	public static long timedJoin(Thread thread, long timeout, TimeUnit unit) throws InterruptedException {
		if(timeout <= 0) return 0;
		if(thread == null) return timeout;
		final long now = System.nanoTime(); unit.timedJoin(thread, timeout);
		return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
	}
	
	/**
	 * 
	 */
	public static long terminate(ExecutorService exec, long timeout, TimeUnit unit)
	throws InterruptedException {
		if(timeout <= 0) return 0;
		if(exec == null) return timeout;
		final long now = System.nanoTime();
		if(!exec.isShutdown()) exec.shutdown(); exec.awaitTermination(timeout, unit);
		return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
	}
	
	public static long terminateQuietly(ExecutorService exec, long timeout, TimeUnit unit) {
		final long now = System.nanoTime();
		try {
			return terminate(exec, timeout, unit);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
			return sub(timeout, unit.convert(System.nanoTime() - now, TimeUnit.NANOSECONDS));
		}
	}
	
	public static long terminateQuietly(ExecutorService[] execs, long timeout, TimeUnit unit) {
		if(execs == null || execs.length == 0) return timeout;
		for(ExecutorService exec : execs) timeout = terminateQuietly(exec, timeout, unit); return timeout;
	}
	
	public static long terminateQuietly(Collection<? extends ExecutorService> execs, long timeout, TimeUnit unit) {
		if(execs == null || execs.isEmpty()) return timeout;
		for(ExecutorService exec : execs) timeout = terminateQuietly(exec, timeout, unit); return timeout;
	}
}
