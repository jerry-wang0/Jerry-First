package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static java.lang.Double.NaN;


public class XKdjIndex extends AbstractXIndex {
    //
    private double[] line0 = NULL; // FastK
    private double[] line1 = NULL; // FastD
    private double[] line2 = NULL; // SlowD

    /**
     *
     */
    public XKdjIndex(XChartModel model) {
        super(model, IndicatorType.KDJ);
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

    /**
     *
     */
    public void setLines(double[][] lines) {
        this.line0 = lines == null ? NULL : lines[0];
        this.line1 = lines == null ? NULL : lines[1];
        this.line2 = lines == null ? NULL : lines[2];
    }

    public double getLine0(final int i) {
        if(line0 == null) return NaN;
        return (i < 0 || i >= line0.length) ? NaN : line0[i];
    }

    public double getLine1(final int i) {
        if(line1 == null) return NaN;
        return (i < 0 || i >= line1.length) ? NaN : line1[i];
    }

    public double getLine2(final int i) {
        if(line2 == null) return NaN;
        return (i < 0 || i >= line2.length) ? NaN : line2[i];
    }
}
