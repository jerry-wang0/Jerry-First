package jerry.test.com.mvp.xchart.model;

import java.util.Comparator;

import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Comparators;

public interface XIndicatorConfig {

    /**
     *
     */
    void reset();

    int getOrder();

    IndicatorType getType();

    /**
     *
     */
    Comparator<XIndicatorConfig> ASC = new Comparator<XIndicatorConfig>() {
        @Override
        public int compare(XIndicatorConfig lhs, XIndicatorConfig rhs) {
            return Comparators.cmp(lhs.getOrder(), rhs.getOrder(), true);
        }
    };
}
