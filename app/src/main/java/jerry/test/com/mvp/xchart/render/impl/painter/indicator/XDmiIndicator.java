package jerry.test.com.mvp.xchart.render.impl.painter.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXIndicator;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XDmiConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XDmiIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.drawLines;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.adx;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.adxr;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.mdi;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.pdi;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;
import static java.lang.Double.isNaN;
import static java.lang.Float.NaN;


public class XDmiIndicator extends AbstractXIndicator {
    //
    protected final Paint paint0 = new Paint();
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();
    protected final Paint paint3 = new Paint();

    /**
     *
     */
    public XDmiIndicator() {
        super(IndicatorType.DMI, 100);
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
        final XDmiConfig c = model.getConfig().getIndicator(type);
        float size = theme.getProperty(INDICATOR_TEXT_SIZE, 10.0f);
        float width = theme.getProperty(INDICATOR_LINE_WIDTH, 2.0f);
        setup(this.paint, true, c.getColor(), width, size * density);
        setup(this.paint0, true, c.getColor(0), width, size * density);
        setup(this.paint1, true, c.getColor(1), width, size * density);
        setup(this.paint2, true, c.getColor(2), width, size * density);
        setup(this.paint3, true, c.getColor(3), width, size * density);
    }

    /**
     *
     */
    @Override
    protected void doEval(final XChartContext context, Canvas canvas) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XDmiConfig c = model.getConfig().getIndicator(type);

        //
        final int size = window.size();
        final int period = c.getMaxPeriod();
        final XDmiIndex i = new XDmiIndex(model);
        final float p[][] = model.getPrices(size + (3 * period - 2), 0); int length = p[0].length;
        if(c.isVisible(0)) i.setLine0(mdi(p[0], p[1], p[3], 0, length - 1, c.getPeriod(0), size));
        if(c.isVisible(1)) i.setLine1(pdi(p[0], p[1], p[3], 0, length - 1, c.getPeriod(1), size));
        if(c.isVisible(2)) i.setLine2(adx(p[0], p[1], p[3], 0, length - 1, c.getPeriod(2), size));
        if(c.isVisible(3)) i.setLine3(adxr(p[0], p[1], p[3], 0, length - 1, c.getPeriod(3), size));
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
        final XDmiConfig c = model.getConfig().getIndicator(type);
        final XDmiIndex i = window.getIndex(type); if(i == null) return;

        // Index
        double prev0 = NaN, prev1 = NaN, prev2 = NaN, prev3 = NaN;
        double curr0 = NaN, curr1 = NaN, curr2 = NaN, curr3 = NaN;
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final double minB = i.getMin(true), maxB = i.getMax(true);
        final double unitB = b.getNetHeight() / subtract(maxB, minB);
        final XChartLines line0 = c.isVisible(0) ? getLines("LINE0") : null;
        final XChartLines line1 = c.isVisible(1) ? getLines("LINE1") : null;
        final XChartLines line2 = c.isVisible(2) ? getLines("LINE2") : null;
        final XChartLines line3 = c.isVisible(3) ? getLines("LINE3") : null;
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
            if(c.isVisible(1)) {
                prev1 = curr1; curr1 = i.getLine1(k);
                adds2(x1, prev1, x2, curr1, bb, minB, unitB, line1);
            }
            if(c.isVisible(2)) {
                prev2 = curr2; curr2 = i.getLine2(k);
                adds2(x1, prev2, x2, curr2, bb, minB, unitB, line2);
            }
            if(c.isVisible(3)) {
                prev3 = curr3; curr3 = i.getLine3(k);
                adds2(x1, prev3, x2, curr3, bb, minB, unitB, line3);
            }
        }
        if (c.isVisible(0)) drawLines(line0, canvas, this.paint0, true);
        if (c.isVisible(1)) drawLines(line1, canvas, this.paint1, true);
        if (c.isVisible(2)) drawLines(line2, canvas, this.paint2, true);
        if (c.isVisible(3)) drawLines(line3, canvas, this.paint3, true);

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
            if(c.isVisible(1)) {
                final String p1 = isNaN((curr1 = i.getLine1(k))) ? "-" : format(curr1, digits);
                cursors.add(new Cursor(c.getName(1) + " (" + c.getPeriod(1) + ")", p1, paint1));
            }
            if(c.isVisible(2)) {
                final String p2 = isNaN((curr2 = i.getLine2(k))) ? "-" : format(curr2, digits);
                cursors.add(new Cursor(c.getName(2) + " (" + c.getPeriod(2) + ")", p2, paint2));
            }
            if(c.isVisible(3)) {
                final String p3 = isNaN((curr3 = i.getLine3(k))) ? "-" : format(curr3, digits);
                cursors.add(new Cursor(c.getName(3) + " (" + c.getPeriod(3) + ")", p3, paint3));
            }
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
