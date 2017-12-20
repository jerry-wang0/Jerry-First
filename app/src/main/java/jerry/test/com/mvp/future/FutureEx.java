package jerry.test.com.mvp.future;

import java.util.concurrent.Future;

/**
 * 
 * @author Jingqi Xu
 */
public interface FutureEx<T> extends Future<T> {

	/**
	 * 
	 */
	<V> V getCookie();
	
	FutureEx<T> setCookie(Object cookie);
	
	Listener<T> setListener(Listener<T> listener);
	
	/**
	 * 
	 */
	interface Listener<T> {
		
		void onComplete(FutureEx<T> future);
	}
}
