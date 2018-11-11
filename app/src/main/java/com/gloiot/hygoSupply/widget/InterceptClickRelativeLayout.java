package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by azadljy on 2017/8/23.
 */

public class InterceptClickRelativeLayout extends RelativeLayout  {

    static String TAG = "InterceptRelativeLayout";

    private int[] allowIds;

    public InterceptClickRelativeLayout(Context context) {
        super(context);
    }

    public InterceptClickRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptClickRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    private boolean mScrolling;
    private float touchDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = ev.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownY - ev.getY()) >= ViewConfiguration.get(getContext()).getScaledEdgeSlop()) {
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return !mScrolling;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }


    private View isClicked(View view, MotionEvent event) {
        View result = null;
        if (inRangeOfView(view, event) && view.getVisibility() == View.VISIBLE) {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View chilView = group.getChildAt(i);
                    result = isClicked(chilView, event);
                    if (result != null) {
                        return result;
                    }
                }
            }
            result = view;
        }
        return result;
    }

    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y
                || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    public void setAllowIds(int[] allowIds) {
        this.allowIds = allowIds;
    }
}
