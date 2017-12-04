package jerry.test.com.mvp.xchart;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Jingqi Xu
 */
public interface Lifecycle {
	
	boolean stop();
	
	boolean start();
	
	boolean isRunning();
	
	boolean stop(long timeout, TimeUnit unit);
}
