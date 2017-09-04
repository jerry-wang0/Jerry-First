package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;

/**
 * Created by wanggl on 2016/11/29.
 */
public class XChartRemoveLineAction extends AbstractAction {

    /**
     */
    public XChartRemoveLineAction() {
        super(Type.REMOVELINE);
    }

    @Override
    public boolean apply(XChartContext context) {
        final XChartAdapter adapter = context.getAdapter();
        final XChartModel model = adapter.getModel();
        model.removeSelectedLine();
        return true;
    }
}