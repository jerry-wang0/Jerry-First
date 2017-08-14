package jerry.test.com.mvp.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import cn.nextop.erebor.mid.common.util.Comparators;
import cn.nextop.erebor.mid.common.util.Maps;
import cn.nextop.erebor.mid.common.util.Strings;

import static cn.nextop.erebor.mid.common.util.DateTimes.format;
import static cn.nextop.erebor.mid.common.util.Maps.putIfAbsent;
import static cn.nextop.erebor.mid.common.util.Objects.cast;

/**
 * Created by Jingqi Xu on 7/23/15.
 */
public final class TradingDate implements Comparable<TradingDate>, Serializable {
    //
    private static final long UNIT = 24L * 60 * 60 * 1000;
    private static final DateTimeZone TIMEZONE = DateTimeZone.forID("Asia/Tokyo");
    private static final ConcurrentHashMap<Short, TradingDate> DAY = new ConcurrentHashMap<>(512);
    private static final ConcurrentHashMap<String, TradingDate> YYMMDD = new ConcurrentHashMap<>(512);
    private static final long OFFSET = DateTimes.parse("yyyy-MM-dd HH:mm:ss.SSS", "2013-01-01 00:00:00.000", TIMEZONE);

    //
    private short days;
    private transient volatile String string1;
    private transient volatile String string2;

    /**
     *
     */
    private TradingDate(short days) {
        this.days = days;
    }

    /**
     *
     */
    @Override
    public int hashCode() {
        return (int)this.days;
    }

    public boolean after(TradingDate rhs) {
        return this.compareTo(rhs) > 0;
    }

    public boolean before(TradingDate rhs) {
        return this.compareTo(rhs) < 0;
    }

    @Override
    public int compareTo(final TradingDate rhs) {
        return Comparators.cmp(this.days, rhs.days, true);
    }

    @Override
    public boolean equals(final Object rhs) {
        if(this == rhs) return true;
        if(rhs == null) return false;
        if(!(rhs instanceof TradingDate)) return false;
        return this.days == ((TradingDate)cast(rhs)).days;
    }

    /**
     *
     */
    public String toString(String pattern) {
        return DateTimes.format(pattern, getDatetime(), TIMEZONE);
    }

    @Override
    public String toString() {
        if(this.string1 != null) return this.string1;
        return this.string1 = format("yyyyMMdd", getDatetime(), TIMEZONE);
    }

    public String toCanonicalString() {
        if(this.string2 != null) return this.string2;
        return this.string2 = format("yyyy-MM-dd", getDatetime(), TIMEZONE);
    }

    /**
     *
     */
    private final long getDatetime() {
        return this.days * UNIT + OFFSET;
    }

    private static short toDays(final long millis) {
        return (short)((millis - OFFSET) / UNIT);
    }

    private static final TradingDate valueOf(final long millis) {
        final short days = toDays(millis); TradingDate r = DAY.get(days);
        return r != null ? r : Maps.putIfAbsent(DAY, days, new TradingDate(days));
    }

    private static long parse(final String yyyyMMdd, final String pattern) {
        if(Strings.isEmpty(yyyyMMdd)) return 0L;
        try{ return DateTimes.parse(pattern, yyyyMMdd, TIMEZONE); } catch(Throwable t) { return 0L; }
    }

    private static final boolean isValid(final long millis) {
        if(millis == 0L) return false; if(DAY.containsKey(toDays(millis))) return true;
        final DateTime d = new DateTime(millis, TIMEZONE); final int dow = d.getDayOfWeek();
        if(d.getDayOfMonth() == 1 && d.getMonthOfYear() == DateTimeConstants.JANUARY) return false;
        return (dow == DateTimeConstants.SATURDAY || dow == DateTimeConstants.SUNDAY) ? false : true;
    }

    /**
     *
     */
    public final java.sql.Date toSqlDate() {
        return java.sql.Date.valueOf(toCanonicalString());
    }

    public static final TradingDate valueOf(String yyyyMMdd) {
        return valueOf(yyyyMMdd, true);
    }

    public static final boolean isValid(final String yyyyMMdd) {
        return valueOf(yyyyMMdd, true) != null;
    }

    public static TradingDate valueOf(String yyyyMMdd, boolean lenient) {
        if(yyyyMMdd == null) return null; TradingDate r = YYMMDD.get(yyyyMMdd); if(r != null) return r;
        long m = parse(yyyyMMdd, "yyyyMMdd"); if(isValid(m)) return putIfAbsent(YYMMDD, yyyyMMdd, valueOf(m));
        if(lenient) return null; else throw new IllegalArgumentException("invalid trading date: " + yyyyMMdd);
    }

    /**
     *
     */
    public static final Comparator<TradingDate> ASC = new Comparator<TradingDate>() {
        @Override
        public final int compare(final TradingDate o1, final TradingDate o2) { return Comparators.cmp(o1, o2, true); }
    };

    public static final Comparator<TradingDate> DESC = new Comparator<TradingDate>() {
        @Override
        public final int compare(final TradingDate o1, final TradingDate o2) { return Comparators.cmp(o1, o2, false);}
    };
}
