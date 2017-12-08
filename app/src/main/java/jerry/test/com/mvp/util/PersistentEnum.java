package jerry.test.com.mvp.util;

import java.util.Map;

/**
 * 
 * @author Jingqi Xu
 */
public interface PersistentEnum<T> {
	
	T getValue();
	
	String getDisplayName();

	Map<T, ? extends PersistentEnum<T>> getAll();
}
