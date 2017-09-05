package jerry.test.com.mvp.xchart.render.impl.painter.indicator.config;

import java.io.Serializable;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XIndicatorConfig;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;


public abstract class AbstractXConfig implements Serializable, XIndicatorConfig {
    //
    protected int order;
    protected IndicatorType type;

    /**
     *
     */
    public AbstractXConfig(IndicatorType type) {
        this.type = type;
    }

    public AbstractXConfig(IndicatorType type, int order) {
        this.type = type; this.order = order;
    }

    /**
     *
     */
    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public IndicatorType getType() {
        return type;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     *
     */
    protected void copy(AbstractXConfig src, AbstractXConfig dst) {
        dst.type = src.type; dst.order = src.order;
    }
}
