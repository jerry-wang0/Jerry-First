package jerry.test.com.mvp.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.nextop.erebor.mid.common.util.collection.map.ByteMap;

/**
 * 
 * @author Jingqi Xu
 */
public final class PersistentEnums {
	//
	private static final ConcurrentMap<Class<?>, Type> TYPE_ARGUMENTS = new ConcurrentHashMap<>(128);
	
	/**
	 * 
	 */
	public static <T extends PersistentEnum<?>> Type getTypeArgument(Class<T> clazz) {
		//
		Type r = TYPE_ARGUMENTS.get(clazz);
		if(r != null) return r;

		//
		Type interfaces[] = clazz.getGenericInterfaces();
		if(interfaces == null || interfaces.length == 0) {
			interfaces = clazz.getEnclosingClass().getGenericInterfaces();
		}

		//
		for(int i = 0; i < interfaces.length; i++) {
			if(!(interfaces[i] instanceof ParameterizedType)) continue;
			final ParameterizedType pt = (ParameterizedType)interfaces[i];
			if(pt.getRawType() == PersistentEnum.class) {r = pt.getActualTypeArguments()[0]; break;}
		}
		if(r != null) r = Maps.putIfAbsent(TYPE_ARGUMENTS, clazz, r);
		return r;
	}

    /**
	 *
     */
    public static <V, T extends Enum<T> & PersistentEnum<V>> Map<V, T> objects(Class<T> clazz) {
        final Map<V, T> r = new HashMap<V, T>(); for(T t : clazz.getEnumConstants()) r.put(t.getValue(), t); return r;
    }

    public static <T extends Enum<T> & PersistentEnum<Byte>> Map<Byte, T> bytes(Class<T> clazz) {
        final Map<Byte, T> r = new ByteMap<T>(); for(T t : clazz.getEnumConstants()) r.put(t.getValue(), t); return r;
    }
}
