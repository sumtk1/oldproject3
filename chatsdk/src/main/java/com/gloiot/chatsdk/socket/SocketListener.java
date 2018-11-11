package com.gloiot.chatsdk.socket;

import android.text.TextUtils;
import android.util.Log;

import com.gloiot.chatsdk.utlis.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 创建者 zengming.
 * 功能：Socket事件监听器
 */
public class SocketListener {

    private Socket mSocket = LinkServer.getSocket();
    public static SocketListener instance;
    private String userid, random;
    private static CallBackListener.MessageListener messageListener;
    private static CallBackListener.RenzhengResultListener renzhengResultListener;
    // 定时发送socket心跳，taskInt记录没有心跳的次数，3次后重新连接socket
    private Timer timer = null;
    private TimerTask task = null;
    // 认证结果  用于发送消息（true可发送 false不可发送）
    private boolean isAuth = false;

    public boolean getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    private SocketListener() {
    }

    /**
     * 初始化
     */
    public static SocketListener getInstance() {
        if (instance == null) {
            instance = new SocketListener();
        }
        return instance;
    }

    /**
     * 链接服务器成功的监听
     */
    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 监听服务器主动发送的下线消息
            SocketServer.socketOn("connect_status", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("connect_status-message", "---1---");
                    try {
                        Log.e("connect_status", args[0] + "");
                        JSONObject jsonObject = new JSONObject((String) args[0]);
                        String code = jsonObject.getString("code");

                        if (code.equals("-1")){ // 被挤下线
                            renzhengResultListener.renzhengResult(Constant.RENZHENG_RANDOM_ERROR);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            SocketServer.socketOn("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("chatsdk-message", "message_ack");
                    Log.e("chatsdk-message", args[0] + "");
                    messageListener.messageContent(args[0].toString());

                    try {
                        Ack ack = (Ack) args[args.length - 1];
                        String uuid = getUUID();
                        ack.call(uuid);
                    } catch (Exception e){

                    }
                }
            });
            Log.e("chatsdk-onConnect", "onConnect");
            setIsAuth(false);   // 链接socket成功设置认证状态
            // 打开心跳
            heartbeatSocket();
        }
    };

    /**
     * 链接服务器错误的监听
     */
    public Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            SocketServer.socketOff("message");
            Log.e("chatsdk-Error", "onConnectError------" + args.length + "-------" + args[0]);
            renzhengResultListener.renzhengResult(Constant.RENZHENG_FAILURE); // 返回认证失败
            setIsAuth(false);   // 设置认证失败 用于发送消息（不可发送）
        }
    };

    /**
     * app重开后，将持久化存储的账号和随机码传入
     *
     * @param userid 认证账号
     * @param random 认证随机码
     */
    public void staredData(final String userid, final String random) {
        this.userid = userid;
        this.random = random;
    }

    /**
     * 连接后进行认证，认证后可以接收指定消息
     *
     * @param userid 认证账号
     * @param random 认证随机码
     */
    public void connectionRenZheng(final String userid, final String random) {
        this.userid = userid;
        this.random = random;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userid);
            jsonObject.put("random", random);
            Log.e("chatsdk-auth-jsonObject", jsonObject.toString());
            auth(jsonObject + "", new CallBackListener.AuthListener() {
                @Override
                public void onSuccess(JSONObject result) {
                    setIsAuth(true);
                    renzhengResultListener.renzhengResult(Constant.RENZHENG_SUCCESS);
                }

                @Override
                public void onFailure(JSONObject result) {
                    renzhengResultListener.renzhengResult(Constant.RENZHENG_FAILURE);
                    setIsAuth(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("onConnect1", "JSONException");
            renzhengResultListener.renzhengResult(Constant.RENZHENG_FAILURE);
            setIsAuth(false);
        }
    }

    private void auth(String authConten, final CallBackListener.AuthListener emitCall) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000); // 线程暂停10秒后 ack超时返回发送消息失败
                    emitCall.onFailure(null);
                } catch (InterruptedException e) {
                    Log.e("chatsdk-auth-thread", "收到ack回调结束线程");
                }
            }
        });
        thread.start();
        mSocket.emit("auth", authConten, new Ack() {
            @Override
            public void call(Object... args) {
                thread.interrupt(); // 收到ack回调结束线程
                try {
                    Log.e("chatsdk-auth-Ack", "args.length----" + args.length);
                    Log.e("chatsdk-auth-Ack", args[0] + "");
                    JSONObject argsObject = new JSONObject(args[0].toString());
                    String code = argsObject.getString("code");
                    if (code.equals("1")) {
                        emitCall.onSuccess(argsObject);
                    } else {
                        emitCall.onFailure(argsObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    emitCall.onFailure(null);
                    Log.e("chatsdk-auth-Ack", "" + e);
                }
            }
        });
    }

    /**
     * 退出认证，并清除进程缓存的账号信息
     */
    public void signoutRenZheng() {
        this.userid = "";
        this.random = "";
        stopTimer();
        setIsAuth(false);   // 设置认证失败 用于发送消息（不可发送）
        SocketServer.disReconnection();
    }

    /**
     * 心跳socket连接，定时器的创建和使用
     */
    public void heartbeatSocket() {
        stopTimer();
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    // 打印socket是否在连接中
                    Log.e("chatsdk---task", SocketServer.connected() + ", socketid:" + SocketServer.mSocket.id());

                    // socket链接成功且认证成功 才向服务器发心跳包
                    if (getIsAuth() && SocketServer.connected()) {
                        // 发送心跳时间戳
                        SocketServer.socketHeartbeat(new Ack() {
                            @Override
                            public void call(Object... args) {
                                // 打印时间戳
                                try {
                                    Log.e("chatsdk---task", args[0] + "");
                                    JSONObject jsonObject = (JSONObject) args[0];
                                    String code = jsonObject.getString("code");

                                    if (code.equals("-1") || code.equals("-2")){
                                        setIsAuth(false);
                                        renzhengResultListener.renzhengResult(Constant.RENZHENG_FAILURE);
                                        // socket链接成功且认证失败 才去认证
                                        if (!getIsAuth() && SocketServer.connected()) {
                                            if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(random)) {
                                                Log.e("chatsdk---task", "重新认证");
                                                connectionRenZheng(userid, random);
                                            }
                                        }
                                    } else if (code.equals("-3")) {
                                        renzhengResultListener.renzhengResult(Constant.RENZHENG_RANDOM_ERROR);
                                        signoutRenZheng();
                                    } else if (code.equals("1")) {
                                        renzhengResultListener.renzhengResult(Constant.RENZHENG_SUCCESS);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    // socket链接成功且认证失败 才去认证
                    if (!getIsAuth() && SocketServer.connected()) {
                        if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(random)) {
                            Log.e("chatsdk---task", "重新认证");
                            connectionRenZheng(userid, random);
                        }
                    }
                }
            };
        }
        if (timer != null && task != null)
            timer.schedule(task, 0, 30000); // 打开定时器，30s执行一次
    }

    /**
     * 销毁定时器
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel(); // 关闭定时器
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }

    }

    /**
     * 随机生成一个不重复的uuid，用于接收消息后返回给后台
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str;
    }

    public static void setMessageListener(CallBackListener.MessageListener messageListener) {
        SocketListener.messageListener = messageListener;
    }

    public static void setRenzhengResultListener(CallBackListener.RenzhengResultListener renzhengResultListener) {
        SocketListener.renzhengResultListener = renzhengResultListener;
    }

}
