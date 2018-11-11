package com.gloiot.hygoSupply.utlis;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.gloiot.hygoSupply.ui.App;


/**
 * Created by hygo00 on 2016/7/7.
 */
public class MToastUtils {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null;//toast隐藏后，将其置为null
        }
    };
    private static String toastContet = "";

    /**
     * 显示Toast
     *
     * @param context
     * @param content
     */
    public static void showToast(final Context context, final String content) {
        if (toastContet.equals(content)) {//判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) {//mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 显示Toast,用App的context
     *
     * @param content
     */
    public static void showToast(final String content) {
        if (toastContet.equals(content)) {//判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(App.mContext, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) {//mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(App.mContext, content, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 显示Toast,用App的context
     *
     * @param content
     */
    public static void showToast(final String content,int graviry, int height) {
        if (toastContet.equals(content)) {//判断内容是否跟上一个显示的相同
            mHandler.removeCallbacks(r);
            if (mToast == null) {//只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = Toast.makeText(App.mContext, content, Toast.LENGTH_SHORT);
            }
        } else {
            toastContet = content;
            if (mToast != null) {//mToast!=null时 清除当前toast
                mToast.cancel();
            }
            mToast = Toast.makeText(App.mContext, content, Toast.LENGTH_SHORT);
        }
        mToast.setGravity(graviry,0,height);
        mToast.show();
    }

}
