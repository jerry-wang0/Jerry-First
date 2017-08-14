package jerry.test.com.mvp.util;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * 
 * @author Jingqi Xu
 */
public final class DateTimes {
	//
	private static final ISOChronology CHRONOLOGY = ISOChronology.getInstance();

	/**
	 *
	 */
    public static long toMillis(long nanos) {
        return MILLISECONDS.convert(nanos, NANOSECONDS);
    }

    public static long toNanos(long millis) {
        return NANOSECONDS.convert(millis, MILLISECONDS);
    }

    /**
     *
     */
    public static int getYear(final long millis) {
        return CHRONOLOGY.year().get(millis);
    }

    public static long addDays(final long millis, int delta) {
        return CHRONOLOGY.days().add(millis, delta);
    }

    public static long addYears(final long millis, int delta) {
        return CHRONOLOGY.years().add(millis, delta);
    }

    /**
     * Local
     */
    public static DateTimeZone getTimeZone(final int offset) {
        return DateTimeZone.forOffsetHours(offset);
    }

    public static Date toLocalTime(final int tz, long millis) {
        return new Date(millis); // NOP
    }

    public static long fromLocalTimeMillis(int tz, long millis) {
        return millis; // NOP
    }

    /**
     * Trim
     */
    public static final Date date(final Date date) {
        return date(date.getTime());
    }

    public static final Date time(final Date date) {
        Calendar c1 = Calendar.getInstance(); c1.setTime(date);
        Calendar c2 = Calendar.getInstance(); c2.setTime(new Date());
        c2.set(HOUR_OF_DAY, c1.get(HOUR_OF_DAY)); c2.set(MILLISECOND, c2.get(MILLISECOND));
        c2.set(MINUTE, c1.get(MINUTE)); c2.set(SECOND, c1.get(SECOND)); return c2.getTime();
    }

    public static final Date date(final long millis) {
        Calendar c = Calendar.getInstance(); c.setTimeInMillis(millis);
        c.set(HOUR_OF_DAY, 0); c.set(MINUTE, 0); c.set(SECOND, 0); c.set(MILLISECOND, 0); return c.getTime();
    }

    /**
     * Format
     */
    public static String format(String pattern, long millis) {
        return DateTimeFormat.forPattern(pattern).print(millis);
    }

    public static String format(final String pattern, Date date) {
        return date == null ? "" : format(pattern, date.getTime());
    }

    public static String format(String pattern, Date date, Locale locale) {
        return date == null ? "" : format(pattern, date.getTime(), locale);
    }

    public static String format(String pattern, long millis, Locale locale) {
        return DateTimeFormat.forPattern(pattern).withLocale(locale).print(millis);
    }

    public static String format(String pattern, long millis, DateTimeZone timezone) {
        return DateTimeFormat.forPattern(pattern).withZone(timezone).print(millis);
    }

    /**
     * Parse
     */
    public static long parse(final String pattern, String date) {
        return DateTimeFormat.forPattern(pattern).parseMillis(date);
    }

    public static long parse(final String pattern, String date, Locale locale) {
        return DateTimeFormat.forPattern(pattern).withLocale(locale).parseMillis(date);
    }

    public static long parse(final String pattern, String date, DateTimeZone timezone) {
        return DateTimeFormat.forPattern(pattern).withZone(timezone).parseMillis(date);
    }
}
