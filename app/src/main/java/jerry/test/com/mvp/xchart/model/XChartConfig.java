package jerry.test.com.mvp.xchart.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.ChartStyle;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.Strings;

/**
 * Created by Jingqi Xu on 8/28/15.
 */
public class XChartConfig {
    //
    protected int digits = 3, precision = 6;
    protected ChartType type = ChartType.getDefault();
    protected ChartStyle style = ChartStyle.getDefault();
    protected ChartScale scale = ChartScale.getDefault();
    protected Map<IndicatorType, XIndicatorConfig> indicators = new HashMap<>();

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("type", type)
        .append("scale", scale)
        .append("style", style)
        .append("digits", digits)
        .append("precision", precision)
        .append("indicators", indicators)
        .toString();
    }

    /**
     *
     */
    public int getDigits() {
        return digits;
    }

    public int getPrecision() {
        return precision;
    }

    public ChartType getType() {
        return type;
    }

    public ChartStyle getStyle() {
        return style;
    }

    public ChartScale getScale() {
        return scale;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public void setType(ChartType type) {
        this.type = type;
    }

    public void setScale(ChartScale scale) {
        this.scale = scale;
    }

    public void setStyle(ChartStyle style) {
        this.style = style;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     *
     */
    public IndicatorType getIndicatorType(IndicatorArea area) {
        for(IndicatorType type : indicators.keySet()) {
            if(type.getArea() == area) return type;
        }
        return null;
    }

    public Map<IndicatorType, XIndicatorConfig> getIndicators() {
        return indicators;
    }

    public <T extends XIndicatorConfig> T getIndicator(IndicatorArea area) {
        final IndicatorType type = getIndicatorType(area);
        return Objects.cast(type == null ? null : getIndicator(type));
    }

    public <T extends XIndicatorConfig> T getIndicator(IndicatorType type) {
        return Objects.cast(this.indicators.get(type));
    }

    public void setIndicators(Map<IndicatorType, XIndicatorConfig> indicators) {
        this.indicators = indicators;
    }

    public void setIndicator(final IndicatorArea area, XIndicatorConfig indicator) {
        //
        Iterator<Map.Entry<IndicatorType, XIndicatorConfig>> it;
        for (it = indicators.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<IndicatorType, XIndicatorConfig> e = it.next();
            if (e.getKey().getArea() == area) it.remove(); // Clear the area
        }

        //
        if(indicator != null) this.indicators.put(indicator.getType(), indicator);
    }
}
