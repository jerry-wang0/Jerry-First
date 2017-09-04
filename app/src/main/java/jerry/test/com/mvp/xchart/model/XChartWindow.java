package jerry.test.com.mvp.xchart.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.util.Objects;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.add;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.let;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.max;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_Y_MARGIN;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public class XChartWindow {
    //
    protected int last;
    protected int first;
    protected XChartModel model;
    protected double min1 = MAX_VALUE;
    protected double max1 = MIN_VALUE;
    protected double min2 = MAX_VALUE;
    protected double max2 = MIN_VALUE;
    protected Map<IndicatorType, XChartIndex> indice;

    /**
     *
     */
    public XChartWindow(XChartModel model, int limit) {
        this.model = model;
        this.last = model.getPivot();
        this.indice = new HashMap<>(8);
        this.first = Math.max(0, this.last - limit + 1);
    }

    /**
     *
     */
    public final int size() {
        return last - first + 1;
    }

    public final int getLast() {
        return last;
    }

    public final int getFirst() {
        return first;
    }

    public final int getMargin() {
        return model.getMargin().getSize();
    }

    public final boolean isEmpty() {
        return size() == 0 && getMargin() == 0;
    }

    /**
     *
     */
    public XChart getChart(final int offset) {
        //
        int i = 0;
        if(offset >= 0) {
            i = this.first + offset;
        } else {
            i = this.last + offset + 1;
        }

        //
        int size = this.model.getCharts().size();
        if(i < 0 || i >= size) {
            return null;
        } else {
            return this.model.getCharts().get(i);
        }
    }

    /**
     *
     */
    public final void addIndex(final XChartIndex index) {
        this.indice.put(index.getType(), index);
        if(index.getType().getArea() == IndicatorArea.A) {
            this.min1 = min(this.min1, index.getMin(false));
            this.max1 = max(this.max1, index.getMax(false));
        }
    }

    public <T extends XChartIndex> T getIndex(IndicatorType t) {
        return Objects.cast(this.indice.get(t));
    }

    /**
     *
     */
    public double getMin(boolean scale) {
        if(!scale) {
            return this.min1;
        } else {
            if(min2 == MAX_VALUE) scale(); return min2;
        }
    }

    public double getMax(boolean scale) {
        if(!scale) {
            return this.max1;
        } else {
            if(max2 == MIN_VALUE) scale(); return max2;
        }
    }

    protected final void scale() {
        final XChartTheme theme = model.getTheme();
        final int digits = model.getConfig().getDigits();
        final float s = (float) Math.pow(10f, -1 * digits);
        final float m = theme.getProperty(COORDINATE_Y_MARGIN, 0.05f);
        double d = XChartUtils.multiply(subtract(this.max1, this.min1), m);
        if(let(d, s)) d = s; this.min2 = subtract(min1, d); this.max2 = add(max1, d);
    }

    /**
     *
     */
    public final Iterator<XChart> iterate(final boolean asc) {
        if(asc) {
            return new Iterator<XChart>() {
                int index = first;
                @Override
                public boolean hasNext() { return this.index <= last; }
                @Override
                public XChart next() { return model.getChart(this.index++); }
                @Override
                public void remove() { throw new UnsupportedOperationException(); }
            };
        } else {
            return new Iterator<XChart>() {
                int index = last;
                @Override
                public boolean hasNext() { return this.index >= first; }
                @Override
                public XChart next() { return model.getChart(this.index--); }
                @Override
                public void remove() { throw new UnsupportedOperationException(); }
            };
        }
    }
}
