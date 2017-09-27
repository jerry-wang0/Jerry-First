package jerry.test.com.mvp.widget.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.nextop.erebor.mid.app.mvc.main.trading.ExecutionHistoryModel;
import cn.nextop.erebor.mid.app.mvc.main.trading.fragment.ExecutionHistoryDetail_;

/**
 * Created by Liqun Wan on 2015/8/21.
 */
public class ExecutionHistoryPagerAdapter extends FragmentPagerAdapter {
    //
    protected final List<ExecutionHistoryAdaptable> executions;

    /**
     *
     */
    public ExecutionHistoryPagerAdapter(FragmentManager fm, ExecutionHistoryModel m) {
        super(fm); this.executions = m.toAdaptables();
    }

    /**
     *
     */
    public void reset() {
        this.executions.clear();
        this.notifyDataSetChanged();
    }

    /**
     *
     */
    @Override
    public int getCount() {
        return this.executions.size();
    }

    /**
     *
     */
    @Override
    public Fragment getItem(final int position) {
        if(position < 0 || position > this.executions.size()) {
            return ExecutionHistoryDetail_.builder().build();
        } else {
            return ExecutionHistoryDetail_.builder().adaptable(executions.get(position)).build();
        }
    }
}
