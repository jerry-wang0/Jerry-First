package jerry.test.com.mvp.view;

import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Jingqi Xu on 9/17/15.
 */
public final class Views {

    /**
     *
     */
    protected static int getPointerIndex(final MotionEvent event) {
        int action = event.getAction();
        action = action & MotionEvent.ACTION_POINTER_INDEX_MASK;
        return action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

    /**
     *
     */
    public static abstract class OnClickAdapter implements OnClickListener {
        //
        protected long interval, timestamp = 0L;

        //
        protected abstract void doOnClick(View v);

        /**
         *
         */
        public OnClickAdapter() {
            this(800L);
        }

        public OnClickAdapter(long interval) {
            this.interval = interval;
        }

        /**
         *
         */
        @Override
        public final void onClick(final View view) {
            final long now = System.currentTimeMillis();
            if (now - timestamp > this.interval) {
                timestamp = now;  doOnClick(view);
            }
        }
    }

    /**
     *
     */
    public static abstract class OnFocusChangeAdapter implements View.OnFocusChangeListener {

    }

    /**
     *
     */
    public static final class OnRepeatClickListener implements Runnable, View.OnTouchListener {
        //
        protected static final int INVALID_POINTER_ID = -1;

        //
        private Rect rect;
        protected View view;
        protected Handler handler;
        protected long initial, delay;
        protected boolean repeat = false;
        protected OnClickListener listener;
        protected volatile int pointerId = INVALID_POINTER_ID;

        /**
         *
         */
        public OnRepeatClickListener(OnClickListener listener, Handler handler, View view) {
            this(listener, handler, view, 500L, 120L);
        }

        public OnRepeatClickListener(OnClickListener listener, Handler handler, View view, long initial, long delay) {
            this.view = view;
            this.initial = initial;
            this.delay = delay;
            this.handler = handler;
            this.listener = listener;
        }

        /**
         *
         */
        @Override
        public void run() {
            //
            long i = this.repeat ? delay : initial;
            this.repeat = true;

            //
            if (this.pointerId != INVALID_POINTER_ID) {
                this.listener.onClick(this.view);
                this.handler.postDelayed(this, i);
            }
        }

        /**
         *
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP: {
                    this.pointerId = INVALID_POINTER_ID;
                    this.handler.removeCallbacks(this);
                    break;
                }
                case MotionEvent.ACTION_CANCEL: {
                    this.pointerId = INVALID_POINTER_ID;
                    this.handler.removeCallbacks(this);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    final int width = v.getLeft() + (int) event.getX();
                    final int height = v.getTop() + (int) event.getY();
                    if (rect != null && !rect.contains(width, height)) {
                        this.pointerId = INVALID_POINTER_ID;
                        this.handler.removeCallbacks(this);
                    }
                    break;
                }
                case MotionEvent.ACTION_DOWN: {
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    this.pointerId = event.getPointerId(0);
                    this.handler.postDelayed(this, 0L);
                    break;
                }
                case MotionEvent.ACTION_POINTER_UP: {
                    final int index = getPointerIndex(event);
                    final int pointerId = event.getPointerId(index);
                    if (pointerId == this.pointerId) {
                        final int next = (index == 0 ? 1 : 0);
                        this.pointerId = event.getPointerId(next);
                    }
                    break;
                }
            }
            return true;
        }
    }
}


