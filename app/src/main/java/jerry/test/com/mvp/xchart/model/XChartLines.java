package jerry.test.com.mvp.xchart.model;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.cast;

/**
 * Created by Jingqi Xu on 9/12/15.
 */
public final class XChartLines {
    //
    private int index;
    private float[] points;

    /**
     *
     */
    public XChartLines() {
        this(512);
    }

    public XChartLines(int capacity) {
        this.index = 0;
        this.points = new float[capacity << 2];
    }

    /**
     *
     */
    public void reset() {
        this.index = 0;
    }

    public int getIndex() {
        return index;
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public float[] getPoints() {
        return points;
    }

    /**
     *
     */
    public void add(final float p1) {
        ensure(1);
        this.points[index++] = p1;
    }

    public void adds(final float p1, final float p2) {
        ensure(2);
        this.points[index++] = p1;
        this.points[index++] = p2;
    }

    public void adds(final double p1, final double p2) {
        ensure(2);
        this.points[index++] = cast(p1);
        this.points[index++] = cast(p2);
    }

    public void adds(float p1, float p2, float p3, float p4) {
        ensure(4);
        this.points[index++] = p1; this.points[index++] = p2;
        this.points[index++] = p3; this.points[index++] = p4;
    }

    public void adds(double p1, double p2, double p3, double p4) {
        ensure(4);
        this.points[index++] = cast(p1); this.points[index++] = cast(p2);
        this.points[index++] = cast(p3); this.points[index++] = cast(p4);
    }

    /**
     *
     */
    protected void ensure(final int i) {
        if(this.points.length - this.index > i) return;
        float next[] = new float[this.points.length << 1];
        System.arraycopy(points, 0, next, 0, index); points = next;
    }
}
