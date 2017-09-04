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
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XBollConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XBollIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.XChartFormula.boll;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;

/**
 * Created by Jingqi Xu on 9/7/15.
 */
public class XBollIndicator extends AbstractXIndicator {
    //
    protected final Paint paint0 = new Paint();
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();
    protected final Paint paint3 = new Paint();

    /**
     *
     */
    public XBollIndicator() {
        super(IndicatorType.BOLL, 100);
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
        final XBollConfig c = model.getConfig().getIndicator(type);
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
        final XBollConfig c = model.getConfig().getIndicator(type);

        //
        final int size = window.size();
        final int period = c.getPeriod();
        final XBollIndex i = new XBollIndex(model);
        final float p[] = model.getClosePrices(size + (period - 1), 0);
        if(c.isVisible(1)) i.setLine1(boll(p, 0, p.length - 1, period, 1, size));
        if(c.isVisible(2)) i.setLine2(boll(p, 0, p.length - 1, period, 2, size));
        if(c.isVisible(3)) i.setLine3(boll(p, 0, p.length - 1, period, 3, size));
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
        final XBollConfig c = model.getConfig().getIndicator(type);
        final XBollIndex i = window.getIndex(type); if(i == null) return;

        // Index
        double prev0 = 0d, curr0 = 0d;
        final XChartLines line0 = getLines("LINE0");
        final int digitsA = model.getConfig().getDigits();
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final float al = a.getLeft(), ar = a.getRight(), ab = a.getBottom();
        final double minA = window.getMin(true), maxA = window.getMax(true);
        final XChartLines line10 = c.isVisible(1) ? getLines("LINE10") : null;
        final XChartLines line11 = c.isVisible(1) ? getLines("LINE11") : null;
        final XChartLines line20 = c.isVisible(2) ? getLines("LINE20") : null;
        final XChartLines line21 = c.isVisible(2) ? getLines("LINE21") : null;
        final XChartLines line30 = c.isVisible(3) ? getLines("LINE30") : null;
        final XChartLines line31 = c.isVisible(3) ? getLines("LINE31") : null;
        final float war = a.getRight() - window.getMargin() * scale.getWidth();
        final double unitA = a.getNetHeight() / XChartUtils.subtract(maxA, minA);
        double[] prev1 = new double[2], prev2 = new double[2], prev3 = new double[2];
        double[] curr1 = new double[2], curr2 = new double[2], curr3 = new double[2];
        for(int j = 0, size = window.size(); j < size; j++) {
            final int k = size - j - 1;
            final float x1 = war - j * sw + sc;
            float x2 = x1 - sw; if (x2 < al) break;
            if(c.isVisible(0)) {
                prev0 = curr0; curr0 = i.getLine0(k);
                adds1(x1, prev0, x2, curr0, ab, minA, unitA, line0);
            }
            if(c.isVisible(1)) {
                prev1[0] = curr1[0]; curr1[0] = i.getLine1(0, k);
                prev1[1] = curr1[1]; curr1[1] = i.getLine1(1, k);
                adds1(x1, prev1[0], x2, curr1[0], ab, minA, unitA, line10);
                adds1(x1, prev1[1], x2, curr1[1], ab, minA, unitA, line11);
            }
            if(c.isVisible(2)) {
                prev2[0] = curr2[0]; curr2[0] = i.getLine2(0, k);
                prev2[1] = curr2[1]; curr2[1] = i.getLine2(1, k);
                adds1(x1, prev2[0], x2, curr2[0], ab, minA, unitA, line20);
                adds1(x1, prev2[1], x2, curr2[1], ab, minA, unitA, line21);
            }
            if(c.isVisible(3)) {
                prev3[0] = curr3[0]; curr3[0] = i.getLine3(0, k);
                prev3[1] = curr3[1]; curr3[1] = i.getLine3(1, k);
                adds1(x1, prev3[0], x2, curr3[0], ab, minA, unitA, line30);
                adds1(x1, prev3[1], x2, curr3[1], ab, minA, unitA, line31);
            }
        }
        if(c.isVisible(0)) XChartUtils.drawLines(line0, canvas, this.paint0, true);
        if(c.isVisible(1)) XChartUtils.drawLines(line10, line11, canvas, this.paint1, true);
        if(c.isVisible(2)) XChartUtils.drawLines(line20, line21, canvas, this.paint2, true);
        if(c.isVisible(3)) XChartUtils.drawLines(line30, line31, canvas, this.paint3, true);

        // Cursor
        final int cursor = getCursor(context);
        final XChartTheme theme = model.getTheme();
        if(theme.getProperty(INDICATOR_CURSOR, true) && cursor > 0) {
            final List<Cursor> cursors = new ArrayList<>(8);
            final int k = window.size() + window.getMargin() - cursor;
            cursors.add(new Cursor(c.getName() + " (" + c.getPeriod() + ")", null, this.paint));
            if(c.isVisible(3)) {
                String p31 = ((curr3[1] = i.getLine3(1, k)) <= 0d) ? "-" : format(curr3[1], digitsA);
                cursors.add(new Cursor(c.getName() + "+3", p31, this.paint3));
            }
            if(c.isVisible(2)) {
                String p21 = ((curr2[1] = i.getLine2(1, k)) <= 0d) ? "-" : format(curr2[1], digitsA);
                cursors.add(new Cursor(c.getName() + "+2", p21, this.paint2));
            }
            if(c.isVisible(1)) {
                String p11 = ((curr1[1] = i.getLine1(1, k)) <= 0d) ? "-" : format(curr1[1], digitsA);
                cursors.add(new Cursor(c.getName() + "+1", p11, this.paint1));
            }
            if(c.isVisible(0)) {
                final String p0 = ((curr0 = i.getLine0(k)) <= 0d) ? "-" : format(curr0, digitsA);
                cursors.add(new Cursor(c.getName(), p0, this.paint0));
            }
            if(c.isVisible(1)) {
                String p10 = ((curr1[0] = i.getLine1(0, k)) <= 0d) ? "-" : format(curr1[0], digitsA);
                cursors.add(new Cursor(c.getName() + "-1", p10, this.paint1));
            }
            if(c.isVisible(2)) {
                String p21 = ((curr2[0] = i.getLine2(0, k)) <= 0d) ? "-" : format(curr2[0], digitsA);
                cursors.add(new Cursor(c.getName() + "-2", p21, this.paint2));
            }
            if(c.isVisible(3)) {
                String p31 = ((curr3[0] = i.getLine3(0, k)) <= 0d) ? "-" : format(curr3[0], digitsA);
                cursors.add(new Cursor(c.getName() + "-3", p31, this.paint3));
            }
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
