package jerry.test.com.mvp.xchart.gesture;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Jingqi Xu on 9/2/15.
 */
public class MoveGestureDetector {
    //
    protected static final int INVALID_POINTER_ID = -1;

    //
    protected float focusX, focusY;
    protected final Context context;
    protected boolean resume = false;
    protected OnMoveGestureListener listener;
    protected int pointerId = INVALID_POINTER_ID;

    /**
     *
     */
    public interface OnMoveGestureListener {

        boolean onMove(MoveGestureDetector detector, MotionEvent event);

        void onMoveEnd(MoveGestureDetector detector, MotionEvent event);

        boolean onMoveBegin(MoveGestureDetector detector, MotionEvent event);
    }

    /**
     *
     */
    public MoveGestureDetector(Context context) {
        this.context = context;
    }

    public MoveGestureDetector(Context context, OnMoveGestureListener listener) {
        this.context = context; this.listener = listener;
    }

    /**
     *
     */
    public float getFocusX() {
        return focusX;
    }

    public float getFocusY() {
        return focusY;
    }

    public int getPointerId() {
        return pointerId;
    }

    public OnMoveGestureListener getListener() {
        return listener;
    }

    public void setListener(OnMoveGestureListener listener) {
        this.listener = listener;
    }

    /**
     *
     */
    protected float getFocusX(final MotionEvent event) {
        int count = event.getPointerCount();
        int index = event.findPointerIndex(pointerId);
        return (index < count) ? event.getX() : -1.0f;
    }

    protected float getFocusY(final MotionEvent event) {
        int count = event.getPointerCount();
        int index = event.findPointerIndex(pointerId);
        return (index < count) ? event.getY() : -1.0f;
    }

    protected int getPointerIndex(final MotionEvent event) {
        int action = event.getAction();
        action = action & MotionEvent.ACTION_POINTER_INDEX_MASK;
        return action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

    /**
     *
     */
    public boolean onTouchEvent(final MotionEvent event) {
        //
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP: {
                this.pointerId = INVALID_POINTER_ID;
                final OnMoveGestureListener listener = this.listener;
                if(listener != null) listener.onMoveEnd(this, event); break;
            }
            case MotionEvent.ACTION_CANCEL: {
                this.pointerId = INVALID_POINTER_ID;
                final OnMoveGestureListener listener = this.listener;
                if(listener != null) listener.onMoveEnd(this, event); break;
            }
            case MotionEvent.ACTION_DOWN: {
                this.focusX = event.getX();
                this.pointerId = event.getPointerId(0);
                final OnMoveGestureListener listener = this.listener;
                if(listener != null) listener.onMoveBegin(this, event); break;
            }
            case MotionEvent.ACTION_MOVE: {
                if(this.pointerId == INVALID_POINTER_ID) {
                    break;
                } else {
                    this.focusX = getFocusX(event);
                    this.focusY = getFocusY(event);
                    if(focusX < 0f || focusY < 0f) break;
                    final OnMoveGestureListener listener = this.listener;
                    if (listener != null) listener.onMove(this, event); break;
                }
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int index = getPointerIndex(event);
                final int pointerId = event.getPointerId(index);
                if (pointerId == this.pointerId) {
                    if(!resume) {
                        this.pointerId = INVALID_POINTER_ID;
                        OnMoveGestureListener listener = this.listener;
                        if(listener != null) listener.onMoveEnd(this, event);
                    } else {
                        final int next = (index == 0 ? 1 : 0);
                        this.pointerId = event.getPointerId(next);
                        this.focusX = event.getX(next); this.focusY = event.getY(next);
                    }
                }
                break;
            }
        }
        return true;
    }
}
