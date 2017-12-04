package jerry.test.com.mvp.xchart.chart;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * 
 * @author Jingqi Xu
 */
public enum ChartType implements PersistentEnum<Byte> {
	//
	M1((byte)1, "M1", TimeUnit.MINUTES.toMillis(1)),
	M5((byte)2, "M5", TimeUnit.MINUTES.toMillis(5)),
	M10((byte)3, "M10", TimeUnit.MINUTES.toMillis(10)),
	M15((byte)4, "M15", TimeUnit.MINUTES.toMillis(15)),
	M30((byte)5, "M30", TimeUnit.MINUTES.toMillis(30)),
	H1((byte)6, "H1", TimeUnit.HOURS.toMillis(1)),
	H2((byte)7, "H2", TimeUnit.HOURS.toMillis(2)),
	H4((byte)8, "H4", TimeUnit.HOURS.toMillis(4)),
	D1((byte)9, "D1", 0L),
	W1((byte)10, "W1", 0L),
	MN1((byte)16, "MN1", 0L);
	
	//
	protected final byte value;
	protected final long interval;
	protected final String displayName;
	
	//
	private static final Map<Byte, ChartType> INDEX = PersistentEnums.bytes(ChartType.class);

	/**
	 * 
	 */
	private ChartType(byte value, String displayName, long interval) {
		this.value = value;
		this.interval = interval;
		this.displayName = displayName;
	}

	/**
	 * 
	 */
	@Override
	public Byte getValue() {
		return this.value;
	}
	
	public boolean isIntraday() {
		return interval > 0L;
	}
	
	public boolean isInterday() {
		return interval == 0L;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public Map<Byte, ChartType> getAll() {
		return INDEX;
	}
	
	/**
	 * 
	 */
	public static ChartType getDefault() {
		return M1;
	}

	public static ChartType parse(Byte value) {
		return value == null ? null : INDEX.get(value);
	}
}
