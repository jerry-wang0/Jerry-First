package jerry.test.com.mvp.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author Jingqi Xu
 */
public final class Iterators {
	//
	private static final Iterable<?> EMPTY = new IterableImpl<Object>(new IteratorImpl<>());

	/**
	 * 
	 */
	public static <T> Iterable<T> iterable(Iterator<T> iterator) {
		if(iterator == null) {
			return Objects.cast(EMPTY);
		} else {
			return new IterableImpl<T>(iterator);
		}
	}
	
	public static <T> Iterable<T> iterable(Collection<T> collection) {
		if(collection == null) {
			return Objects.cast(EMPTY);
		} else {
			return new IterableImpl<T>(collection.iterator());
		}
	}
	
	/**
	 * 
	 */
	private static final class IterableImpl<T> implements Iterable<T> {
		private final Iterator<T> iterator;
		@Override
        public Iterator<T> iterator() { return this.iterator; }
		private IterableImpl(Iterator<T> iterator) { this.iterator = iterator; }
	}

	private static final class IteratorImpl<T> implements Iterator<T> {
		@Override
        public T next() { return null; }
		@Override
        public boolean hasNext() { return false; }
		@Override
        public void remove() { throw new UnsupportedOperationException(); }
	}
}
