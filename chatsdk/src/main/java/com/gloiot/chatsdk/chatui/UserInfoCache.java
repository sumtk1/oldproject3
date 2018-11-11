package com.gloiot.chatsdk.chatui;

import android.content.Context;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.ui.adapter.ChattingListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：Ljy on 2017/5/26.
 * 功能：我的——我的资料
 */


public class UserInfoCache {
    public static Map<String, UserInfo> userInfoMap;
    private Context context;
    private static UserInfoCache instance;

    private UserInfoCache(Context context) {
        this.context = context;
        userInfoMap = new LinkedHashMap<>();
    }

    public static UserInfoCache getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfoCache(context);
        }
        return instance;
    }

    public UserInfo getUserInfo(String userId, String sendOutid) {
        UserInfo userInfo = userInfoMap.get(userId);
        if (null != userInfo) {
            return userInfo;
        }
        userInfo = IMDBManager.getInstance(context, sendOutid).queryUserInfo(userId);
        if (null != userInfo) {
            userInfoMap.put(userId, userInfo);
            return userInfo;
        }
        return null;
    }

    public void putData(String userId, UserInfo userInfo) {
        userInfoMap.put(userId, userInfo);
    }

    public void upData(String userId, UserInfo userInfo, String sendOutid) {
        userInfoMap.put(userId, userInfo);
        IMDBManager.getInstance(context, sendOutid).insertUserInfo(userInfo);//存入数据库
    }
}
