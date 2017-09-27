package jerry.test.com.mvp.xchart.render.impl.painter.chart;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingDepot;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingSegment;
import cn.nextop.erebor.mid.common.glossary.chart.XSegment.SegmentExtendable;

import static android.graphics.Paint.Align.RIGHT;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGPoint;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGSize;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_GRAY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_YELLOW;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_DEPOT_RADIUS;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_HIGHLIGHT_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_NORMAL_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_TEXT_SIZE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type;
import static cn.nextop.erebor.mid.common.glossary.chart.XSegment.SegmentExtendable.kBothSides;
import static cn.nextop.erebor.mid.common.glossary.chart.XSegment.SegmentExtendable.kForward;
import static cn.nextop.erebor.mid.common.glossary.chart.XSegment.SegmentExtendable.kReverse;


public class XDrawingPainter extends AbstractXPainter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XDrawingPainter.class);
    protected Paint paint0 = new Paint();

    public XDrawingPainter(){
        super("painter.drawing", 100);
    }
    /**
     * @param name
     * @param layer
     */
    public XDrawingPainter(String name, int layer) {
        super(name, layer);
    }

    public void setup(Paint p, boolean anti, int color, float width, float size){
        p.setAntiAlias(anti);p.setTextSize(size); p.setColor(color);p.setStrokeWidth(width);
    }

    public void doSetup(XChartContext context,Canvas canvas){
        float density = getDensity(context);
        XChartTheme theme = context.getModel().getTheme();
        float size = theme.getProperty(DRAWING_PAINTER_TEXT_SIZE, 10.0f);
        float width = theme.getProperty(DRAWING_PAINTER_LINE_WIDTH, 1.0f);
        int color = theme.getProperty(DRAWING_PAINTER_NORMAL_COLOR, COLOR_GRAY);
        setup(paint, true, color, width, size * density);
        setup(paint0, true, color, width, size * density);
        this.paint0.setPathEffect(new DashPathEffect(new float[]{5, 5}, 1));
    }

    public void doDraw(XChartContext context,Canvas canvas){
        //
        //long pre = System.currentTimeMillis();
        if(!context.getModel().hasWindow()) return;
        XChartModel model = context.getModel();
        XChartRender render = context.getRender();
        XChartArea a = render.getArea(Type.A);
        float al = a.getLeft(), at = a.getTop(), ar = a.getRight(), ab = a.getBottom();

        XChartTheme theme = context.getModel().getTheme();
        float radius = theme.getProperty(DRAWING_DEPOT_RADIUS, 7.0f);
        List<XDrawingLine> lines = model.getLines();
        if(lines == null || lines.size() == 0) return;
        String hintText = null;
        for (XDrawingLine l: lines) {
            //long prev = System.currentTimeMillis();
            // Draw segments and lines
            for (XDrawingSegment segment : l.segments) {
                CGPoint startPoint = new CGPoint(context.getXWithChart(segment.getStartDepot().getxChart()),context.getYWithPrice(segment.getStartDepot().getyPrice()));
                //if(isnan(startPoint.x) || isnan(startPoint.y)) { break; }
                if (startPoint.x == -1f || startPoint.y == -1f) break;

                int color = segment.getColor(l.highlighted());
                this.paint.setColor(color);
                this.paint0.setColor(color);
                switch (segment.getType()) {
                    case kTypeReal:
                    case kTypeVirtual:
                        CGPoint endPoint = new CGPoint(context.getXWithChart(segment.getEndDepot().getxChart()), context.getYWithPrice(segment.getEndDepot().getyPrice()));
                        //if(isnan(endPoint.x) || isnan(endPoint.y)) { break; }
                        if (startPoint.x == -1f || startPoint.y == -1f) break;
                        doDrawLine(startPoint, endPoint, segment.getExtendableType(), a, canvas);
                        break;
                    case kTypeInfiniteH:
                        XChartUtils.drawLine(al, startPoint.y, ar, startPoint.y, canvas, paint);
                        break;
                    case kTypeInfiniteV:
                        XChartUtils.drawLine((float) startPoint.x, at, (float) startPoint.x, ab, canvas, paint);
                        break;
                    default:
                        break;
                }
                // Draw graduation text
                if (segment.getGraduation() != null) {
                    XChartUtils.drawText(segment.getGraduation(), ar - 4.0f, (float) startPoint.y - 4.0f, Paint.Align.RIGHT, canvas, paint);
                }
            }

            // Draw depots
            for (XDrawingDepot depot : l.depots) {
                int color = depot.getColor(l.highlighted());
                this.paint.setColor(color);
                CGPoint pt = new CGPoint(context.getXWithChart(depot.getxChart()), context.getYWithPrice(depot.getyPrice()));
                doDrawDepot(pt, radius, a, canvas);
                if (model.isDragging()) {
                    // Set hint text
                    if (model.getSelectedDepot() == depot) {
                            hintText = depot == l.depots.get(0) ? "Start Point" : "End Point";
                        //doRedraw(canvas,model);
                    }
                }
            }
            //System.out.println("painter:"+(System.currentTimeMillis() - prev));
        }
        // Draw hint text
        if (hintText != null) {
            XChartTheme theme2 = model.getTheme();
            int color = theme2.getProperty(DRAWING_PAINTER_HIGHLIGHT_COLOR, COLOR_YELLOW);
            this.paint.setColor(color);
            XChartUtils.drawText(hintText, ar, 0, RIGHT, new CGSize(5, 5), true, canvas, paint);
        }
        //System.out.println("XDrawingPainter.doDraw 2222:"+(System.currentTimeMillis() - pre)+",lines.size:"+lines.size());
    }

    public  void doDrawDepot(CGPoint pt,float radius,XChartArea a,Canvas canvas){
        //if (isnan(pt.x) || isnan(pt.y)) { return; }
        if (pt.x == -1.0f || pt.y == -1.0f) return;
        if (!a.contains((float) pt.x, (float) pt.y)) return;
        canvas.drawCircle((float) pt.x, (float) pt.y, radius, paint);
        //System.out.println("XDrawingPainter x:"+pt.x+",pt.y:"+pt.y);
    }

    public void doDrawLine(CGPoint from, CGPoint to, SegmentExtendable type, XChartArea a, Canvas canvas){
        //long pre2 = System.currentTimeMillis();
        // Draw line segment
        float al = a.getLeft(), at = a.getTop(), ar = a.getRight(), ab = a.getBottom();
        CGPoint pl = null,pr = null, pt = null, pb = null;
        if (to.x == from.x || to.y == from.y) {
            if (to.x == from.x) {
                pt = new CGPoint(from.x, at);
                pb = new CGPoint(from.x, ab);
            }
            if (to.y == from.y) {
                pl = new CGPoint(al, from.y);
                pr = new CGPoint(ar, from.y);
            }
        } else {
            double r = (to.y - from.y)/(to.x - from.x);
            pl = new CGPoint(al, (al - from.x) * r + from.y);
            pr = new CGPoint(ar, (ar - from.x) * r + from.y);
            pt = new CGPoint((at - from.y) / r + from.x, at);
            pb = new CGPoint((ab - from.y) / r + from.x, ab);
        }
        CGPoint min = from, max = to;
        if (min.y > max.y) {
            XChartUtils.swap(min, max);
        }

        if (pt != null && pt.y > min.y) min = pt;
        if (pb != null && pb.y < max.y) max = pb;
        //if (!CGPointEqualToPoint(pt, NullPoint) && pt.y > min.y) { min = pt; }
        //if (!CGPointEqualToPoint(pb, NullPoint) && pb.y < max.y) { max = pb; }
        if (min.x > max.x) {
            XChartUtils.swap(min, max);
        }
        if (pl != null && pl.x > min.x) min = pl;
        if (pr != null && pr.x < max.x) max = pr;
        //if (!CGPointEqualToPoint(pl, NullPoint) && pl.x > min.x) { min = pl; }
        //if (!CGPointEqualToPoint(pr, NullPoint) && pr.x < max.x) { max = pr; }
        if(a.contains((float) min.x,(float) min.y) && a.contains((float) max.x,(float) max.y)){
            XChartUtils.drawLine((float) min.x,min.y,(float) max.x,max.y,canvas,this.paint);
        }

        // Draw extension line
        List<CGPoint> points = Arrays.asList(pl, pr, pt, pb);
        List<CGPoint> poles = new ArrayList<>();
        for (int i=0; i < points.size(); i++) {
            CGPoint p = points.get(i);
            if (p != null && a.contains((float) p.x,(float) p.y)) {
                poles.add(p);
            }
        }
        for (int i=0; i < poles.size(); i++) {
            CGPoint p = poles.get(i);
            if (min.x == max.x) {
                if (min.y > max.y) {
                    XChartUtils.swap(min, max);
                }
                if (p.y < min.y) {
                    if (type == kBothSides || (type == kForward && from.y > to.y) || (type == kReverse && from.y < to.y)) {
                        XChartUtils.drawLine((float) min.x, min.y, (float) p.x, p.y, canvas, this.paint0);
                    }
                }
                if (p.y > max.y) {
                    if (type == kBothSides || (type == kForward && from.y < to.y) || (type == kReverse && from.y > to.y)) {
                        XChartUtils.drawLine((float) max.x, max.y, (float) p.x, p.y, canvas, this.paint0);
                    }
                }
            } else {
                if (min.x > max.x) {
                    XChartUtils.swap(min, max);
                }
                if (p.x < min.x) {
                    if (type == kBothSides || (type == kForward && from.x > to.x) || (type == kReverse && from.x < to.x)) {
                        XChartUtils.drawLine((float) min.x, min.y, (float) p.x, p.y, canvas, this.paint0);
                    }
                }
                if (p.x > max.x) {
                    if (type == kBothSides || (type == kForward && from.x < to.x) || (type == kReverse && from.x > to.x)) {
                        XChartUtils.drawLine((float) max.x, max.y, (float) p.x, p.y, canvas, this.paint0);
                    }
                }
            }
        }
        //System.out.println("XDrawingPainter.doDraw 1111:"+(System.currentTimeMillis() - pre2));
    }
}
