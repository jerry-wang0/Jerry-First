package jerry.test.com.mvp.xchart.render.impl.painter.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartIndex;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartLines;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXPainter;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_LIGHT_GRAY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.add;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.drawText;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.AREA_B_STEPS;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_AREA_BG;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_AREA_FRAME_BG;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_FRAME;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_GRID;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_TEXT_SIZE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_X_GRID;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_X_STEP;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_Y_GRID;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_Y_STEP;
import static cn.nextop.erebor.mid.common.util.Strings.isEquals;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public class XFramePainter extends AbstractXPainter {
    //
    protected final Paint paint1 = new Paint();
    protected final Paint paint2 = new Paint();

    /**
     *
     */
    public XFramePainter() {
        super("painter.coord", 2);
        super.addArea(XChartArea.Type.A);
        super.addArea(XChartArea.Type.B);
        super.addArea(XChartArea.Type.X);
        super.addArea(XChartArea.Type.AY);
        super.addArea(XChartArea.Type.BY);
    }

    /**
     *
     */
    @Override
    protected void doSetup(XChartContext context, Canvas canvas) {
        this.paint.reset(); this.paint.setAntiAlias(true);
        final XChartView v = context.getAdapter().getView();
        final XChartModel m = context.getAdapter().getModel();
        final XChartTheme theme = context.getModel().getTheme();
        this.paint1.reset(); this.paint1.setStyle(Paint.Style.STROKE);
        final float density = v.getResources().getDisplayMetrics().density;
        this.paint.setColor(theme.getProperty(COORDINATE_COLOR_FRAME, Color.WHITE));
        this.paint.setTextSize(theme.getProperty(COORDINATE_TEXT_SIZE, 10f) * density);
        this.paint1.setColor(m.getTheme().getProperty(COORDINATE_COLOR_GRID, COLOR_LIGHT_GRAY));
        this.paint1.setPathEffect(new DashPathEffect(new float[]{ 3.0f * density, 2.0f * density}, 0f ));
        this.paint2.setStyle(Paint.Style.FILL);
    }

    /**
     *
     */
    @Override
    protected void doDraw(final XChartContext context, Canvas canvas) {
        // Frame
        final XChartLines frame = getLines("FRAME");
        final XChartModel model = context.getModel();
        final XChartConfig config = model.getConfig();
        final XChartRender render = context.getRender();
        final ChartScale scale = model.getConfig().getScale();
        final XChartTheme theme = context.getModel().getTheme();
        final XChartArea a = render.getArea(XChartArea.Type.A);
        final XChartArea b = render.getArea(XChartArea.Type.B);
        final XChartArea x = render.getArea(XChartArea.Type.X);
        final XChartArea ay = render.getArea(XChartArea.Type.AY);
        final XChartArea by = render.getArea(XChartArea.Type.BY);
        final int stepX = theme.getProperty(COORDINATE_X_STEP, 12);
        final int stepY = theme.getProperty(COORDINATE_Y_STEP, 150);
        boolean visibleX = theme.getProperty(COORDINATE_X_GRID, true);
        boolean visibleY = theme.getProperty(COORDINATE_Y_GRID, true);
        frame.adds(x.getLeft(), x.getTop(), a.getRight() + 1, x.getTop());
        frame.adds(ay.getLeft(), ay.getTop(), ay.getLeft(), ay.getBottom());
        frame.adds(a.getLeft(), a.getTop() + 1, a.getRight() + 1, a.getTop() + 1);
        if(b.isVisible()) frame.adds(by.getLeft(), by.getTop(), by.getLeft(), by.getBottom());
        if(b.isVisible()) frame.adds(b.getLeft(), b.getTop() + 1, b.getRight() + 1, b.getTop() + 1);
        final float bl = b.getLeft(), bt = b.getTop(), br = b.getRight(), bb = b.getBottom();
        final float al = a.getLeft(), at = a.getTop(), ar = a.getRight(), ab = a.getBottom();
        // Background
        this.paint2.setColor(theme.getProperty(COORDINATE_COLOR_AREA_FRAME_BG, Color.parseColor("#282e3b")));
        XChartUtils.drawRect(al, at, ay.getRight(), x.getBottom(), canvas, this.paint2);
        this.paint2.setColor(theme.getProperty(COORDINATE_COLOR_AREA_BG, Color.parseColor("#212e37")));
        XChartUtils.drawRect(al, at, ar, bb, canvas, this.paint2);
        XChartUtils.drawLines(frame, canvas, this.paint, true); if (!model.hasWindow()) return;

        // Grid A
        String prevA = null, nextA = null;
        final XChartWindow w = model.getWindow();
        final XChartLines grid = getLines("GRID");
        final int digitsA = model.getConfig().getDigits();
        final double minA = w.getMin(true), maxA = w.getMax(true);
        final double unitA = subtract(maxA, minA) / a.getNetHeight();
        final float width = scale.getWidth(), center = scale.getCenter();
        final float war = a.getRight() - w.getMargin() * scale.getWidth();

        for(int i = 1, steps = (int)(ay.getHeight() / stepY) + 1; i < steps; i++, prevA = nextA) {
            final float gridY = ab - i * stepY;
            if (gridY <= at) break; else if (visibleX) {
                grid.adds(al + 1, gridY, ar - 1, gridY);
            }
            nextA = format(add((ab - gridY) * unitA, minA), digitsA);
            if(!isEquals(prevA, nextA)) drawText(nextA, ay.getCenterX(), true, gridY, true, canvas, paint);
        }

        // Grid B
        String prevB = null, nextB = null;
        final XChartIndex index = w.getIndex(config.getIndicatorType(IndicatorArea.B));
        if(b.isVisible() && index != null) {
            final int digitsB = index.getDigits();
            final double minB = index.getMin(true), maxB = index.getMax(true);
            final double unitB = subtract(maxB, minB) * index.getUnit() / b.getNetHeight();
            for(int i = 1, steps = theme.getProperty(AREA_B_STEPS, 5); i < steps + 1; i++, prevB = nextB) {
                float gridY = bb - b.getNetHeight() * i / steps;
                if (gridY <= bt) break; else if (visibleX) {
                    grid.adds(bl + 1, gridY, br - 1, gridY);
                }
                nextB = format(add((bb - gridY) * unitB, minB), digitsB);
                if(!isEquals(prevB, nextB)) drawText(nextB, ay.getCenterX(), true, gridY, true, canvas, paint);
            }
        }

        // Grid Y
        Rect prevY = null; XChart chart = null;
        final int total = model.getCharts().size();
        for (int i = 0; i < w.getMargin(); i++) {
            if(i % stepX != (stepX - 1)) continue;
            float gridX = war + i * width + center;
            if (gridX >= ar) break; else if(visibleY) {
                grid.adds(gridX, at, gridX, ab);
                if(b.isVisible()) grid.adds(gridX, bt, gridX, bb);
            }
        }
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if((total - w.getLast() + i) % stepX != 1) continue;
            final float gridX = war - ( i + 1 ) * width + center;
            if (gridX <= al) break; else if(visibleY) {
                grid.adds(gridX, at, gridX, ab);
                if(b.isVisible()) grid.adds(gridX, bt, gridX, bb);
            }
            if ((chart = w.getChart(-1 * (i + 1))) != null) {
                final String time = XChartUtils.getCoordinateTime(config.getType(), chart);
                prevY = drawText(prevY, time, gridX, true, x.getCenterY(), true, canvas, this.paint);
            }
        }
        if(grid != null && !grid.isEmpty()) XChartUtils.drawLines(grid, canvas, this.paint1, true); // Draw grids in batch
    }
}
