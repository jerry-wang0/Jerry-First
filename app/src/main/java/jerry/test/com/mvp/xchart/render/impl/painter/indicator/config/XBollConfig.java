package jerry.test.com.mvp.xchart.render.impl.painter.indicator.config;

import android.graphics.Color;

import java.util.Arrays;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.Strings;

/**
 * Created by Jingqi Xu on 9/8/15.
 */
public class XBollConfig extends AbstractXConfig implements Copyable<XBollConfig> {
    //
    private int color;
    private int period;
    private String name;
    private int colors[] = new int[4];
    private boolean visibilities[] = new boolean[4];
    private transient String names[] = new String[4];

    /**
     *
     */
    public XBollConfig() {
        this(0);
    }

    public XBollConfig(int order) {
        super(IndicatorType.BOLL, order); reset();
    }

    /**
     *
     */
    @Override
    public XBollConfig copy() {
        XBollConfig r = new XBollConfig();
        super.copy(this, r);
        r.name = this.name;
        r.color = this.color;
        r.period = this.period;
        r.names = Objects.copy(this.names);
        r.colors = Objects.copy(this.colors);
        r.visibilities = Objects.copy(this.visibilities);
        return r;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("name", name)
        .append("color", color)
        .append("period", period)
        .append("names", Arrays.toString(names))
        .append("colors", Arrays.toString(colors))
        .append("visibilities", Arrays.toString(visibilities))
        .toString();
    }

    /**
     *
     */
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int getColor(int index) {
        return this.colors[index];
    }

    public void setColor(int index, int color) {
        this.colors[index] = color;
    }

    public String getName(int index) {
        return this.names[index];
    }

    public void setName(int index, String name) {
        this.names[index] = name;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setPeriod(int index, int period) {
        setPeriod(period);
    }

    public boolean isVisible(int index) {
        return this.visibilities[index];
    }

    public void setVisible(int index, boolean visible) {
        this.visibilities[index] = visible;
    }

    public boolean[] getVisibilities() {
        return visibilities;
    }

    public void setVisibilities(boolean[] visibilities) {
        this.visibilities = visibilities;
    }

    /**
     *
     */
    @Override
    public void reset() {
        this.period = 21;
        this.visibilities[0] = true; this.visibilities[1] = true; this.visibilities[2] = true; this.visibilities[3] = true;
        this.color = Color.WHITE; this.colors[0] = Color.CYAN; this.colors[1] = Color.GREEN; this.colors[2] = Color.YELLOW; this.colors[3] = Color.RED;
    }
}
