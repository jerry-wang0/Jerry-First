package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartMargin;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.util.Objects;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.max;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.round;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.ZOOM_SPEED;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.ZOOM_STEP;

public class XChartZoomAction extends AbstractAction {
    //
    protected final float x;
    protected final float span;

    /**
     *
     */
    public XChartZoomAction(float x, float span) {
        super(Type.ZOOM); this.x = x; this.span = span;
    }

    /**
     * prev.merge(next)
     * @see cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter
     */
    @Override
    public XChartAction merge(XChartAction action) {
        if(action.getType() != getType()) return null;
        final XChartZoomAction rhs = Objects.cast(action);
        return new XChartZoomAction(x, this.span + rhs.span);
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        //
        XChartAdapter adapter = context.getAdapter();
        final XChartModel model = adapter.getModel();
        final XChartMargin margin = model.getMargin();
        final XChartWindow window = model.getWindow();
        final XChartRender render = context.getRender();
        if (window == null || window.isEmpty()) return false;

        //
        final XChartTheme theme = model.getTheme();
        float step = theme.getProperty(ZOOM_STEP, 60.0f);
        float speed = theme.getProperty(ZOOM_SPEED, 1.0f);
        int distance = XChartUtils.round(this.span * speed);
        final ChartScale prev = model.getConfig().getScale();
        final XChartArea a = render.getArea(XChartArea.Type.A);
        final ChartScale next = prev.scale(round(-1f * distance / step));
        if(next == prev) return false;

        //
        int delta;
        final int max = model.getCharts().size() - 1;
        final int min = min(next.count(a.getWidth()) - 1, max);
        int pivot = model.getPivot(); model.getConfig().setScale(next);
        final int d1 = round(max(a.getRight() - x, 0f) / prev.getWidth());
        final int d2 = round(max(a.getRight() - x, 0f) / next.getWidth());
        if((delta = (d2 - d1)) > 0) {
            pivot += delta;
            if(pivot > max) { margin.move(pivot - max); pivot = max; }
            model.setPivot(pivot); adapter.notifyOnScale(next); return true;
        } else {
            delta = margin.move(delta);
            if((pivot = pivot + delta) < min) pivot = min;
            model.setPivot(pivot); adapter.notifyOnScale(next); return true;
        }
    }
}