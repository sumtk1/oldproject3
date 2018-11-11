package com.zyd.wlwsdk.widge;

/**
 * Created by hygo00 on 16/10/28.
 * 加载框
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.loopj.android.http.RequestHandle;
import com.zyd.wlwsdk.R;

import java.util.ArrayList;


public class LoadDialog extends Dialog {


    private static final String TAG = "LoadDialog";
    private static ArrayList<RequestHandle> requestHandle;
    private static LoadDialog loadDialog;
    private boolean cancelable;


    public LoadDialog(final Context ctx, boolean cancelable) {
        super(ctx);
        this.cancelable = cancelable;
        this.getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.dialog_loading);
        // 必须放在加载布局后
        setparams();
    }

    private void setparams() {
        this.setCancelable(cancelable);
        this.setCanceledOnTouchOutside(false);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        // Dialog宽度
        lp.width = (int) (display.getWidth() * 0.7);
        Window window = getWindow();
        window.setAttributes(lp);
        try {
            window.getDecorView().getBackground().setAlpha(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown: 0");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelable) {

                return true;
            }
            // 取消网络请求
            if (requestHandle != null) {
                Log.e(TAG, "onKeyDown: " + requestHandle.size());
                for (int i = 0; i < requestHandle.size(); i++) {
                    requestHandle.get(i).cancel(true);
                }
//                requestHandle.get(requestHandle.size()-1).cancel(true);
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    // 带网络请求
    public static void show(Context context, ArrayList<RequestHandle> requestHandleArrayList) {
        requestHandle = requestHandleArrayList;
        show(context, null, true);
    }


    public static void show(Context context) {
        show(context, null, true);
    }

    public static void show(Context context, String message) {
        show(context, message, true);
    }

    public static void show(Context context, int resourceId) {
        show(context, context.getResources().getString(resourceId), true);
    }

    private static void show(Context context, String message, boolean cancelable) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new LoadDialog(context, cancelable);
        loadDialog.show();
    }

    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    loadDialog = null;
                    return;
                }
            }

            if (loadDialog != null && loadDialog.isShowing()) {
                Context loadContext = loadDialog.getContext();
                if (loadContext != null && loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing()) {
                        loadDialog = null;
                        return;
                    }
                }
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog = null;
        }
    }
}