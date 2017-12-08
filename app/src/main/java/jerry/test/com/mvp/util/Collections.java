package jerry.test.com.mvp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.nextop.erebor.mid.common.glossary.Tuple;

/**
 * Created by Jingqi Xu on 7/23/15.
 */
public final class Collections {

    /**
     *
     */
    public static void clear(Collection<?> c) {
        if(c != null) c.clear();
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     *
     */
    public static <T> T getLast(final List<T> list) {
        if (isEmpty(list)) return null;
        return list.get(list.size() - 1);
    }

    /**
     *
     */
    public static <T> void replace(List<T> a, List<T> b) {
        a.clear(); if(!isEmpty(b)) a.addAll(b);
    }

    /**
     *
     */
    public static <T> List<T> toList(final List<T> list) {
        return list == null ? null : new ArrayList<>(list);
    }

    /**
     *
     */
    public static <T> Iterator<T> iterator(Collection<T> c) {
        return c == null ? null : c.iterator();
    }

    /**
     *
     */
    public static <T> Tuple<Boolean, List<T>> trim(List<T> list, int limit) {
        if(list == null || list.size() <= limit) {
            return new Tuple<>(false, list);
        } else {
            return new Tuple<>(true, list.subList(0, limit));
        }
    }

    /**
     *
     */
    public static <T extends Comparable<? super T>> List<T> sort(final List<T> list) {
        java.util.Collections.sort(list); return list;
    }

    public static final <T> T get(final List<T> list, final int index, final T dft) {
        return (index >= 0 && index < list.size()) ? list.get(index) : dft;
    }

    public static <T> void add(final List<T> list, final T t, Comparator<T> comparator) {
        int index = java.util.Collections.binarySearch(list, t, comparator);
        if(index >= 0) list.set(index, t); else list.add((-(index) - 1), t);
    }

    public static <T> List<T> sort(final List<T> list, Comparator<? super T> comparator) {
        java.util.Collections.sort(list, comparator); return list;
    }

    public static <T> int search(final List<T> list, final T t, Comparator<T> comparator) {
        return java.util.Collections.binarySearch(list, t, comparator);
    }
}
