package jerry.test.com.mvp.xchart.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.chart.XAreasPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.chart.XCursorPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.chart.XDrawingPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.chart.XFramePainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.chart.XGraphPainter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XBollIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XDmiIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XEmaIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XIkhIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XKdjIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XMacdIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XRsiIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.XSmaIndicator;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Strings;

import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.BOLL;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.DMI;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.EMA;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.IKH;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.KDJ;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.MACD;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.RSI;
import static cn.nextop.erebor.mid.common.glossary.chart.IndicatorType.SMA;

public class XChartTheme {
    //
    public static String ZOOM_STEP = "ZOOM_STEP";
    public static String ZOOM_SPEED = "ZOOM_SPEED";
    public static String MOVE_SPEED = "MOVE_SPEED";
    public static String MOVE_TIMEOUT = "MOVE_TIMEOUT";
    public static String CURSOR_WIDTH = "CURSOR_WIDTH";
    public static String CURSOR_COLOR = "CURSOR_COLOR";
    public static String AREA_B_RATIO = "AREA_B_RATIO";
    public static String AREA_B_STEPS = "AREA_B_STEPS";
    public static String AREA_A_MARGIN_Y = "AREA_A_MARGIN_Y";
    public static String AREA_B_MARGIN_Y = "AREA_B_MARGIN_Y";

    //
    public static String COORDINATE_X_STEP = "COORDINATE_X_STEP";
    public static String COORDINATE_Y_STEP = "COORDINATE_Y_STEP";
    public static String COORDINATE_X_GRID = "COORDINATE_X_GRID";
    public static String COORDINATE_Y_GRID = "COORDINATE_Y_GRID";
    public static String COORDINATE_X_MARGIN = "COORDINATE_X_MARGIN";
    public static String COORDINATE_Y_MARGIN = "COORDINATE_Y_MARGIN";
    public static String COORDINATE_TEXT_SIZE = "COORDINATE_TEXT_SIZE";
    public static String COORDINATE_COLOR_GRID = "COORDINATE_COLOR_GRID";
    public static String COORDINATE_COLOR_FRAME = "COORDINATE_COLOR_FRAME";
    public static String COORDINATE_COLOR_AREA_BG = "COORDINATE_COLOR_AREA_BG";
    public static String COORDINATE_COLOR_AREA_FRAME_BG = "COORDINATE_COLOR_AREA_FRAME_BG";

    //
    public static String CHART_LINE_WIDTH = "CHART_LINE_WIDTH";
    public static String CHART_LINE_COLOR = "CHART_LINE_COLOR";
    public static String CHART_OHLC_WIDTH = "CHART_OHLC_WIDTH";
    public static String CHART_OHLC_COLOR = "CHART_OHLC_COLOR";
    public static String CHART_CANDLE_COLOR = "CHART_CANDLE_COLOR";
    public static String CHART_CANDLE_COLOR_ASC = "CHART_CANDLE_COLOR_ASC";
    public static String CHART_CANDLE_COLOR_DESC = "CHART_CANDLE_COLOR_DESC";
    public static String CHART_CANDLE_COLOR_EVEN = "CHART_CANDLE_COLOR_EVEN";

    //
    public static String INDICATOR_CURSOR = "INDICATOR_CURSOR";
    public static String INDICATOR_CURSOR_X = "INDICATOR_CURSOR_X";
    public static String INDICATOR_CURSOR_Y = "INDICATOR_CURSOR_Y";
    public static String INDICATOR_TEXT_SIZE = "INDICATOR_TEXT_SIZE";
    public static String INDICATOR_LINE_WIDTH = "INDICATOR_LINE_WIDTH";
    public static String INDICATOR_FILL_ALPHA = "INDICATOR_FILL_ALPHA";
    public static String INDICATOR_FILL_COLOR = "INDICATOR_FILL_COLOR";
    public static String INDICATOR_CURSOR_ALPHA = "INDICATOR_CURSOR_ALPHA";
    public static String INDICATOR_CURSOR_COLOR = "INDICATOR_CURSOR_COLOR";
    public static String INDICATOR_CURSOR_X_SPACE = "INDICATOR_CURSOR_X_SPACE";
    public static String INDICATOR_CURSOR_Y_SPACE = "INDICATOR_CURSOR_Y_SPACE";

    public static String DRAWING_PAINTER_EMPTY_COLOR = "DRAWING_PAINTER_EMPTY_COLOR";
    public static String DRAWING_PAINTER_VIRTUAL_COLOR = "DRAWING_PAINTER_VIRTUAL_COLOR";
    public static String DRAWING_PAINTER_NORMAL_COLOR = "DRAWING_PAINTER_NORMAL_COLOR";
    public static String DRAWING_PAINTER_HIGHLIGHT_COLOR = "DRAWING_PAINTER_HIGHLIGHT_COLOR";
    public static String DRAWING_PAINTER_TEXT_SIZE = "DRAWING_PAINTER_TEXT_SIZE";
    public static String DRAWING_PAINTER_LINE_WIDTH = "DRAWING_PAINTER_LINE_WIDTH";
    public static String DRAWING_PAINTER_REDRAW_WIDTH = "DRAWING_PAINTER_REDRAW_WIDTH";
    public static String DRAWING_PAINTER_REDRAW_HEIGHT = "DRAWING_PAINTER_REDRAW_HEIGHT";
    public static String DRAWING_DEPOT_RADIUS = "DRAWING_DEPOT_RADIUS";
    public static String DRAWING_TOUCH_SENSITIVITY = "DRAWING_TOUCH_SENSITIVITY";
    public static String DRAWING_LINE_DOUBLE_SPACE_V = "DRAWING_LINE_DOUBLE_SPACE_V";
    //
    protected List<XChartPainter> painters = new ArrayList<>();
    protected Map<String, Object> properties = new HashMap<>(32);
    protected Map<IndicatorType, XChartPainter> indicators = new HashMap<>(32);

    /**
     *
     */
    public XChartTheme() {
        //
        this.painters.add(new XAreasPainter());
        this.painters.add(new XFramePainter());
        this.painters.add(new XGraphPainter());
        this.painters.add(new XCursorPainter());
        this.painters.add(new XDrawingPainter());

        //
        this.indicators.put(SMA, new XSmaIndicator());
        this.indicators.put(EMA, new XEmaIndicator());
        this.indicators.put(DMI, new XDmiIndicator());
        this.indicators.put(IKH, new XIkhIndicator());
        this.indicators.put(KDJ, new XKdjIndicator());
        this.indicators.put(RSI, new XRsiIndicator());
        this.indicators.put(BOLL, new XBollIndicator());
        this.indicators.put(MACD, new XMacdIndicator());
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("painters", painters)
        .append("indicators", indicators)
        .append("properties", properties).toString();
    }

    /**
     *
     */
    public List<XChartPainter> getPainters() {
        return painters;
    }

    public void setPainters(List<XChartPainter> painters) {
        this.painters = painters;
    }

    /**
     *
     */
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public <T> T getProperty(final String key, T value) {
        boolean r = this.properties.containsKey(key);
        return !r ? value : (T)this.properties.get(key);
    }

    /**
     *
     */
    public XChartPainter getIndicator(IndicatorType type) {
        return this.indicators.get(type);
    }

    public Map<IndicatorType, XChartPainter> getIndicators() {
        return indicators;
    }

    public void setIndicators(Map<IndicatorType, XChartPainter> indicators) {
        this.indicators = indicators;
    }
}
