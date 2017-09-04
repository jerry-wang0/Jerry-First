package jerry.test.com.mvp.xchart.render;

import android.graphics.Canvas;

import java.util.Comparator;
import java.util.Set;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.common.util.Comparators;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public interface XChartPainter {

    /**
     *
     */
    int getLayer();

    String getName();

    Set<XChartArea.Type> getAreas();

    /**
     *
     */
    void eval(XChartContext context, Canvas canvas);

    void draw(XChartContext context, Canvas canvas);

    void setup(XChartContext context, Canvas canvas);

    /**
     *
     */
    Comparator<XChartPainter> ASC = new Comparator<XChartPainter>() {
        @Override
        public int compare(XChartPainter lhs, XChartPainter rhs) {
            return Comparators.cmp(lhs.getLayer(), rhs.getLayer(), true);
        }
    };
}
