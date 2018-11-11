package com.gloiot.hygoSupply.ui.activity.message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygoSupply.ui.App;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.FasongShanagPingLianJieActivity;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;

import java.util.List;

/**
 * Created by hygo00 on 17/5/23.
 */

public class ChatEvent implements ChatUiIM.UserInfoProvider, ChatUiIM.ConversationBehaviorListener, SocketEvent.ConnectionStatusListener,
                                  ChatUiIM.SendShangPinListener , ChatUiIM.SimpleApps {

    private static ChatEvent instance;
    private Context context;
    private Context nowContext;
    private ChatUiIM chatUiIM;
    private SharedPreferences preferences;

    public ChatEvent(Context context) {
        this.context = context;
        chatUiIM = ChatUiIM.getInstance();
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        initListener();
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (ChatEvent.class) {
                if (instance == null) {
                    instance = new ChatEvent(context);
                }
            }
        }
    }

    public static ChatEvent getInstance() {
        return instance;
    }


    private void initListener() {
        chatUiIM.setUserInfoProvider(this);
        chatUiIM.setConversationBehaviorListener(this);
        chatUiIM.setSimpleApps(this);
        chatUiIM.setSendShangPinListener(this);
        SocketEvent.getInstance().setConnectionStatusListener(this);
    }


    @Override
    public UserInfo getUserInfo(String userId) {
        if (userId.contains("商家")) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(preferences.getString(ConstantUtils.SP_USERNICHENG, ""));
            userInfo.setId(preferences.getString(ConstantUtils.SP_MYID, "") + "_商家");
            userInfo.setUrl(preferences.getString(ConstantUtils.SP_USERIMG, ""));
            UserInfoCache.getInstance(context).putData(userInfo.getId(), userInfo);//存入缓存
            IMDBManager.getInstance(context, preferences.getString(ConstantUtils.SP_MYID, "")).insertUserInfo(userInfo);//存入数据库
//            IMDBManager.getInstance(context).insertUserInfo(userInfo);//存入数据库
            return userInfo;
        } else {
            UserInfoManager.getInstance(context).getUserInfo(userId);
            return null;
        }

    }

    @Override
    public List<ExtendBean> addApps() {
        return AppsManager.getInstence(context).addApps();
    }

    @Override
    public void appsOnClick(int position) {
        AppsManager.getInstence(context).setAppsOnClick(position);
    }

    @Override
    public boolean onUserPortraitClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        UserInfoManager.getInstance(context).getUserInfo(imMsgBean.getSendid()); // 点击用户头像刷新头像
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public boolean onMessageClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        L.e("-onMessageClick-", "--" + imMsgBean.toString());
        MessageClickListener.MessageClick(context, imMsgBean);
        return false;
    }

    @Override
    public boolean onMessageLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder) {
        return false;
    }

    @Override
    public void onChanged(String result) {
        switch (result) {
            case Constant.RENZHENG_FAILURE:
                // 认证失败
//                t("聊天链接失败");
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_FAULT);
                break;
            case Constant.RENZHENG_RANDOM_ERROR:
                // 当被挤下线后，立即关闭socket
                SocketListener.getInstance().signoutRenZheng();
                // 随机码不正确 其他设备登录
                nowContext = App.getInstance().mActivityStack.getLastActivity();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        DialogUtlis.oneBtnNormal(nowContext, "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(ConstantUtils.SP_LOGINTYPE, "未登录");
                                editor.putString(ConstantUtils.SP_RANDOMCODE, "");
                                editor.commit();
                                Intent intent = new Intent(nowContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                IMDBManager.getInstance(nowContext).ClearnData();
                                nowContext.startActivity(intent);

                            }
                        });
                        Looper.loop();
                    }
                }).start();
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_FAULT);
                break;
            case Constant.RENZHENG_SUCCESS:
                // 认证成功
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_SUCCEED);
                break;
            default:
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.LINK_CHANGED, MessageManager.LINK_CHANGED_FAULT);
                t(result + "");
        }
    }

    public void t(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                MToast.showToast(s);
                Looper.loop();
            }
        }).start();
    }

    /**
     * 发送商品信息，当订单中包含多件商品是，触发此方法
     * @param context 消息产生的页面
     * @param bundle  请求订单数据的条件
     */
    @Override
    public void sendShangPin(Context context, Bundle bundle) {
        Intent intent = new Intent(new Intent(context, FasongShanagPingLianJieActivity.class));
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }
}
