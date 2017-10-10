package jerry.test.com.mvp.widget.fragment;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionRes;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.mvc.main.trading.fragment.PortfolioContract;
import cn.nextop.erebor.mid.app.mvc.main.trading.fragment.PortfolioContractSummary;
import cn.nextop.erebor.mid.app.mvc.main.trading.fragment.PortfolioContractSummary_;
import cn.nextop.erebor.mid.app.mvc.main.trading.fragment.PortfolioContract_;
import cn.nextop.erebor.mid.app.mvc.support.AbstractWidget;
import cn.nextop.erebor.mid.app.mvc.support.widget.pager.ViewPagerEx;
import cn.nextop.erebor.mid.app.mvc.support.widget.utility.ViewPagers;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.collection.map.IntHashMap;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.event.QuoteListener;
import cn.nextop.erebor.mid.rpc.service.domain.pricing.model.QuoteVo;
import cn.nextop.erebor.mid.rpc.service.domain.trading.event.PositionListener;
import cn.nextop.erebor.mid.rpc.service.domain.trading.model.TradingPositionVo;

import static cn.nextop.erebor.mid.app.mvc.View.ID;
import static cn.nextop.erebor.mid.app.mvc.Widget.Hint.DESTROY;
import static cn.nextop.erebor.mid.app.mvc.main.trading.PortfolioModel.Page.CONTRACT;
import static cn.nextop.erebor.mid.app.mvc.main.trading.PortfolioModel.Page.SUMMARY;

/**
 * Created by Liqun Wan on 2015/8/24.
 */
@EFragment(R.layout.widget_trading_portfolio)
public class PortfolioWidget extends AbstractWidget {

    public interface PortfolioPage {
        void refresh(Object hint, PortfolioModel model);
    }

    @ViewById(R.id.item_inc_header_left_button) protected Button hbLeft;
    @ViewById(R.id.item_inc_header_right_button) protected Button hbRight;
    @ViewById(R.id.item_inc_header_tab_left) protected RadioButton rbLeft;
    @ViewById(R.id.item_inc_header_tab_right) protected RadioButton rbRight;
    @ViewById(R.id.item_trading_portfolio_pager) protected ViewPagerEx pager;
    @ViewById(R.id.item_inc_header_tab_center) protected RadioButton rbCenter;
    @DimensionRes(R.dimen.portfolio_contract_row_urgency_width) protected float offset;

    //
    protected PortfolioPagerAdapter adapter;
    protected final PortfolioContract contract = PortfolioContract_.builder().build();
    protected final PortfolioContractSummary summary = PortfolioContractSummary_.builder().build();

    /**
     *
     */
    @Click(R.id.item_inc_header_tab_left)
    protected void onPageSummary() {
        onPage(null, SUMMARY);
    }

    @Click(R.id.item_inc_header_tab_right)
    protected void onPageContract() {
        onPage(null, CONTRACT);
    }

    /**
     *
     */
    @Override
    @UiThread
    public void prevHide(ID target, boolean destroy) {
        //
        super.prevHide(target, destroy);
        final PortfolioModel model = this.view.getModel();
        model.setQuoteListener(null); model.setPositionListener(null);
        model.reset(); summary.refresh(DESTROY, model); contract.refresh(DESTROY, model);
    }

    @Override
    public void postShow(ID source, boolean create) {
        //
        super.postShow(source, create);
        final PortfolioModel model = view.getModel();
        if (this.pager != null) this.onPage(model.getPage(), true);
        final PortfolioController controller = view.getController();

        //
        model.setQuoteListener(new QuoteListener() {
            @Override
            public void onQuotes(IntHashMap<QuoteVo> quotes) {
                if (!model.getStatus().isActive()) return;
                controller.evaluate(model); onRefresh(Hint.REFRESH); // Refresh
            }
        });
        model.setPositionListener(new PositionListener() {
            @Override
            public void onPosition(final TradingPositionVo position) {
                if (!model.getStatus().isActive()) return;
                controller.evaluate(model); onRefresh(Hint.RESTART); // Restart
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.hbLeft.setVisibility(View.GONE);
        this.rbRight.setText(R.string.trading_portfolio_contract);
        this.rbLeft.setText(R.string.trading_portfolio_contract_summary);
        final FragmentManager manager = getChildFragmentManager();

        this.pager.setOffseting(offset);
        final PortfolioView v = Objects.cast(view);
        final PortfolioModel model = view.getModel();
        this.contract.setView(v); this.summary.setView(v);
        this.adapter = new PortfolioPagerAdapter(manager);
        this.adapter.addItem(this.pager, this.summary, true);
        this.adapter.addItem(this.pager, this.contract, true);
        this.pager.setOffscreenPageLimit(this.adapter.getCount() - 1);
        this.pager.setAdapter(adapter); this.onPage(model.getPage(), false);
        this.pager.addOnPageChangeListener(new ViewPagers.OnPageChangeAdapter() {
            @Override
            public void onPageSelected(final int index) {
                for (PortfolioModel.Page page : PortfolioModel.Page.values()) {
                    if (page.ordinal() == index) { onPage(page, true); break; }
                }
            }
        });
    }

    /**
     *
     */
    @Override
    protected void onRefresh(final Object hint) {
        final PortfolioModel model = view.getModel();
        this.adapter.refresh(this.pager.getCurrentItem(), model, hint);
    }

    /**
     *
     */
    public void onPage(Short symbolId, PortfolioModel.Page page) {
        if(this.pager == null) return;
        final PortfolioModel model = view.getModel();
        model.setSymbolId(symbolId); this.pager.setCurrentItem(page.ordinal());
    }

    /**
     *
     */
    protected void onPage(PortfolioModel.Page page, boolean force) {
        if(this.pager == null) return;
        this.pager.setCurrentItem(page.ordinal());
        final PortfolioModel model = view.getModel();
        model.setSwipedId(0L); model.getContracts().clear();
        switch (page) {
            case SUMMARY:
                this.rbLeft.setChecked(true); if(force) onRefresh(Hint.SHOW); break;
            case CONTRACT:
                this.rbRight.setChecked(true); if(force) onRefresh(Hint.SHOW); break;
        }
    }
}
