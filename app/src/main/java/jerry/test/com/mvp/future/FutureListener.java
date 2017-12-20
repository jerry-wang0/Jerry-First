package jerry.test.com.mvp.future;

import java.util.concurrent.Future;

/**
 * 
 * @author Jingqi Xu
 */
public interface FutureListener<T> {
	
	void onComplete(Future<T> future, T result, Throwable throwable);
}
