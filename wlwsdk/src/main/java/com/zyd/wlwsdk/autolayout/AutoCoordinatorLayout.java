package com.zyd.wlwsdk.autolayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zyd.wlwsdk.autolayout.utils.AutoLayoutHelper;

/**
 * Created by hygo00 on 17/5/3.
 */

public class AutoCoordinatorLayout extends CoordinatorLayout {
    public AutoCoordinatorLayout(Context context) {
        super(context);
    }

    public AutoCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isInEditMode())
//            mHelper.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
    }

//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs)
//    {
//        return new LayoutParams(getContext(), attrs);
//    }


    public static class LayoutParams extends CoordinatorLayout.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams
    {
        private AutoLayoutInfo mAutoLayoutInfo;

//        public LayoutParams(Context c, AttributeSet attrs)
//        {
//            super(c, attrs);
//            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
//        }

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo()
        {
            return mAutoLayoutInfo;
        }


    }
}
