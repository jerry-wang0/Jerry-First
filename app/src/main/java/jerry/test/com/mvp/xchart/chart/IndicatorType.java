package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * 
 * @author Jingqi Xu
 */
public enum IndicatorType implements PersistentEnum<Byte> {
	//
	SMA((byte)1, IndicatorArea.A, "SMA"),
    EMA((byte)2, IndicatorArea.A, "EMA"),
	DMI((byte)3, IndicatorArea.B, "DMI"),
	IKH((byte)4, IndicatorArea.A, "IHK"),
	KDJ((byte)5, IndicatorArea.B, "KDJ"),
	RSI((byte)6, IndicatorArea.B, "RSI"),
    BOLL((byte)7, IndicatorArea.A, "BOLL"),
    MACD((byte)8, IndicatorArea.B, "MACD");

	//
	private static final Map<Byte, IndicatorType> INDEX = PersistentEnums.bytes(IndicatorType.class);

	//
	private final byte value;
	private final String displayName;
	private final IndicatorArea area;

	/**
	 *
	 */
	private IndicatorType(byte value, IndicatorArea area, String displayName) {
        this.area = area;
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

    public IndicatorArea getArea() {
        return area;
    }

    @Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public Map<Byte, IndicatorType> getAll() {
		return INDEX;
	}
	
	/**
	 * 
	 */
	public static IndicatorType parse(Byte value) {
		return value == null ? null : INDEX.get(value);
	}
}
