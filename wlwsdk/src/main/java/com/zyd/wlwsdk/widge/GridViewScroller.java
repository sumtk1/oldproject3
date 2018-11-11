package com.zyd.wlwsdk.widge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by Admin on 2016/9/27.
 */
public class GridViewScroller extends ScrollView {


    private int downX;
    private int downY;
    private int mTouchSlop;

    public GridViewScroller(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public GridViewScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public GridViewScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    private GridViewScrollerListener gridViewScrollerListener;

    public void setGridViewScrollerListener(GridViewScrollerListener gridViewScrollerListener) {
        this.gridViewScrollerListener = gridViewScrollerListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (gridViewScrollerListener != null) {
            gridViewScrollerListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
