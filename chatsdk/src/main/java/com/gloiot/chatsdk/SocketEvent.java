package com.gloiot.chatsdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.socket.CallBackListener;
import com.gloiot.chatsdk.socket.SocketListener;

/**
 * 创建者 zengming.
 * 功能：socket事件监听
 */
public class SocketEvent implements CallBackListener.MessageListener, CallBackListener.RenzhengResultListener {

    private Context mContext;
    private static SocketEvent mSocketInstance;

    private SocketEvent(Context context) {
        mContext = context;

        // 设置监听器
        setListener();
    }

    /**
     * 初始化 socket.
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mSocketInstance == null) {
            synchronized (SocketEvent.class) {
                if (mSocketInstance == null) {
                    mSocketInstance = new SocketEvent(context);
                }
            }
        }
    }

    /**
     * SocketEvent 实例。
     */
    public static SocketEvent getInstance() {
        return mSocketInstance;
    }

    private void setListener() {
        SocketListener.setRenzhengResultListener(this); // 设置认证结果监听器。
        SocketListener.setMessageListener(this); // 设置消息接收监听器。
    }

    /**
     * 认证结果
     * @param result
     */
    @Override
    public void renzhengResult(String result) {
        Log.e("---renzhengResult--", "result: " + result);
        connectionStatusListener.onChanged(result);
    }

    /**
     * 监听接收的消息内容
     * @param data
     */
    @Override
    public void messageContent(final String data) {
        Log.e("---messageContent--", "onSuccess: " + data);
        MessageManager.getInstance(mContext).setMessage(data);
    }


    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }

    ConnectionStatusListener connectionStatusListener;
    public interface ConnectionStatusListener {
        void onChanged(String result);
    }

}
