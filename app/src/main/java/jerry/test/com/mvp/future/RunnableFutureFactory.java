package jerry.test.com.mvp.future;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * 
 * @author Jingqi Xu
 */
public interface RunnableFutureFactory {
	
	<T> RunnableFuture<T> create(Callable<T> callable);
	
	<T> RunnableFuture<T> create(Runnable runnable, T result);
}
