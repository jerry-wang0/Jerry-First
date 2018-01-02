package jerry.test.com.mvp.support;

import com.google.protobuf.ProtocolMessageEnum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.nextop.erebor.mid.app.ApplicationEx;
import cn.nextop.erebor.mid.app.domain.resource.ResourceManager;
import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.concurrent.collection.ConcurrentMultiKeyMap;

import static cn.nextop.erebor.mid.common.util.Maps.putIfAbsent;
import static cn.nextop.erebor.mid.common.util.Strings.underscore;

/**
 * Created by Jingqi Xu on 10/14/15.
 */
public final class XResources {
    //
    private static ConcurrentMap<Class<?>, String> CLASSES = new ConcurrentHashMap<>();
    private static ConcurrentMultiKeyMap<String, Object, Integer> RESOURCES = new ConcurrentMultiKeyMap<>(256);

    /**
     *
     */
    public static String getString(final int resId) {
        ApplicationEx a = ApplicationEx.getInstance();
        return a.getResourceManager().getString(resId);
    }

    public static String[] getStrings(final int resId) {
        ApplicationEx a = ApplicationEx.getInstance();
        return a.getResourceManager().getStrings(resId);
    }

    /**
     *
     */
    public static String getString(PersistentEnum<?> value) {
        return getString(getResId(value));
    }

    /**
     *
     */
    protected static String getClass(final PersistentEnum<?> value) {
        Class<?> clazz = value.getClass(); if(clazz.getEnclosingClass() != null) clazz = clazz.getEnclosingClass();
        String r = CLASSES.get(clazz); return r != null ? r : putIfAbsent(CLASSES, clazz, underscore(clazz.getSimpleName(), true));
    }

    public static final int getDrawableId(final PersistentEnum<?> value) {
        if(value == null) return 0; String clazz = getClass(value);
        final String k = clazz + "_" + value.toString().toLowerCase();
        Integer r = RESOURCES.get(clazz, k); if (r != null) return r;
        final ResourceManager resource = ApplicationEx.getInstance().getResourceManager();
        r = resource.getDrawableId("ic_" + k); if(r > 0) RESOURCES.put(clazz, k, r); return r;
    }

    public static final int getResId(final PersistentEnum<?> value) {
        if(value == null) return 0; String clazz = getClass(value);
        Integer r = RESOURCES.get(clazz, value); if (r != null) return r;
        final ResourceManager resource = ApplicationEx.getInstance().getResourceManager();
        r = resource.getStringId("enums_" + clazz + "_" + value.toString().toLowerCase()); RESOURCES.put(clazz, value, r); return r;
    }

    /**
     *
     */
    protected static String getClass(final ProtocolMessageEnum value) {
        Class<?> c = value.getClass().getEnclosingClass(); String r = CLASSES.get(c); if(r != null) return r;
        return putIfAbsent(CLASSES, c, underscore(c.getSimpleName(), true) + "_" + value.getClass().getSimpleName().toLowerCase());
    }

    public static final int getResId(final ProtocolMessageEnum value) {
        if(value == null) return 0; String clazz = getClass(value);
        Integer r = RESOURCES.get(clazz, value); if (r != null) return r;
        final ResourceManager resource = ApplicationEx.getInstance().getResourceManager();
        r = resource.getStringId("results_" + clazz + "_" + value.toString().toLowerCase()); RESOURCES.put(clazz, value, r); return r;
    }
}
