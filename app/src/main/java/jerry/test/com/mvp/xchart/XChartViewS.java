package jerry.test.com.mvp.xchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Created by wanggl on 2016/12/27.
 */
public class XChartViewS  extends SurfaceView implements Callback {
    protected SurfaceHolder holder;
    private Canvas canvas;
    private boolean ThreadFlag;
    private int counter;

    public XChartViewS(Context context) {
        super(context);init();
    }

    public XChartViewS(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    public XChartViewS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }
    private void init(){
        holder = getHolder();
        holder.addCallback(this);
    }

//    public XChartViewS(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        counter = 0;
        ThreadFlag = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ThreadFlag = false;
    }

    private Thread mThread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (ThreadFlag) {
                // 锁定画布，得到Canvas对象
                canvas = holder.lockCanvas();
                // 设定Canvas对象的背景颜色
                //canvas.drawColor(Color.GREEN);
                //实现透明
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                // 创建画笔
                Paint p = new Paint();
                // 创建一个Rect对象rect
                // public Rect (int left, int top, int right, int bottom)
                Rect rect = new Rect(100, 50, 400, 350);
                // 设置画笔颜色
                p.setColor(Color.RED);
                // 在canvas上绘制rect
                canvas.drawRect(rect, p);
                // 设置文字大小
                p.setTextSize(40);
                p.setColor(Color.GREEN);
                canvas.drawText("this is MySurfaceView",0,100,p);

                // 在canvas上显示时间
                // public void drawText (String text, float x, float y, Paint
                // paint)
                canvas.drawText("时间 = " + (counter++) + " 秒", 500, 200, p);

                if (canvas != null) {
                    // 解除锁定，并提交修改内容，更新屏幕
                    holder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    });
}
