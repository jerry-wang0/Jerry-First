package jerry.test.com.mvp.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.nextop.erebor.mid.common.util.assertion.AssertionException;

import static cn.nextop.erebor.mid.common.util.Objects.isEquals;
import static java.util.Arrays.fill;

/**
 * Optimized for Get/Put/Copy
 * @author Jingqi Xu
 * @param <V>
 */
@SuppressWarnings({"unchecked"})
public class ByteMap<V> implements Map<Byte, V>, Serializable {
	//
	private static final int MASK = 0x000000FF;
	private static final byte[] INT2BYTE = new byte[256];
	static {
		INT2BYTE[Byte.MAX_VALUE & MASK] = Byte.MAX_VALUE;
		for(byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
			INT2BYTE[b & MASK] = b;
		}
	}
	
	//
	private int size;
	private final Object[] table;
	
	/**
	 * 
	 */
	public ByteMap() {
		this.size = 0;
		this.table = new Object[256];
	}
	
	public ByteMap(ByteMap<V> rhs) {
		this.size = rhs.size;
		this.table = Arrays.copyOf(rhs.table, rhs.table.length);
	}

	/**
	 * 
	 */
	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	@Override
	public void clear() {
		if(this.size <= 0) return;
        fill(table, null); size = 0;
	}
	
	@Override
	public Set<Byte> keySet() {
    	return new KeySet();
    }
    
	@Override
    public Collection<V> values() {
		return new Values();
	}
    
	@Override
	public V get(final Object key) {
		return (V)this.table[index(key)];
	}

	@Override
	public V put(Byte key, V value) {
		final int index = index(key);
		V r = (V)this.table[index];
		if(r == null) this.size++;
		this.table[index] = value;
		return r;
	}
	
	@Override
	public V remove(final Object key) {
		final int index = index(key);
		V r = (V)this.table[index];
		if(r != null) this.size--;
		this.table[index] = null;
		return r;
	}
	
	@Override
	public boolean containsKey(Object key) {
		return this.table[index(key)] != null;
	}
	
	@Override
    public Set<Map.Entry<Byte, V>> entrySet() {
        return new EntrySet();
    }
	
	@Override
	public boolean containsValue(final Object value) {
		for(final Object element : this.table) {
			if(isEquals(element, value)) return true;
		}
		return false;
	}

	@Override
	public void putAll(Map<? extends Byte, ? extends V> map) {
		if(map == null) return;
		for(Map.Entry<? extends Byte, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 
	 */
	protected int index(Object key) {
		final Byte index = (Byte)key;
		int r = index.byteValue() & MASK;
		if(r >= 0 && r < table.length) {
			return r;
		} else {
			throw new AssertionException("invalid index: " + r);
		}
	}
	
	protected Iterator<V> valueIterator() {
    	return new ValueIterator();
    }
    
    protected Iterator<Byte> keyIterator() {
    	return new KeyIterator();
    }
    
    protected Iterator<Map.Entry<Byte, V>> entryIterator() {
    	return new EntryIterator();
    }
	
    /**
     * 
     */
    private class Values extends AbstractCollection<V> {

    	@Override
		public int size() {
			return size;
		}
    	
    	@Override
    	public void clear() {
    		ByteMap.this.clear();
        }
    	
		@Override
		public Iterator<V> iterator() {
			return valueIterator();
		}
		
		@Override
		public boolean contains(Object value) {
            return containsValue(value);
        }
    }
    
    private class KeySet extends AbstractSet<Byte> {
    	
    	@Override
        public int size() {
            return size;
        }
        
    	@Override
        public void clear() {
        	ByteMap.this.clear();
        }
        
    	@Override
        public Iterator<Byte> iterator() {
            return keyIterator();
        }
        
    	@Override
        public boolean contains(final Object key) {
        	if(key == null || !(key instanceof Byte)) return false;
            return containsKey((Byte)key);
        }
    }
    
    private class EntrySet extends AbstractSet<Map.Entry<Byte, V>> {
    	
    	@Override
        public int size() {
            return size;
        }
        
    	@Override
        public void clear() {
        	ByteMap.this.clear();
        }
        
    	@Override
        public Iterator<Map.Entry<Byte, V>> iterator() {
            return entryIterator();
        }
        
    	@Override
        public boolean contains(final Object entry) {
            if (!(entry instanceof Map.Entry)) return false;
            final Map.Entry<Byte, V> e = (Map.Entry<Byte, V>)entry;
            final V candidate = get(e.getKey());
            return candidate != null && candidate.equals(e.getValue());
        }
    }
    
    /**
     * 
     */
    private abstract class AbstractIterator {
    	//
    	protected int index = prefetch(0);
    	
    	/**
    	 * 
    	 */
		public final boolean hasNext() {
			return this.index >= 0;
		}
    	
		public final void remove() {
			throw new UnsupportedOperationException();
		}
		
		protected final int prefetch(int index) {
			for(int i = index, length = table.length; i < length; i++) {
				if(table[i] != null) return i;
	        }
			return -1;
		}
    }
    
    private class KeyIterator extends AbstractIterator implements Iterator<Byte> {
		
		@Override
		public Byte next() {
			if(this.index < 0) return null;
			final byte r = INT2BYTE[this.index];
			this.index = prefetch(this.index + 1);
			return r;
		}
    }
    
    private class ValueIterator extends AbstractIterator implements Iterator<V> {
		
		@Override
		public V next() {
			if(this.index < 0) return null;
			final V r = (V)table[this.index];
			this.index = prefetch(this.index + 1);
			return r;
		}
    }
    
    private class EntryIterator extends AbstractIterator implements Iterator<Map.Entry<Byte, V>> {

		@Override
		public Map.Entry<Byte, V> next() {
			if(this.index < 0) return null;
			final Map.Entry<Byte, V> r = new Entry(this.index);
			this.index = prefetch(this.index + 1);
			return r;
		}
    }
	
	/**
	 * 
	 */
    protected class Entry implements Map.Entry<Byte, V> {
		//
		private final int index;
		
		/**
		 * 
		 */
		public Entry(int index) {
			this.index = index;
		}

		@Override
		public Byte getKey() {
			return (byte)this.index;
		}

		@Override
		public V getValue() {
			return (V)table[this.index];
		}

		@Override
		public V setValue(V value) {
			final V r = (V)table[this.index];
			table[this.index] = value;
			if(r == null) size++;
			return r;
		}
	}
}
