package jerry.test.com.mvp.util;

import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Jingqi Xu
 * @param <V>
 */
@SuppressWarnings({"unchecked"})
public final class LinkedLongHashMap<V> extends LongHashMap<V> {
	//
	private int capacity;
	private Entry<V> header;
	private boolean accessOrder = false;

	/**
	 * 
	 */
	public LinkedLongHashMap() {
		this(Integer.MAX_VALUE);
	}
	
	public LinkedLongHashMap(int capacity) {
		this(capacity, false);
	}
	
	public LinkedLongHashMap(int capacity, boolean accessOrder) {
		this.capacity = capacity;
		this.accessOrder = accessOrder;
		this.header = new Entry<>(0L, null, null);
        this.header.before = this.header.after = this.header;
	}
	
	public LinkedLongHashMap(LinkedLongHashMap<V> rhs) {
		this.capacity = rhs.capacity;
		this.threshold = rhs.threshold;
		this.accessOrder = rhs.accessOrder;
		this.table = new LongHashMap.Entry[rhs.table.length];
		this.header = new Entry<>(0L, null, null);
        this.header.before = this.header.after = this.header;
		for(Map.Entry<Long, V> e : rhs.entrySet()) put(e.getKey(), e.getValue());
	}
	
	/**
	 * 
	 */
	@Override
	public void clear() {
        super.clear();
        this.header.before = this.header.after = this.header;
    }
	
	public int capacity() {
		return capacity;
	}
	
	@Override
	public V get(long key) {
        final Entry<V> e = (Entry<V>)super.findEntry(key);
        if(e == null) return null;
        if(this.accessOrder) e.onAccess(this);
        return e.value;
    }
	
	public V remove(long key) {
    	final Entry<V> e = (Entry<V>)super.removeEntry(key);
    	if(e == null) return null;
    	e.onRemove(this);
        return e.value;
    }
	
	public V put(long key, V value) {
		Entry<V> e = (Entry<V>)super.findEntry(key);
        if(e != null) {
        	e.onAccess(this);
        	final V r = e.value;
        	e.value = value;
        	return r;
        } else {
        	//
        	this.size++;
        	if(this.size > this.capacity) {
        		this.remove(this.header.after.key);
        	} else if(this.size > this.threshold) {
        		super.rehash();
        	}
        	
        	//
        	e = this.createEntry(key, value);
        	e.onAccess(this);
            return null;
        }
    }
	
	/**
	 * 
	 */
	protected Iterator<V> valueIterator() {
    	return new ValueIterator();
    }
    
    protected Iterator<Long> keyIterator() {
    	return new KeyIterator();
    }
    
    protected Iterator<Map.Entry<Long, V>> entryIterator() {
    	return new EntryIterator();
    }
    
	protected Entry<V> createEntry(long key, V value) {
    	final int index = indexFor(hash(key), this.table.length);
        final Entry<V> e = new Entry<V>(key, value);
        e.next = this.table[index];
        this.table[index] = e;
        return e;
    }

	/**
	 * Iterator
	 */
	private abstract class AbstractLinkedIterator {
		//
		protected Entry<V> next = header.after;

		public boolean hasNext() {
			return this.next != header;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	private class KeyIterator extends AbstractLinkedIterator implements Iterator<Long> {
    	
		@Override
		public Long next() {
			if(this.next == header) return null;
			final Long r = this.next.key;
			this.next = this.next.after;
			return r;
		}
    }
    
	private class ValueIterator extends AbstractLinkedIterator implements Iterator<V> {
		
		@Override
		public V next() {
			if(this.next == header) return null;
			final V r = this.next.value;
			this.next = this.next.after;
			return r;
		}
    }
    
    private class EntryIterator extends AbstractLinkedIterator implements Iterator<Map.Entry<Long, V>> {

		@Override
		public Map.Entry<Long, V> next() {
			if(this.next == header) return null;
			Map.Entry<Long, V> r = this.next;
			this.next = this.next.after;
			return r;
		}
    }

    /**
     * 
     */
	private static class Entry<V> extends LongHashMap.Entry<V> {
		//
		private Entry<V> before, after;

		/**
		 * 
		 */
		Entry(long key, V value) {
            super(key, value);
            this.before = this.after = this;
        }
		
        Entry(long key, V value, LongHashMap.Entry<V> next) {
            super(key, value, next);
            this.before = this.after = this;
        }

        /**
         * 
         */
        protected void onRemove(LinkedLongHashMap<V> map) {
        	this.before.after = this.after;
            this.after.before = this.before;
		}
        
        protected void onAccess(LinkedLongHashMap<V> map) {
        	//
        	this.before.after = this.after;
            this.after.before = this.before;
            
            //
            this.after = map.header;
        	this.before = map.header.before;
        	this.before.after = this;
        	this.after.before = this;
		}
	}
}
