package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

/**
 * Created by Jingqi Xu on 9/10/15.
 */
public class XDmiIndex extends AbstractXIndex {
    //
    private double[] line0;
    private double[] line1;
    private double[] line2;
    private double[] line3;

    /**
     *
     */
    public XDmiIndex(XChartModel model) {
        super(model, IndicatorType.DMI);
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

    public void setLine1(double[] line1) {
        this.line1 = line1;
    }

    public void setLine2(double[] line2) {
        this.line2 = line2;
    }

    public void setLine3(double[] line3) {
        this.line3 = line3;
    }

    public double getLine0(final int i) {
        if(line0 == null) return Double.NaN;
        return (i < 0 || i >= line0.length) ? Double.NaN : line0[i];
    }

    public double getLine1(final int i) {
        if(line1 == null) return Double.NaN;
        return (i < 0 || i >= line1.length) ? Double.NaN : line1[i];
    }

    public double getLine2(final int i) {
        if(line2 == null) return Double.NaN;
        return (i < 0 || i >= line2.length) ? Double.NaN : line2[i];
    }

    public double getLine3(final int i) {
        if(line3 == null) return Double.NaN;
        return (i < 0 || i >= line3.length) ? Double.NaN : line3[i];
    }
}
