package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;


public class XChartSetupAction extends AbstractAction {
    //
    protected final XChartTheme theme;
    protected final XChartConfig config;

    /**
     *
     */
    public XChartSetupAction(XChartConfig config, XChartTheme theme) {
        super(Type.SETUP); this.config = config; this.theme = theme;
    }

    /**
     *
     */
    @Override
    public boolean isSetup() {
        return true;
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        final XChartAdapter adapter = context.getAdapter();
        if(theme != null) adapter.getModel().setTheme(theme);
        if(config != null) adapter.getModel().setConfig(config); return true;
    }
}
