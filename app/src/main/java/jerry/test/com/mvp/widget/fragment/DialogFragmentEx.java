package jerry.test.com.mvp.widget.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import cn.nextop.erebor.mid.R;

/**
 * Created by Jingqi Xu on 7/27/15.
 */
@EFragment
public class DialogFragmentEx extends DialogFragment {
    //
    private static final Logger LOGGER = LoggerFactory.getLogger(DialogFragmentEx.class);

    //
    protected static List<DialogFragmentEx> DIALOGS = new ArrayList<>();

    //
    protected boolean visible;
    protected boolean verbose = false;
    protected FragmentManager manager;
    protected List<Listener> listeners = new ArrayList<>();

    //
    public interface Listener { void onDismiss(DialogFragmentEx dialog); }

    //
    protected void onCreate() {}
    protected <T> T getModel() { return null; }
    protected boolean isBlur() { return true; }
    public void setManager(FragmentManager m) { manager = m; }
    protected boolean isCanceledOnTouchOutside() { return true; }
    public boolean addListener(Listener listener) { return listeners.add(listener); }
    public boolean delListener(Listener listener) { return listeners.remove(listener); }

    /**
     *
     */
    public DialogFragmentEx() {
        super();
        setStyle(STYLE_NO_TITLE, R.style.dialog_style);
    }

    public boolean visible() { return this.visible; }


    /**
     *
     */
    @AfterViews
    protected void afterViews() {
        if(getModel() != null) onCreate();
    }

    /**
     *
     */
    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        final Dialog dialog = getDialog();
        if(dialog != null) dialog.setCancelable(cancelable);
        if(dialog != null) dialog.setCanceledOnTouchOutside(cancelable);
    }

    /**
     *
     */
    public void resize(View v, int w, int h) {
        try {
            if(v == null) return;
            final Rect rect = getDisplaySize();
            if(w <= 0) w = (int)(getDisplayMetrics().widthPixels * 0.8f);
            if(h <= 0) h = (int)(getDisplayMetrics().heightPixels * 0.8f);
            v.getLayoutParams().width = w; v.getLayoutParams().height = h;
            v.setX((rect.width() - w) / 2); v.setY((rect.height() - h) / 2);
        } catch(Throwable t) {
            LOGGER.info("failed to resize, view: " + v + ", height: " + h, t);
        }
    }

    /**
     *
     */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onStop() {
        super.onStop(); dismissAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     *
     */
    @Override
    public void dismiss() {
        super.dismiss(); this.visible = false;
    }

    /**
     *
     */
    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss(); this.visible = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.visible = false; DIALOGS.remove(this);
        for(Listener listener : listeners) listener.onDismiss(this);
        if (verbose) LOGGER.info("dismiss[1]: {}, total: {}", this, DIALOGS.size());
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public void onViewCreated(final View view, Bundle saved) {
        super.onViewCreated(view, saved); this.setCancelable(isCanceledOnTouchOutside());
    }

    /**
     *
     */
    @Override
    public void show(FragmentManager fm, String tag) {
        this.visible = true; super.show(fm, tag); DIALOGS.add(this); // Add to active dialogs
        if(verbose) LOGGER.info("show: {}, total: {}", this, DIALOGS.size());
    }

    @UiThread
    protected void hide(final String tag) {
        final Fragment f = manager.findFragmentByTag(tag); if(f == null) return;
        FragmentTransaction t = manager.beginTransaction(); t.remove(f); t.commit();
    }

    /**
     *
     */
    protected final DisplayMetrics getDisplayMetrics() {
        final Context context = getActivity().getApplicationContext();
        return context == null ? null : context.getResources().getDisplayMetrics();
    }

    /**
     *
     */
    protected final Rect getDisplaySize() {
        Rect rect = new Rect(); if(getActivity() == null) return rect;
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect;
    }

    /**
     *
     */
    public static final void dismiss(DialogFragmentEx dialog) {
        if (dialog != null && dialog.isVisible()) dialog.dismiss();
        if (dialog == null) { for(DialogFragmentEx d : DIALOGS) d.dismiss(); DIALOGS.clear(); }
    }

    public static final void dismissAllowingStateLoss(DialogFragmentEx dialog) {
        if (dialog != null && dialog.isVisible()) dialog.dismissAllowingStateLoss();
        if (dialog == null) { for(DialogFragmentEx d : DIALOGS) d.dismissAllowingStateLoss(); DIALOGS.clear(); }
    }
}
