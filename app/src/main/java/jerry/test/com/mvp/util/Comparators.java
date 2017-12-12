package jerry.test.com.mvp.util;

import java.util.Comparator;

/**
 * 
 * @author Jingqi Xu
 */
public final class Comparators {
	
	/**
	 * 
	 */
	public static int cmp(int v1, int v2, boolean asc) {
		int r = 0;
		if(v1 < v2) r = -1;
		else if(v1 > v2) r = 1;
		return asc ? r : -r;
	}
	
	public static int cmp(long v1, long v2, boolean asc) {
		int r = 0;
		if(v1 < v2) r = -1;
		else if(v1 > v2) r = 1;
		return asc ? r : -r;
	}
	
	public static int cmp(short v1, short v2, boolean asc) {
		int r = 0;
		if(v1 < v2) r = -1;
		else if(v1 > v2) r = 1;
		return asc ? r : -r;
	}
	
	/**
	 * 
	 */
	public static <T extends Comparable<T>> T min(final T v1, final T v2) {
		if(v1 == null && v2 == null) return null;
		else if(v1 == null) return v2;
		else if(v2 == null) return v1;
		return v1.compareTo(v2) <= 0 ? v1 : v2;
	}
	
	public static <T extends Comparable<T>> T max(final T v1, final T v2) {
		if(v1 == null && v2 == null) return null;
		else if(v1 == null) return v2;
		else if(v2 == null) return v1;
		return v1.compareTo(v2) >= 0 ? v1 : v2;
	}
	
	/**
	 * 
	 */
	public static <T extends Comparable<T>> boolean lt(T v1, T v2, boolean nig) {
		return cmp(v1, v2, true, nig) < 0;
	}
	
	public static <T extends Comparable<T>> boolean gt(T v1, T v2, boolean nig) {
		return cmp(v1, v2, true, nig) > 0;
	}
	
	public static <T extends Comparable<T>> boolean let(T v1, T v2, boolean nig) {
		return cmp(v1, v2, true, nig) <= 0;
	}
	
	public static <T extends Comparable<T>> boolean get(T v1, T v2, boolean nig) {
		return cmp(v1, v2, true, nig) >= 0;
	}
	
	public static final <T extends Comparable<? super T>> int cmp(T v1, T v2, boolean asc) {
		return asc ? v1.compareTo(v2) : -(v1.compareTo(v2));
	}
	
	public static final <T extends Comparable<? super T>> int cmp(T v1, T v2, boolean asc, boolean nullIsGreater) {
		int r = 0;
		if(v1 == null && v2 == null) r = 0;
		else if(v1 == null) r = nullIsGreater ? 1 : -1;
		else if(v2 == null) r = nullIsGreater ? -1 : 1;
		else r = v1.compareTo(v2);
		return asc ? r : -r;
	}
	
	/**
	 * 
	 */
	public static final class LongComparator implements Comparator<Long> {
		private final boolean asc;
		public LongComparator(boolean asc) { this.asc = asc; }
		@Override
		public int compare(Long v1, Long v2) { return cmp(v1, v2, asc); }
	}
	
	public static final class IntegerComparator implements Comparator<Integer> {
		private final boolean asc;
		public IntegerComparator(boolean asc) { this.asc = asc; }
		@Override
		public int compare(Integer v1, Integer v2) { return cmp(v1, v2, asc); }
	}
}
