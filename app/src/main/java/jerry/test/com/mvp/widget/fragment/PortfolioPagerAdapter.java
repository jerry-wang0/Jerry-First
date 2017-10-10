package jerry.test.com.mvp.widget.fragment;

import android.support.v4.app.FragmentManager;

import cn.nextop.erebor.mid.app.mvc.main.trading.PortfolioModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.pager.FragmentPagerAdapterEx;

/**
 * Created by Liqun Wan on 2015/7/29.
 */
public class PortfolioPagerAdapter extends FragmentPagerAdapterEx {
    /**
     *
     */
    public PortfolioPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**
     *
     */
    public void refresh(int position, PortfolioModel model, Object hint) {
        ((PortfolioWidget.PortfolioPage)getItem(position)).refresh(hint, model);
    }
}
