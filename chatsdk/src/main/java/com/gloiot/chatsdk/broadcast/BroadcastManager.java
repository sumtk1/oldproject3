package com.gloiot.chatsdk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.gloiot.chatsdk.bean.ImMsgBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hygo00 on 17/5/19.
 * 广播管理类
 */

public class BroadcastManager {

    private Context mContext;
    private static BroadcastManager instance;
    private Map<String, BroadcastReceiver> receiverMap;

    private BroadcastManager(Context context) {
        this.mContext = context.getApplicationContext();
        receiverMap = new HashMap<String, BroadcastReceiver>();
    }

    public static BroadcastManager getInstance(Context context) {
        if (instance == null) {
            instance = new BroadcastManager(context);
        }
        return instance;
    }

    /**
     * 添加广播
     *
     * @param
     */
    public void addAction(String action, BroadcastReceiver receiver) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            mContext.registerReceiver(receiver, filter);
            receiverMap.put(action, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送参数为 String 的数据广播
     *
     * @param action
     * @param s
     */
    public void sendBroadcast(String action, String s) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", s);
        mContext.sendBroadcast(intent);
    }

    /**
     * 发送参数为 boolean 的数据广播
     *
     * @param action
     * @param b
     */
    public void sendBroadcast(String action, boolean b) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", b);
        mContext.sendBroadcast(intent);
    }

    /**
     * 发送参数为 ImMsgBean 的数据广播
     *
     * @param action
     * @param imMsgBean
     */
    public void sendBroadcast(String action, ImMsgBean imMsgBean) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", imMsgBean);
        mContext.sendBroadcast(intent);
    }


    /**
     * 销毁广播
     *
     * @param action
     */
    public void destroy(String action) {
        if (receiverMap != null) {
            BroadcastReceiver receiver = receiverMap.remove(action);
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
        }
    }


    private BroadcastCallback broadcastCallback;

    public BroadcastCallback getBroadcastCallback() {
        return broadcastCallback;
    }

    public void setBroadcastCallback(BroadcastCallback broadcastCallback) {
        this.broadcastCallback = broadcastCallback;
    }

    /**
     * 发送参数为 ImMsgBean 的数据广播
     *
     * @param action
     * @param imMsgBean
     * @param broadcastCallback 发送广播成功，回调
     */
    public void sendBroadcast(String action, ImMsgBean imMsgBean, BroadcastCallback broadcastCallback) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("data", imMsgBean);
        mContext.sendBroadcast(intent);
        setBroadcastCallback(broadcastCallback);
    }

    public interface BroadcastCallback {
        void callback(String msgId);
    }
}
