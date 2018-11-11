package com.gloiot.chatsdk.utlis;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.gloiot.chatsdk.R;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by hygo00 on 2017/8/4.
 * 消息通知栏
 */

public class ChatNotification {

    private static ChatNotification instance;
    private Context context;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private String userId;
    private Intent intent = null;
    private PendingIntent pendingIntent;

    private ChatNotification(Context context){
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new ChatNotification(context);
        }
    }

    public static ChatNotification getInstance() {
        return instance;
    }

    /**
     * 设置intent跳转
     * @param intent
     */
    public void setIntent(Intent intent){
        this.intent = intent;
    }

    /**
     * 设置当前界面用户id
     * @param userId
     */
    public void setCurrentId(String userId){
        this.userId = userId;
    }

    public void createNotification(String text) {
        if (intent!=null){
            pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
        }
        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("你有新消息")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // 系统声音
                .setContentIntent(pendingIntent);
    }

    public void sendNotify(String receiveId) {
        if (!receiveId.equals(userId)){
            mNotificationManager.notify(1, builder.build());
        }
    }

    public void cancelNotify(){
        mNotificationManager.cancel(1);
    }

}
