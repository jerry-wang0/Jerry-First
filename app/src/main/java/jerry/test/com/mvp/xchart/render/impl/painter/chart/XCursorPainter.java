package jerry.test.com.mvp.xchart.render.impl.painter.chart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

import cn.nextop.erebor.mid.R;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartIndex;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXPainter;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.ChartType;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_LIGHT_GRAY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_ORANGE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.add;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.cast;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.drawText;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.format;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.getCoordinateTime;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.subtract;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.trim;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_FRAME;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_COLOR_GRID;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_TEXT_SIZE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CURSOR_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.CURSOR_WIDTH;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public class XCursorPainter extends AbstractXPainter {
    //
    protected final Paint paint1 = new Paint();

    /**
     *
     */
    public XCursorPainter() {
        super("painter.cursor", 999);
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
        this.paint.setStrokeWidth(theme.getProperty(CURSOR_WIDTH, 2.0f));
        final float density = v.getResources().getDisplayMetrics().density;
        this.paint.setTextSize(theme.getProperty(COORDINATE_TEXT_SIZE, 10f) * density);
        this.paint1.setColor(m.getTheme().getProperty(COORDINATE_COLOR_GRID, COLOR_LIGHT_GRAY));
        this.paint1.setPathEffect(new DashPathEffect(new float[]{3.0f * density, 2.0f * density}, 0f));
    }

    /**
     *
     */
    @Override
    protected void doDraw(final XChartContext context, Canvas canvas) {
        //
        if(!context.getModel().hasWindow()) return;
        final XChartModel model = context.getModel();
        final XChartConfig config = model.getConfig();
        final XChartWindow window = model.getWindow();
        final XChartRender render = context.getRender();
        final XChartAdapter adapter = context.getAdapter();
        final ChartScale scale = model.getConfig().getScale();
        final XChartTheme theme = adapter.getModel().getTheme();
        final XChartArea a = render.getArea(XChartArea.Type.A);
        final XChartArea b = render.getArea(XChartArea.Type.B);
        final XChartArea x = render.getArea(XChartArea.Type.X);
        final XChartArea ay = render.getArea(XChartArea.Type.AY);
        final XChartArea by = render.getArea(XChartArea.Type.BY);

        // Cursor
        final int digitsA = model.getConfig().getDigits();
        final XChart last = context.getModel().getLastChart();
        final double minA = window.getMin(true), maxA = window.getMax(true);
        final float al = a.getLeft(), at = a.getTop(), ar = a.getRight(), ab = a.getBottom();
        final float bl = b.getLeft(), bt = b.getTop(), br = b.getRight(), bb = b.getBottom();
        if(model.getCursorX() < 0f && model.getCursorY() < 0f) {
            adapter.notifyOnCursor(last); // Not matter null nor not
            if(last != null && XChartUtils.between(last.getClose(), minA, maxA)) {
                //
                final double unitA = a.getNetHeight() / subtract(maxA, minA);
                float cursorY = cast(ab - subtract(last.getClose(), minA) * unitA);
                this.paint.setColor(theme.getProperty(CURSOR_COLOR, COLOR_ORANGE));
                canvas.drawLine(al + 1, cursorY, ay.getLeft() - 1, cursorY, this.paint);

                //
                final String price = format(last.getClose(), digitsA);
                final Rect bounds = XChartUtils.getTextBounds(price, this.paint);
                final Bitmap imageY = adapter.getBitmap(R.mipmap.chart_cursor_y);
                this.paint.setColor(theme.getProperty(COORDINATE_COLOR_FRAME, Color.WHITE));
                final float imageL = ay.getLeft() + 1, imageR = ay.getRight() - 1, half = bounds.height() / 2;
                final Rect imageRect = XChartUtils.toRect1(imageL, cursorY - half - 6, imageR, cursorY + half + 6);
                final float priceX = ay.getCenterX() - bounds.width() / 2f, priceY = cursorY + bounds.height() / 2f;
                canvas.drawBitmap(imageY, null, imageRect, this.paint); canvas.drawText(price, priceX, priceY, this.paint);
            }
        } else {
            // Grid
            final int cursor = getCursor(context);
            this.paint.setColor(theme.getProperty(CURSOR_COLOR, COLOR_ORANGE));
            final float cursorX = ar - cursor * scale.getWidth() + scale.getCenter();
            canvas.drawLine(cursorX, at + 1, cursorX, a.getBottom() - 1, this.paint);
            if(b.isVisible()) canvas.drawLine(cursorX, bt, cursorX, bb - 1, this.paint);

            // Y
            final Bitmap imageY = context.getAdapter().getBitmap(R.mipmap.chart_cursor_y);
            final XChartIndex i = window.getIndex(config.getIndicatorType(IndicatorArea.B));
            if(a.containsY(model.getCursorY()) || i == null) {
                //
                final double unitA = subtract(maxA, minA) / a.getNetHeight();
                float cursorY = trim(model.getCursorY(), at + a.getMarginY(), ab);
                canvas.drawLine(al + 1, cursorY, ay.getLeft() - 1, cursorY, this.paint);
                final String price = format(add((ab - cursorY) * unitA, minA), digitsA);

                //
                final Rect bounds = XChartUtils.getTextBounds(price, this.paint);
                this.paint.setColor(theme.getProperty(COORDINATE_COLOR_FRAME, Color.WHITE));
                final float imageL = ay.getLeft() + 1, imageR = ay.getRight() - 1, half = bounds.height() / 2;
                final Rect imageRect = XChartUtils.toRect1(imageL, cursorY - half - 6, imageR, cursorY + half + 6);
                final float priceX = ay.getCenterX() - bounds.width() / 2f, priceY = cursorY + bounds.height() / 2f;
                canvas.drawBitmap(imageY, null, imageRect, this.paint); canvas.drawText(price, priceX, priceY, this.paint);
            } else {
                //
                final float cursorY = trim(model.getCursorY(), bt, bb);
                final double minB = i.getMin(true), maxB = i.getMax(true);
                double unitB = subtract(maxB, minB) * i.getUnit() / b.getNetHeight();
                canvas.drawLine(bl + 1, cursorY, by.getLeft() - 1, cursorY, this.paint);
                final String price = format(add((bb - cursorY) * unitB, minB), i.getDigits());

                //
                final Rect bounds = XChartUtils.getTextBounds(price, this.paint);
                this.paint.setColor(theme.getProperty(COORDINATE_COLOR_FRAME, Color.WHITE));
                final float imageL = ay.getLeft() + 1, imageR = ay.getRight() - 1, half = bounds.height() / 2;
                final Rect imageRect = XChartUtils.toRect1(imageL, cursorY - half - 6, imageR, cursorY + half + 6);
                final float priceX = ay.getCenterX() - bounds.width() / 2f, priceY = cursorY + bounds.height() / 2f;
                canvas.drawBitmap(imageY, null, imageRect, this.paint); canvas.drawText(price, priceX, priceY, this.paint);
            }

            // X
            final int index = cursor - window.getMargin();
            final ChartType type = model.getConfig().getType();
            final XChart chart = index <= 0 ? null : window.getChart(-1 * index); adapter.notifyOnCursor(chart);
            if(chart != null) {
                final String time = getCoordinateTime(type, chart);
                final Rect bounds = XChartUtils.getTextBounds(time, this.paint);
                final Bitmap imageX = adapter.getBitmap(R.mipmap.chart_cursor_x);
                this.paint.setColor(theme.getProperty(COORDINATE_COLOR_FRAME, Color.WHITE));
                float l = cursorX - bounds.width() / 2f, t = x.getCenterY() - bounds.height() / 2f - 5f;
                final Rect imageRect = XChartUtils.toRect1(l - 9, t - 4, l + bounds.width() + 12, t + bounds.height() + 8);
                canvas.drawBitmap(imageX, null, imageRect, paint); drawText(time, cursorX, true, x.getCenterY(), true, canvas, paint);
            }
        }
    }
}
