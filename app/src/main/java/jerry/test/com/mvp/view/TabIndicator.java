package jerry.test.com.mvp.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import cn.nextop.erebor.mid.R;

/**
 * Created by wanggl on 2016/8/25.
 */
public class TabIndicator extends View implements ViewPager.OnPageChangeListener {

    private Bitmap arrow;
    private ViewPager pager;
    private TabLayout  tabLayout;
    private final Paint paint = new Paint();
    private int left, top, arrowWidth, screenWidth;

    public TabIndicator(Context context) {
        super(context);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);init();
    }

    private void init(){
        this.arrow = BitmapFactory.decodeResource(getResources(), R.mipmap.tab_indicator_arrow);
        this.screenWidth = getScreenWith(); this.arrowWidth = this.arrow.getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.arrow, this.left, this.top, paint);
    }

    public void setupWith(final TabLayout tabLayout, final ViewPager pager){
        this.tabLayout = tabLayout; this.pager = pager;
        this.pager.addOnPageChangeListener(this);
    }

    private void setPosition(int count, int position){
        View tabView = null;
        if(this.tabLayout != null && count > 0) {
            final ViewGroup vg = (ViewGroup) this.tabLayout.getChildAt(0);
            tabView =  vg != null ? vg.getChildAt(position) : null;
        }
        if(tabView == null) return ;
//        int width = tabView.getWidth();
        int width = this.screenWidth / count;
        this.top   = tabView.getTop() + getPaddingTop();
        this.left = tabView.getLeft() + (width - this.arrowWidth) / 2;
    }

    public void notifyDataSetChanged(int count, int position) {
        setPosition(count, position); invalidate();
    }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        notifyDataSetChanged(this.tabLayout.getTabCount(), this.pager.getCurrentItem());
    }

    private int getScreenWith() {
        WindowManager windowManager=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point(); windowManager.getDefaultDisplay().getSize(point); return point.x;
    }
}
