package jerry.test.com.mvp.xchart.model;

import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;


public interface XChartIndex {

    float getUnit();

    int getDigits();

    int getPrecision();

    IndicatorType getType();

    double getMin(boolean scale);

    double getMax(boolean scale);
}
