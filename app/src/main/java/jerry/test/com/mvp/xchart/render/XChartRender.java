package jerry.test.com.mvp.xchart.render;

import java.util.Map;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public interface XChartRender {

    /**
     *
     */
    boolean render(XChartContext context);

    /**
     *
     */
    XChartArea getArea(XChartArea.Type type);

    Map<XChartArea.Type, XChartArea> getAreas();

    void putArea(XChartArea.Type type, XChartArea area);
}
