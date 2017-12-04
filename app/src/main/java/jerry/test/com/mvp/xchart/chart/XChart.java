package jerry.test.com.mvp.xchart.chart;

import java.util.Comparator;
import java.util.Date;

import cn.nextop.erebor.mid.common.glossary.TradingDate;
import cn.nextop.erebor.mid.common.util.Comparators;

/**
 * Created by Jingqi Xu on 8/28/15.
 */
public interface XChart {

    /**
     *
     */
    long getId();

    float getLow();

    float getHigh();

    float getOpen();

    float getClose();

    <T> T getCookie();

    Date getChartTime();

    TradingDate getChartDate();

    void setCookie(Object cookie);

    /**
     *
     */
    Comparator<XChart> ASC = new Comparator<XChart>() {
        @Override
        public int compare(final XChart lhs, final XChart rhs) {
            return Comparators.cmp(lhs.getId(), rhs.getId(), true);
        }
    };
}
