package jerry.test.com.mvp.xchart.render.impl.painter.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXPainter;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.ChartStyle;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_SKY_BLUE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR_ASC;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR_DESC;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_CANDLE_COLOR_EVEN;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_LINE_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_OHLC_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CHART_OHLC_WIDTH;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public class XGraphPainter extends AbstractXPainter {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(XGraphPainter.class);

    //
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();
    protected final Paint paint3 = new Paint();
    protected final Paint paint4 = new Paint();
    protected Map<ChartStyle, Paint> paints = new HashMap<>();

    /**
     *
     */
    public XGraphPainter() {
        super("painter.graph", 3);
        super.addArea(XChartArea.Type.A);
    }

    /**
     *
     */
    @Override
    protected void doSetup(final XChartContext context, Canvas canvas) {
        //
        this.paints.clear();
        this.paints.put(ChartStyle.CANDLE, paint);
        final Paint p1 = new Paint(), p2 = new Paint();
        final XChartTheme t = context.getModel().getTheme();
        p1.setStrokeWidth(t.getProperty(CHART_LINE_WIDTH, 3.0f));
        p2.setStrokeWidth(t.getProperty(CHART_OHLC_WIDTH, 1.0f));
        p2.setColor(t.getProperty(CHART_OHLC_COLOR, COLOR_SKY_BLUE));
        p1.setColor(t.getProperty(CHART_LINE_COLOR, COLOR_SKY_BLUE));
        this.paints.put(ChartStyle.LINE, p1); p1.setAntiAlias(true);
        this.paints.put(ChartStyle.OHLC, p2); p2.setAntiAlias(false);

        //
        final XChartTheme theme = context.getModel().getTheme();
        paint1.setColor(theme.getProperty(CHART_CANDLE_COLOR, Color.WHITE));
        paint2.setColor(theme.getProperty(CHART_CANDLE_COLOR_DESC, Color.RED));
        paint3.setColor(theme.getProperty(CHART_CANDLE_COLOR_ASC, Color.GREEN));
        paint4.setColor(theme.getProperty(CHART_CANDLE_COLOR_EVEN, Color.WHITE));
    }

    /**
     *
     */
    @Override
    protected void doDraw(final XChartContext context, final Canvas canvas) {
        //
        if(!context.getModel().hasWindow()) return;
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XChartRender render = context.getRender();
        final ChartScale scale = model.getConfig().getScale();
        final ChartStyle style = model.getConfig().getStyle();
        final XChartArea a = render.getArea(XChartArea.Type.A);

        //
        int index = 0; XChart prev = null, curr = null;
        final float ar = a.getRight(), ab = a.getBottom();
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final float war = ar - window.getMargin() * scale.getWidth();
        final double minA = window.getMin(true), maxA = window.getMax(true);
        final double unitA = a.getNetHeight() / XChartUtils.subtract(maxA, minA);
        final float snw = scale.getNetWidth(), snw1 = snw * 0.5f, so = scale.getOffset();
        final XChartLines line = style == ChartStyle.LINE ? getLines(style.name()) : null;
        final XChartLines ohlc = style == ChartStyle.OHLC ? getLines(style.name()) : null;
        for(final Iterator<XChart> iterator = window.iterate(false); iterator.hasNext(); ) {
            index++; prev = curr; curr = iterator.next();
            if(style == ChartStyle.LINE) {
                if(prev == null) continue;
                final float x1 = war - (index - 1) * sw + sc;
                line.adds(x1, ab - (prev.getClose() - minA) * unitA);
                line.adds(x1 - sw, ab - (curr.getClose() - minA) * unitA);
            } else if(style == ChartStyle.OHLC) {
                final float x1 = war - index * sw + sc;
                double yo = ab - (curr.getOpen() - minA) * unitA;
                double yc = ab - (curr.getClose() - minA) * unitA;
                ohlc.adds(x1, ab - (curr.getLow() - minA) * unitA);
                ohlc.adds(x1, ab - (curr.getHigh() - minA) * unitA);
                ohlc.adds(x1, yo, x1 - snw1, yo); ohlc.adds(x1, yc, x1 + snw1, yc);
            } else if(style == ChartStyle.CANDLE) {
                float x0 = war - index * sw;
                float x1 = x0 + so, x2 = x0 + sc;
                final double yl = ab - (curr.getLow() - minA) * unitA;
                final double yh = ab - (curr.getHigh() - minA) * unitA;
                final double yo = ab - (curr.getOpen() - minA) * unitA;
                final double yc = ab - (curr.getClose() - minA) * unitA;
                XChartUtils.drawLine(x2, yl, x2, yh, canvas, this.paint1);
                if(yo < yc) {
                    XChartUtils.drawRect(x1, yo, x1 + snw, yc, canvas, this.paint2);
                } else if(yo > yc) {
                    XChartUtils.drawRect(x1, yo, x1 + snw, yc, canvas, this.paint3);
                } else { // Line instead of Rect
                    XChartUtils.drawLine(x1, yo, x1 + snw, yc, canvas, this.paint4);
                }
            }
        }

        //
        if(style == ChartStyle.LINE) XChartUtils.drawLines(line, canvas, this.paints.get(style), true);
        if(style == ChartStyle.OHLC) XChartUtils.drawLines(ohlc, canvas, this.paints.get(style), true);
    }
}
