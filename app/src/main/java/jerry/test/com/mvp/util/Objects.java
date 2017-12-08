package jerry.test.com.mvp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;

/**
 * 
 * @author Jingqi Xu
 */
public final class Objects {

	/**
	 * 
	 */
	public static <T> T cast(Object obj) {
		return (T)obj;
	}

	/**
	 *
	 */
	public static <T> boolean isEquals(T lhs, T rhs) {
		if(lhs == null && rhs == null) return true;
		else if(lhs == null || rhs == null) return false;
		else return lhs.equals(rhs);
	}

	/**
	 *
	 */
    public static int[] copy(final int[] array) {
        if(array == null) return null;
        final int[] r = new int[array.length];
        System.arraycopy(array, 0, r, 0, r.length); return r;
    }

    public static String[] copy(final String[] array) {
        if(array == null) return null;
        final String[] r = new String[array.length];
        System.arraycopy(array, 0, r, 0, r.length); return r;
    }

    public static boolean[] copy(final boolean[] array) {
        if(array == null) return null;
        final boolean[] r = new boolean[array.length];
        System.arraycopy(array, 0, r, 0, r.length); return r;
    }

	public static <T extends Copyable<T>> T copy(final T t) {
		return t == null ? null : t.copy();
	}

	public static <T extends Copyable<T>> List<T> copy(final List<T> l) {
        if(l == null) return null;
        ArrayList<T> r = new ArrayList<>(l.size());
        for(final T t : l) r.add(t.copy()); return r;
	}

	public static <T extends Copyable<T>> Map<String, T> copy(Map<String, T> m) {
		if(m == null) return null;
		Map<String, T> r = Maps.newHashMap(m.size());
		for(final Map.Entry<String, T> entry : m.entrySet()) {
			r.put(entry.getKey(), copy(entry.getValue()));
		}
		return r;
	}

	public static <T extends Copyable<T>> LongHashMap<T> copy(LongHashMap<T> m) {
		if(m == null) return null;
		LongHashMap<T> r = Maps.newLongHashMap(m.size());
		for(final Map.Entry<Long, T> entry : m.entrySet()) {
			r.put(entry.getKey(), copy(entry.getValue()));
		}
		return r;
	}
}
