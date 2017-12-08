package jerry.test.com.mvp.util;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.nextop.erebor.mid.common.util.collection.map.IntHashMap;
import cn.nextop.erebor.mid.common.util.collection.map.LinkedLongHashMap;
import cn.nextop.erebor.mid.common.util.collection.map.LongHashMap;
import cn.nextop.erebor.mid.common.util.concurrent.collection.ConcurrentMultiKeyMap;

/**
 * 
 * @author Jingqi Xu
 */
@SuppressWarnings("unchecked")
public final class Maps {

    /**
	 * 
	 */
	public static int size(Map<?, ?> m) {
		return m == null ? 0 : m.size();
	}
	
	public static void clear(Map<?, ?> m) {
		if(m != null) m.clear();
	}
	
	public static int size(IntHashMap<?> m) {
		return m == null ? 0 : m.size();
	}
	
	public static int size(LongHashMap<?> m) {
		return m == null ? 0 : m.size();
	}
	
	public static boolean isEmpty(Map<?, ?> m) {
		return m == null || m.isEmpty();
	}
	
	public static boolean isEmpty(IntHashMap<?> m) {
		return m == null || m.isEmpty();
	}
	
	public static boolean isEmpty(LongHashMap<?> m) {
		return m == null || m.isEmpty();
	}
	
	public static final <K, V> Map<K, V> emptyMap() {
		return (Map<K, V>)java.util.Collections.EMPTY_MAP;
	}
	
	public static <K, V> List<V> valueList(Map<K, V> m) {
		return m == null ? null : new ArrayList<>(m.values());
	}
	
	public static <V> List<V> valueList(IntHashMap<V> m) {
		return m == null ? null : new ArrayList<>(m.values());
	}
	
	public static <V> List<V> valueList(LongHashMap<V> m) {
		return m == null ? null : new ArrayList<>(m.values());
	}
	
	public static <K, V> Map<K, V> nullToEmpty(Map<K, V> m) {
		return m != null ? m : (Map<K, V>)emptyMap();
	}

    /**
     *
     */
    protected static int getInitialCapacity(final int size) {
        return getInitialCapacity(size, 0.75d);
    }

    protected static int getInitialCapacity(int size, double lf) {
        return (int)(size / lf) + 1;
    }
	
	/**
	 * 
	 */
	public static <K, V> V get(Map<K, V> m, K k) {
		return m == null ? null : m.get(k);
	}
	
	public static <T> T get(LongHashMap<T> m, long k) {
		return m == null ? null : m.get(k);
	}
	
	public static <K, V> V get(Map<K, V> m, K k1, K k2) {
		if(m == null) return null;
		V v = m.get(k1); return v != null ? v : m.get(k2);
	}
	
	public static <K, V> V put(Map<K, V> m, K k, V v) {
		if(m == null || k == null || v == null) return null;
		return m.put(k, v);
	}
	
	public static <V> V put(LongHashMap<V> m, Long k, V v) {
		if(m == null || k == null || v == null) return null;
		return m.put(k, v);
	}
	
	public static <V> V put(IntHashMap<V> m, Integer k, V v) {
		if(m == null || k == null || v == null) return null;
		return m.put(k, v);
	}
	
	public static <K, V> void putAll(Map<K, V> m1, Map<K, V> m2) {
		if(m1 != null && m2 != null) m1.putAll(m2);
	}
	
	public static <K, V> V putIfAbsent(ConcurrentMap<K, V> m, K k, V v) {
        final V result = m.putIfAbsent(k, v);
        return result != null ? result : v;
    }

	public static final <K, V> void put(ConcurrentMap<K, List<V>> m, K k, V v) {
		List<V> list = m.get(k); if (list != null) { list.add(v); return; }
		list = putIfAbsent(m, k, new CopyOnWriteArrayList<V>()); list.add(v);
	}
	
	/**
	 * 
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return new HashMap<K, V>(getInitialCapacity(size));
	}
	
	public static <V> IntHashMap<V> newIntHashMap(int size) {
		return new IntHashMap<V>(getInitialCapacity(size));
	}
	
	public static <V> LongHashMap<V> newLongHashMap(int size) {
		return new LongHashMap<V>(getInitialCapacity(size));
	}

	public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
		return map == null ? new HashMap<K, V>(0) : new HashMap<>(map);
	}
	
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int size) {
		return new LinkedHashMap<K, V>(getInitialCapacity(size));
	}

    public static <V> LinkedLongHashMap<V> newLinkedLongHashMap(int size) {
        return new LinkedLongHashMap<V>(getInitialCapacity(size));
    }
	
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
		return new ConcurrentHashMap<K, V>(getInitialCapacity(size));
	}

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(LinkedHashMap<K, V> map) {
        return map == null ? new LinkedHashMap<K, V>(0) : new LinkedHashMap<>(map);
    }

    public static <K1, K2, V> ConcurrentMultiKeyMap<K1, K2, V> newConcurrentMultiKeyMap(int size) {
        return new ConcurrentMultiKeyMap<>(getInitialCapacity(size));
    }

    public static <K, V> ConcurrentLinkedHashMap<K, V> newConcurrentLinkedHashMap(final int weight) {
        return new ConcurrentLinkedHashMap.Builder<K, V>().maximumWeightedCapacity(weight).build();
    }

    /**
     *
     */
    public static final <K, V> List<V> toList(final ConcurrentMap<K, List<V>> m) {
        List<V> r = new ArrayList<>(); if(isEmpty(m)) return r;
        for(List<V> values : m.values()) r.addAll(values); return r;
    }

	/**
	 * 
	 */
	public static final <K, V> Map<K, V> toMap(K key, V value) {
		HashMap<K, V> r = newHashMap(1); r.put(key, value);
		return r;
	}

	public static final <K, V> Map<K, V> toMap(K k1, V v1, K k2, V v2) {
		HashMap<K, V> r = newHashMap(2); r.put(k1, v1); r.put(k2, v2);
		return r;
	}
	
	public static final Map<String, String> toMap(final String... kvs) {
		if(kvs == null || kvs.length % 2 != 0) {
			throw new IllegalArgumentException("invalid parameter kvs: " + kvs);
		} else {
			final Map<String, String> r = newHashMap(kvs.length / 2);
			for(int i = 0; i < kvs.length; i += 2) r.put(kvs[i], kvs[i + 1]); return r;
		}
	}

	public static final Map<String, String> toMap(String v, final char d1, final char d2) {
        if(Strings.isEmpty(v)) return new HashMap<>(1);
        final String[] array = v.split(String.valueOf(d1));
        Map<String, String> r = Maps.newHashMap(array.length);
        for(final String kv : array) {
            if(Strings.isEmpty(kv)) continue; final int index = kv.indexOf(d2);
            r.put(kv.substring(0, index).trim(), kv.substring(index + 1, kv.length()).trim());
        }
		return r;
	}

    /**
     *
     */
	public static <K, V> ConcurrentSkipListMap<K, V> compact(ConcurrentSkipListMap<K, V> m, Comparator<K> c, int capacity) {
		ConcurrentSkipListMap<K, V> r = new ConcurrentSkipListMap<>(c);
		int size = 0; for(final Map.Entry<K, V> entry : m.entrySet()) {
			r.put(entry.getKey(), entry.getValue()); if(++size >= capacity) break;
		}
        return m;
	}
}
