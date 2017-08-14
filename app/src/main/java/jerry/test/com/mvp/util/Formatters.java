package jerry.test.com.mvp.util;

import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.google.protobuf.ProtocolMessageEnum;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import cn.nextop.erebor.mid.app.ApplicationEx;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.common.glossary.enums.CurrencyCode;
import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Strings;
import cn.nextop.erebor.mid.rpc.service.domain.common.model.ProductVo;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static cn.nextop.erebor.mid.app.domain.support.Spans.getSpan;
import static cn.nextop.erebor.mid.app.domain.trading.TradingUtils.getTradingVolumeScale;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

/**
 * Created by Jingqi Xu on 8/6/15.
 */
public final class Formatters {
    //
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final ConcurrentMap<String, Spanned> VOLUMES = Maps.newConcurrentLinkedHashMap(512);
    private static final ConcurrentMap<String, Spannable> PRICES = Maps.newConcurrentLinkedHashMap(512);

    //
    private static final DecimalFormat newDecimalFormat(final String pattern) {
        final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        DecimalFormat r = (DecimalFormat)format; r.applyPattern(pattern); return r;
    }

    private static final ThreadLocal<DecimalFormat> POINT_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() { return newDecimalFormat("#,###"); }
    };

    private static final ThreadLocal<DecimalFormat> PERCENTAGE_FORMAT = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() { return newDecimalFormat("#,##0.00"); }
    };

    private static final ThreadLocal<Map<Integer, DecimalFormat>> AMOUNT_FORMAT = new ThreadLocal<Map<Integer, DecimalFormat>>() {
        @Override
        protected Map<Integer, DecimalFormat> initialValue() { return new HashMap<>(); }
    };

    private static final ThreadLocal<Map<Integer, DecimalFormat>> VOLUME_FORMAT = new ThreadLocal<Map<Integer, DecimalFormat>>() {
        @Override
        protected Map<Integer, DecimalFormat> initialValue() { return new HashMap<>(); }
    };

    /**
     *
     */
    public static String format(long value, String dft) {
        return format(value, "yyyy-MM-dd HH:mm:ss", dft);
    }

    public static String format(Date value, String dft) {
        return format(value, "yyyy-MM-dd HH:mm:ss", dft);
    }

    public static String format(Object value, String dft) {
        return value == null ? dft : value.toString();
    }

    public static String format(TradingDate value, String dft) {
        return value == null ? dft : value.toCanonicalString();
    }

    public static String format(final int resId, final String dft) {
        if(resId < 0) return dft; return XResources.getString(resId);
    }

    public static String format(long value, String pattern, String dft) {
        return value == 0 ? dft : DateTimes.format(pattern, value);
    }

    public static String format(Date value, String pattern, String dft) {
        return value == null ? dft : DateTimes.format(pattern, value);
    }

    public static String format(TradingDate value, String pattern, String dft) {
        return value == null ? dft : value.toString(pattern);
    }

    /**
     * Enum
     */
    public static String format(ProtocolMessageEnum value, String dft) {
        if(value == null) return dft;
        int resId = XResources.getResId(value);
        final ApplicationEx application = ApplicationEx.getInstance();
        return resId <= 0 ? value.toString() : application.getString(resId);
    }

    public static String format(final PersistentEnum<?> value, String dft) {
        if(value == null) return dft;
        int resId = XResources.getResId(value);
        final ApplicationEx application = ApplicationEx.getInstance();
        return resId <= 0 ? value.getDisplayName() : application.getString(resId);
    }

    public static int format(final PersistentEnum<?> value, int dft) {
        if(value == null) return dft;
        return XResources.getDrawableId(value);
    }

    /**
     * Point
     */
    public static String formatPoints(final BigDecimal value, final String dft) {
        if(value == null) return dft;
        return POINT_FORMAT.get().format(value.setScale(0, HALF_UP));
    }

    /**
     * pip
     */
    public static String formatPips(final BigDecimal value, final String dft) {
        if(value == null) return dft;
        return getFormat(1).format(value.setScale(1, HALF_UP));
    }

    public static Spanned formatPipsEx(final BigDecimal value, final String dft) {
        return formatPipsEx(value, " Pips", dft);
    }

    public static Spanned formatPipsEx(final BigDecimal value, String unit, final String dft) {
        if(value == null) return new SpannableStringBuilder(dft);
        final String v = getFormat(1).format(value.setScale(1, HALF_UP));
        final SpannableStringBuilder b = new SpannableStringBuilder();b.append(v);
        final int color = (value != null && value.compareTo(ZERO) < 0) ? RED : WHITE;
        b.setSpan(Spans.getColor(color), 0, v.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        if(!Strings.isEmpty(unit)) b.append(getSpan(12, unit)); return b;
    }

    /**
     * Percentage
     */
    public static String formatPercentage(final BigDecimal value, final String dft) {
        if(value == null) return dft;
        return PERCENTAGE_FORMAT.get().format(value.multiply(HUNDRED).setScale(2, HALF_UP));
    }

    public static Spanned formatPercentageEx(final BigDecimal v, final String dft) {
        final String s = formatPercentage(v, dft);
        final SpannableStringBuilder b = new SpannableStringBuilder(s);
        final int color = (v != null && v.compareTo(ZERO) < 0) ? RED : WHITE;
        b.setSpan(Spans.getColor(color), 0, s.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        b.append(getSpan(12, " %")); return b;
    }

    /**
     * Price
     */
    public static int getColor(final BigDecimal prev, BigDecimal next, Resources resources) {
        int c = (prev == null || next == null) ? 0 : prev.compareTo(next);
        if (c < 0) return Colors.COLOR_PRICE_UP; if (c > 0) return Colors.COLOR_PRICE_DOWN; return Colors.COLOR_PRICE_EVEN;
    }

    public static final String formatPrice(final BigDecimal value, final int scale, final String dft) {
        if(value == null || value.compareTo(ZERO) <= 0) return dft;
        return value.setScale(scale, HALF_UP).toPlainString();
    }

    public static Spanned formatPrice(final BigDecimal value, final float span, int scale, String dft) {
        //
        if(value == null) return new SpannableString(dft);
        int start, end; final String v = formatPrice(value, scale, dft);
        if(scale >= 3) {
            end = v.length() - 1; start = end - 2; if (start < 0) start = 0;
        } else { // XAUUSD
            end = v.length() - scale - 1; start = end - 2; if (start < 0) start = 0;
        }
        if(end < start) end = start + 1; // if end < start throw exception

        //
        final String key = v + "-" + span + "-" + scale;
        final int style = SPAN_EXCLUSIVE_EXCLUSIVE;
        Spannable r = PRICES.get(key); if(r != null) return r; else r = new SpannableString(v);
        r.setSpan(new RelativeSizeSpan(span), start, end, style); return Maps.putIfAbsent(PRICES, key, r);
    }

    /**
     * Volume
     */
    protected static DecimalFormat getFormat(final int scale) {
        if(scale <= 0) return newDecimalFormat("#,###");
        final StringBuilder r = new StringBuilder("#,##0.");
        for(int i = 0; i < scale; i++) r.append("0"); return newDecimalFormat(r.toString());
    }

    public static String formatVolume(final BigDecimal value, final ProductVo product, final String dft) {
        if(value == null) return dft;
        final int scale = getTradingVolumeScale(product);
        final BigDecimal unit = product.getContractSize();
        DecimalFormat format = VOLUME_FORMAT.get().get(scale);
        if(format == null) VOLUME_FORMAT.get().put(scale, format = getFormat(scale));
        return format.format(value.divide(unit, scale, HALF_UP).setScale(scale, HALF_UP));
    }

    public static String formatVolume(final BigDecimal value, final SymbolSetting symbol, final String dft) {
        if(value == null) return dft;
        final int scale = getTradingVolumeScale(symbol);
        final BigDecimal unit = symbol.getContractSize();
        DecimalFormat format = VOLUME_FORMAT.get().get(scale);
        if(format == null) VOLUME_FORMAT.get().put(scale, format = getFormat(scale));
        return format.format(value.divide(unit, scale, HALF_UP).setScale(scale, HALF_UP));
    }

    public static Spanned formatVolumeEx(final BigDecimal value, final ProductVo product, final String dft) {
        final String v = formatVolume(value, product, dft); final Spanned r = VOLUMES.get(v);
        return r != null ? r : Maps.putIfAbsent(VOLUMES, v, new SpannableStringBuilder(v).append(getSpan(12, " Lot")));
    }

    public static Spanned formatVolumeEx(final BigDecimal value, final SymbolSetting symbol, final String dft) {
        final String v = formatVolume(value, symbol, dft); final Spanned r = VOLUMES.get(v);
        return r != null ? r : Maps.putIfAbsent(VOLUMES, v, new SpannableStringBuilder(v).append(getSpan(12, " Lot")));
    }

    public static final Spanned formatVolumeEx(final BigDecimal value, final int scale, final String dft) {
        if(value == null) return new SpannableStringBuilder(dft);
        final String v = value.setScale(scale, HALF_UP).toPlainString(); final Spanned r = VOLUMES.get(v);
        return r != null ? r : Maps.putIfAbsent(VOLUMES, v, new SpannableStringBuilder(v).append(getSpan(12, " Lot")));
    }

    /**
     * Amount
     */
    public static String formatAmount(final BigDecimal value, final CurrencyCode currency, final String dft) {
        final int scale = currency.getScale();
        DecimalFormat format = AMOUNT_FORMAT.get().get(scale);
        if(format == null) AMOUNT_FORMAT.get().put(scale, format = getFormat(scale));
        return value == null ? dft : format.format(value.setScale(scale, HALF_UP));
    }

    /**
     * sample : -290 jpy
     */
    public static Spanned formatAmountEx(final BigDecimal value, final CurrencyCode currency, final String dft) {
        return formatAmountEx(value, currency, format(currency, currency.name()), dft);
    }

    /**
     * sample : -290
     */
    public static Spanned formatAmountEx(final BigDecimal v, final CurrencyCode currency, final String unit, final String dft) {
        final String s = formatAmount(v, currency, dft);
        final SpannableStringBuilder b = new SpannableStringBuilder(s);
        final int color = (v != null && v.compareTo(ZERO) < 0) ? RED : WHITE;
        b.setSpan(Spans.getColor(color), 0, s.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        if(!Strings.isEmpty(unit)) b.append(" ").append(getSpan(12, unit)); return b;
    }
}
