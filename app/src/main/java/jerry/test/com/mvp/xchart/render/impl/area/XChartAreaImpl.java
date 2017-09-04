package jerry.test.com.mvp.xchart.render.impl.area;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.XChartArea;
import cn.nextop.erebor.mid.common.util.Strings;

/**
 * Created by Jingqi Xu on 8/31/15.
 */
public class XChartAreaImpl implements XChartArea {
    //
    protected float marginX, marginY;
    protected boolean visible = false;
    protected float x, y, width, height;

    /**
     *
     */
    public XChartAreaImpl() {
    }

    public XChartAreaImpl(float x, float y, float w, float h) {
        this.x = x; this.y = y; width = w; height = h;
        this.visible = (this.width > 0f && this.height > 0f);
    }

    /**
     *
     */
    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getLeft() {
        return x;
    }

    @Override
    public float getTop() {
        return y;
    }

    @Override
    public float getRight() {
        return x + width;
    }

    @Override
    public float getBottom() {
        return y + height;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public float getCenterX() {
        return x + width / 2;
    }

    @Override
    public float getCenterY() {
        return y + height / 2;
    }

    @Override
    public float getMarginX() {
        return marginX;
    }

    @Override
    public float getMarginY() {
        return marginY;
    }

    @Override
    public float getNetWidth() {
        return width - marginX;
    }

    @Override
    public float getNetHeight() {
        return height - marginY;
    }

    @Override
    public void setMarginX(float marginX) {
        this.marginX = marginX;
    }

    @Override
    public void setMarginY(float marginY) {
        this.marginY = marginY;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("toError", x)
        .append("y", y)
        .append("width", width)
        .append("height", height)
        .append("visible", visible)
        .toString();
    }

    /**
     *
     */
    @Override
    public boolean containsX(final float x) {
        return (x >= getLeft() && x <= getRight());
    }

    @Override
    public boolean containsY(final float y) {
        return (y >= getTop() && y <= getBottom());
    }

    @Override
    public boolean contains(float x, float y) {
        return this.containsX(x) && this.containsY(y);
    }

    /**
     *
     */
    public static final XChartArea valueOf(float x, float y, float w, float h) {
        return new XChartAreaImpl(x, y, w, h);
    }
}
