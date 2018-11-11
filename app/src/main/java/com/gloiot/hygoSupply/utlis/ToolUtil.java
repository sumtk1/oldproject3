package com.gloiot.hygoSupply.utlis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;

/**
 *
 * Created by zxl on 2017/5/8.
 */

public class ToolUtil {

    /**
     *
     *
     * isNeedCloseActivity  : true 关闭activity false 不关闭
     *
     */
    public static Dialog showUpdateVersion (final Context context , @NonNull String version_id , @NonNull String content) {
        final Dialog dialog;
        View viewDialog;
        TextView cotent_txt, content_id;

        dialog = new Dialog(context, R.style.dialogshow);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);//取消点击框外部，dialog消失

        viewDialog = LayoutInflater.from(context).inflate(R.layout.version_dialog, null,false);
        content_id = (TextView) viewDialog.findViewById(R.id.version_id);
        cotent_txt = (TextView) viewDialog.findViewById(R.id.version_content);
        viewDialog.findViewById(R.id.version_cancle_txt).setOnClickListener((View.OnClickListener) context);
        viewDialog.findViewById(R.id.version_sure_txt).setOnClickListener((View.OnClickListener) context);
//        viewDialog.findViewById(R.id.version_cancle_txt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        viewDialog.findViewById(R.id.version_sure_txt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        cotent_txt.setText(content);
        content_id.setText("v" + version_id);
        try {
            dialog.setContentView(viewDialog);
            WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
            setParams(wlp, (Activity) context);
            dialog.show();
            ScreenUtil.setDialogMetrics(dialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static void setParams(WindowManager.LayoutParams wlp,Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        wlp.height = dm.heightPixels;
        wlp.width = dm.widthPixels;
        wlp.alpha = wlp.alpha;
    }


    public static void showXiajiaDig (final Context context) {
        final Dialog dialog;
        View viewDialog;

        dialog = new Dialog(context, R.style.dialogshow);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);//取消点击框外部，dialog消失
        viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_xiajia, null,false);
        try {
            dialog.setContentView(viewDialog);
            WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
            setParams(wlp, (Activity) context);
            dialog.show();
            ScreenUtil.setDialogMetrics(dialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
