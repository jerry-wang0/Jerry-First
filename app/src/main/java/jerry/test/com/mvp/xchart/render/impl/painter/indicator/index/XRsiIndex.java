package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;


public class XRsiIndex extends AbstractXIndex {
    //
    private double[] line0;

    /**
     *
     */
    public XRsiIndex(XChartModel model) {
        super(model, IndicatorType.RSI);
    }

    /**
     *
     */
    @Override
    public int getDigits() {
        return 2;
    }

    @Override
    public int getPrecision() {
        return 4;
    }

    @Override
    protected final double getMin() {
        return 0d;
    }

    @Override
    protected final double getMax() {
        return 100d;
    }

    @Override
    public double getMin(boolean scale) {
        return getMin();
    }

    @Override
    public double getMax(boolean scale) {
        return getMax();
    }

    public void setLine0(double[] line0) {
        this.line0 = line0;
    }

    public double getLine0(final int i) {
        if(line0 == null) return Double.NaN;
        return (i < 0 || i >= line0.length) ? Double.NaN : line0[i];
    }
}
