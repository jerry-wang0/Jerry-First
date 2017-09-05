package jerry.test.com.mvp.xchart.formula.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class IKH {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(IKH.class);

    /**
     *
     */
    protected static double mid(double v1, double v2) {
        return (v1 <= 0d || v2 <= 0d) ? 0d : (v1 + v2) / 2;
    }

    protected static double left(float[] close, int index, int limit) {
        final int i = index + limit;
        return i < close.length ? close[i] : 0d;
    }

    protected static double low(final float[] low, int index, int limit) {
        if(index < limit) return 0d; double r = Double.MAX_VALUE;
        for(int i = 0; i <= limit; i++) r = Math.min(r, low[index - i]);
        return r;
    }

    protected static double high(final float[] high, int index, int limit) {
        if(index < limit) return 0d; double r = Double.MIN_VALUE;
        for(int i = 0; i <= limit; i++) r = Math.max(r, high[index - i]);
        return r;
    }

    protected static void right(double[] values, double value, int index, int limit) {
        if(index + limit < values.length) values[index + limit] = value;
    }

    /**
     *
     */
    public static double[][] ikh(float[] low, float[] high, float[] close, int p0, int p1, int p2, int size, int right, int length) {
        //
        final double[][] r = new double[5][];
        final int offset = close.length - size - right;
        for(int i = 0; i < r.length; i++) r[i] = new double[length];

        //
        for(int i = 0; i < size; i++) {
            final int j = offset + i;
            r[2][i] = left(close, j, p1 - 1);
            r[0][i] = mid(low(low, j, p0 - 1), high(high, j, p0 - 1));
            r[1][i] = mid(low(low, j, p1 - 1), high(high, j, p1 - 1));
            if(i + p1 - 1 < r[3].length) r[3][i + p1 - 1] = mid(r[0][i], r[1][i]);
            if(i + p1 - 1 < r[4].length) r[4][i + p1 - 1] = mid(low(low, j, p2 - 1), high(high, j, p2 - 1));
        }

        //
        for(int i = -p1 + 1; i < 0; i++) {
            final int j = offset + i;
            r[4][i + p1 - 1] = mid(low(low, j, p2 - 1), high(high, j, p2 - 1));
            r[3][i + p1 - 1] = mid(mid(low(low, j, p0 - 1), high(high, j, p0 - 1)), mid(low(low, j, p1 - 1), high(high, j, p1 - 1)));
        }
        return r;
    }
}
