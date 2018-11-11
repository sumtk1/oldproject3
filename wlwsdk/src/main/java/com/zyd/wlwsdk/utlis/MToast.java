package com.zyd.wlwsdk.utlis;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by hygo00 on 2016/7/7.
 * Toast工具类
 */
public class MToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null; // toast隐藏后，将其置为null
        }
    };
    private static String toastContet = "";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 显示Toast
     *
     * @param content
     */
    public static void showToast(String content) {
        if (toastContet.equals(content)) { // 判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) { // 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) { // mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 显示自定义Toast
     *
     * @param content
     * @param toastCallback
     */
    public static void showToast(String content, ToastCallback toastCallback) {
        if (toastContet.equals(content)) { // 判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) { // 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
                toastCallback.toastCallback(mContext, content, mToast);
            }
        } else {
            toastContet = content;
            if (mToast != null) { // mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
            toastCallback.toastCallback(mContext, content, mToast);
        }
        mToast.show();
    }

    /**
     * 自定义Toast回调
     */
    public interface ToastCallback {
        void toastCallback(Context context, String content, Toast mToast);
    }

}
