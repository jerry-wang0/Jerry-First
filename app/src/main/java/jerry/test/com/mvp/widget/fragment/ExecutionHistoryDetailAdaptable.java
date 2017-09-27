package jerry.test.com.mvp.widget.fragment;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;

import java.io.Serializable;

import cn.nextop.erebor.mid.common.util.Strings;

/**
 * Created by Liqun Wan on 2015/7/20.
 */
public class ExecutionHistoryDetailAdaptable implements Serializable {
    //
    private int name;
    private Spanned value;
    private int color = Color.WHITE;

    /**
     *
     */
    public ExecutionHistoryDetailAdaptable() {
    }

    public ExecutionHistoryDetailAdaptable(int name) {
        this.name = name;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return Strings.build(this)
        .append("name", name)
        .append("value", value)
        .append("color", color)
        .toString();
    }

    /**
     *
     */
    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Spanned getValue() {
        return value;
    }

    public void setValue(Spanned value) {
        this.value = value;
    }

    public void setValue(final String value) {
        this.value = new SpannableString(value);
    }
}
