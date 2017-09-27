package jerry.test.com.mvp.xchart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.CGPoint;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingDepot;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingLine;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.render.impl.painter.drawing.XDrawingSegment;
import cn.nextop.erebor.mid.common.glossary.chart.ChartStyle;
import cn.nextop.erebor.mid.common.glossary.chart.XChart;
import cn.nextop.erebor.mid.common.glossary.chart.XDrawingStyle;
import cn.nextop.erebor.mid.common.util.Collections;
import cn.nextop.erebor.mid.common.util.Strings;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.max;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.min;


public class XChartModel {
    //
    protected int pivot;
    protected float cursorX;
    protected float cursorY;
    protected XChartTheme theme;
    protected XChartConfig config;
    protected XChartWindow window;
    protected XChartMargin margin;
    protected final List<XChart> charts = new ArrayList<>();
    protected List<XDrawingLine> lines = new ArrayList<>();
    protected XDrawingDepot selectedDepot;
    protected XDrawingStyle drawingStyle;
    protected XDrawingSegment selectedSegment;
    protected boolean isDragging;
    protected boolean isMagneting;
    protected CGPoint pointToMagnify;
    protected float touchX;
    protected float touchY;

    /**
     *
     */
    public XChartModel() {
        reset(null);
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
                .append("pivot", pivot)
                .append("theme", theme)
                .append("margin", margin)
                .append("config", config)
                .append("cursorX", cursorX)
                .append("cursorY", cursorY).toString();
    }

    /**
     *
     */
    public int getPivot() {
        return pivot;
    }

    public void setPivot(int pivot) {
        this.pivot = pivot;
    }

    public float getCursorX() {
        return cursorX;
    }

    public void setCursorX(float cursorX) {
        this.cursorX = cursorX;
    }

    public float getCursorY() {
        return cursorY;
    }

    public void setCursorY(float cursorY) {
        this.cursorY = cursorY;
    }

    public XChartTheme getTheme() {
        return theme;
    }

    public void setTheme(XChartTheme theme) {
        this.theme = theme;
    }

    public XChartConfig getConfig() {
        return config;
    }

    public void setConfig(XChartConfig config) {
        this.config = config;
    }

    public XChartMargin getMargin() {
        return margin;
    }

    public void setMargin(XChartMargin margin) {
        this.margin = margin;
    }

    /**
     * Prices
     */
    public float[] getClosePrices(int left, int right) {
        //
        int first = max(0, this.pivot - left + 1);
        int last = min(charts.size() - 1, pivot + right);

        //
        float[] r = new float[last - first + 1];
        for (int i = 0, size = r.length; i < size; i++) {
            r[i] = this.charts.get(i + first).getClose();
        }
        return r;
    }

    public float[][] getPrices(final int left, final int right) {
        //
        final float[][] r = new float[4][];
        int first = max(0, this.pivot - left + 1);
        int last = min(charts.size() - 1, pivot + right);
        for (int i = 0, size = r.length; i < size; i++) {
            r[i] = new float[last - first + 1];
        }

        //
        for (int i = 0, size = r[0].length; i < size; i++) {
            final XChart c = this.charts.get(i + first);
            r[0][i] = c.getLow();
            r[1][i] = c.getHigh();
            r[2][i] = c.getOpen();
            r[3][i] = c.getClose();
        }
        return r;
    }

    /**
     * Charts
     */
    public List<XChart> getCharts() {
        return charts;
    }

    public XChart getChart(int index) {
        return charts.get(index);
    }

    public final XChart getLastChart() {
        return Collections.getLast(charts);
    }

    public void addChart(XChart chart) {
        Collections.add(charts, chart, XChart.ASC);
    }

    public XChart[] getCharts(final int limit) {
        int first = max(0, pivot - limit + 1);
        XChart[] r = new XChart[pivot - first + 1];
        for (int i = 0, size = r.length; i < size; i++) {
            r[i] = this.charts.get(i + first);
        }
        return r;
    }

    public void reset(final Collection<? extends XChart> charts) {
        this.charts.clear();
        this.margin = new XChartMargin();
        this.cursorX = this.cursorY = -1f;
        this.pivot = -1;
        this.window = null;
        if (charts != null) this.charts.addAll(charts);
        if (!this.charts.isEmpty()) this.pivot = this.charts.size() - 1;
        if (!this.charts.isEmpty()) Collections.sort(this.charts, XChart.ASC);
    }

    public void addCharts(final Collection<? extends XChart> charts) {
        XChart x = pivot < 0 ? null : getChart(pivot);
        boolean last = (pivot == this.charts.size() - 1);
        for (final XChart chart : charts) addChart(chart);
        if (last && !this.charts.isEmpty()) this.pivot = this.charts.size() - 1;
        if (!last && x != null) this.pivot = Collections.search(this.charts, x, XChart.ASC);
    }

    /**
     * Window
     */
    public XChartWindow getWindow() {
        return window;
    }

    public void setWindow(XChartWindow window) {
        this.window = window;
    }

    public final boolean hasWindow() {
        return window != null && charts.size() > 0;
    }

    public final XChartWindow open(final int limit) {
        final ChartStyle style = this.config.getStyle();
        final int size = max(0, limit - margin.getSize());
        final XChartWindow window = new XChartWindow(this, size);
        for (int index = window.last; index >= window.first; index--) {
            XChart c = getChart(index);
            if (style != ChartStyle.LINE) {
                window.max1 = max(window.max1, c.getHigh());
                window.min1 = (window.min1 == 0f ? c.getLow() : min(window.min1, c.getLow()));
            } else {
                window.max1 = max(window.max1, c.getClose());
                window.min1 = (window.min1 == 0f ? c.getClose() : min(window.min1, c.getClose()));
            }
        }
        return window;
    }

    public void setSelectedDepot(XDrawingDepot depot) {
        this.selectedDepot = depot;
        for (XDrawingLine line : this.lines) {
            for (XDrawingDepot d : line.depots) {
                d.highlighted = depot == d;
            }
        }
    }

    public XDrawingDepot getSelectedDepot() {
        return this.selectedDepot;
    }

    public void setSelectedSegment(XDrawingSegment segment) {
        this.selectedSegment = segment;
        if (segment == null) return;
        XDrawingDepot depot = segment.getStartDepot();
        for (XDrawingLine l : this.lines) {
            for (XDrawingDepot d : l.depots) {
                d.highlighted = depot == d;
            }
        }
    }

    public XDrawingSegment getSelectedSegment() {
        return this.selectedSegment;
    }

    public void setDrawingStyle(XDrawingStyle style) {
        this.drawingStyle = style;
    }

    public XDrawingStyle getDrawingStyle() {
        return this.drawingStyle;
    }

    public void setMagneting(boolean magneting) {
        this.isMagneting = magneting;
    }

    public boolean isMagneting() {
        return this.isMagneting;
    }

    public float getTouchX() {
        return touchX;
    }

    public void setTouchX(float touchX) {
        this.touchX = touchX;
    }

    public float getTouchY() {
        return touchY;
    }

    public void setTouchY(float touchY) {
        this.touchY = touchY;
    }

    public List<XDrawingLine> getLines() {
        return this.lines;
    }

    public void addDepot(XDrawingDepot depot) {
        if (depot == null) return;
        if (this.drawingStyle != null) {
            if (this.selectedDepot != null) {
                this.selectedDepot.getParent().addDepot(depot);
            } else {
                XDrawingLine line = new XDrawingLine(this, this.drawingStyle);
                line.addDepot(depot);
                this.lines.add(line);
            }
            this.setSelectedDepot(depot);
        }
    }

    public void removeSelectedLine() {
//        XDrawingLine line = null;
//        if (this.selectedDepot != null) {
//            line = this.selectedDepot.getParent();
//            this.lines.remove(line);
//        } else if (this.selectedSegment != null) {
//            line = this.getSelectedSegment().getStartDepot().getParent();
//            this.lines.remove(line);
//        }
        this.lines.clear();
        resetDrawing();
        validateLines();
    }

    public void redraw(List<XDrawingLine> lines) {
        resetDrawing();
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void validateLines() {
        //List<XDrawingLine> toRemove = new ArrayList<>();
        for (XDrawingLine l : this.lines) {
            if (!l.isValid()) {
                //toRemove.add(l);
                this.lines.remove(l);
            }
        }
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public void setPointToMagnify(CGPoint pointToMagnify) {
        this.pointToMagnify = pointToMagnify;
    }

    public CGPoint getPointToMagnify() {
        return this.pointToMagnify;
    }

    public void resetDrawing() {
        setDrawingStyle(null);
        setSelectedDepot(null);
        setSelectedSegment(null);
        setDragging(false);
        setPointToMagnify(new CGPoint(-1.0f, -1.0f));
    }
}