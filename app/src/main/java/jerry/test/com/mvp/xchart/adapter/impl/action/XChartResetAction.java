package jerry.test.com.mvp.xchart.adapter.impl.action;

import java.util.ArrayList;
import java.util.Collection;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

public class XChartResetAction extends AbstractAction {
    //
    protected final XChartConfig config;
    protected final Collection<? extends XChart> charts;

    /**
     *
     */
    public XChartResetAction(Collection<? extends XChart> charts) {
        super(Type.RESET); this.config = null; this.charts = charts;
    }

    public XChartResetAction(XChartConfig c1, Collection<? extends XChart> c2) {
        super(Type.RESET); this.config = c1; this.charts = c2;
    }

    /**
     *
     */
    @Override
    public boolean isSetup() {
        return this.config != null;
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        final XChartAdapter adapter = context.getAdapter();
        if(this.config != null) adapter.getModel().setConfig(this.config);
        adapter.getModel().reset(charts != null ? charts : new ArrayList<XChart>(0)); return true;
    }
}
