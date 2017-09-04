package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;

/**
 * Created by wanggl on 2016/11/29.
 */
public class XChartActivateAction extends AbstractAction {
    final XDrawingStyle style;
    /**
     * @param style
     */
    public XChartActivateAction(XDrawingStyle style) {
        super(Type.ACTIVATE);
        this.style = style;
    }

    @Override
    public boolean apply(XChartContext context) {
        final XChartAdapter adapter = context.getAdapter();
        final XChartModel model = adapter.getModel();
        model.setDrawingStyle(this.style);
        model.setSelectedDepot(null);
        return true;
    }
}
