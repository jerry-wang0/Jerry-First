package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartAction;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartWindow;
import cn.nextop.erebor.mid.common.util.Objects;

/**
 * Created by Jingqi Xu on 8/28/15.
 */
public class XChartSelectAction extends AbstractAction {
    //
    protected final float x, y;

    /**
     *
     */
    public XChartSelectAction(float x, float y) {
        super(Type.SELECT); this.x = x; this.y = y;
    }

    /**
     *
     */
    @Override
    public boolean isReadonly() {
        return true;
    }

    /**
     * prev.merge(next)
     * @see cn.nextop.erebor.mid.widget.xchart.adapter.XChartAdapter
     */
    @Override
    public XChartAction merge(XChartAction action) {
        if(action.getType() != getType()) return null;
        final XChartSelectAction rhs = Objects.cast(action);
        return new XChartSelectAction(rhs.x, rhs.y); // Discard the prev action
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        //
        final XChartModel model = context.getModel();
        final XChartWindow window = model.getWindow();
        if (window == null || window.isEmpty()) return false;

        //
        model.setCursorX(this.x); model.setCursorY(this.y); return true;
    }
}