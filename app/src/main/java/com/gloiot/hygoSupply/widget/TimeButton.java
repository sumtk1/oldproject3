package com.gloiot.hygoSupply.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygoSupply.ui.App;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取验证码显示倒计时控件，继承Button的话，无法去掉背景边框，所以直接继承TextView
 * <p/>
 * 由于timer每次cancle()之后不能重新schedule方法,所以计时完毕只恐timer.
 * 每次开始计时的时候重新设置timer, 没想到好办法出此下策
 * 注意把该类的onCreate()onDestroy()和activity的onCreate()onDestroy()同步处理
 * <p/>
 * Created by hygo00 on 2016/8/12.
 */
public class TimeButton extends TextView implements View.OnClickListener {

    boolean condition = false; // 判断是否符合条件
    private long time; // 剩余时间
    private long lenght = 0 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重新获取";
    private String textbefore = "获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private int cl_before,cl_after;
    private OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;

    public TimeButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                Log.e("textbefore",textbefore+"---");
               TimeButton.this.setText(textbefore);
               TimeButton.this.setTextColor(cl_before);
               clearTimer();
            }
        }
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
               // Log.e("TimeButton", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
    }


    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        Log.e("TimeButton", "onCreate");
        if (App.timeMap == null)
            return;
        if (App.timeMap.size() <= 0) // 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - App.timeMap.get(CTIME) - App.timeMap.get(TIME);
        App.timeMap.clear();
        if (time > 0) // 超过时间
            return;
        else {
            initTimer();
            this.time = Math.abs(time); // 取绝对值
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (App.timeMap == null)
            App.timeMap = new HashMap<>();
        App.timeMap.put(TIME, time);
        App.timeMap.put(CTIME, System.currentTimeMillis());
        clearTimer();
        Log.e("TimeButton", "onDestroy");
    }


    /**
     * 设置计时时候显示的文本
     */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置计时时候显示的文本 和颜色
     */
    public TimeButton setTextAfter(String text1,int cl_after) {
        this.textafter = text1;
        this.cl_after=cl_after;
        return this;
    }

    /**
     * 设置点击之前的文本 和颜色
     */
    public TimeButton setTextBefore(String text0,int cl_before) {
        this.textbefore = text0;
        this.setText(textbefore);
        this.cl_before=cl_before;
        this.setTextColor(cl_before);
        return this;
    }

    /**
     * 设置到计时长度
     * @param lenght 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }

    /**
     * 设置是否符合判断条件
     * @param condition
     * @return
     */
    public TimeButton setCondition(boolean condition) {
        if (condition){
            initTimer();
            this.setText(time / 1000 + textafter);
            this.setEnabled(false);
            this.setTextColor(cl_after);
            t.schedule(tt, 0, 1000);
            this.condition = condition;
        }
        return this;
    }
}
