package jerry.test.com.mvp.xchart.render.impl.painter.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartView;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartConfig;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartRender;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.AbstractXPainter;
import cn.nextop.erebor.mid.common.glossary.chart.ChartScale;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorArea;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.AREA_A_MARGIN_Y;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.AREA_B_MARGIN_Y;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.AREA_B_RATIO;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_TEXT_SIZE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_X_MARGIN;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.COORDINATE_Y_MARGIN;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.A;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.AY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.B;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.BY;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea.Type.X;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.area.XChartAreaImpl.valueOf;



public class XAreasPainter extends AbstractXPainter {

    /**
     *
     */
    public XAreasPainter() {
        super("painter.basic", 1);
    }

    /**
     *
     */
    @Override
    protected void doDraw(XChartContext context, Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    /**
     *
     */
    @Override
    protected void doEval(XChartContext context, Canvas canvas) {
        //
        if(context.getAction().isReadonly()) return;
        final XChartModel model = context.getModel();
        final XChartRender render = context.getRender();
        final ChartScale scale = model.getConfig().getScale();
        final XChartArea a = render.getArea(A);

        //
        final int max = model.getCharts().size() - 1;
        final int min = min(scale.count(a.getWidth()) - 1, max);
        if (model.getPivot() < min) {
            model.setPivot(min); // At left end & portrait -> landscape
        }
        model.setWindow(model.open(scale.count(a.getWidth()))); // Attach window to model
    }

    /**
     *
     */
    @Override
    protected void doSetup(final XChartContext context, final Canvas canvas) {
        //
        final Paint paint = new Paint();
        final XChartView view = context.getView();
        final XChartModel model = context.getModel();
        final XChartConfig config = model.getConfig();
        final XChartRender render = context.getRender();
        final XChartTheme theme = context.getModel().getTheme();
        final int p = config.getPrecision(), d = config.getDigits();
        float density = view.getResources().getDisplayMetrics().density;
        paint.setTextSize(theme.getProperty(COORDINATE_TEXT_SIZE, 10f) * density);
        final Rect r = XChartUtils.getTextBounds(XChartUtils.limit(p, d, false), paint);

        //
        float bHeight = 0f;
        final IndicatorType b = config.getIndicatorType(IndicatorArea.B);
        final float width = canvas.getWidth(), height = canvas.getHeight();
        final float xMargin = theme.getProperty(COORDINATE_X_MARGIN, 8.0f);
        final float yMargin = theme.getProperty(COORDINATE_Y_MARGIN, 20.0f);
        final float yWidth = r.width() + xMargin, xHeight = r.height() + yMargin;
        if(b != null) bHeight = height * theme.getProperty(AREA_B_RATIO, 0.382f);

        //
        render.getAreas().clear();
        render.putArea(X, valueOf(0, height - xHeight, width, xHeight));
        render.putArea(A, valueOf(0, 0, width - yWidth, height - bHeight - xHeight));
        render.putArea(B, valueOf(0, height - bHeight - xHeight, width - yWidth, bHeight));
        render.putArea(AY, valueOf(width - yWidth, 0, yWidth, height - bHeight - xHeight));
        render.putArea(BY, valueOf(width - yWidth, height - bHeight - xHeight, yWidth, bHeight));

        //
        render.getArea(A).setMarginY(theme.getProperty(AREA_A_MARGIN_Y, 20.0f));
        render.getArea(B).setMarginY(theme.getProperty(AREA_B_MARGIN_Y, 20.0f));
        render.getArea(AY).setMarginY(theme.getProperty(AREA_A_MARGIN_Y, 20.0f));
        render.getArea(BY).setMarginY(theme.getProperty(AREA_B_MARGIN_Y, 20.0f));
    }
}
