package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;


public class XIkhIndex extends AbstractXIndex {
    //
    private double[] line0 = NULL;
    private double[] line1 = NULL;
    private double[] line2 = NULL;
    private double[] line3 = NULL;
    private double[] line4 = NULL;
    private double min = MAX_VALUE;
    private double max = MIN_VALUE;

    /**
     *
     */
    public XIkhIndex(XChartModel model) {
        super(model, IndicatorType.IKH);
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

    protected final boolean traverse() {
        double max = MIN_VALUE, min = MAX_VALUE;
        for(int i = 0; i < line0.length; i++) {
            double v = line0[i];
            if(v <= 0d) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line1.length; i++) {
            double v = line1[i];
            if(v <= 0d) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line2.length; i++) {
            double v = line2[i];
            if(v <= 0d) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line3.length; i++) {
            double v = line3[i];
            if(v <= 0d) continue;
            min = min(min, v); max = max(max, v);
        }
        for(int i = 0; i < line4.length; i++) {
            double v = line4[i];
            if(v <= 0d) continue;
            min = min(min, v); max = max(max, v);
        }
        this.min = min; this.max = max; return true;
    }

    /**
     *
     */
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

    public void setLine4(double[] line4) {
        this.line4 = line4;
    }

    public void setLines(double[][] lines) {
        this.line0 = lines == null ? NULL : lines[0];
        this.line1 = lines == null ? NULL : lines[1];
        this.line2 = lines == null ? NULL : lines[2];
        this.line3 = lines == null ? NULL : lines[3];
        this.line4 = lines == null ? NULL : lines[4];
    }

    public double getLine0(final int i) {
        if(line0 == null) return 0d;
        return (i < 0 || i >= line0.length) ? 0d : line0[i];
    }

    public double getLine1(final int i) {
        if(line1 == null) return 0d;
        return (i < 0 || i >= line1.length) ? 0d : line1[i];
    }

    public double getLine2(final int i) {
        if(line2 == null) return 0d;
        return (i < 0 || i >= line2.length) ? 0d : line2[i];
    }

    public double getLine3(final int i) {
        if(line3 == null) return 0d;
        return (i < 0 || i >= line3.length) ? 0d : line3[i];
    }

    public double getLine4(final int i) {
        if(line4 == null) return 0d;
        return (i < 0 || i >= line4.length) ? 0d : line4[i];
    }
}
