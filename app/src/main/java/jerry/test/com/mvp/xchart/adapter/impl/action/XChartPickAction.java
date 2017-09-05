package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGPoint;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingDepot;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.util.Objects;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_LINE_DOUBLE_SPACE_V;

public class XChartPickAction extends AbstractAction {

    protected final float x, y;

    /**
     */
    public XChartPickAction(float x, float y) {
        super(Type.PICK);this.x = x; this.y = y;
    }

    @Override
    public XChartAction merge(XChartAction action) {
        if(action.getType() != getType()) return null;
        final XChartPickAction rhs = Objects.cast(action);
        return new XChartPickAction(rhs.x, rhs.y);// Discard the prev action
    }

    @Override
    public boolean apply(XChartContext context) {
        //
        XChartModel model = context.getModel();
        XChartAdapter adapter = context.getAdapter();
        if (this.x < 0 || this.y < 0) {
            model.setPointToMagnify(new CGPoint(this.x,this.y));
            model.setDragging(false);
            adapter.notifyOnPickMove(this.x, this.y);
            return true;
        }
        //
        if (model.getDrawingStyle() != null) {
            XChart chart = context.getChartWithX(this.x);
            double price = context.getPriceWithY(this.y);
            XDrawingDepot depot = new XDrawingDepot(model);
            depot.setxChart(chart);
            depot.setyPrice(price);
            model.addDepot(depot);
            model.setPointToMagnify(new CGPoint(this.x,this.y));
            adapter.notifyOnPickMove(this.x,this.y);
            if (model.getDrawingStyle() == XDrawingStyle.DOUBLE && depot.getParent().depots.size() == 2) {
                // add the 3rd depot automatically
                XDrawingDepot d = new XDrawingDepot(model);
                d.setxChart(chart);
                XChartTheme theme = model.getTheme();
                float s = theme.getProperty(DRAWING_LINE_DOUBLE_SPACE_V, 60.0f);
                float y = this.y - s < 0 ? this.y + s : this.y - s;
                d.setyPrice(context.getPriceWithY(y));
                model.addDepot(d);
                model.setSelectedDepot(d);// reset selected depot
            }
            if (depot.getParent().isValid()) {
                model.setDrawingStyle(null);
                model.validateLines();
                adapter.notifyOnDrawComplete(depot.getParent());
            }
        } else {
            XDrawingDepot depot = context.getDepot(this.x, this.y);
            if (depot != null) {
                model.setSelectedDepot(depot);
            } else {
                model.setSelectedDepot(null);
                model.setSelectedSegment(context.getSegment(this.x, this.y));
            }
        }
        model.setDragging(true);
        return true;
    }
}
