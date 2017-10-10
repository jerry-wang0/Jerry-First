package jerry.test.com.mvp.widget.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.mvc.main.trading.adapter.PlaceOrderPanelPagerAdapter;
import cn.nextop.erebor.mid.app.mvc.main.trading.panel.order.PlaceOrderPanel;

/**
 *
 */
public class PlaceOrderPanelViewPager extends ViewPager {

    private float minMeasureHeight = getResources().getDimension(R.dimen.order_panel_min_height);

    public PlaceOrderPanelViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int wms, int hms) {
        super.onMeasure(wms, hms);

        final PlaceOrderPanelPagerAdapter adapter = (PlaceOrderPanelPagerAdapter) getAdapter();
        final PlaceOrderPanel panel = (adapter.getCount() > 0) ? adapter.getItem(this.getCurrentItem()) : null;

        if (panel != null) {
            panel.measure(wms, hms);
            setMeasuredDimension(getMeasuredWidth(), measureHeight(hms, panel));
        }
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }else{
            result = view.getMeasuredHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result < minMeasureHeight ? (int)minMeasureHeight : result;
    }
}
