package jerry.test.com.mvp.xchart.render.impl.painter.drawing;

import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartModel;
import cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme;
import cn.nextop.erebor.mid.common.glossary.chart.XSegment;

import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_GREEN;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_WHITE;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.XChartUtils.COLOR_YELLOW;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_EMPTY_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_HIGHLIGHT_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_NORMAL_COLOR;
import static cn.nextop.erebor.mid.app.mvc.support.widget.xchart.model.XChartTheme.DRAWING_PAINTER_VIRTUAL_COLOR;


public class XDrawingSegment implements XSegment {

    private XChartModel model;
    private boolean draggable;
    private SegmentType type;
    private SegmentExtendable extendableType;
    private XDrawingDepot startDepot;
    private XDrawingDepot endDepot;
    private String graduation;

    public XDrawingSegment(XChartModel model){
        this.model = model;
    }

    public XChartModel getModel(){
        return this.model;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public SegmentType getType() {
        return type;
    }

    public void setType(SegmentType type) {
        this.type = type;
    }

    public SegmentExtendable getExtendableType() {
        return extendableType;
    }

    public void setExtendableType(SegmentExtendable extendableType) {
        this.extendableType = extendableType;
    }

    public XDrawingDepot getStartDepot() {
        return startDepot;
    }

    public void setStartDepot(XDrawingDepot startDepot) {
        this.startDepot = startDepot;
    }

    public XDrawingDepot getEndDepot() {
        return endDepot;
    }

    public void setEndDepot(XDrawingDepot endDepot) {
        this.endDepot = endDepot;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public int getColor(boolean selected){
        XChartTheme theme = this.model.getTheme();
        if(selected){
            switch (type) {
                case kTypeVirtual:
                    return theme.getProperty(DRAWING_PAINTER_VIRTUAL_COLOR,COLOR_GREEN);
                case kTypeReal:
                case kTypeInfiniteH:
                case kTypeInfiniteV:
                default:
                    return theme.getProperty(DRAWING_PAINTER_HIGHLIGHT_COLOR,COLOR_YELLOW);
            }
        }else{
            switch (type) {
                case kTypeVirtual:
                    return theme.getProperty(DRAWING_PAINTER_EMPTY_COLOR,COLOR_WHITE);
                case kTypeReal:
                case kTypeInfiniteH:
                case kTypeInfiniteV:
                default:
                    return theme.getProperty(DRAWING_PAINTER_NORMAL_COLOR,COLOR_WHITE);
            }
        }
    }
}
