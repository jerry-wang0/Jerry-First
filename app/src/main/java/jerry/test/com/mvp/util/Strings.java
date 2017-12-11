package jerry.test.com.mvp.util;

/**
 * 
 * @author Jingqi Xu
 */
public final class Strings {

    /**
     *
     */
    public static ToStringBuilder build(Object obj) {
        return new ToStringBuilder(obj);
    }

	/**
	 *
	 */
	public static String toString(final Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 *
	 */
	public static String nullToEmpty(final String id) {
		return id == null ? "" : id;
	}

    /**
     *
     */
    public static String underscore(String s) {
        return underscore(s, false);
    }

    public static String underscore(String s, boolean lowercase) {
        if(isEmpty(s)) return s;
        String r = s.replaceAll("(.)(\\p{Upper})", "$1_$2");
        if(lowercase) return r.toLowerCase(); else return r;
    }

	/**
	 * 
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	public static boolean isEmpty(String s, boolean trim) {
		if(s == null) return true;
		return trim ? s.trim().length() == 0 : s.length() == 0;
	}
	
	public static boolean isEquals(final String s1, final String s2) {
		return isEquals(s1, s2, false);
	}
	
	public static boolean isEquals(String s1, String s2, boolean ic) {
		if(s1 == null && s2 == null) return true;
		else if(s1 == null || s2 == null) return false;
		else return ic ? s1.equalsIgnoreCase(s2) : s1.equals(s2);
	}

	public static boolean isEquals(final String s1, final String... s2) {
		if(s1 == null && s2 == null) return true;
		for(String s : s2) { if(isEquals(s1, s)) return true; } return false;
	}

	/**
	 *
	 */
	public static final class ToStringBuilder {
		//
		private boolean guard = false;
		private final StringBuilder builder;

		/**
		 *
		 */
		public ToStringBuilder(Object obj) {
			final String clazz = obj.getClass().getSimpleName();
			this.builder = new StringBuilder(clazz).append("[");
		}

		/**
		 *
		 */
		public String toString() {
			return builder.append("]").toString();
		}

		public ToStringBuilder append(String name, Object value) {
			if(!guard) guard = true; else builder.append(",");
			final String v = value == null ? "null" : value.toString();
			this.builder.append(name).append("=").append(v); return this;
		}
	}
}
