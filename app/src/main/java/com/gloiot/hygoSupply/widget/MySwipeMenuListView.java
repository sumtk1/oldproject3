package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

/**
 * Created by ZXL on 2017/7/10.
 */

public class MySwipeMenuListView extends SwipeMenuListView {

    private RefreshLayout mRefreshLayout;
    int downX = 0;
    int lastX = 0;
    int x;
    private int MAX_X = 3;
    String TAG = "MySwipeMenuListView";

    public MySwipeMenuListView(Context context) {
        super(context);
        MAX_X = dp2px(MAX_X);
    }

    public MySwipeMenuListView(Context context,RefreshLayout refreshLayout) {
        super(context);
        mRefreshLayout = refreshLayout;
    }
    public void setmRefreshLayout (RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

    public MySwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = (int) ev.getRawX();
                if (mRefreshLayout != null) {
                    x = Math.abs(lastX - downX);
                    if (x < MAX_X) {
                        mRefreshLayout.setEnabled(true);
                        Log.e(TAG, "onTouchEvent: " + "============  MotionEvent.ACTION_DOWN _  true  " + " x =" + x );
                    } else {
                        mRefreshLayout.setEnabled(false);
                        Log.e(TAG, "onTouchEvent: " + "============  MotionEvent.ACTION_DOWN _  false  " + " x = " + x );
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouchEvent: " + "============  MotionEvent.ACTION_UP");
                if (mRefreshLayout != null) {
                    mRefreshLayout.setEnabled(true);
                    Log.e(TAG, "onTouchEvent: " + "============  true");
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /*
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        //这里的parentScrollView就是listview外面的那个scrollview
        Log.e("MyLog", "setParentScrollAble -- " + flag);
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}
