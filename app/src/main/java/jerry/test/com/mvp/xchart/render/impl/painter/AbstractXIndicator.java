package jerry.test.com.mvp.xchart.render.impl.painter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartIndex;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.monitor.impl.XMonitorKey;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.EMPTY_RECT;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.drawText;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.getTextBounds;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.limit;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.toRect2;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_ALPHA;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_X;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_X_SPACE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_Y;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.INDICATOR_CURSOR_Y_SPACE;


public abstract class AbstractXIndicator extends AbstractXPainter {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractXIndicator.class);

    //
    protected final IndicatorType type;

    /**
     *
     */
    public AbstractXIndicator(IndicatorType type, int layer) {
        super("indicator." + type.name(), layer);
        this.type = type; this.paint.setAntiAlias(true);
    }

    /**
     *
     */
    @Override
    public final void eval(final XChartContext context, Canvas canvas) {
        try {
            if(context.getAction().isReadonly()) return;
            if(context.getModel().hasWindow()) doEval(context, canvas);
        } catch(Throwable throwable) {
            this.monitor.add(XMonitorKey.INTERNAL_ERROR, "eval", 1);
            LOGGER.error("[" + this.name + "]failed to eval", throwable);
        }
    }

    @Override
    public final void draw(final XChartContext context, Canvas canvas) {
        try {
            if(context.getModel().hasWindow()) doDraw(context, canvas);
        } catch(Throwable throwable) {
            this.monitor.add(XMonitorKey.INTERNAL_ERROR, "draw", 1);
            LOGGER.error("[" + this.name + "]failed to draw", throwable);
        }
    }

    /**
     *
     */
    protected final void setup(Paint p, boolean anti, int color, float width, float size) {
        p.setAntiAlias(anti); p.setTextSize(size); p.setColor(color); p.setStrokeWidth(width);
    }

    /**
     *
     */
    protected static final class Cursor {
        //
        protected static final Paint CURSOR = new Paint();

        //
        protected Paint paint;
        protected String key, value;
        protected float width, height;

        /**
         *
         */
        public Cursor(String key, String value, Paint paint) {
            this.key = key; this.value = value; this.paint = paint;
        }

        /**
         *
         */
        public static void draw(XChartContext context, Canvas canvas, XChartIndex index, List<Cursor> cursors) {
            //
            final XChartRender render = context.getRender();
            final XChartArea a = render.getArea(XChartArea.Type.A);
            final XChartArea b = render.getArea(XChartArea.Type.B);
            final XChartTheme theme = context.getModel().getTheme();
            final XChartConfig config = context.getModel().getConfig();
            float xs = theme.getProperty(INDICATOR_CURSOR_X_SPACE, 15.0f);
            float ys = theme.getProperty(INDICATOR_CURSOR_Y_SPACE, 10.0f);
            final XChartArea area = index.getType().getArea().isA() ? a : b;
            float x = area.getX() + theme.getProperty(INDICATOR_CURSOR_X, 15.0f);
            float y = area.getY() + theme.getProperty(INDICATOR_CURSOR_Y, 45.0f);
            CURSOR.setColor(theme.getProperty(INDICATOR_CURSOR_COLOR, Color.BLACK));
            CURSOR.setAlpha(theme.getProperty(INDICATOR_CURSOR_ALPHA, (int)(255 * 0.618)));

            //
            float width = 0f, height = 0f;
            String max = limit(config.getPrecision(), config.getDigits(), false);
            for(final Cursor c : cursors) {
                Rect r1 = c.key == null ? EMPTY_RECT : getTextBounds(c.key, c.paint);
                Rect r2 = c.value == null ? EMPTY_RECT : getTextBounds(max, c.paint);
                height += (c.height = (XChartUtils.max(r1.height(), r2.height()) + ys));
                width = XChartUtils.max(width, c.width = (r1.width() + xs + r2.width()));
            }

            //
            y = y - cursors.get(0).height; canvas.drawRect(toRect2(x, y, width, height), CURSOR);
            for(final Cursor cursor : cursors) {
                y += cursor.height;
                if (cursor.key != null) canvas.drawText(cursor.key, x, y, cursor.paint);
                if (cursor.value != null) drawText(cursor.value, x + width, y, Align.RIGHT, canvas, cursor.paint);
            }
        }
    }
}
