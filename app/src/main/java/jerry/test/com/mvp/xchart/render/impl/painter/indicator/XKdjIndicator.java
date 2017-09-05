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
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XKdjConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XKdjIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format1;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.kdj;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;
import static java.lang.Double.isNaN;
import static java.lang.Float.NaN;

public class XKdjIndicator extends AbstractXIndicator {
    //
    protected final Paint paint0 = new Paint();
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();

    /**
     *
     */
    public XKdjIndicator() {
        super(IndicatorType.KDJ, 100);
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
        final XKdjConfig c = model.getConfig().getIndicator(type);
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
        final XKdjConfig c = model.getConfig().getIndicator(type);

        //
        final int p0 = c.getPeriod(0);
        final int size = window.size();
        final XKdjIndex i = new XKdjIndex(model);
        final int p1 = c.getPeriod(1), p2 = c.getPeriod(2);
        final float p[][] = model.getPrices(size + p0 + p1 + p2 - 3, 0);
        i.setLines(kdj(p[0], p[1], p[3], 0, p[0].length - 1, p0, p1, p2, size));
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
        final XKdjConfig c = model.getConfig().getIndicator(type);
        final XKdjIndex i = window.getIndex(type); if(i == null) return;

        // Index
        double prev0 = NaN, prev1 = NaN, prev2 = NaN;
        double curr0 = NaN, curr1 = NaN, curr2 = NaN;
        double minB = i.getMin(true), maxB = i.getMax(true);
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final double unitB = b.getNetHeight() / subtract(maxB, minB);
        final XChartLines line0 = c.isVisible(0) ? getLines("LINE0") : null;
        final XChartLines line1 = c.isVisible(1) ? getLines("LINE1") : null;
        final XChartLines line2 = c.isVisible(2) ? getLines("LINE2") : null;
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
        }
        if (c.isVisible(0)) XChartUtils.drawLines(line0, canvas, this.paint0, true);
        if (c.isVisible(1)) XChartUtils.drawLines(line1, canvas, this.paint1, true);
        if (c.isVisible(2)) XChartUtils.drawLines(line2, canvas, this.paint2, true);

        // Cursor
        final int digits = i.getDigits();
        final int cursor = getCursor(context);
        final XChartTheme theme = model.getTheme();
        if(theme.getProperty(INDICATOR_CURSOR, true) && cursor > 0) {
            final List<Cursor> cursors = new ArrayList<>(4);
            cursors.add(new Cursor(c.getName(), null, this.paint));
            final int k = window.size() + window.getMargin() - cursor;
            if(c.isVisible(0)) {
                final String p0 = isNaN(curr0 = i.getLine0(k)) ? "-" : format1(curr0, digits);
                cursors.add(new Cursor(c.getName(0) + " (" + c.getPeriod(0) + ")", p0, this.paint0));
            }
            if(c.isVisible(1)) {
                final String p1 = isNaN(curr1 = i.getLine1(k)) ? "-" : format1(curr1, digits);
                cursors.add(new Cursor(c.getName(1) + " (" + c.getPeriod(1) + ")", p1, this.paint1));
            }
            if(c.isVisible(2)) {
                final String p2 = isNaN(curr2 = i.getLine2(k)) ? "-" : format1(curr2, digits);
                cursors.add(new Cursor(c.getName(2) + " (" + c.getPeriod(2) + ")", p2, this.paint2));
            }
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
