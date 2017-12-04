package jerry.test.com.mvp.xchart.chart;

import java.util.Map;

import cn.nextop.erebor.mid.common.glossary.enums.PersistentEnum;
import cn.nextop.erebor.mid.common.util.PersistentEnums;

/**
 * 
 * @author Jingqi Xu
 */
public enum ChartScale implements PersistentEnum<Byte> {
	//
	S1((byte)1, 5, 2, ""),
	S2((byte)2, 9, 4, ""),
	S3((byte)3, 13, 6, ""),
    S4((byte)4, 17, 8, ""),
    S5((byte)5, 21, 10, ""),
    S6((byte)6, 25, 12, ""),
    S7((byte)7, 29, 14, ""),
    S8((byte)8, 33, 16, ""),
    S9((byte)9, 37, 18, ""),
    S10((byte)10, 41, 20, ""),
    S11((byte)11, 45, 22, ""),
    S12((byte)12, 49, 24, "");

	//
	private static final Map<Byte, ChartScale> INDEX = PersistentEnums.bytes(ChartScale.class);

	//
	private final int width;
	private final int margin;
    private final byte value;
    private final int offset;
    private final int center;
	private final String displayName;

	/**
	 *
	 */
	private ChartScale(byte value, int width, int margin, String displayName) {
		this.value = value;
		this.width = width;
		this.margin = margin;
        this.center = width / 2;
        this.offset = margin / 2;
		this.displayName = displayName;
	}

    /**
	 * 
	 */
	@Override
	public Byte getValue() {
		return this.value;
	}

    public int getWidth() {
        return width;
    }

    public int getMargin() {
        return margin;
    }

    public int getOffset() {
        return offset;
    }

    public int getCenter() {
        return center;
    }

    public int getNetWidth() {
        return width - margin;
    }

    public int getGrossWidth() {
        return width - offset;
    }

    @Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public Map<Byte, ChartScale> getAll() {
		return INDEX;
	}

    /**
     *
     */
    public ChartScale scale(int delta) {
        byte r = (byte)(value + delta);
        if(r < S1.getValue()) return S1;
        if(r > S12.getValue()) return S12;
        return parse(r);
    }

    public int count(final float width) {
        if(width < this.width) return 0;
        final int w = this.width, r = (int)(width / w);
        return width % w <= getGrossWidth() ? r : r + 1;
    }

	/**
	 * 
	 */
    public static ChartScale getDefault() {
        return S5;
    }

	public static ChartScale parse(Byte value) {
		return value == null ? null : INDEX.get(value);
	}
}
