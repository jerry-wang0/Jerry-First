package jerry.test.com.mvp.xchart.render.impl.painter.indicator.index;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Jingqi Xu on 9/9/15.
 */
public class XBollIndex extends AbstractXIndex {
    //
    private double[] line0;
    private double[][] line1;
    private double[][] line2;
    private double[][] line3;
    private double min = MAX_VALUE;
    private double max = MIN_VALUE;

    /**
     *
     */
    public XBollIndex(XChartModel model) {
        super(model, IndicatorType.BOLL);
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
        if(line3 != null) return traverse(line3);
        if(line2 != null) return traverse(line2);
        if(line1 != null) return traverse(line1);
        return false;
    }

    protected boolean traverse(double[][] line) {
        double max = MIN_VALUE, min = MAX_VALUE;
        for(int i = 0; i < line.length; i++) {
            for(int j = 0; j < line[i].length; j++) {
                double v = line[i][j]; if(v > 0d) {
                    min = min(min, v); max = max(max, v);
                }
            }
        }
        this.min = min; this.max = max; return true;
    }

    /**
     *
     */
    public void setLine1(double[][] line1) {
        line0 = line1[1]; this.line1 = new double[2][];
        this.line1[0] = line1[0]; this.line1[1] = line1[2];
    }

    public void setLine2(double[][] line2) {
        line0 = line2[1]; this.line2 = new double[2][];
        this.line2[0] = line2[0]; this.line2[1] = line2[2];
    }

    public void setLine3(double[][] line3) {
        line0 = line3[1]; this.line3 = new double[2][];
        this.line3[0] = line3[0]; this.line3[1] = line3[2];
    }

    /**
     *
     */
    public double getLine0(int i) {
        if(line0 == null) return 0d;
        return (i < 0 || i >= line0.length) ? 0d : line0[i];
    }

    public double getLine1(int type, int i) {
        if(line1 == null) return 0d;
        return (i < 0 || i >= line1[type].length) ? 0d : line1[type][i];
    }

    public double getLine2(int type, int i) {
        if(line2 == null) return 0d;
        return (i < 0 || i >= line2[type].length) ? 0d : line2[type][i];
    }

    public double getLine3(int type, int i) {
        if(line3 == null) return 0d;
        return (i < 0 || i >= line3[type].length) ? 0d : line3[type][i];
    }
}
