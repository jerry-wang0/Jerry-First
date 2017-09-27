package jerry.test.com.mvp.xchart.render.impl.painter.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XRsiConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XRsiIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.rsi;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;
import static java.lang.Double.isNaN;
import static java.lang.Float.NaN;


public class XRsiIndicator extends AbstractXIndicator {
    //
    protected final Paint paint0 = new Paint();

    /**
     *
     */
    public XRsiIndicator() {
        super(IndicatorType.RSI, 100);
        super.addArea(XChartArea.Type.B);
    }

    /**
     *
     */
    @Override
    protected void doSetup(final XChartContext context, Canvas canvas) {
        final float density = getDensity(context);
        final XChartModel model = context.getModel();
        final XChartTheme theme = context.getModel().getTheme();
        final XRsiConfig c = model.getConfig().getIndicator(type);
        float size = theme.getProperty(INDICATOR_TEXT_SIZE, 10.0f);
        float width = theme.getProperty(INDICATOR_LINE_WIDTH, 2.0f);
        setup(this.paint, true, c.getColor(), width, size * density);
        setup(this.paint0, true, c.getColor(0), width, size * density);
    }

    /**
     *
     */
    @Override
    protected void doEval(final XChartContext context, Canvas canvas) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XRsiConfig c = model.getConfig().getIndicator(type);

        //
        final int size = window.size();
        final int period = c.getPeriod(0);
        final XRsiIndex i = new XRsiIndex(model);
        final float p[] = model.getClosePrices(size + period, 0);
        if(c.isVisible(0)) i.setLine0(rsi(p, 0, p.length - 1, period, size));
        window.addIndex(i);
    }

    /**
     *
     */
    @Override
    protected void doDraw(final XChartContext context, Canvas canvas) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XChartRender render = context.getRender();
        final ChartScale scale = model.getConfig().getScale();
        final XChartArea b = render.getArea(XChartArea.Type.B);
        final XRsiConfig c = model.getConfig().getIndicator(type);
        final XRsiIndex i = window.getIndex(type); if(i == null) return;

        // Index
        double prev0 = NaN, curr0 = NaN;
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final double minB = i.getMin(true), maxB = i.getMax(true);
        final double unitB = b.getNetHeight() / subtract(maxB, minB);
        final XChartLines line0 = c.isVisible(0) ? getLines("LINE0") : null;
        final float bl = b.getLeft(), br = b.getRight(), bb = b.getBottom();
        final float wbr = b.getRight() - window.getMargin() * scale.getWidth();
        for(int j = 0, size = window.size(); j < size; j++) {
            final int k = size - j - 1;
            final float x1 = wbr - j * sw + sc;
            float x2 = x1 - sw; if (x2 < bl) break;
            if(c.isVisible(0)) {
                prev0 = curr0; curr0 = i.getLine0(k);
                adds2(x1, prev0, x2, curr0, bb, minB, unitB, line0);
            }
        }
        if (c.isVisible(0)) XChartUtils.drawLines(line0, canvas, this.paint0, true);

        // Cursor
        final int digits = i.getDigits();
        final int cursor = getCursor(context);
        final XChartTheme theme = model.getTheme();
        if(theme.getProperty(INDICATOR_CURSOR, true) && cursor > 0) {
            final List<Cursor> cursors = new ArrayList<>(2);
            cursors.add(new Cursor(c.getName(), null, this.paint));
            final int k = window.size() + window.getMargin() - cursor;
            if(c.isVisible(0)) {
                final String p0 = isNaN((curr0 = i.getLine0(k))) ? "-" : format(curr0, digits);
                cursors.add(new Cursor(c.getName(0) + " (" + c.getPeriod(0) + ")", p0, paint0));
            }
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
