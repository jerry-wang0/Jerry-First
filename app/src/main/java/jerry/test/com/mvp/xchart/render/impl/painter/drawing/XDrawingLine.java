package jerry.test.com.mvp.xchart.render.impl.painter.drawing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.glossary.chart.XSegment;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class XDrawingLine implements XSegment {
    protected boolean highlighted;
    protected XChartModel model;
    protected XDrawingStyle style;
    public List<XDrawingDepot> depots = new ArrayList();
    public List<XDrawingSegment> segments = new ArrayList();
    private double[] MULTIPLE_PERCENTAGE = {0.236, 0.382, 0.5, 0.618};

    public XDrawingLine(XChartModel model,XDrawingStyle style){
        this.model = model;
        this.style = style;
    }

    public XDrawingStyle getStyle() {
        return style;
    }

    public void setStyle(XDrawingStyle style) {
        this.style = style;
    }

    public boolean highlighted(){
        boolean r = false;
        for(XDrawingDepot depot:depots){
            r = r || depot.highlighted;
        }
        return r;
    }

    public void unhighlight(){
        for (XDrawingDepot depot:depots){
            depot.highlighted = false;
        }
    }

    public void addDepot(XDrawingDepot depot){
        depot.parent = this;
        this.depots.add(depot);
        if (this.style == XDrawingStyle.SINGLE) {
            if (depots.size() == 2) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(SegmentType.kTypeReal);
                s.setExtendableType(SegmentExtendable.kForward);
                s.setStartDepot(this.depots.get(0));
                s.setEndDepot(this.depots.get(1));
                this.segments.add(s);
            }
        }else if (this.style == XDrawingStyle.DOUBLE) {
            if (depots.size() == 2) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(SegmentType.kTypeReal);
                s.setExtendableType(SegmentExtendable.kForward);
                s.setStartDepot(this.depots.get(0));
                s.setEndDepot(this.depots.get(1));
                this.segments.add(s);
            } else if (depots.size() == 3) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(false);
                s.setType(SegmentType.kTypeVirtual);
                s.setExtendableType(SegmentExtendable.kNone);
                s.setStartDepot(this.depots.get(1));
                s.setEndDepot(this.depots.get(2));
                this.segments.add(s);
                s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(SegmentType.kTypeReal);
                s.setExtendableType(SegmentExtendable.kReverse);
                s.setStartDepot(this.depots.get(2));
                XDrawingDepot d = new XDrawingDepot(this.model);
                d.xChart = this.depots.get(0).xChart;
                d.yPrice = this.depots.get(0).yPrice + (this.depots.get(2).yPrice - this.depots.get(1).yPrice);
                s.setEndDepot(d);
                this.segments.add(s);
            }
        }else if (this.style == XDrawingStyle.HORIZONTAL ||
                this.style == XDrawingStyle.VERTICAL) {
            if (depots.size() == 1) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(this.style == XDrawingStyle.HORIZONTAL ? SegmentType.kTypeInfiniteH : SegmentType.kTypeInfiniteV);
                s.setExtendableType(SegmentExtendable.kNone);
                s.setStartDepot(this.depots.get(0));
                this.segments.add(s);
            }
        } else if (this.style == XDrawingStyle.HORIZONMULTIPLE) {
            if (depots.size() == 1) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(SegmentType.kTypeInfiniteH);
                s.setExtendableType(SegmentExtendable.kNone);
                s.setStartDepot(this.depots.get(0));
                s.setGraduation("0%");
                this.segments.add(s);
            } else if (depots.size() == 2) {
                XDrawingSegment s = new XDrawingSegment(model);
                s.setDraggable(true);
                s.setType(SegmentType.kTypeInfiniteH);
                s.setExtendableType(SegmentExtendable.kNone);
                s.setStartDepot(this.depots.get(1));
                s.setGraduation("100%");
                this.segments.add(s);

                double diff = this.depots.get(1).yPrice - this.depots.get(0).yPrice;
                for (int i = 0; i < 4; i++) {
                    double p = MULTIPLE_PERCENTAGE[i];
                    double price = this.depots.get(0).yPrice + diff * p;
                    XDrawingDepot d = new XDrawingDepot(this.model);
                    d.xChart = this.depots.get(0).xChart;
                    d.yPrice = price;
                    s = new XDrawingSegment(model);
                    s.setDraggable(false);
                    s.setType(SegmentType.kTypeInfiniteH);
                    s.setExtendableType(SegmentExtendable.kNone);
                    s.setStartDepot(d);
                    s.setGraduation(formatPrice(new BigDecimal(p * 100), 1, "-"));
                    this.segments.add(s);
                }
            }
        }
    }

    public static final String formatPrice(final BigDecimal value, final int scale, final String dft) {
        if(value == null || value.compareTo(ZERO) <= 0) return dft;
        return value.setScale(scale, HALF_UP).toPlainString();
    }

    public boolean isValid(){
        if (this.style == XDrawingStyle.HORIZONTAL || this.style == XDrawingStyle.VERTICAL) {
            return this.depots.size() > 0;
        } else if (this.style == XDrawingStyle.SINGLE || this.style == XDrawingStyle.HORIZONMULTIPLE) {
            return this.depots.size() > 1;
        } else if (this.style == XDrawingStyle.DOUBLE) {
            return this.depots.size() > 2;
        }
        return false;
    }
}
