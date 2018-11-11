package com.gloiot.chatsdk.socket;

import android.util.Log;

import com.zyd.wlwsdk.server.network.utlis.JsonUtils;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import io.socket.client.Ack;
import io.socket.client.Socket;

/**
 * Created by hygo00 on 2017/8/18.
 * 消息发送
 */

public class SocketSend {

    public static SocketSend instance;
    private static CallBackListener.SendSingleMessageListener sendSingleMessageListener;
    private Socket mSocket = LinkServer.getSocket();

    public static SocketSend getInstance() {
        if (instance == null) {
            instance = new SocketSend();
        }
        return instance;
    }

    public void setSendSingleMessageListener(CallBackListener.SendSingleMessageListener sendSingleMessageListener) {
        SocketSend.sendSingleMessageListener = sendSingleMessageListener;
    }

    public void sendMsg(final HashMap userMap, String messagecontent, String msgType, final String msgIdFse, String pushData) {
        // 认证失败返回消息发送失败
        if (!SocketListener.getInstance().getIsAuth()) {
            sendFalse((String) userMap.get("receiveId"), msgIdFse);
            return;
        }
        HashMap<String, Object> hashMapInfo = new HashMap<>();
        hashMapInfo.putAll(userMap);
        hashMapInfo.put("message", messagecontent);
        hashMapInfo.put("msgType", msgType);
        hashMapInfo.put("msgIdFse", msgIdFse);
        hashMapInfo.put("pushData", pushData);
        Iterator ite = hashMapInfo.keySet().iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            Log.e("chatsdk-hashMapInfo-", key + "==>" + hashMapInfo.get(key));
        }
//        String msg = userMap.get("random") + EnDecryptUtlis.aesEncrypt(JsonUtils.createJSON(hashMapInfo).toString(), (String) userMap.get("random"));
//        String msg = JsonUtils.createJSON(hashMapInfo).toString();
        send((String) userMap.get("receiveId"), msgIdFse, JsonUtils.createJSONObject(hashMapInfo), new CallBackListener.SendEmitListener() {
            @Override
            public void onStart() {
                Log.e("chatsdk-sendmsg", "onStart");
            }

            @Override
            public void onSuccess(JSONObject result) {
                Log.e("chatsdk-sendmsg", "onSuccess");
                sendSingleMessageListener.sendResult(result.toString());
            }

            @Override
            public void onFailure(String receiveId, String msgIdFse) {
                Log.e("chatsdk-sendmsg", "onFailure");
                sendFalse(receiveId, msgIdFse);
            }
        });
    }

    private void sendFalse(String receiveId, String msgIdFse){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        sendSingleMessageListener.sendResult("{\"msg\":\"请求失败\",\n" +
                "    \"msgId\":\"" + msgIdFse + "\",\n" +
                "    \"receiveId\":\"" + receiveId + "\",\n" +
                "    \"sendTime\":\"" + currentTime + "\",\n" +
                "    \"msgIdFse\":\"" + msgIdFse + "\"}");
    }


    private void send(final String receiveId, final String msgIdFse, final JSONObject msg, final CallBackListener.SendEmitListener emitCall) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000); // 线程暂停10秒后 ack超时返回发送消息失败
                    emitCall.onFailure(receiveId, msgIdFse);
                } catch (InterruptedException e) {
                    Log.e("chatsdk-sendmsg-thread", "收到ack回调结束线程");
                }
            }
        });
        thread.start();
        emitCall.onStart();
        mSocket.emit("singleChat", msg, new Ack() {
            @Override
            public void call(Object... args) {
                thread.interrupt(); // 收到ack回调结束线程
                try {
                    Log.e("chatsdk-sendmsg-ack", args[0].toString());
                    JSONObject argsObject = new JSONObject(args[0].toString());
                    String msg = argsObject.getString("msg");
                    if (msg.equals("发送成功")) {
                        emitCall.onSuccess(argsObject);
                    } else {
                        emitCall.onFailure(receiveId, msgIdFse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitCall.onFailure(receiveId, msgIdFse);
                }
            }
        });
    }

}
