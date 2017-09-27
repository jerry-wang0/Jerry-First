package jerry.test.com.mvp.widget.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.mvc.support.widget.dialog.DialogFragmentEx;
import cn.nextop.erebor.mid.app.mvc.support.widget.utility.ViewPagers;

/**
 * Created by Liqun Wan on 2015/8/20.
 */
@EFragment(R.layout.widget_trading_execution_history_dialog)
public class ExecutionHistoryDialog extends DialogFragmentEx {
    //
    protected static final String TAG = ExecutionHistoryDialog.class.getSimpleName();

    //
    protected int position;
    protected ExecutionHistoryModel model;
    protected ExecutionHistoryPagerAdapter adapter;
    @ViewById(R.id.item_inc_pagination_index) protected TextView tvIndex;
    @ViewById(R.id.item_inc_dialog_header_title) protected TextView tvTitle;
    @ViewById(R.id.item_trading_execution_history_dialog_pager) protected ViewPager pager;

    @Click(R.id.item_inc_pagination_prev)
    protected void onPrev() { onPage(pager.getCurrentItem() - 1, true, true); }

    @Click(R.id.item_inc_pagination_next)
    protected void onNext() { onPage(pager.getCurrentItem() + 1, true, true); }

    //
    @Click(R.id.item_inc_detail_bottom_back)
    protected void onBack() { dismissAllowingStateLoss(); }

    /**
     *
     */
    @Override
    protected ExecutionHistoryModel getModel() {
        return this.model;
    }

    /**
     *
     */
    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        this.model = null; this.adapter.reset();
    }

    /**
     *
     */
    @UiThread
    public void show(Fragment f, ExecutionHistoryModel m, int index) {
        if(this.visible) return;
        this.position = index; this.model = m;
        this.show(f.getChildFragmentManager(), TAG);
    }

    /**
     *
     */
    @UiThread
    protected void onPage(int index, boolean force, boolean scroll) {
        final int count = adapter.getCount();
        if(index < 0 || index >= count) return;
        this.tvIndex.setText((index + 1) + "/" + count);
        if(force) this.pager.setCurrentItem(position = index, scroll);
    }

    /**
     *
     */
    @Override
    protected void onCreate() {
        //
        super.onCreate();
        this.tvTitle.setText(R.string.trading_execution_history_detail);

        final FragmentManager manager = getChildFragmentManager();
        this.adapter = new ExecutionHistoryPagerAdapter(manager, this.model);
        this.pager.addOnPageChangeListener(new ViewPagers.OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int index) { onPage(index, false, false); }
        });

        //
        setCancelable(true); pager.setAdapter(adapter); onPage(position, true, false);
    }
}
