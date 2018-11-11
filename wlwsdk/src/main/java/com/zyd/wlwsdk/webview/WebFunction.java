package com.zyd.wlwsdk.webview;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;


import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hygo00 on 2017/9/8.
 * web操作
 */

public class WebFunction {

    private Context context;

    public WebFunction(Context context) {
        this.context = context;
    }

    /**
     * 通讯录状态返回给web
     *
     * @param data
     */
    public void contacts(final Intent data, final ContactsCallBack contactsCallBack) {
        try {
            ContentResolver reContentResolverol = context.getContentResolver();
            if (data == null) {
                return;
            }
            Uri contactData = data.getData();
            Cursor cursor = ((Activity) context).managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();// 获得DATA表中的名字
            final String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));  // 条件为联系人ID
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);  // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            ArrayList<String> phonelist = new ArrayList();
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phonelist.add(usernumber);
            }
            final String[] phones = new String[phonelist.size()];
            for (int i = 0; i < phonelist.size(); i++) {
                phones[i] = phonelist.get(i);
            }
            if (phonelist.size() == 1) {
                contactsCallBack.callback("{\"状态\":\"成功\",\"姓名\":\"" + username + "\",\"电话\":\"" + phones[0] + "\"}");
                L.e("WebManage_usernumber", "手机号" + phones[0]);
            } else {
                new AlertDialog.Builder(context).setTitle("请选择手机号").setSingleChoiceItems(phones, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactsCallBack.callback("{\"状态\":\"成功\",\"姓名\":\"" + username + "\",\"电话\":\"" + phones[which] + "\"}");
                        Log.e("WebManage_usernumber", "手机号" + phones[which]);
                        dialog.dismiss();
                    }
                }).show();
            }
        } catch (Exception e) {
            MToast.showToast("请获取通讯录权限");
        }
    }

    public interface ContactsCallBack {
        void callback(String content);
    }


    /**
     * 倒计时时间 内部类
     */
    public static class MyTimerTask extends TimerTask {

        private int time;
        private Timer timer;
        private TextView tvTime;
        private Context context;
        private WebView webView;

        public MyTimerTask(Context context, WebView webView, TextView tvTime, int time) {
            this.context = context;
            this.webView = webView;
            this.tvTime = tvTime;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time--;
                        tvTime.setText("剩余支付时间：" + formatTime(time * 1000));
                        if (time < 240) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            tvTime.setVisibility(View.GONE);
                            webView.loadUrl("javascript:closeWin()");
                        }
                    }
                });
            } catch (Exception e){

            }
        }

        public void startSchedule() {
            timer = new Timer();
            timer.schedule(this, 0, 1000);    // timeTask
            tvTime.setVisibility(View.VISIBLE);
        }

        public void finishWeb() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            tvTime.setVisibility(View.GONE);
        }

        /**
         * 毫秒转时分秒
         *
         * @param ms
         * @return
         */
        public static String formatTime(long ms) {
            int ss = 1000;
            int mi = ss * 60;
            int hh = mi * 60;
            int dd = hh * 24;
            long day = ms / dd;
            long hour = (ms - day * dd) / hh;
            long minute = (ms - day * dd - hour * hh) / mi;
            long second = (ms - day * dd - hour * hh - minute * mi) / ss;
            String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
            String strSecond = second < 10 ? "0" + second : "" + second;//秒
            return strMinute + " : " + strSecond;
        }
    }

}
