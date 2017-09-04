package jerry.test.com.mvp.xchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.gesture.MoveGestureDetector;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.common.glossary.Lifecyclet;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.abs;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.MOVE_TIMEOUT;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.ZOOM_STEP;

/**
 * Created by Jingqi Xu on 8/27/15.
 */
public class XChartView extends SurfaceView implements SurfaceHolder.Callback {
    //
    protected SurfaceHolder holder;
    protected XChartAdapter adapter;
    protected boolean movable = true;
    protected boolean scalable = true;
    protected boolean touchable = false;
    protected MoveListener moveListener;
    protected ScaleListener scaleListener;
    protected MoveGestureDetector moveDetector;
    protected ScaleGestureDetector scaleDetector;

    //
    public boolean isMovable() { return movable; }
    public boolean isScalable() { return scalable; }
    public boolean isTouchable() { return touchable; }
    public XChartAdapter getAdapter() { return this.adapter; }
    public void setMovable(boolean movable) { this.movable = movable; }
    public void setScalable(boolean scalable) { this.scalable = scalable; }
    public void setTouchable(boolean touchable) { this.touchable = touchable; }

    /**
     *
     */
    public XChartView(Context ctx) {
        super(ctx); init();
    }

    public XChartView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs); init();
    }

    public XChartView(Context ctx, AttributeSet attrs, int styleAttr) {
        super(ctx, attrs, styleAttr); init();
    }

    @SuppressLint("NewApi")
    public XChartView(Context ctx, AttributeSet attrs, int styleAttr, int styleRes) {
        super(ctx, attrs, styleAttr, styleRes); init();
    }

    /**
     *
     */
    protected void init() {
        //setZOrderOnTop(true);
        this.adapter = new XChartAdapter(this);
        this.moveListener = new MoveListener();
        this.scaleListener = new ScaleListener();
        (this.holder = getHolder()).addCallback(this);
        super.setLayerType(View.LAYER_TYPE_NONE, null);
        this.moveDetector = new MoveGestureDetector(getContext(), moveListener);
        this.scaleDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (isScalable()) {
            this.scaleDetector.onTouchEvent(event);
        }
        if (isMovable() && !scaleDetector.isInProgress()) {
            this.moveDetector.onTouchEvent(event);
        }
        return true;
    }

    /**
     *
     */
    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    /**
     *
     */
    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        Lifecyclet.startQuietly(this.adapter);
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        Lifecyclet.stopQuietly(this.adapter, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.adapter.resize(format, width, height);
    }

    /**
     *
     */
    protected class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        //
        protected float prev = -1f;

        //
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            this.prev = -1f;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            moveListener.onMoveEnd(null, null);
            this.prev = detector.getCurrentSpanX(); return true;
        }

        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            XChartModel model = adapter.getModel();
            final XChartTheme theme = model.getTheme();
            float step = theme.getProperty(ZOOM_STEP, 60.0f);
            float next = detector.getCurrentSpanX(), span = this.prev - next;
            //--------------
            System.out.println("XChartView.onScale prve:"+prev+",next:"+next+",step:"+step+",span:"+span);
            //--------------
            if(abs(span) >= step) { adapter.zoom(detector.getFocusX(), span); this.prev = next; }
            return false;
        }
    }

    /**
     *
     */
    protected class MoveListener implements MoveGestureDetector.OnMoveGestureListener {
        //
        protected float prevX = -1f, prevY = -1f;
        protected boolean moving = false;
        protected boolean selecting = false;

        //
        @Override
        public void onMoveEnd(MoveGestureDetector detector, MotionEvent event) {
            this.prevX = -1f; this.prevY = -1f; this.moving = false;
            if(this.selecting) {
                adapter.select(-1f, -1f); this.selecting = false;
            }
            adapter.pick(-1f,-1f);
            if(adapter.getModel().getDrawingStyle() == null){
                adapter.getModel().setSelectedSegment(null);
                adapter.getModel().setSelectedDepot(null);
            }
            setTouchable(false);
        }

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector, MotionEvent event) {
            float next = detector.getFocusX();
            final XChartArea area = adapter.getRender().getArea(XChartArea.Type.A);
            adapter.pick(event.getX(),event.getY());
            setTouchable(true);
            if(area != null && area.containsX(next)) this.prevX = next; return true;
        }

        @Override
        public boolean onMove(final MoveGestureDetector detector, MotionEvent event) {
            if(this.prevX < 0f) return true;
            final XChartModel model = adapter.getModel();
            final ChartScale scale = model.getConfig().getScale();
            float nextX = detector.getFocusX(),nextY = detector.getFocusY();
            model.setTouchX(this.prevX); model.setTouchY(this.prevY);
            if(model.getSelectedDepot() != null || model.getSelectedSegment() != null){
                this.prevX = abs(this.prevX - nextX) >= scale.getWidth() ? nextX : this.prevX;
                //if(abs(this.prevX - nextX) >= scale.getWidth())
                adapter.move( prevX, prevY = nextY);//prevX = nextX;prevY = nextY;
                    //adapter.move(prevX = nextX,prevY = nextY);
            }else{
                //model.setTouchX(this.prevX); model.setTouchY(this.prevY);
                if(abs(this.prevX - nextX) >= scale.getWidth()) {
                    if(this.selecting) {
                        adapter.select(this.prevX = nextX, detector.getFocusY());
                    } else {
                        this.moving = true; adapter.move(this.prevX = nextX,this.prevY = nextY);
                    }
                } else if(event.getPointerCount() == 1) {
                    final XChartTheme theme = model.getTheme();
                    final long timeout = theme.getProperty(MOVE_TIMEOUT, 300L);
                    final long time = event.getEventTime() - event.getDownTime();
                    final float x = detector.getFocusX(), y = detector.getFocusY();
                    if(!this.moving && time >= timeout) { this.selecting = true; adapter.select(x, y); }
                } else {
                    if(this.selecting) adapter.select(-1f, -1f); this.selecting = false; // Prepare for scaling
                }
            }
            return true;
        }
    }
}
