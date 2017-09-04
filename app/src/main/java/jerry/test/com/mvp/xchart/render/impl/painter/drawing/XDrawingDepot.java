package jerry.test.com.mvp.xchart.render.impl.painter.drawing;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_LIGHT_GRAY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_YELLOW;

/**
 * Created by wanggl on 2016/11/29.
 */
public class XDrawingDepot {

    public boolean highlighted;
    protected XChart xChart;
    protected double yPrice;
    protected XDrawingLine parent;
    protected XChartModel model;

    public XDrawingDepot(XChartModel model){
        this.model = model;
    }

    public int getColor(boolean selected){
        return selected ? COLOR_YELLOW : COLOR_LIGHT_GRAY;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public XChart getxChart() {
        return xChart;
    }

    public void setxChart(XChart xChart) {
        this.xChart = xChart;
    }

    public double getyPrice() {
        return yPrice;
    }

    public void setyPrice(double yPrice) {
        this.yPrice = yPrice;
    }

    public XDrawingLine getParent() {
        return parent;
    }

    public void setParent(XDrawingLine parent) {
        this.parent = parent;
    }
}
