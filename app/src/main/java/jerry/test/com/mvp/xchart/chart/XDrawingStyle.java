package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * Created by wanggl on 2016/11/30.
 */
public enum  XDrawingStyle implements PersistentEnum<Byte> {

    SINGLE((byte)1,"DrawingStyleSingle"),
    DOUBLE((byte)2,"DrawingStyleDouble"),
    HORIZONTAL((byte)3,"DrawingStyleHorizontal"),
    VERTICAL((byte)4,"DrawingStyleVertical"),
    HORIZONMULTIPLE((byte)5,"DrawingStyleHorizonMultiple");

    private final byte value;
    private final String displayName;

    private XDrawingStyle(byte value, String displayName){
        this.value = value;
        this.displayName = displayName;
    }
    private static final Map<Byte, XDrawingStyle> INDEX = PersistentEnums.bytes(XDrawingStyle.class);

    public String getName(){
        switch (this.value){
            case 1:return "DrawingStyleSingle";
            case 2:return "DrawingStyleDouble";
            case 3:return "DrawingStyleHorizontal";
            case 4:return "DrawingStyleVertical";
            case 5:return "DrawingStyleHorizonMultiple";
        }
        return null;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Map<Byte, XDrawingStyle> getAll() {
        return INDEX;
    }

    public static XDrawingStyle parse(Byte value) {
        return value == null ? null : INDEX.get(value);
    }
};
