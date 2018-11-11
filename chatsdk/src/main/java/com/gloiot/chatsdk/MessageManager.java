package com.gloiot.chatsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.DataBase.MessageChatCallBack;
import com.gloiot.chatsdk.DataBase.Widget.DataChange;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.utlis.ChatNotification;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;
import com.zyd.wlwsdk.utlis.ImageCompress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by hygo00 on 17/5/18.
 * 消息管理
 */

public class MessageManager {

    private static MessageManager instance;
    private Context context;

    // 用于广播发送标志
    public static final String NEW_MESSAGE = "GLOIOT_NEW_MESSAGE_SUPPLY";              // 收到新消息
    public static final String REFRESH_USERINFO = "GLOIOT_REFRESH_USERINFO_SUPPLY";    // 用户信息刷新
    public static final String LINK_CHANGED = "LINK_CHANGED_SUPPLY";                   // 链接状态改变
    // 链接状态
    public static final String LINK_CHANGED_SUCCEED = "LINK_CHANGED_SUCCEED_SUPPLY";   // 链接成功
    public static final String LINK_CHANGED_FAULT = "LINK_CHANGED_FAULT_SUPPLY";       // 链接失败
    public static final String LINK_CHANGED_DONW = "LINK_CHANGED_DONW_SUPPLY";         // 用户强制下线

    public MessageManager(Context context) {
        this.context = context;
    }

    public static MessageManager getInstance(Context context) {
        if (instance == null) {
            instance = new MessageManager(context);
        }
        return instance;
    }

    private JSONObject jsonObject;

    public synchronized void setMessage(final String data) {
        File file = null;
        try {
            jsonObject = new JSONObject(data);
            final JSONObject messObj = new JSONObject(jsonObject.getString("message"));

            if ("图片".equals(jsonObject.getString("msgType")) && messObj.getString("img").length() > 20000) {
                file = ImageCompress.base64ToFile(messObj.getString("img"), context.getCacheDir() + "/imageCache.text");
            }

            if (file != null && file.length() > 20000) {

                long fileSize = file.length();
                double scale = Math.sqrt((float) fileSize / 20240);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                int height = options.outHeight;
                int width = options.outWidth;

                final Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                compressOptions.width = (int) (width / scale);
                compressOptions.height = (int) (height / scale);
                compressOptions.size = 20;
                compressOptions.config = Bitmap.Config.ARGB_8888;

                Tiny.getInstance().source(file).asFile().withOptions(compressOptions).compress(new FileWithBitmapCallback() {
                    @Override
                    public void callback(boolean isSuccess, final Bitmap bitmap, final String outfile) {
                        if (!isSuccess) {
                            return;
                        }

                        try {
                            JSONObject message = new JSONObject();
                            message.put("img", ImageCompress.fileToBase64(new File(outfile)));
                            message.put("url", messObj.getString("url"));
                            message.put("extra", messObj.getString("extra"));

                            jsonObject.remove("message");
                            jsonObject.put("message", message.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        IMDBManager.getInstance(context).insertChatMsg((new DataChange()).JsonToSystemBean(jsonObject), 0, new MessageChatCallBack() {
                            @Override
                            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                                if (imMsgBean.getMsgtype().contains("图片")) {
                                    imMsgBean.setMessage(""); // 防止图片字节过大导致发送广播异常，接收广播处处理（android.app.RemoteServiceException: can't deliver broadcast）
                                }
                                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, imMsgBean);
                                sendNotification(imMsgBean);
                            }

                            @Override
                            public void DoChatDBFault() {

                            }
                        });
                    }
                });
            } else {
                IMDBManager.getInstance(context).insertChatMsg((new DataChange()).JsonToSystemBean(jsonObject), 0, new MessageChatCallBack() {
                    @Override
                    public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                        if (imMsgBean.getMsgtype().contains("图片")) {
                            imMsgBean.setMessage(""); // 防止图片字节过大导致发送广播异常，接收广播处处理（android.app.RemoteServiceException: can't deliver broadcast）
                        }
                        BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, imMsgBean);
                        sendNotification(imMsgBean);
                    }

                    @Override
                    public void DoChatDBFault() {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送通知栏通知
     * @param imMsgBean
     */
    private void sendNotification(ImMsgBean imMsgBean){
        if(Long.parseLong(getTime())-Long.parseLong(dataOne(imMsgBean.getSendTime())) <= 100){
            ChatNotification.getInstance().createNotification(imMsgBean.getPushdata());
            ChatNotification.getInstance().sendNotify(imMsgBean.getSendid());
        }
    }

    /**
     * 时间转换成时间戳
     * @param time
     * @return
     */
    private String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        return String.valueOf(time);
    }
}
