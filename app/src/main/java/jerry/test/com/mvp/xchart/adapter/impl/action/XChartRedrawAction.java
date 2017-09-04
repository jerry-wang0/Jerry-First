package jerry.test.com.mvp.xchart.adapter.impl.action;

import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAdapter;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;

/**
 * Created by wanggl on 2016/12/2.
 */
public class XChartRedrawAction extends AbstractAction {

    protected List<XDrawingLine> lines = new ArrayList<>();

    public XChartRedrawAction(List<XDrawingLine> lines){
        super(Type.PICK);
        this.lines = lines;
    }
    @Override
    public boolean apply(XChartContext context) {
        XChartAdapter adapter = context.getAdapter();
        adapter.getModel().redraw(lines);
        return true;
    }
}
