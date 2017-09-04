package jerry.test.com.mvp.xchart.render.impl.painter.indicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.config.XIkhConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.indicator.index.XIkhIndex;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction.Type.RESIZE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.formula.impl.IKH.ikh;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_FILL_ALPHA;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_FILL_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_LINE_WIDTH;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_TEXT_SIZE;

/**
 * Created by Jingqi Xu on 9/16/15.
 */
public class XIkhIndicator extends AbstractXIndicator {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(XIkhIndicator.class);

    //
    protected final Paint paint0 = new Paint();
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();
    protected final Paint paint3 = new Paint();
    protected final Paint paint4 = new Paint();
    protected final Paint paintX = new Paint();

    /**
     *
     */
    public XIkhIndicator() {
        super(IndicatorType.IKH, 100);
        super.addArea(XChartArea.Type.A);
    }

    /**
     *
     */
    @Override
    protected void doSetup(final XChartContext context, Canvas canvas) {
        //
        final float density = getDensity(context);
        final XChartModel model = context.getModel();
        final XChartTheme theme = context.getModel().getTheme();
        final XIkhConfig c = model.getConfig().getIndicator(type);
        if(context.getAction().getType() != RESIZE) { // Reset margin
            context.getModel().getMargin().resize(c.getPeriod(1) - 1);
        }

        //
        float size = theme.getProperty(INDICATOR_TEXT_SIZE, 10.0f);
        float width = theme.getProperty(INDICATOR_LINE_WIDTH, 2.0f);
        setup(this.paint, true, c.getColor(), width, size * density);
        setup(this.paint0, true, c.getColor(0), width, size * density);
        setup(this.paint1, true, c.getColor(1), width, size * density);
        setup(this.paint2, true, c.getColor(2), width, size * density);
        setup(this.paint3, true, c.getSenkouColor1(), width, size * density);
        setup(this.paint4, true, c.getSenkouColor2(), width, size * density);
        paintX.setColor(theme.getProperty(INDICATOR_FILL_COLOR, Color.WHITE));
        paintX.setAlpha(theme.getProperty(INDICATOR_FILL_ALPHA, (int)(255 * 0.382)));
    }

    /**
     *
     */
    @Override
    protected void doEval(final XChartContext context, Canvas canvas) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        final XIkhConfig c = model.getConfig().getIndicator(type);

        //
        final int size = window.size();
        final XIkhIndex i = new XIkhIndex(model);
        final int max = model.getCharts().size() - 1;
        final int p0 = c.getPeriod(0), p1 = c.getPeriod(1);
        final int p2 = c.getPeriod(2), mp = c.getMaxPeriod();
        final float p[][] = model.getPrices(size + mp - 1 + p1 - 1, p1 - 1);
        final int right = min(max, model.getPivot() + p1 - 1) - model.getPivot();
        i.setLines(ikh(p[0], p[1], p[3], p0, p1, p2, window.size(), right, window.size() + p1 - 1));
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
        final XIkhConfig c = model.getConfig().getIndicator(type);
        final XIkhIndex i = window.getIndex(type); if(i == null) return;

        // Index
        final XChartLines line0 = getLines("LINE0");
        final XChartLines line1 = getLines("LINE1");
        final XChartLines line2 = getLines("LINE2");
        final XChartLines line3 = getLines("LINE3");
        final XChartLines line4 = getLines("LINE4");
        final int digitsA = model.getConfig().getDigits();
        final float sw = scale.getWidth(), sc = scale.getCenter();
        final int size = window.size(), margin = window.getMargin();
        double prev0 = 0d, prev1 = 0d, prev2 = 0d, prev3 = 0d, prev4 = 0d;
        double curr0 = 0d, curr1 = 0d, curr2 = 0d, curr3 = 0d, curr4 = 0d;
        final double minA = window.getMin(true), maxA = window.getMax(true);
        final float al = a.getLeft(), ar = a.getRight(), ab = a.getBottom();
        final float war = a.getRight() - window.getMargin() * scale.getWidth();
        final double unitA = a.getNetHeight() / XChartUtils.subtract(maxA, minA);
        for(int j = 0; j < margin; j++) {
            final int k = size + margin - j - 1;
            final float x1 = ar - j * sw + sc, x2 = x1 - sw;
            prev3 = curr3; curr3 = i.getLine3(k); adds1(x1, prev3, x2, curr3, ab, minA, unitA, line3);
            prev4 = curr4; curr4 = i.getLine4(k); adds1(x1, prev4, x2, curr4, ab, minA, unitA, line4);
            drawPolygon1(x1, prev3, x2, curr3, x2, curr4, x1, prev4, ab, minA, unitA, canvas, paintX);
        }
        for(int j = 0; j < size; j++) {
            final int k = size - j - 1;
            final float x1 = war - j * sw + sc, x2 = x1 - sw; if(x2 < al) break;
            prev0 = curr0; curr0 = i.getLine0(k); adds1(x1, prev0, x2, curr0, ab, minA, unitA, line0);
            prev1 = curr1; curr1 = i.getLine1(k); adds1(x1, prev1, x2, curr1, ab, minA, unitA, line1);
            prev2 = curr2; curr2 = i.getLine2(k); adds1(x1, prev2, x2, curr2, ab, minA, unitA, line2);
            prev3 = curr3; curr3 = i.getLine3(k); adds1(x1, prev3, x2, curr3, ab, minA, unitA, line3);
            prev4 = curr4; curr4 = i.getLine4(k); adds1(x1, prev4, x2, curr4, ab, minA, unitA, line4);
            drawPolygon1(x1, prev3, x2, curr3, x2, curr4, x1, prev4, ab, minA, unitA, canvas, paintX);
        }
        if(line0 != null && !line0.isEmpty()) XChartUtils.drawLines(line0, canvas, this.paint0, true);
        if(line1 != null && !line1.isEmpty()) XChartUtils.drawLines(line1, canvas, this.paint1, true);
        if(line2 != null && !line2.isEmpty()) XChartUtils.drawLines(line2, canvas, this.paint2, true);
        if(line3 != null && !line3.isEmpty()) XChartUtils.drawLines(line3, canvas, this.paint3, true);
        if(line4 != null && !line4.isEmpty()) XChartUtils.drawLines(line4, canvas, this.paint4, true);

        // Cursor
        final int cursor = getCursor(context);
        final XChartTheme theme = model.getTheme();
        if(theme.getProperty(INDICATOR_CURSOR, true) && cursor > 0) {
            final List<Cursor> cursors = new ArrayList<>(5);
            cursors.add(new Cursor(c.getName(), null, this.paint));
            final int k = window.size() + window.getMargin() - cursor;
            final String p0 = ((curr0 = i.getLine0(k)) <= 0d) ? "-" : format(curr0, digitsA);
            cursors.add(new Cursor(c.getName(0) + " (" + c.getPeriod(0) + ")", p0, this.paint0));
            final String p1 = ((curr1 = i.getLine1(k)) <= 0d) ? "-" : format(curr1, digitsA);
            cursors.add(new Cursor(c.getName(1) + " (" + c.getPeriod(1) + ")", p1, this.paint1));
            final String p2 = ((curr2 = i.getLine2(k)) <= 0d) ? "-" : format(curr2, digitsA);
            cursors.add(new Cursor(c.getChinkouName(), p2, this.paint2));
            final String p3 = ((curr3 = i.getLine3(k)) <= 0d) ? "-" : format(curr3, digitsA);
            cursors.add(new Cursor(c.getSenkouName() + "1 (" + c.getPeriod(2) + ")", p3, this.paint3));
            final String p4 = ((curr4 = i.getLine4(k)) <= 0d) ? "-" : format(curr4, digitsA);
            cursors.add(new Cursor(c.getSenkouName() + "2 (" + c.getPeriod(2) + ")", p4, this.paint4));
            Cursor.draw(context, canvas, i, cursors); // Draw cursors in batch with blur background
        }
    }
}
