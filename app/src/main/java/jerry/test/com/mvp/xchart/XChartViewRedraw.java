package jerry.test.com.mvp.xchart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.TimeUnit;

import cn.nextop.erebor.mid.common.glossary.Lifecyclet;
import cn.nextop.erebor.mid.common.util.concurrent.thread.StopableThread;


public class XChartViewRedraw extends SurfaceView implements SurfaceHolder.Callback {

    protected volatile StopableThread worker;
    protected Redraw redraw = new Redraw();
    protected SurfaceHolder holder;
    protected Bitmap cache;

    public XChartViewRedraw(Context context) {
        super(context);init();
    }

    public XChartViewRedraw(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    public XChartViewRedraw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }

    private void init(){
        holder = getHolder();
        holder.addCallback(this);
    }

    public Bitmap getCache() {
        return cache;
    }

    public void setCache(Bitmap cache) {
        this.cache = cache;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Lifecyclet.startQuietly(redraw);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Lifecyclet.stopQuietly(redraw, 0L, TimeUnit.MILLISECONDS);
    }

    class Redraw extends Lifecyclet implements Runnable {

        @Override
        public void run() {
            Canvas canvas = holder.lockCanvas(null);
            if(canvas == null) return;
//            Rect rect = new Rect(2,2,100,100);
//            canvas.clipRect(rect);
            //canvas.drawColor(Color.GRAY);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);paint.setColor(Color.GREEN);
            paint.setTextSize(14);
            if(cache != null){
                canvas.drawBitmap(cache,2,2,paint);
            }
            canvas.drawText("aaa",0,0,paint);
            if (canvas != null) holder.unlockCanvasAndPost(canvas);
        }

        @Override
        protected void doStart() throws Exception {
            worker = StopableThread.start("chart.XChartViewRedraw", this);
        }

        @Override
        protected long doStop(long timeout, TimeUnit unit) throws Exception {
            return StopableThread.stop(worker, timeout, unit);
        }
    }
}
