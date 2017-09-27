package jerry.test.com.mvp.xchart.render.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Collections;
import cn.nextop.erebor.mid.common.util.monitor.Monitor;
import cn.nextop.erebor.mid.common.util.monitor.MonitorFactory;
import cn.nextop.erebor.mid.common.util.monitor.impl.XMonitorKey;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_REDRAW_HEIGHT;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_REDRAW_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.X;
import static cn.nextop.erebor.mid.common.util.DateTimes.toMillis;


public class XChartRenderImpl implements XChartRender {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(XChartRenderImpl.class);

    //
    protected final String name;
    protected final Monitor monitor;
    protected final XChartAdapter adapter;
    protected volatile boolean verbose = false;
    protected final Map<XChartArea.Type, XChartArea> areas;

    /**
     *
     */
    public XChartRenderImpl(XChartAdapter adapter) {
        this.adapter = adapter;
        this.name = "chart.render";
        this.areas = new HashMap<>(32);
        this.monitor = MonitorFactory.getMonitor(name);
    }

    /**
     *
     */
    @Override
    public XChartArea getArea(XChartArea.Type type) {
        return this.areas.get(type);
    }

    @Override
    public Map<XChartArea.Type, XChartArea> getAreas() {
        return this.areas;
    }

    @Override
    public void putArea(XChartArea.Type type, XChartArea area) {
        this.areas.put(type, area);
    }

    /**
     *
     */
    protected List<XChartPainter> getPainters(XChartModel model) {
        //
        final XChartTheme theme = model.getTheme();
        final XChartConfig config = model.getConfig();
        if (theme == null || config == null) {
            return java.util.Collections.emptyList();
        }

        //
        List<XChartPainter> r = new ArrayList<>(theme.getPainters());
        final Set<IndicatorType> types = config.getIndicators().keySet();
        for (IndicatorType type : types) r.add(theme.getIndicator(type));
        return Collections.sort(r, XChartPainter.ASC); // Sort by layer ASC
    }

    /**
     *
     */
    @Override
    public final boolean render(final XChartContext context) {
        long et, mark = System.nanoTime();
        Canvas canvas = null; boolean r = false;
        final XChartView view = context.getView();
        final XChartModel model = context.getModel();
        final XChartAction action = context.getAction();
        try {
            // Apply
            r = action.apply(context); if(!r) return r;

            // Render
            try {
                //
                canvas = view.getHolder().lockCanvas(null);
                Bitmap bitmap = null;
                Canvas canvas1 = null;
                final float redrawW = model.getTheme().getProperty(DRAWING_PAINTER_REDRAW_WIDTH, 200.0f);
                final float redrawH = model.getTheme().getProperty(DRAWING_PAINTER_REDRAW_HEIGHT, 200.0f);
                if((model.getSelectedDepot() != null || model.getSelectedSegment() != null) && adapter.getView().isTouchable()){
                    bitmap = Bitmap.createBitmap((int) redrawW, (int) redrawH, Bitmap.Config.ARGB_8888);
                    canvas1 = new Canvas(bitmap);
                    Matrix matrix = new Matrix();
                    matrix.setScale(2f, 2f, (float)model.getPointToMagnify().x * 2 - redrawW / 2, (float)model.getPointToMagnify().y * 2 - redrawH / 2);
                    canvas1.setMatrix(matrix);
                }
                if(canvas == null) return false; // Should NOT reach here

                //
                final List<XChartPainter> painters = getPainters(model);
                if (model.getConfig() != null && model.getTheme() != null) {
                    for(XChartPainter p : painters)  p.setup(context, canvas);
                }
                //
                if (model.getConfig() != null && model.getTheme() != null) {
                    for(final XChartPainter p : painters) p.eval(context, canvas);
                    for(final XChartPainter p : painters){
                        if(canvas1 != null)p.draw(context, canvas1);
                        p.draw(context, canvas);
                    }
                }
                if((model.getSelectedDepot() != null || model.getSelectedSegment() != null) && adapter.getView().isTouchable()){
                    Paint paint = new Paint();paint.setColor(Color.WHITE);paint.setStyle(Paint.Style.STROKE);
                    final float xHeight = areas.get(X).getHeight();
                    if(model.getTouchX() < redrawW * 1.2 && model.getTouchY() < redrawH * 1.2) {
                        if (bitmap != null) canvas.drawBitmap(bitmap, 0f, canvas.getHeight() - redrawW - xHeight, null);
                        canvas.drawRect(0f, canvas.getHeight() - redrawH - xHeight, redrawW, canvas.getHeight() - xHeight, paint);
                    }else{
                        if (bitmap != null) canvas.drawBitmap(bitmap, 0f, 0f, null);
                        canvas.drawRect(0f, 0f, redrawW, redrawH, paint);
                    }
                }
            } finally {
                if (canvas != null) view.getHolder().unlockCanvasAndPost(canvas); // Commit
            }
        } catch (Throwable throwable) {
            this.monitor.add(XMonitorKey.INTERNAL_ERROR, action.getType().name(), 1, et = System.nanoTime() - mark);
            LOGGER.error("failed to render, action: " + action.getType() + ", elapsed time: " + toMillis(et) + " ms", throwable);
        } finally {
            this.monitor.add(XMonitorKey.PROCESS, action.getType().name(), 1, et = System.nanoTime() - mark);
            if (verbose) LOGGER.info("render[{}], action: {}, elapsed time: {} ms", new Object[]{r, action.getType(), toMillis(et)});
        }
        return r;
    }
}