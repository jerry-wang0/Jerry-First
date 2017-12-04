package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * 
 * @author Jingqi Xu
 */
public enum IndicatorArea implements PersistentEnum<Byte> {
	//
	A((byte)1, "A"),
    B((byte)2, "B");

	//
	private static final Map<Byte, IndicatorArea> INDEX = PersistentEnums.bytes(IndicatorArea.class);

	//
	private final byte value;
	private final String displayName;

	/**
	 *
	 */
	private IndicatorArea(byte value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	/**
	 * 
	 */
	public boolean isA() {
		return this == A;
	}

	public boolean isB() {
		return this == B;
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
	public Map<Byte, IndicatorArea> getAll() {
		return INDEX;
	}
	
	/**
	 * 
	 */
	public static IndicatorArea parse(Byte value) {
		return value == null ? null : INDEX.get(value);
	}
}
