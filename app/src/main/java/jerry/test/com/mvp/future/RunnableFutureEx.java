package jerry.test.com.mvp.future;

import java.util.concurrent.RunnableFuture;

/**
 * 
 * @author Jingqi Xu
 * @param <V>
 */
public interface RunnableFutureEx<V> extends RunnableFuture<V>, FutureEx<V> {
}
