package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * Created by Jingqi Xu on 2015/7/13.
 */
public enum ChartOrderMode implements PersistentEnum<Byte> {
    //
    BASIC((byte)1,""),
    LEAVE((byte)2,"");

    //
    private static final Map<Byte, ChartOrderMode> INDEX = PersistentEnums.bytes(ChartOrderMode.class);

    //
    private final byte value;
    private final String displayName;

    /**
     *
     */
    private ChartOrderMode(byte value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     *
     */
    @Override
    public Byte getValue() {
        return this.value;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public Map<Byte, ChartOrderMode> getAll() {
        return INDEX;
    }

    /**
     *
     */
    public boolean isVerbose() {
        return false;
    }

    /**
     *
     */
    public static ChartOrderMode parse(Byte value) {
        return value == null ? null : INDEX.get(value);
    }
}
