package jerry.test.com.mvp.xchart.render.impl.painter.indicator.config;

import android.graphics.Color;

import java.util.Arrays;

import cn.nextop.erebor.mid.common.glossary.Copyable;
import cn.nextop.erebor.mid.common.glossary.chart.IndicatorType;
import cn.nextop.erebor.mid.common.util.Objects;
import cn.nextop.erebor.mid.common.util.Strings;


public class XIkhConfig extends AbstractXConfig implements Copyable<XIkhConfig> {
    //
    private int colors[] = new int[3];
    private int periods[] = new int[3];
    private String name, senkouName, chinkouName;
    private int color, senkouColor1, senkouColor2;
    private transient String names[] = new String[3];

    /**
     *
     */
    public XIkhConfig() {
        this(0);
    }

    public XIkhConfig(int order) {
        super(IndicatorType.IKH, order); reset();
    }

    /**
     *
     */
    @Override
    public XIkhConfig copy() {
        XIkhConfig r = new XIkhConfig();
        super.copy(this, r);
        r.name = this.name;
        r.color = this.color;
        r.senkouName = this.senkouName;
        r.chinkouName = this.chinkouName;
        r.senkouColor1 = this.senkouColor1;
        r.senkouColor2 = this.senkouColor2;
        r.names = Objects.copy(this.names);
        r.colors = Objects.copy(this.colors);
        r.periods = Objects.copy(this.periods);
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
        .append("senkouName", senkouName)
        .append("chinkouName", chinkouName)
        .append("senkouColor1", senkouColor1)
        .append("senkouColor2", senkouColor2)
        .append("names", Arrays.toString(names))
        .append("colors", Arrays.toString(colors))
        .append("periods", Arrays.toString(periods))
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

    public String getSenkouName() {
        return senkouName;
    }

    public void setSenkouName(String senkouName) {
        this.senkouName = senkouName;
    }

    public String getChinkouName() {
        return chinkouName;
    }

    public void setChinkouName(String chinkouName) {
        this.chinkouName = chinkouName;
    }

    public int getSenkouColor1() {
        return senkouColor1;
    }

    public void setSenkouColor1(int senkouColor1) {
        this.senkouColor1 = senkouColor1;
    }

    public int getSenkouColor2() {
        return senkouColor2;
    }

    public void setSenkouColor2(int senkouColor2) {
        this.senkouColor2 = senkouColor2;
    }

    /**
     *
     */
    public final int getMaxPeriod() {
        return Math.max(this.periods[0], Math.max(this.periods[1], this.periods[2]));
    }

    /**
     *
     */
    @Override
    public void reset() {
        this.periods[0] = 9; this.periods[1] = 26; this.periods[2] = 52;
        this.colors[0] = Color.GREEN; this.colors[1] = Color.RED; this.colors[2] = Color.CYAN;
        this.color = Color.WHITE; this.senkouColor1 = Color.YELLOW; this.senkouColor2 = Color.parseColor("#FFBF00");
    }
}
