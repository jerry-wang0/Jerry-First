package jerry.test.com.mvp.widget.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wanggl on 2016/8/24.
 */
public class ViewPagerEx extends ViewPager {

    private volatile float x;
    private volatile float offseting;

    public ViewPagerEx(Context context) {
        super(context);
    }

    public ViewPagerEx(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOffseting(float offseting) {
        this.offseting = offseting;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: this.x = event.getX(); break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(this.x - event.getX()) < offseting) { return false; } break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onMeasure(int wm, int hm) {
        super.onMeasure(wm, hm);
        View view = getChildAt(0);
        if (view != null) view.measure(wm, hm);
        setMeasuredDimension(getMeasuredWidth(), measureHeight(hm, view));
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (view != null)  result = view.getMeasuredHeight();
            if (specMode == MeasureSpec.AT_MOST) result = Math.min(result, specSize);
        }
        return result;
    }
}
