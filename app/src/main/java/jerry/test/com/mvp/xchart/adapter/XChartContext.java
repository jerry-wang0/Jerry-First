package jerry.test.com.mvp.xchart.adapter;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingDepot;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingSegment;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

public interface XChartContext {

    XChartView getView();

    XChartModel getModel();

    XChartRender getRender();

    XChartAdapter getAdapter();

    <T extends XChartAction> T getAction();

    XChart getChartWithX(float x);

    double getPriceWithY(float y);

    double getXWithChart(XChart chart);

    double getYWithPrice(double price);

    XDrawingDepot getDepot(float x, float y);

    XDrawingSegment getSegment(float x, float y);
}
