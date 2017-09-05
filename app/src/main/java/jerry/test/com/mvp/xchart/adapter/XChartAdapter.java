package jerry.test.com.mvp.xchart.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cn.nextop.erebor.mid.app.mvc.Widget;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartViewRedraw;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartActivateAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartMagnetAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartMoveAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartPickAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartRedrawAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartRefreshAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartRemoveLineAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartResetAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartResizeAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartSelectAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartSetupAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.action.XChartZoomAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.impl.context.XChartContextImpl;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.XChartRenderImpl;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.common.glossary.Lifecyclet;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.util.concurrent.thread.StopableThread;

import static cn.nextop.erebor.mid.common.util.Maps.putIfAbsent;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class XChartAdapter extends Lifecyclet implements Runnable {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(XChartAdapter.class);

    //
    protected XChartView view;
    protected XChartModel model;
    protected XChartRenderImpl render;
    protected volatile StopableThread worker;
    protected volatile XChartListener listener;
    protected BlockingQueue<XChartAction> actions = new LinkedBlockingQueue<>();
    protected ConcurrentMap<Integer, Bitmap> bitmaps = new ConcurrentHashMap<>();
    protected Widget widget;
    protected XChartViewRedraw redraw;
    protected ImageView img;

    /**
     *
     */
    public XChartAdapter(XChartView view) {
        this.view = view;
        this.model = new XChartModel();
        this.render = new XChartRenderImpl(this);
    }

    @Override
    protected void doStart() throws Exception {
        this.worker = StopableThread.start("chart.adapter", this);
    }

    @Override
    protected long doStop(long timeout, TimeUnit unit) throws Exception {
        return StopableThread.stop(worker, timeout, unit);
    }

    /**
     *
     */
    public XChartView getView() {
        return this.view;
    }

    public XChartModel getModel() {
        return this.model;
    }

    public XChartRender getRender() {
        return this.render;
    }

    public XChartListener getListener() {
        return this.listener;
    }

    public void setListener(XChartListener listener) {
        this.listener = listener;
    }

    public void setWidget(Widget widget){
        this.widget = widget;
    }

    public Widget getWidget(){
        return this.widget;
    }

    public XChartViewRedraw getRedraw() {
        return redraw;
    }

    public void setRedraw(XChartViewRedraw redraw) {
        this.redraw = redraw;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    /**
     *
     */
    public void notifyOnCursor(final XChart chart) {
        final XChartListener listener = this.listener;
        if(listener != null) listener.onCursor(this, chart);
    }

    public void notifyOnMove(final boolean iterate) {
        final XChartListener listener = this.listener;
        if(listener != null) listener.onMove(this, iterate);
    }

    public void notifyOnScale(final ChartScale scale) {
        final XChartListener listener = this.listener;
        if (listener != null) listener.onScale(this, scale);
    }

    public void notifyOnPickMove(final float x,final float y){
        final XChartListener listener = this.listener;
        if (listener != null) listener.onPickMove(this, x, y);
    }

    public void notifyOnRemoveLine(XDrawingLine line){
        final XChartListener listener = this.listener;
        if (listener != null) listener.onRemove(this, line);
    }

    public void notifyOnDrawComplete(XDrawingLine line){
        final XChartListener listener = this.listener;
        if (listener != null) listener.onDrawComplete(this, line);
    }

    /**
     *
     */
    public final Bitmap getBitmap(final int resId) {
        Bitmap r = this.bitmaps.get(resId); if(r != null) return r;
        r = BitmapFactory.decodeResource(view.getResources(), resId);
        return r == null ? null : putIfAbsent(this.bitmaps, resId, r);
    }

    /**
     *
     */
    public void move(final float x,final float y) {
        submit(new XChartMoveAction(x,y));
    }

    protected void submit(final XChartAction action) {
        this.actions.offer(action);
    }

    public void select(final float x, final float y) {
        submit(new XChartSelectAction(x, y));
    }

    public void zoom(final float x, final float span) {
        submit(new XChartZoomAction(x, span));
    }

    public void resize(int format, int width, int height) {
        submit(new XChartResizeAction(format, width, height));
    }

    public void reset(final Collection<? extends XChart> charts) {
        submit(new XChartResetAction(charts));
    }

    public void refresh(final Collection<? extends XChart> charts) {
        submit(new XChartRefreshAction(charts));
    }

    public void setup(final XChartConfig config, final XChartTheme theme) {
        submit(new XChartSetupAction(config, theme));
    }

    public void reset(XChartConfig config, Collection<? extends XChart> charts) {
        submit(new XChartResetAction(config, charts));
    }

    public void magnet(boolean on){
        submit(new XChartMagnetAction(on));
    }

    public void activate(XDrawingStyle style){
        submit(new XChartActivateAction(style));
    }

    public void pick(final float x,final float y){
        submit(new XChartPickAction(x,y));
    }

    public void redraw(List<XDrawingLine> lines){
        submit(new XChartRedrawAction(lines));
    }

    public void removeLine(){
        submit(new XChartRemoveLineAction());
    }

    /**
     *
     */
    @Override
    public void run() {
        //
        XChartAction prev = null, next = null;
        try {
            prev = this.actions.take();
            while(true) {
                next = actions.poll(0L, NANOSECONDS); if(next == null) break;
                final XChartAction r = prev.merge(next); if(r == null) break;
                prev = r; next = null;
            }
        } catch(InterruptedException ignore) {
            return;
        } catch(Throwable throwable1) {
            LOGGER.error("[chart.adapter]failed to poll, action: " + prev, throwable1);
            return;
        }
        //--------------
//        if(prev != null)System.out.println("XChartAdapter.run prve:"+prev.getClass().getSimpleName());
//        if(next != null)System.out.println("XChartAdapte.run next:"+next.getClass().getSimpleName());
        //--------------
        //
        XChartAction action = null;
        try {
            if(prev != null) render.render(new XChartContextImpl(this, action = prev));
            if(next != null) render.render(new XChartContextImpl(this, action = next));
        } catch(Throwable throwable2) {
            LOGGER.error("[chart.adapter]failed to render, action: " + action, throwable2);
        }
    }
}
