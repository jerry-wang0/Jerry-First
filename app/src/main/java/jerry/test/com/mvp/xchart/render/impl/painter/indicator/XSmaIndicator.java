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
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XSmaConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XSmaIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.max;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.sma;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;


public class XSmaIndicator extends AbstractXIndicator {
    //
    protected final Paint paint0 = new Paint();
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();

    /**
     *
     */
    public XSmaIndicator() {
        super(IndicatorType.SMA, 100);
        super.addArea(XChartArea.Type.A);
    }

    /**
     *
     */
    @Override
    protected void doSetup(final XChartContext context, Canvas canvas) {
        final float density = getDensity(context);
        final XChartModel model = context.getModel();
        final XChartTheme theme = context.getModel().getTheme();
        final XSmaConfig c = model.getConfig().getIndicator(type);
        float size = theme.getProperty(INDICATOR_TEXT_SIZE, 10.0f);
        float width = theme.getProperty(INDICATOR_LINE_WIDTH, 2.0f);
        setup(this.paint, true, c.getColor(), width, size * density);
        setup(this.paint0, true, c.getColor(0), width, size * density);
        setup(this.paint1, true, c.getColor(1), width, size * density);
        setup(this.paint2, true, c.getColor(2), width, size * density);
    }

    /**
     *
     */
    @Override
    protected void doEval(final XChartContext context, Canvas canvas) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XSmaConfig c = model.getConfig().getIndicator(type);

        //
        final int size = window.size();
        final int max = c.getMaxPeriod();
        final XSmaIndex i = new XSmaIndex(model);
        final float p[] = model.getClosePrices(size + (max - 1), 0);
        final int from0 = max(0, p.length - size - (c.getPeriod(0) - 1));
        final int from1 = max(0, p.length - size - (c.getPeriod(1) - 1));
        final int from2 = max(0, p.length - size - (c.getPeriod(2) - 1));
        if(c.isVisible(0)) i.setLine0(sma(p, from0, p.length - 1, c.getPeriod(0), size));
        if(c.isVisible(1)) i.setLine1(sma(p, from1, p.length - 1, c.getPeriod(1), size));
        if(c.isVisible(2)) i.setLine2(sma(p, from2, p.length - 1, c.getPeriod(2), size));
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
        final XChartArea a = render.getArea(XChartArea.Type.A);
        final XSmaConfig c = model.getConfig().getIndicator(type);
        final XSmaIndex i = window.getIndex(type); if(i == null) return;

        // Index
        double prev0 = 0d, prev1 = 0d, prev2 = 0d;
        double curr0 = 0d, curr1 = 0d, curr2 = 0d;
        final int digitsA = model.getConfig().getDigits();
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final XChartLines line0 = c.isVisible(0) ? getLines("LINE0") : null;
        final XChartLines line1 = c.isVisible(1) ? getLines("LINE1") : null;
        final XChartLines line2 = c.isVisible(2) ? getLines("LINE2") : null;
        final double minA = window.getMin(true), maxA = window.getMax(true);
        final float al = a.getLeft(), ar = a.getRight(), ab = a.getBottom();
        final float war = a.getRight() - window.getMargin() * scale.getWidth();
        final double unitA = a.getNetHeight() / XChartUtils.subtract(maxA, minA);
        for(int j = 0, size = window.size(); j < size; j++) {
            final int k = size - j - 1;
            final float x1 = war - j * sw + sc;
            float x2 = x1 - sw; if (x2 < al) break;
            if(c.isVisible(0)) {
                prev0 = curr0; curr0 = i.getLine0(k);
                adds1(x1, prev0, x2, curr0, ab, minA, unitA, line0);
            }
            if(c.isVisible(1)) {
                prev1 = curr1; curr1 = i.getLine1(k);
                adds1(x1, prev1, x2, curr1, ab, minA, unitA, line1);
            }
            if(c.isVisible(2)) {
                prev2 = curr2; curr2 = i.getLine2(k);
                adds1(x1, prev2, x2, curr2, ab, minA, unitA, line2);
            }
        }
        if (c.isVisible(0)) XChartUtils.drawLines(line0, canvas, this.paint0, true);
        if (c.isVisible(1)) XChartUtils.drawLines(line1, canvas, this.paint1, true);
        if (c.isVisible(2)) XChartUtils.drawLines(line2, canvas, this.paint2, true);

        // Cursor
        final int cursor = getCursor(context);
        final XChartTheme theme = model.getTheme();
        if(theme.getProperty(INDICATOR_CURSOR, true) && cursor > 0) {
            final List<Cursor> cursors = new ArrayList<>(4);
            cursors.add(new Cursor(c.getName(), null, this.paint));
            final int k = window.size() + window.getMargin() - cursor;
            if(c.isVisible(0)) {
                final String p0 = ((curr0 = i.getLine0(k)) <= 0d) ? "-" : format(curr0, digitsA);
                cursors.add(new Cursor(c.getName(0) + " (" + c.getPeriod(0) + ")", p0, this.paint0));
            }
            if(c.isVisible(1)) {
                final String p1 = ((curr1 = i.getLine1(k)) <= 0d) ? "-" : format(curr1, digitsA);
                cursors.add(new Cursor(c.getName(1) + " (" + c.getPeriod(1) + ")", p1, this.paint1));
            }
            if(c.isVisible(2)) {
                final String p2 = ((curr2 = i.getLine2(k)) <= 0d) ? "-" : format(curr2, digitsA);
                cursors.add(new Cursor(c.getName(2) + " (" + c.getPeriod(2) + ")", p2, this.paint2));
            }
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
