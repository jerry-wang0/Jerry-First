package jerry.test.com.mvp.widget.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.domain.storage.impl.bean.SymbolSetting;
import cn.nextop.erebor.mid.app.mvc.main.other.MenuView;
import cn.nextop.erebor.mid.app.mvc.main.trading.adapter.ExecutionHistoryAdapter;
import cn.nextop.erebor.mid.app.mvc.support.AbstractWidget;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.PullToRefreshRecyclerViewEx;
import cn.nextop.erebor.mid.app.mvc.support.widget.list.recycler.DividerDottedLineDecoration;
import cn.nextop.erebor.mid.app.mvc.support.widget.pull2refresh.PullToRefreshBase;
import cn.nextop.erebor.mid.app.mvc.support.widget.utility.PullToRefreshs;
import cn.nextop.erebor.mid.common.glossary.enums.Side;
import cn.nextop.erebor.mid.common.glossary.enums.TradeType;
import cn.nextop.erebor.mid.common.glossary.enums.TradingQueryPeriod;

/**
 * Created by wanggl on 2016/7/1.
 */
@EFragment(R.layout.widget_trading_execution_history)
public class ExecutionHistoryWidget extends AbstractWidget {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionHistoryWidget.class);

    @ViewById(R.id.item_inc_header_with_search_center) protected TextView tvTitle;
    @ViewById(R.id.item_trading_execution_history_rows) protected PullToRefreshRecyclerViewEx pullRefresh;

    //
    protected ExecutionHistoryAdapter adapter;
    protected ExecutionHistorySearch search = ExecutionHistorySearch_.builder().build();
    protected ExecutionHistoryDialog detail = ExecutionHistoryDialog_.builder().build();

    /**
     *
     */
    @Click(R.id.item_inc_header_with_search_right)
    protected void onRight() {
        search();
    }

    @Click(R.id.item_inc_header_with_search_left)
    protected void onLeft() {
        view.getManager().open(view.getId(), MenuView.ID, null, false);
    }

    /**
     *
     */
    @Override
    protected void hideProgress() {
        super.hideProgress();
        if(this.pullRefresh != null) this.pullRefresh.onRefreshComplete();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        this.tvTitle.setText(R.string.trading_execution_history);
        this.pullRefresh.setMode(PullToRefreshBase.Mode.BOTH);

        RecyclerView rvRows = pullRefresh.getRefreshableView();
        rvRows.setLayoutManager(new LinearLayoutManager(activity));
        rvRows.setAdapter(adapter = new ExecutionHistoryAdapter(this));
        rvRows.addItemDecoration(new DividerDottedLineDecoration(activity));

        this.pullRefresh.setOnPullEventFilter(new PullToRefreshRecyclerViewEx.OnPullEventFilter() {
            @Override
            public boolean isPullable(PullToRefreshBase base, PullToRefreshBase.Mode mode) {
                final ExecutionHistoryModel model = view.getModel();
                return mode == PullToRefreshBase.Mode.PULL_FROM_END ? model.isIterable() : true;
            }
        });

        //
        this.pullRefresh.setOnRefreshListener(new PullToRefreshs.OnPullRefreshAdapter<RecyclerView>(){

            @Override
            protected void onPullUp(PullToRefreshBase p2r) { iterate();}

            @Override
            protected void onPullDown(PullToRefreshBase p2r) { refresh(); }
        });


    }

    @Override
    protected void onRefresh(Object hint) {
        adapter.refresh((ExecutionHistoryModel)view.getModel());
    }

    /**
     *
     */
    public final void detail(final int position) {
        final ExecutionHistoryModel model = view.getModel();
        detail.show(ExecutionHistoryWidget.this, model, position);
    }

    /**
     *
     */
    protected final void refresh() {
        final ExecutionHistoryModel model = view.getModel();
        if(!model.getStatus().isActive()) { hideProgress(); return; }
        showProgress(); ExecutionHistoryController c = view.getController();
        c.restart(model, view, false).setListener(new RemotingCallback<Boolean>() {
            @Override
            public final Boolean call() throws Exception { onRefresh(Hint.RESTART); return true; }
        });
    }

    /**
     *
     */
    protected final void iterate() {
        showProgress(); final ExecutionHistoryController c = view.getController();
        c.iterate(view.getModel(), view).setListener(new RemotingCallback<Boolean>() {
            @Override
            public final Boolean call() throws Exception { onRefresh(Hint.REFRESH); return true; }
        });
    }

    /**
     *
     */
    public final void search() {
        final ExecutionHistoryModel model = view.getModel();
        search.show(ExecutionHistoryWidget.this, model, new ExecutionHistorySearch.OnSelectListener() {
            @Override
            public void onSelect(SymbolSetting symbol, TradeType type, TradingQueryPeriod period, Side side) {
                final ExecutionHistoryController c = view.getController();
                showProgress(); model.setPeriod(period); model.setSide(side);
                model.setType(type); model.setSymbolId(symbol == null ? null : symbol.getSymbolId());
                c.restart(model, view, false).setListener(new RemotingCallback<Boolean>() {
                    @Override
                    public final Boolean call() throws Exception { onRefresh(Hint.RESTART); return true; }
                });
            }
        });
    }
}
