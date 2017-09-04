package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Double.NaN;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Jingqi Xu on 9/9/15.
 */
public class XMacdIndex extends AbstractXIndex {
    //
    private double[] line0 = NULL; // MACD
    private double[] line1 = NULL; // Diff
    private double[] line2 = NULL; // Signal
    private double min = MAX_VALUE;
    private double max = MIN_VALUE;

    /**
     *
     */
    public XMacdIndex(XChartModel model) {
        super(model, IndicatorType.MACD);
    }

    /**
     *
     */
    @Override
    protected final double getMin() {
        if(this.min != MAX_VALUE) {
            return this.min;
        } else {
            traverse(); return this.min;
        }
    }

    @Override
    protected final double getMax() {
        if(this.max != MIN_VALUE) {
            return this.max;
        } else {
            traverse(); return this.max;
        }
    }

    protected final void traverse() {
        //
        double max = MIN_VALUE, min = MAX_VALUE;
        for(int i = 0; i < line0.length; i++) {
            final double v = line0[i];
            if(Double.isNaN(v)) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line1.length; i++) {
            final double v = line1[i];
            if(Double.isNaN(v)) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line2.length; i++) {
            final double v = line2[i];
            if(Double.isNaN(v)) continue;
            min = min(min, v); max = max(max, v);
        }

        //
        this.min = min(min, 0f); this.max = max(max, 0f);
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
