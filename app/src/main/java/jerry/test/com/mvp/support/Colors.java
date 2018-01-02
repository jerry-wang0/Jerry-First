package jerry.test.com.mvp.support;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Jingqi Xu on 12/23/15.
 */
public final class Colors {
    //
    public static final int RED = Color.parseColor("#FF0000"); // red
    public static final int WHITE = Color.parseColor("#FFFFFF"); // white
    public static final int RB_BLUE = Color.parseColor("#0080cc");
    public static final int BLUE_LIGHT = Color.parseColor("#3598db");
    public static final int COLOR_PRICE_UP = Color.parseColor("#FF0000"); // red
    public static final int COLOR_PRICE_EVEN = Color.parseColor("#FFFFFF"); // white
    public static final int COLOR_PRICE_DOWN = Color.parseColor("#1B6AA5"); // green

    //
    public static final ColorStateList COLOR_STATES1 = new ColorStateList(new int[][] {
            { android.R.attr.state_pressed },
            {-android.R.attr.state_enabled, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed,  android.R.attr.state_checked}},
            new int[]{Color.GRAY, Color.GRAY, RB_BLUE,  Color.WHITE});

    public static final ColorStateList COLOR_STATES2 = new ColorStateList(new int[][] {
            { android.R.attr.state_pressed },
            {-android.R.attr.state_enabled, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed,  android.R.attr.state_checked}},
            new int[]{Color.GRAY, Color.GRAY, Color.RED,  WHITE});

    public static final ColorStateList COLOR_STATES3 = new ColorStateList(new int[][] {
            { android.R.attr.state_pressed },
            {-android.R.attr.state_enabled, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed, -android.R.attr.state_checked},
            {-android.R.attr.state_pressed,  android.R.attr.state_checked}},
            new int[]{Color.GRAY, Color.GRAY, Color.WHITE, Color.BLACK});

    /**
     *
     */
    public static Drawable getEffectDrawable(int solidColor, int strokeColor, float radius) {
        GradientDrawable draw = new GradientDrawable();
        if(radius > 0) draw.setCornerRadius(radius);
        draw.setColor(solidColor); draw.setStroke(1, strokeColor);  return draw;
    }

    public static int getColor(final Context context, final int colorId){
        return ContextCompat.getColor(context, colorId);
    }

}
