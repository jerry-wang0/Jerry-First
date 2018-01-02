package jerry.test.com.mvp.support;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Jingqi Xu on 10/23/15.
 */
public final class SoftKeyboards {

    /**
     *
     */
    public static void hide(final Activity activity) {
        if(activity == null) return;
        final View v = activity.getCurrentFocus();
        final String service = Context.INPUT_METHOD_SERVICE;
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(service);
        if (imm != null && v != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // Hide
    }
}
