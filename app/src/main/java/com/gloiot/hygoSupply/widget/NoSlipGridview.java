package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 作者：Ljy on 2016/11/24.
 * 邮箱：enjoy_azad@sina.com
 */

public class NoSlipGridview extends GridView {
    public NoSlipGridview(Context context) {
        super(context);
    }

    public NoSlipGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlipGridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
