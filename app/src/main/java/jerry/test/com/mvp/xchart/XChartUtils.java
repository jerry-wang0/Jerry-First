package jerry.test.com.mvp.xchart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.util.DateTimes;
import cn.nextop.erebor.mid.common.util.assertion.AssertionException;


public final class XChartUtils {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(XChartUtils.class);

    //
    public static final Rect EMPTY_RECT = toRect1(0f, 0f, 0f, 0f);

    //
    public static int COLOR_ORANGE = Color.argb(255, 0xFF, 0x90, 0x45);
    public static int COLOR_SKY_BLUE = Color.argb(255, 0x87, 0xCE, 0xFA);
    public static int COLOR_LIGHT_GRAY = Color.argb(255, 0x80, 0x80, 0x80);
    public static int COLOR_YELLOW = Color.argb(255, 0xFF, 0xFF, 0x00);
    public static int COLOR_GREEN = Color.argb(255, 0x00, 0x9F, 0x3B);
    public static int COLOR_WHITE = Color.argb(255, 0xFF, 0xFF, 0xFF);
    public static int COLOR_GRAY = Color.argb(255, 0xB2, 0xB4, 0xB3);
    public static int COLOR_CLEAR = Color.argb(255, 0x00, 0x00, 0x00);
    //
    private static final float[] UNITS_1 = new float[16];
    private static final double[] UNITS_2 = new double[16];
    private static final String[] FORMATS = new String[16];
    static {
        for(int digits = 0; digits < 16; digits++) {
            FORMATS[digits] = "%." + digits + "f";
            UNITS_1[digits] = (float) Math.pow(10, digits);
            UNITS_2[digits] = (double) Math.pow(10, digits);
        }
    }

    public static class CGPoint{
        public double x,y;
        public CGPoint(double x,double y){
            this.x = x;this.y = y;
        }
    }

    public static class CGSize{
        public float width,height;
        public CGSize(float width,float height){
            this.width = width;this.height = height;
        }
    }

    /**
     *
     */
    public static int min(int v1, int v2) {
        return Math.min(v1, v2);
    }

    public static int max(int v1, int v2) {
        return Math.max(v1, v2);
    }

    public static float min(float v1, float v2) {
        return Math.min(v1, v2);
    }

    public static float max(float v1, float v2) {
        return Math.max(v1, v2);
    }

    public static double min(double v1, double v2) {
        return Math.min(v1, v2);
    }

    public static double max(double v1, double v2) {
        return Math.max(v1, v2);
    }

    public static float min(float v1, float v2, float v3) {
        return Math.min(Math.min(v1, v2), v3);
    }

    public static float max(float v1, float v2, float v3) {
        return Math.max(Math.max(v1, v2), v3);
    }

    public static double min(double v1, double v2, double v3) {
        return Math.min(Math.min(v1, v2), v3);
    }

    public static double max(double v1, double v2, double v3) {
        return Math.max(Math.max(v1, v2), v3);
    }

    /**
     *
     */
    public static float abs(float v) {
        return Math.abs(v);
    }

    public static float cast(double v) {
        return (float)v;
    }

    public static double abs(double v) {
        return Math.abs(v);
    }

    public static final int round(float v) {
        return Math.round(v);
    }

    public static final long round(double v) {
        return Math.round(v);
    }

    public static float add(float v1, float v2) {
        return v1 + v2;
    }

    public static double add(double v1, double v2) {
        return v1 + v2;
    }

    public static final boolean let(float v1, float v2) {
        return v1 - v2 <= 0f;
    }

    public static final boolean get(float v1, float v2) {
        return v1 - v2 >= 0f;
    }

    public static final boolean let(double v1, double v2) {
        return v1 - v2 <= 0d;
    }

    public static final boolean get(double v1, double v2) {
        return v1 - v2 >= 0d;
    }

    public static final float multiply(float v1, float v2) {
        return v1 * v2;
    }

    public static final float subtract(float v1, float v2) {
        return v1 - v2;
    }

    public static final double multiply(double v1, double v2) {
        return v1 * v2;
    }

    public static final double subtract(double v1, double v2) {
        return v1 - v2;
    }

    public static boolean between(float v, float min, float max) {
        return let(min, v) && let(v, max);
    }

    public static boolean between(float v, double min, double max) {
        return let(min, v) && let(v, max);
    }

    public static final float add(float v1, float v2, int digits) {
        final float v = v1 + v2; return scale(v, digits);
    }

    public static final float scale(final float v, final int digits) {
        float x = UNITS_1[digits]; return Math.round(v * x) / x;
    }

    public static final double scale(final double v, final int digits) {
        double x = UNITS_2[digits]; return Math.round(v * x) / x;
    }

    public static final float multiply(float v1, float v2, int digits) {
        final float v = v1 * v2; return scale(v, digits);
    }

    /**
     *
     */
    public static boolean isNegativeZero(float v) {
        return v == 0.0f && (1 / v < 0f);
    }

    public static boolean isNegativeZero(double v) {
        return v == 0.0d && (1 / v < 0d);
    }

    public static final String format(float v, final int digits) {
        return String.format(FORMATS[digits], v);
    }

    public static final String format(double v, final int digits) {
        return String.format(FORMATS[digits], v);
    }

    public static final String format1(float v, final int digits) {
        v = round(v * UNITS_1[digits]) / UNITS_1[digits];
        return String.format(FORMATS[digits], isNegativeZero(v) ? 0f : v);
    }

    public static final String format1(double v, final int digits) {
        v = round(v * UNITS_2[digits]) / UNITS_2[digits];
        return String.format(FORMATS[digits], isNegativeZero(v) ? 0d : v);
    }

    public static float trim(float v, final float min, final float max) {
        if(let(v, min)) return min; if(get(v, max)) return max; return v;
    }

    public static String limit(final int p, final int s, boolean positive) {
        StringBuilder r = new StringBuilder(positive ? "" : "-");
        for(int i = 0; i < (p - s); i++) r.append('8');
        if(s > 0) r.append('.'); for(int i = 0; i < s; i++) r.append('8');
        return r.toString();
    }

    /**
     *
     */
    public static String getChartTime(final ChartType type, final XChart chart) {
        if(type.isIntraday()) {
            return DateTimes.format("yyyy-MM-dd HH:mm", chart.getChartTime());
        } else if(type.isInterday()) {
            return DateTimes.format("yyyy-MM-dd", chart.getChartDate().toSqlDate());
        } else {
            throw new AssertionException("failed to get chart time, invalid chart type: " + type);
        }
    }

    public static String getCoordinateTime(final ChartType type, final XChart chart) {
        String r = chart.getCookie(); if(r != null) return r;
        if(type.isIntraday()) {
            r = DateTimes.format("HH:mm", chart.getChartTime().getTime());
            chart.setCookie(r); return r;
        } else if(type.isInterday()) {
            r = DateTimes.format("MM/dd", chart.getChartDate().toSqlDate());
            chart.setCookie(r); return r;
        } else {
            throw new AssertionException("failed to get chart time, invalid chart type: " + type);
        }
    }

    public static void swap(CGPoint p1,CGPoint p2){
        CGPoint temp = p1; p1 = p2; p2 = temp;
    }

    public static double distance(CGPoint p1,CGPoint p2){
        return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public static CGPoint getFootOfPerpendicular(CGPoint pt,CGPoint begin,CGPoint end){
        CGPoint retVal = new CGPoint(-1d,-1d);
        double dx = begin.x - end.x;
        double dy = begin.y - end.y;
        if(Math.abs(dx) < 0.00000001 && Math.abs(dy) < 0.00000001 ) {
            retVal = begin;
            return retVal;
        }
        double u = (pt.x - begin.x) * (begin.x - end.x) + (pt.y - begin.y) * (begin.y - end.y);
        u = u / ((dx * dx) + (dy * dy));
        retVal.x = begin.x + u * dx;
        retVal.y = begin.y + u * dy;
        return retVal;
    }

    /**
     *
     */
    public static Rect toRect1(float left, float top, float right, float bottom) {
        return new Rect(round(left), round(top), round(right), round(bottom));
    }

    public static Rect toRect2(float left, float top, float width, float height) {
        return new Rect(round(left), round(top), round(left + width), round(top + height));
    }

    public static final Rect getTextBounds(final String text, final Paint paint) {
        final Rect r = new Rect(); paint.getTextBounds(text, 0, text.length(), r); return r;
    }

    /**
     *
     */
    public static void drawLines(final XChartLines l, Canvas canvas, Paint paint, boolean reset) {
        if(l == null || l.getIndex() == 0) return;
        try { canvas.drawLines(l.getPoints(), 0, l.getIndex(), paint); } finally { if(reset) l.reset(); }
    }

    public static void drawLines(XChartLines l1, XChartLines l2, Canvas canvas, Paint paint, boolean reset) {
        if(l1 != null) drawLines(l1, canvas, paint, reset); if(l2 != null) drawLines(l2, canvas, paint, reset);
    }

    /**
     *
     */
    public static void drawRect(float l, double t, float r, double b, Canvas canvas, Paint paint) {
        canvas.drawRect(cast(l), cast(t), cast(r), cast(b), paint);
    }

    public static void drawLine(float x1, double y1, float x2, double y2, Canvas canvas, Paint paint) {
        canvas.drawLine(cast(x1), cast(y1), cast(x2), cast(y2), paint);
    }

    public static void drawText(String text, float x, float y, Align align, Canvas canvas, Paint paint) {
        Align prev = paint.getTextAlign(); paint.setTextAlign(align);
        canvas.drawText(text, x, y, paint); paint.setTextAlign(prev);
    }

    public static void drawText(final String text, float x, boolean xc, float y, boolean yc, Canvas canvas, Paint paint) {
        final Rect r = getTextBounds(text, paint);
        if(xc) x = x - r.width() * 0.5f; if(yc) y = y + r.height() * 0.5f; canvas.drawText(text, x, y, paint);
    }

    public static Rect drawText(Rect prev, String text, float x, boolean xc, float y, boolean yc, Canvas canvas, Paint paint) {
        final Rect r = getTextBounds(text, paint);
        if(xc) x = x - r.width() * 0.5f; if(yc) y = y + r.height() * 0.5f;
        final Rect next = new Rect(round(x), round(y), round(x + r.width()), round(y + r.height()));
        if(prev == null || !prev.intersect(next)) { canvas.drawText(text, x, y, paint); return next; } else return null;
    }

    public static void drawText(String text, float x, float y, Align align, CGSize padding, boolean bounded, Canvas canvas, Paint paint) {
        Align prev = paint.getTextAlign(); paint.setTextAlign(align);
        Rect r = getTextBounds(text,paint);
        float bx = x, by = y, br = 0, bb = 0;
        switch (paint.getTextAlign()) {
            case CENTER:
                x -= r.width()/2.0f;
                bx -= r.width()/2.0f + padding.width;
            break;
            case RIGHT:
                x -= padding.width;
                bx -= r.width() + padding.width * 2;
            break;
            case LEFT:
            default:
                x += padding.width;
                break;
        }
        y += padding.height - r.top;
        br = bx + r.width() + padding.width * 2;
        bb = by + r.height() + padding.height * 2;
        canvas.drawText(text, x, y, paint); paint.setTextAlign(prev);
        if (bounded) {
            Paint.Style prevMode = paint.getStyle(); paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(bx, by, br, bb, paint); paint.setStyle(prevMode);
        }
    }
}
