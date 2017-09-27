package jerry.test.com.mvp.xchart.adapter.impl.action;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.adapter.XChartContext;

public class XChartResizeAction extends AbstractAction {
    //
    private int width;
    private int format;
    private int height;

    /**
     *
     */
    public XChartResizeAction(int format, int width, int height) {
        super(Type.RESIZE); this.format = format;
        this.width = width; this.height = height;
    }

    /**
     *
     */
    public int getWidth() {
        return width;
    }

    public int getFormat() {
        return format;
    }

    public int getHeight() {
        return height;
    }

    /**
     *
     */
    @Override
    public boolean isSetup() {
        return true;
    }

    /**
     *
     */
    @Override
    public boolean apply(final XChartContext context) {
        return true;
    }
}
