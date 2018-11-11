package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Created by hygo03 on 2017/4/6.
 */

public class PercentTextView extends TextView {

    private String mPercent;

    private BarAnimation animation;

    private static final long DEFAULT_DURATION = 500;
    public PercentTextView(Context context) {
        super(context);
        init();
    }

    public PercentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText("已认证");
    }

    public void setMaxValue(String value) {
        mPercent = value;
    }

    public void start() {
        if (!TextUtils.isEmpty(mPercent)) {
            animation = new BarAnimation(this, mPercent);
            animation.setDuration(DEFAULT_DURATION);
            startAnimation(animation);
        }
    }
    public void start(final long time) {
        if (!TextUtils.isEmpty(mPercent)) {
            animation = new BarAnimation(this, mPercent);
            animation.setDuration(time);
            startAnimation(animation);
        }
    }

    public class BarAnimation extends Animation {

        private TextView mTextView;
        private String maxValue;

        public BarAnimation(TextView view, String maxValue) {
            mTextView = view;
            this.maxValue = maxValue;
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {

            setInterpolator(new LinearInterpolator());
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            int count = (int) (interpolatedTime * Integer.valueOf(maxValue));
            if(count==Integer.valueOf(maxValue)){
                mTextView.setVisibility(GONE);
            }
//            mTextView.setText(count+"");

        }

    }
}
