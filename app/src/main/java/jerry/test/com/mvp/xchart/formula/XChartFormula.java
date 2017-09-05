package jerry.test.com.mvp.xchart.formula;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import static com.tictactec.ta.lib.MAType.Ema;
import static com.tictactec.ta.lib.MAType.Sma;
import static java.lang.Float.NaN;


public class XChartFormula {
    //
    private static final Core CORE = new Core();
    private static final double[] NULL = new double[0];

    /**
     *
     */
    protected static final double[] trim(double[] values, int limit, int length) {
        final double[] r = new double[length];
        if(limit <= length) {
            System.arraycopy(values, 0, r, length - limit, limit); return r;
        } else {
            System.arraycopy(values, limit - length, r, 0, length); return r;
        }
    }

    protected static final double[][] trim(double[][] values, int limit, int length) {
        final double[][] r = new double[values.length][];
        for(int i = 0; i < values.length; i++) r[i] = trim(values[i], limit, length);
        return r;
    }

    /**
     *
     */
    protected static final double[] trim(double[] values, int limit, int length, double padding) {
        final double[] r = new double[length];
        if(limit <= length) {
            for(int i = 0, s = length - limit; i < s; i++) r[i] = padding;
            System.arraycopy(values, 0, r, length - limit, limit); return r;
        } else {
            for(int i = length; i < r.length; i++) r[i] = padding;
            System.arraycopy(values, limit - length, r, 0, length); return r;
        }
    }

    protected static final double[][] trim(double[][] values, int limit, int length, double padding) {
        final double[][] r = new double[values.length][];
        for(int i = 0; i < values.length; i++) r[i] = trim(values[i], limit, length, padding);
        return r;
    }

    /**
     * MA
     * Primary
     */
    public static double[] sma(final float[] close, final int from, final int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.sma(from, to, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length) : NULL;
    }

    public static double[] ema(final float[] close, final int from, final int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.ema(from, to, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length) : NULL;
    }

    /**
     * RSI
     * Secondary(0 ~ 100)
     */
    public static double[] rsi(final float[] close, final int from, final int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        final RetCode rc = CORE.rsi(from, to, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : NULL;
    }

    /**
     * DMI
     * Secondary(0 ~ 100)
     */
    public static double[] adx(float[] low, float[] high, float[] close, int from, int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.adx(from, to, high, low, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : NULL;
    }

    public static double[] adxr(float[] low, float[] high, float[] close, int from, int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.adxr(from, to, high, low, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : NULL;
    }

    public static double[] mdi(float[] low, float[] high, float[] close, int from, int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.minusDI(from, to, high, low, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : NULL;
    }

    public static double[] pdi(float[] low, float[] high, float[] close, int from, int to, int period, int length) {
        final double[] r = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.plusDI(from, to, high, low, close, period, offset, limit, r);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : NULL;
    }

    /**
     * BOLL
     * Primary
     */
    public static double[][] boll(final float[] close, final int from, final int to, int period, int delta, int length) {
        final double[][] r = new double[3][];
        for (int i = 0; i < 3; i++) r[i] = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.bbands(from, to, close, period, delta, delta, Sma, offset, limit, r[2], r[1], r[0]);
        return rc == RetCode.Success ? trim(r, limit.value, length) : null;
    }

    /**
     * MACD
     * Secondary(-1 ~ +1)
     */
    public static double[][] macd(final float[] close, final int from, int to, int slow, int fast, int signal, int length) {
        final double[][] r = new double[3][];
        for (int i = 0; i < 3; i++) r[i] = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        RetCode rc = CORE.macdExt(from, to, close, fast, Ema, slow, Ema, signal, Ema, offset, limit, r[0], r[2], r[1]);
        return rc == RetCode.Success ? trim(r, limit.value, length, Double.NaN) : null;
    }

    /**
     * Stochastic
     * Secondary(0 ~ 100)
     */
    public static double[][] kdj(float[] low, float[] high, float[] close, int from, int to, int fk, int fd, int sd, int length) {
        double[][] r = new double[3][];
        for (int i = 0; i < 3; i++) r[i] = new double[to - from + 1];
        final MInteger offset = new MInteger(), limit = new MInteger();
        final RetCode rc1 = CORE.stochF(from, to, high, low, close, fk, fd, Sma, offset, limit, r[0], r[1]);
        if(rc1 != RetCode.Success) {
            return null;
        } else {
            final double sk[] = new double[to - from + 1];
            r[0] = trim(r[0], limit.value, length, NaN); r[1] = trim(r[1], limit.value, length, NaN);
            RetCode rc2 = CORE.stoch(from, to, high, low, close, fk, fd, Sma, sd, Sma, offset, limit, sk, r[2]);
            if (rc2 != RetCode.Success) return null; r[2] = trim(r[2], limit.value, length, Double.NaN); return r;
        }
    }
}
