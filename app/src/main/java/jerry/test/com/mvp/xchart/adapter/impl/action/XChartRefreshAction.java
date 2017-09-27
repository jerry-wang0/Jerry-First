package jerry.test.com.mvp.xchart.adapter.impl.action;

import java.util.Collection;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.util.Objects;


public class XChartRefreshAction extends AbstractAction {
    //
    protected final Collection<? extends XChart> charts;

    /**
     *
     */
    public XChartRefreshAction(Collection<? extends XChart> charts) {
        super(Type.REFRESH); this.charts = charts;
    }

    /**
     * prev.merge(next)
     * @see cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter
     */
    @Override
    public XChartAction merge(XChartAction action) {
        if(action.getType() != getType()) return null;
        final XChartRefreshAction rhs = Objects.cast(action);
        return new XChartRefreshAction(merge(this.charts, rhs.charts));
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        context.getAdapter().getModel().addCharts(this.charts); return true;
    }
}
