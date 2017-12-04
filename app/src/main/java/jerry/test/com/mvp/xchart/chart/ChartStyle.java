package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * 
 * @author Jingqi Xu
 */
public enum ChartStyle implements PersistentEnum<Byte> {
	//
	LINE((byte)1, "Line"),
	OHLC((byte)2, "OHLC Line"),
	CANDLE((byte)3, "Candle Stick");

	//
	private static final Map<Byte, ChartStyle> INDEX = PersistentEnums.bytes(ChartStyle.class);

	//
	private final byte value;
	private final String displayName;

	/**
	 *
	 */
	private ChartStyle(byte value, String displayName) {
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
	public Map<Byte, ChartStyle> getAll() {
		return INDEX;
	}
	
	/**
	 * 
	 */
	public static ChartStyle getDefault() {
		return CANDLE;
	}

	public static ChartStyle parse(Byte value) {
		return value == null ? null : INDEX.get(value);
	}
}
