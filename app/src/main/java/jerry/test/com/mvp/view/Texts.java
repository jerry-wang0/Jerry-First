package jerry.test.com.mvp.view;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jingqi Xu on 9/21/15.
 */
public class Texts {

    /**
     *
     */
    public static class TextWatcherAdapter implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    }

    /**
     *
     */
    public static final InputFilter length(int length) {
        return new InputFilter.LengthFilter(length);
    }

    /**
     *
     */
    public static final InputFilter scale(final int scale) {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    final String r = merge(source, start, end, dest, dstart, dend);
                    int index = r.indexOf("."); return (index < 0 || ((r.length() - index - 1) <= scale)) ? null : "";
                }
                return null;
            }
        };
    }

    /**
     *
     */
    public static final int getInputType(final boolean decimal, final boolean signed) {
        int r = InputType.TYPE_CLASS_NUMBER;
        if(signed) r |= InputType.TYPE_NUMBER_FLAG_SIGNED;
        if(decimal) r |= InputType.TYPE_NUMBER_FLAG_DECIMAL; return r;
    }

    public static final void setText(final EditText text, final String value, final boolean select) {
        setText(text, value); if(select) text.setSelection(text.getText().length());
    }

    public static final void setText(final TextView t, final String v) { t.setError(null); t.setText(v); }

    public static final String merge(final CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        final String d = dest.toString(); return d.substring(0, dstart) + source.subSequence(start, end) + d.substring(dend);
    }
}
