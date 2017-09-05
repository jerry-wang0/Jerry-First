package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;


public class XChartMagnetAction extends AbstractAction  {

    protected boolean isMagneting;

    public XChartMagnetAction(boolean isMagneting){
        super(Type.ACTIVATE);
        this.isMagneting = isMagneting;
    }

    @Override
    public boolean apply(XChartContext context) {
        XChartAdapter adapter = context.getAdapter();
        adapter.getModel().setMagneting(isMagneting);
        return false;
    }
}
