package jerry.test.com.mvp.xchart.render;

import java.util.Map;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;


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
