package wuxian.me.andbootstrapdemo.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private boolean disallowInterceptTouchEvent = false;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        disallowInterceptTouchEvent = b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            disallowInterceptTouchEvent = false;
        }
        if (disallowInterceptTouchEvent) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
