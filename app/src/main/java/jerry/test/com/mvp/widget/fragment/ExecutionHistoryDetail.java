package jerry.test.com.mvp.widget.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.mvc.main.trading.adapter.ExecutionHistoryAdaptable;
import cn.nextop.erebor.mid.app.mvc.main.trading.adapter.ExecutionHistoryDetailAdapter;
import cn.nextop.erebor.mid.app.mvc.support.FragmentEx;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.recycler.DividerDottedLineDecoration;

/**
 * Created by Liqun Wan on 2015/8/21.
 */
@EFragment(R.layout.widget_trading_execution_history_detail)
public class ExecutionHistoryDetail extends FragmentEx {
    //
    protected ExecutionHistoryDetailAdapter adapter;
    @FragmentArg("adaptable") protected ExecutionHistoryAdaptable adaptable;
    @ViewById(R.id.item_trading_execution_history_detail_rows) protected RecyclerView rvRows;

    /**
     *
     */
    @AfterViews
    protected void afterViews() {
        //
        this.rvRows.setLayoutManager(new LinearLayoutManager(activity));
        this.rvRows.addItemDecoration(new DividerDottedLineDecoration(activity));
        this.rvRows.setAdapter(adapter = new ExecutionHistoryDetailAdapter(this));

        //
        this.adapter.refresh(this.adaptable);
    }
}

