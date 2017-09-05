package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartIndex;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.add;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.let;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_Y_MARGIN;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;


public abstract class AbstractXIndex implements XChartIndex {
    //
    protected static final double[] NULL = new double[0];

    //
    private double min = MAX_VALUE;
    private double max = MIN_VALUE;
    protected final XChartModel model;
    protected final IndicatorType type;

    //
    protected abstract double getMin();
    protected abstract double getMax();

    /**
     *
     */
    public AbstractXIndex(XChartModel model, IndicatorType type) {
        this.model = model; this.type = type;
    }

    /**
     *
     */

    @Override
    public float getUnit() {
        return 1f;
    }

    public final XChartModel getModel() {
        return model;
    }

    @Override
    public final IndicatorType getType() {
        return this.type;
    }

    @Override
    public int getDigits() {
        return model.getConfig().getDigits();
    }

    @Override
    public int getPrecision() {
        return model.getConfig().getPrecision();
    }

    /**
     *
     */
    @Override
    public double getMin(final boolean scale) {
        if(!scale) {
            return getMin();
        } else {
            if(min == MAX_VALUE) scale(); return min;
        }
    }

    @Override
    public double getMax(final boolean scale) {
        if(!scale) {
            return getMax();
        } else {
            if(max == MIN_VALUE) scale(); return max;
        }
    }

    /**
     *
     */
    protected void scale() {
        final int digits = getDigits();
        this.min = getMin(); this.max = getMax();
        final XChartTheme theme = model.getTheme();
        final float s = (float) Math.pow(10f, -1 * digits);
        final float m = theme.getProperty(COORDINATE_Y_MARGIN, 0.05f);
        double d = XChartUtils.multiply(subtract(max, min), m);
        if(let(d, s)) d = s; this.min = subtract(min, d); this.max = add(max, d);
    }
}
