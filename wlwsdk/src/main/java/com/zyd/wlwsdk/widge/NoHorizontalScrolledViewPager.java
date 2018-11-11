package com.zyd.wlwsdk.widge;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可禁掉左右滑动的ViewPager
 * Created by hygo04 on 2017/4/17 14:12
 */
public class NoHorizontalScrolledViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public NoHorizontalScrolledViewPager(Context context) {
        super(context);
    }

    public NoHorizontalScrolledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(ev);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
