package jerry.test.com.mvp.support;

import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import cn.nextop.erebor.mid.common.util.Maps;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by Jingqi Xu on 11/12/15.
 */
public final class Spans {
    //
    private static final ConcurrentLinkedHashMap<Integer, Parcelable> FONTS = Maps.newConcurrentLinkedHashMap(512);
    private static final ConcurrentLinkedHashMap<Integer, Parcelable> COLORS = Maps.newConcurrentLinkedHashMap(512);

    /**
     *
     */
    public static Parcelable getFont(int size) {
        final Parcelable r = FONTS.get(size); if(r != null) return r;
        return Maps.putIfAbsent(FONTS, size, new AbsoluteSizeSpan(size, true));
    }

    public static Parcelable getColor(int color) {
        final Parcelable r = COLORS.get(color); if(r != null) return r;
        return Maps.putIfAbsent(COLORS, color, new ForegroundColorSpan(color));
    }

    /**
     *
     */
    public static Spanned getSpan(int size, final String text) {
        final SpannableString r = new SpannableString(text);
        r.setSpan(getFont(size), 0, text.length(), SPAN_EXCLUSIVE_EXCLUSIVE); return r;
    }
}
