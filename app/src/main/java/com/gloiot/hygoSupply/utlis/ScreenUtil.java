package com.gloiot.hygoSupply.utlis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 功能：屏幕工具类
 * Created by zxl on 2017/5/8.
 */

public class ScreenUtil {
    private static ScreenUtil mScreenUtil;

    public static int screenWidth = 0 ;
    public static int screenHeight = 0 ;

    public static ScreenUtil getInstance () {
        if (mScreenUtil == null) {
            mScreenUtil = new ScreenUtil();
        }
        return mScreenUtil;
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha,Context context)
    {
        WindowManager.LayoutParams lp =  ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
//        if (bgAlpha == 1) {
//            ((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//        } else {
//            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//        }
        ((Activity)context).getWindow().setAttributes(lp);
    }


    /**
     * popwind从下往上冒出的动画
     *
     * */
    public Animation inDownFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f
        );
        inFromRight.setDuration(120);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    /** 初始化屏幕尺寸 */
    public void initPhoneSize(Context c){
        try {
            Resources resources = c.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            float density1 = dm.density;
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 设置弹出框的宽和高( 必须先调show方法，再调此方法设置Attributes，否则改变不了对话框的宽高)
     * @param dialog 弹出框
     * @param context
     */
    public static void setDialogMetrics(Dialog dialog, Context context) {
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        DisplayMetrics mDisplayMetrics = getMetric((Activity) context);
        layoutParams.width = (int) (mDisplayMetrics.widthPixels * 0.7);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }


    /**
     * Activity显示的宽和高
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getMetric(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
