package jerry.test.com.mvp.xchart.render.impl.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.util.monitor.Monitor;
import cn.nextop.erebor.mid.common.util.monitor.MonitorFactory;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.cast;
import static java.lang.Double.isNaN;


public abstract class AbstractXPainter implements XChartPainter {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractXPainter.class);

    //
    protected final int layer;
    protected final String name;
    protected final Monitor monitor;
    protected final Paint paint = new Paint();
    protected Set<XChartArea.Type> areas = new HashSet<>();
    protected Map<String, XChartLines> lines = new HashMap<>();

    //
    protected void doDraw(XChartContext context, Canvas canvas) {}
    protected void doEval(XChartContext context, Canvas canvas) {}
    protected void doSetup(XChartContext context, Canvas canvas) {}

    /**
     *
     */
    public AbstractXPainter(String name, int layer) {
        this.name = name; this.layer = layer;
        this.monitor = MonitorFactory.getMonitor(name);
    }

    /**
     *
     */
    @Override
    public final int getLayer() {
        return this.layer;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public Set<XChartArea.Type> getAreas() {
        return this.areas;
    }

    public void addArea(XChartArea.Type area) {
        this.areas.add(area);
    }

    public void setAreas(Set<XChartArea.Type> areas) {
        this.areas = areas;
    }

    protected XChartLines getLines(final String name) {
        XChartLines r = null;
        r = this.lines.get(name); if(r != null) return r;
        this.lines.put(name, r = new XChartLines()); return r;
    }

    /**
     *
     */
    protected float getDensity(XChartContext context) {
        final XChartView view = context.getAdapter().getView();
        return view.getResources().getDisplayMetrics().density;
    }

    /**
     *
     */
    protected final int getCursor(final XChartContext context) {
        final XChartModel model = context.getModel();
        final XChartRender render = context.getRender();
        final ChartScale scale = model.getConfig().getScale();
        final XChartArea a = render.getArea(XChartArea.Type.A);
        if (model.getCursorX() < 0f && model.getCursorY() < 0f) {
            return -1;
        } else {
            final int count = scale.count(a.getWidth());
            final float deltaX = a.getRight() - model.getCursorX();
            int r = XChartUtils.round((deltaX) / scale.getWidth());
            if(r < 1) r = 1; else if(r > count) r = count; return r;
        }
    }

    /**
     *
     */
    @Override
    public void eval(final XChartContext context, final Canvas canvas) {
        doEval(context, canvas);
    }

    @Override
    public void draw(final XChartContext context, final Canvas canvas) {
        doDraw(context, canvas);
    }

    @Override
    public void setup(final XChartContext context, final Canvas canvas) {
        if (context.getAction().isSetup()) doSetup(context, canvas);
    }

    /**
     *
     */
    protected static final void adds1(float x1, double v1, float x2, double v2, float bottom, double min, double unit, XChartLines lines) {
        if (v1 > 0d && v2 > 0d) lines.adds(x1, cast(bottom - (v1 - min) * unit), x2, cast(bottom - (v2 - min) * unit));
    }

    protected static final void adds2(float x1, double v1, float x2, double v2, float bottom, double min, double unit, XChartLines lines) {
        if (!isNaN(v1) && !isNaN(v2)) lines.adds(x1, cast(bottom - (v1 - min) * unit), x2, cast(bottom - (v2 - min) * unit));
    }

    protected static final void drawPolygon1(float x1, double v1, float x2, double v2, float x3, double v3, float x4, double v4, float bottom, double min, double unit, Canvas canvas, Paint paint) {
        if (v1 <= 0d || v2 <= 0d || v3 <= 0d || v4 <= 0d) return;
        final Path path = new Path(); path.moveTo(x1, cast(bottom - (v1 - min) * unit));
        path.lineTo(x2, cast(bottom - (v2 - min) * unit)); path.lineTo(x3, cast(bottom - (v3 - min) * unit));
        path.lineTo(x4, cast(bottom - (v4 - min) * unit)); path.lineTo(x1, cast(bottom - (v1 - min) * unit)); canvas.drawPath(path, paint);
    }

    protected static final void drawPolygon2(float x1, double v1, float x2, double v2, float x3, double v3, float x4, double v4, float bottom, double min, double unit, Canvas canvas, Paint paint) {
        if (isNaN(v1)  || isNaN(v2) || isNaN(v3) || isNaN(v4)) return;
        final Path path = new Path(); path.moveTo(x1, cast(bottom - (v1 - min) * unit));
        path.lineTo(x2, cast(bottom - (v2 - min) * unit)); path.lineTo(x3, cast(bottom - (v3 - min) * unit));
        path.lineTo(x4, cast(bottom - (v4 - min) * unit)); path.lineTo(x1, cast(bottom - (v1 - min) * unit)); canvas.drawPath(path, paint);
    }
}
