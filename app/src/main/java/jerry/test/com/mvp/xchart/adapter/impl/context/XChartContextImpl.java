package jerry.test.com.mvp.xchart.adapter.impl.context;

import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGPoint;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingDepot;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingSegment;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.util.Objects;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_TOUCH_SENSITIVITY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type;

/**
 * Created by Jingqi Xu on 9/8/15.
 */
public class XChartContextImpl implements XChartContext {
    //
    private XChartAction action;
    private XChartAdapter adapter;

    /**
     *
     */
    public XChartContextImpl(XChartAdapter adapter, XChartAction action) {
        this.action = action;
        this.adapter = adapter;
    }

    /**
     *
     */
    @Override
    public XChartView getView() {
        return adapter.getView();
    }

    @Override
    public XChartModel getModel() {
        return adapter.getModel();
    }

    @Override
    public XChartRender getRender() {
        return adapter.getRender();
    }

    @Override
    public XChartAdapter getAdapter() {
        return this.adapter;
    }

    @Override
    public <T extends XChartAction> T getAction() {
        return Objects.cast(this.action);
    }

    @Override
    public XChart getChartWithX(float x){
        //if (x == null || isnan(*x)) return nil;
        if (!getModel().hasWindow()) return null;
        //if (window == nil || window.isEmpty) return nil;

        XChartModel model = getModel();
        XChartWindow window = model.getWindow();
        XChartRender render = getRender();
        XChartArea a = render.getArea(Type.A);//[render getArea:CHARTAREA_TYPE_A];
        int cursor = -1;
        if (x < 0.0f) { return null; }

        ChartScale scale = model.getConfig().getScale();
        int count = scale.count(a.getWidth());
        float deltaX = a.getRight() - x;
        cursor = (int) Math.round(deltaX / scale.getWidth() + 0.5);
        if(cursor < 1) cursor = 1; else if(cursor > count) cursor = count;
        int index = cursor - window.getMargin();
        XChart chart = index <= 0 ? null : window.getChart(-1 * index);//[window getChart:-1 * index];
        x = a.getRight() - scale.getWidth() * index + scale.getCenter();
        return chart;
    }

    @Override
    public double getPriceWithY(float y){
        //if (isnan(y)) return nan(NULL);
        //if (!self.getModel.hasWindow) return nan(NULL);
        if(!getModel().hasWindow()) return -1f;
        XChartModel model = getModel();
        XChartWindow window = model.getWindow();
        XChartRender render = getRender();
        XChartArea a = render.getArea(Type.A);//[render getArea:CHARTAREA_TYPE_A];
        float ab = a.getBottom();
        double minA = window.getMin(true), maxA = window.getMax(true);
        double unitA = XChartUtils.subtract(maxA,minA)/a.getNetHeight();
        double price = XChartUtils.add((ab - y) * unitA,minA);
        return price > 0 ? price : -1f;
    }
    @Override
    public double getXWithChart(XChart chart){
        if(chart == null || !getModel().hasWindow()) return -1f;
        //if (chart == nil) return nan(NULL);
        //if (!self.getModel.hasWindow) return nan(NULL);

        XChartModel model = getModel();
        XChartWindow window = model.getWindow();
        XChartRender render = getRender();
        ChartScale scale = model.getConfig().getScale();
        XChartArea a = render.getArea(Type.A);
        List<XChart> charts = model.getCharts();
        int index = charts.indexOf(chart);//[charts indexOfObject:chart];
        if (index < 0 || index >= charts.size()) return -1f;

        float ar = a.getRight();
        float sw = scale.getWidth(), sc = scale.getCenter();
        float war = ar - window.getMargin() * scale.getWidth();
        float x = war - (window.getLast() - index + 1) * sw + sc;
        return x;
    }

    @Override
    public double getYWithPrice(double price){
        //if (isnan(price)) return nan(NULL);
        //if (!self.getModel.hasWindow) return nan(NULL);
        if(!getModel().hasWindow()) return -1f;

        XChartModel model = getModel();
        XChartWindow window = model.getWindow();
        XChartRender render = getRender();
        XChartArea a = render.getArea(Type.A);
        float ab = a.getBottom();
        double minA = window.getMin(true), maxA = window.getMax(true);
        double y = ab - XChartUtils.subtract(price,minA) / XChartUtils.subtract(maxA,minA)* a.getNetHeight();//[XChartUtils subtract:maxA v2:minA] * a.getNetHeight;
        return y;
    }

    @Override
    public XDrawingDepot getDepot(float x,float y){
        double price = getPriceWithY(y);
        XChartModel model = getModel();
        XChartWindow window = model.getWindow();
        XChartRender render = getRender();
        XChartArea a = render.getArea(Type.A);
        XChartTheme theme = model.getTheme();

        double minA = window.getMin(true), maxA = window.getMax(true);
        double unitA = XChartUtils.subtract(maxA,minA)/a.getNetHeight();
        float s = theme.getProperty(DRAWING_TOUCH_SENSITIVITY,20f);
        List<XDrawingLine> lines = model.getLines();
        for (XDrawingLine l : lines) {
            for (XDrawingDepot d : l.depots) {
                double dx = getXWithChart(d.getxChart());
                if (Math.abs(dx - x) < s && Math.abs(d.getyPrice() - price) < unitA * s) {
                    return d;
                }
            }
        }
        return null;
    }

    @Override
    public XDrawingSegment getSegment(float x,float y){
        XChart chart = getChartWithX(x);
        double price = getPriceWithY(y);
        XChartModel model = getModel();
        XDrawingDepot target = new XDrawingDepot(model);
        target.setxChart(chart);
        target.setyPrice(price);

        XChartTheme theme = model.getTheme();
        float s = theme.getProperty(DRAWING_TOUCH_SENSITIVITY, 20f);
        List<XDrawingLine> lines = model.getLines();
        for (XDrawingLine l : lines) {
//        XDrawingSegment *r = [l getSegmentNearDepot:target vOffset:unitA * [s doubleValue]];
            XDrawingSegment r = getSegmentNearDepot(target, l, s);
            if (r != null) { return r; }
        }
        return null;
    }

    public XDrawingSegment getSegmentNearDepot(XDrawingDepot target,XDrawingLine l,double v){
        if (l.isValid()) {
            for (XDrawingSegment segment : l.segments) {
                if (segment.isDraggable()) {
                    switch (segment.getType()) {
                        case kTypeInfiniteH:
                        {
                            double y1 = getYWithPrice(segment.getStartDepot().getyPrice());
                            double y0 = getYWithPrice(target.getyPrice());
                            if (Math.abs(y0 - y1) <= v) {
                                return segment;
                            }
                            break;
                        }
                        case kTypeInfiniteV:
                        {
                            double x0 = getXWithChart(target.getxChart());
                            double x1 = getXWithChart(segment.getStartDepot().getxChart());
                            if (Math.abs(x0 - x1) <= v) {
                                return segment;
                            }
                            break;
                        }
                        case kTypeReal:
                        {
                            // Referred to the webpage: http://blog.csdn.net/zhouschina/article/details/14647587
                            double x1 = getXWithChart(segment.getStartDepot().getxChart());
                            double y1 = getYWithPrice(segment.getStartDepot().getyPrice());
                            double x2 = getXWithChart(segment.getEndDepot().getxChart());
                            double y2 = getYWithPrice(segment.getEndDepot().getyPrice());
                            double x0 = getXWithChart(target.getxChart());
                            double y0 = getYWithPrice(target.getyPrice());
                            CGPoint pn = XChartUtils.getFootOfPerpendicular(new CGPoint(x0,y0),new CGPoint(x1,y1),new CGPoint(x2,y2));
                            double xn = pn.x;
                            double yn = pn.y;
                            if (xn >= Math.min(x1, x2) && xn <= Math.max(x1, x2) && yn >= Math.min(y1, y2) && yn <= Math.max(y1, y2)) {
                                double d = XChartUtils.distance(new CGPoint(x0, y0),new CGPoint(xn, yn));
                                if (d <= v) { return segment; }
                            }
                            break;
                        }
                        case kTypeVirtual:
                        default:
                            break;
                    }
                }
            }
        }
        return null;
    }
}
