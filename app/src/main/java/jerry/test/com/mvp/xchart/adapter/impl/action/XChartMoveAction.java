package jerry.test.com.mvp.xchart.adapter.impl.action;

import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGPoint;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartMargin;
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
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.util.Objects;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.round;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.MOVE_SPEED;

/**
 * Created by Jingqi Xu on 8/28/15.
 */
public class XChartMoveAction extends AbstractAction {
    //
    protected float x,y;
    private double[] MULTIPLE_PERCENTAGE = {0.236, 0.382, 0.5, 0.618};
    /**
     *
     */
    public XChartMoveAction(float x,float y) {
        super(Type.MOVE); this.x = x;this.y = y;
    }

    /**
     * prev.merge(next)
     * @see cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter
     */
    @Override
    public XChartAction merge(XChartAction action) {
        if(action.getType() != getType()) return null;
        final XChartMoveAction rhs = Objects.cast(action);
        return new XChartMoveAction(rhs.x, rhs.y);
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        //
        final XChartModel model = context.getModel();
        final XChartAdapter adapter = context.getAdapter();
        if (this.x < 0 || this.y < 0 || model.getTouchX() < 0 || model.getTouchY() < 0) {
            model.setTouchX(this.x);
            model.setTouchY(this.y);
            if (this.x < 0 || this.y < 0) {
                model.setPointToMagnify(new CGPoint(this.x,this.y));
                model.setDragging(false);
                adapter.notifyOnPickMove(this.x,this.y);
                return true;
            }
            return false;
        }
        final XChartWindow window = model.getWindow();
        final XChartMargin margin = model.getMargin();
        final XChartRender render = context.getRender();
        if (window == null || window.isEmpty()) return false;

        //
        final XChartTheme theme = model.getTheme();
        ChartScale scale = model.getConfig().getScale();
        final XChartArea a = render.getArea(XChartArea.Type.A);
        float speed = theme.getProperty(MOVE_SPEED, 1.0f);

        if (model.getSelectedDepot() != null || model.getSelectedSegment() != null) {
            XDrawingLine l = null;
            if (model.getSelectedDepot() != null) {
                long prev = System.currentTimeMillis();
                XDrawingDepot depot = model.getSelectedDepot();
                // x、y
                XChart chart = context.getChartWithX(this.x);
                double price = context.getPriceWithY(this.y);
                if (chart == null) return false;
                if(price < 0f) return false;
                //if (isnan(price)) { return NO; }
                depot.setxChart(chart);
                if (model.isMagneting()) {
                    double mid = (depot.getxChart().getHigh() + depot.getxChart().getLow()) / 2;
                    price = price > mid ? depot.getxChart().getHigh() : depot.getxChart().getLow();
                    this.y = (float) context.getYWithPrice(price);
                }
                depot.setyPrice(price);

                //CGPoint pt = new CGPoint(this.x,this.y);//CGPointMake(self.x, self.y);
                //model.setPointToMagnify(new CGPoint(this.x,this.y));
                adapter.notifyOnPickMove(this.x,this.y);//[adapter notifyOnPickMove:pt];
                l = depot.getParent();
                System.out.println("move 1111:"+(System.currentTimeMillis() - prev));
            } else {
                long prev = System.currentTimeMillis();
                int distanceX = round(model.getTouchX() - this.x);
                int distanceY = round(model.getTouchY() - this.y);
                int max = model.getCharts().size() - 1;
                int delta = round(-distanceX / scale.getWidth());
                List<XChart> charts = model.getCharts();
                List<XDrawingDepot> depots = new ArrayList<>();
                if (model.getSelectedSegment().getEndDepot() != null) {
                    depots.add(model.getSelectedSegment().getStartDepot());
                    depots.add(model.getSelectedSegment().getEndDepot());//,@[model.getSelectedSegment.startDepot, model.getSelectedSegment.endDepot];
                } else {
                    depots.add(model.getSelectedSegment().getStartDepot());// = @[model.getSelectedSegment.startDepot];
                }
                List<XDrawingDepot> r = new ArrayList<>();//NSMutableArray<XDrawingDepot *> *r = [NSMutableArray array];
                for (XDrawingDepot depot : depots) {
                    XDrawingDepot d = new XDrawingDepot(model);
                    // x
                    int index = charts.indexOf(depot.getxChart());//[charts indexOfObject:depot.xChart];
                    index += delta;
                    if (index > max) { return false; }
                    if (index < 0) { return false; }
                    d.setxChart(charts.get(index));//[charts objectAtIndex:index];

                    // y
                    double minA = window.getMin(true),maxA = window.getMax(true);
                    double unitA = XChartUtils.subtract(minA,maxA)/a.getNetHeight();
                    double price = XChartUtils.add(-distanceY * unitA,depot.getyPrice());//[XChartUtils add:-distanceY * unitA v2:depot.yPrice];
                    d.setyPrice(price);
                    r.add(d);
                }
                if (r.size() != depots.size()) { return false; }
                for (int i=0; i < r.size(); i++) {
                    System.out.println("move 2222 depots.y("+i+"):"+depots.get(i).getyPrice()+"," +
                            "r.y("+i+"):"+r.get(i).getyPrice());
                    depots.get(i).setxChart(r.get(i).getxChart());
                    depots.get(i).setyPrice(r.get(i).getyPrice());
                }
                l = depots.get(0).getParent();//depots.firstObject.parent;
                System.out.println("move 2222 distanceX:"+distanceX+",distanceY:"+distanceY+",model.getTouchY():"+model.getTouchY()+",this.y:"+this.y+
                ",model.getTouchX():"+model.getTouchX()+",this.x:"+this.x);
            }

            // Apply to related line segments
            if (l.getStyle() == XDrawingStyle.DOUBLE && l.segments.size() > 0) {
                long prev = System.currentTimeMillis();
                // Only needs to modify the last depot in the last segment (not among the control depots)
                List<XDrawingSegment> segments = l.segments;
                XDrawingSegment segFirst = segments.get(0);//segments.firstObject;
                XDrawingSegment segLast = segments.get(segments.size() - 1);//segments.lastObject;
                List<XChart> charts = model.getCharts();
                int idxFirstStart = charts.indexOf(segFirst.getStartDepot().getxChart());//[charts indexOfObject:segFirst.startDepot.xChart];
                int idxFirstEnd = charts.indexOf(segFirst.getEndDepot().getxChart());//[charts indexOfObject:segFirst.endDepot.xChart];
                if (idxFirstEnd == idxFirstStart) {
                    segLast.getEndDepot().setxChart(segLast.getStartDepot().getxChart());//endDepot.xChart = segLast.startDepot.xChart;
                    segLast.getEndDepot().setyPrice(segLast.getStartDepot().getyPrice() + (segFirst.getStartDepot().getyPrice() - segFirst.getEndDepot().getyPrice()));
                    //segLast.endDepot.yPrice = segLast.startDepot.yPrice + (segFirst.startDepot.yPrice - segFirst.endDepot.yPrice);
                } else {
                    int offsetX = idxFirstStart - idxFirstEnd;
                    int idxLastStart = charts.indexOf(segLast.getStartDepot().getxChart());//[charts indexOfObject:segLast.startDepot.xChart];
                    int idxLastEnd = idxLastStart + offsetX;
                    idxLastEnd = Math.max(Math.min(idxLastEnd , charts.size() - 1), 0);
                    double offsetY = (idxLastStart - idxLastEnd) * (segFirst.getStartDepot().getyPrice() - segFirst.getEndDepot().getyPrice())/(idxFirstEnd - idxFirstStart);
                    segLast.getEndDepot().setxChart(charts.get(idxLastEnd));
                    segLast.getEndDepot().setyPrice(segLast.getStartDepot().getyPrice() + offsetY);
                }
                System.out.println("move 3333:"+(System.currentTimeMillis() - prev));
            } else if (l.getStyle() == XDrawingStyle.HORIZONMULTIPLE) {
                long prev = System.currentTimeMillis();
                if (l.depots.size() < 2) { return false; }
                double diff = l.depots.get(1).getyPrice() - l.depots.get(0).getyPrice();
                for (int i = 2; i < l.segments.size(); i++) {
                    int index = i-2;
                    if(index > MULTIPLE_PERCENTAGE.length) break;
                    //if (index > sizeof(MULTIPLE_PERCENTAGE)/sizeof(double)-1) { break; }
                    double p = MULTIPLE_PERCENTAGE[index];
                    double price = l.depots.get(0).getyPrice() + diff * p;
                    XDrawingSegment segment = l.segments.get(i);
                    segment.getStartDepot().setyPrice(price);
                }
                System.out.println("move 4444:"+(System.currentTimeMillis() - prev));
            }
            model.setPointToMagnify(new CGPoint(this.x,this.y));
            //model.setTouchX(abs(model.getTouchX() - this.x) >= scale.getWidth() ? this.x : model.getTouchX());
//            model.setTouchX(this.x);
//            model.setTouchY(this.y);
//            [model setTouchX:self.x];
//            [model setTouchY:self.y];
            return true;
        }else{
            //int distance = XChartUtils.round(span * speed);
            int distance = XChartUtils.round((model.getTouchX() - this.x) * speed);
            if (Math.abs(distance) < scale.getWidth()) return false;
            //
            int pivot = model.getPivot();
            int max = model.getCharts().size() - 1;
            int delta = round(distance / scale.getWidth());
            final int min = min(scale.count(a.getWidth()) - 1, max);
            if(delta > 0) {
                pivot += delta;
                if(pivot > max) { margin.move(pivot - max); pivot = max; }
                //System.out.println("distance:"+distance+",delta:"+delta+",getTouchX:"+model.getTouchX()+",pivot:"+pivot+",max:"+max);
                model.setPivot(pivot); adapter.notifyOnMove(false); return true;
            } else {
                delta = margin.move(delta);
                if((pivot = pivot + delta) < min) { pivot = min; }
                //System.out.println("distance:"+distance+",delta:"+delta+",getTouchX:"+model.getTouchX()+",pivot:"+pivot+",min:"+min);
                model.setPivot(pivot); adapter.notifyOnMove(pivot == min); return true;
            }
        }
    }
}