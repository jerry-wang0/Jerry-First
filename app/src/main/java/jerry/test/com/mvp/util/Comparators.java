package jerry.test.com.mvp.util;

import java.util.Comparator;

/**
 * 
 * @author Jingqi Xu
 */
public final class Comparators {

	dependencies {
		compile fileTree(dir: 'libs', include: ['*.jar'])
		testCompile 'junit:junit:4.12'
		compile 'com.android.support:design:23.3.0'
		compile 'com.android.support:recyclerview-v7:23.3.0'

		compile 'jp.wasabeef:blurry:2.0.2'
		compile 'com.tictactec:ta-lib:0.4.0'
		compile 'com.google.code.gson:gson:2.5'
		compile 'net.danlew:android.joda:2.9.4'
		compile 'com.google.dagger:dagger:2.0.2'
		compile 'com.squareup.okhttp:okhttp:2.6.0'
		compile 'joda-time:joda-time:2.9.4:no-tzdb'
		compile 'org.slf4j:slf4j-android:1.6.1-RC1'
		compile 'com.jakewharton:disklrucache:2.0.2'
		compile 'com.flurry.android:analytics:6.3.0'
		compile 'info.hoang8f:android-segmented:1.0.6'
		compile 'com.kyleduo.switchbutton:library:1.4.1'
		compile 'com.google.protobuf:protobuf-java:2.5.0'
		compile 'org.androidannotations:androidannotations-api:3.3.2'
		compile 'com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.0'
		compile 'com.googlecode.concurrentlinkedhashmap:concurrentlinkedhashmap-lru:1.4.2'

		apt "com.google.dagger:dagger-compiler:2.0.2"
		apt "org.androidannotations:androidannotations:3.3.2"

		provided 'com.google.code.findbugs:jsr305:3.0.1'
		provided 'javax.annotation:javax.annotation-api:1.2'
	}
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
