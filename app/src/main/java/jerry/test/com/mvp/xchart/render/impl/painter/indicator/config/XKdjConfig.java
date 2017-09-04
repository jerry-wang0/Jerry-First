package jerry.test.com.mvp.xchart.render.impl.painter.indicator.config;

import android.graphics.Color;

import java.util.Arrays;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.Strings;

/**
 * Created by Jingqi Xu on 9/12/15.
 */
public class XKdjConfig extends AbstractXConfig implements Copyable<XKdjConfig> {
    //
    private int color;
    private String name;
    private int colors[] = new int[3];
    private int periods[] = new int[3];
    private boolean visibilities[] = new boolean[3];
    private transient String names[] = new String[3];

    /**
     *
     */
    public XKdjConfig() {
        this(0);
    }

    public XKdjConfig(int order) {
        super(IndicatorType.KDJ, order); reset();
    }

    /**
     *
     */
    @Override
    public XKdjConfig copy() {
        XKdjConfig r = new XKdjConfig();
        super.copy(this, r);
        r.name = this.name;
        r.color = this.color;
        r.names = Objects.copy(this.names);
        r.colors = Objects.copy(this.colors);
        r.periods = Objects.copy(this.periods);
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
        .append("names", Arrays.toString(names))
        .append("colors", Arrays.toString(colors))
        .append("periods", Arrays.toString(periods))
        .append("visibilities", Arrays.toString(visibilities))
        .toString();
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public int[] getPeriods() {
        return periods;
    }

    public void setPeriods(int[] periods) {
        this.periods = periods;
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

    public int getPeriod(int index) {
        return this.periods[index];
    }

    public void setPeriod(int index, int period) {
        this.periods[index] = period;
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
        this.periods[0] = 15; this.periods[1] = 5; this.periods[2] = 5;
        this.visibilities[0] = true; this.visibilities[1] = true; this.visibilities[2] = true;
        this.color = Color.WHITE; this.colors[0] = Color.CYAN; this.colors[1] = Color.GREEN; this.colors[2] = Color.RED;
    }
}
