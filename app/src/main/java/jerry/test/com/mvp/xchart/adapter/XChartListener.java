package jerry.test.com.mvp.xchart.adapter;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

/**
 * Created by Jingqi Xu on 9/5/15.
 */
public interface XChartListener {

    void onCursor(XChartAdapter adapter, XChart chart);

    void onMove(XChartAdapter adapter, boolean iterate);

    void onScale(XChartAdapter adapter, ChartScale scale);

    void onPickMove(XChartAdapter adapter, float x, float y);

    void onRemove(XChartAdapter adapter, XDrawingLine line);

    void onDrawComplete(XChartAdapter adapter, XDrawingLine line);
}
