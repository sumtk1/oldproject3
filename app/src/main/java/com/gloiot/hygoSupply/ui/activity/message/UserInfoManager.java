package com.gloiot.hygoSupply.ui.activity.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygoSupply.serrver.network.RequestAction;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;
import com.loopj.android.http.RequestHandle;
import com.zyd.wlwsdk.server.network.OnDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 作者：Ljy on 2017/5/27.
 */


public class UserInfoManager implements OnDataListener {

    protected ArrayList<RequestHandle> requestHandleArrayList;
    protected RequestAction requestAction;
    private static UserInfoManager instance;
    private Context context;
    private String phone;
    private SharedPreferences preferences;

    private UserInfoManager(Context context) {
        requestHandleArrayList = new ArrayList<>();
        requestAction = new RequestAction();
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        phone = preferences.getString(ConstantUtils.SP_ZHANGHAO, "");
        this.context = context;
    }

    public static UserInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfoManager(context);
        }
        return instance;
    }

    //联网获取用户信息
    public void getUserInfo(String userId) {
        requestHandleArrayList.add(requestAction.GetUserInfo(this, userId, "商家", phone));
    }


    @Override
    public void onStart(int requestTag, int showLoad) {

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        Log.e("TAG", "onSuccess: 获取用户信息成功" + response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_shop_hp_Info:
                try {
                    if ("成功".equals(response.getString("状态"))) {
                        JSONArray array = response.getJSONArray("userInfo");
                        if (array.length() == 0) {
                            return;
                        }
                        UserInfo info = new UserInfo();
                        JSONObject object = (JSONObject) array.get(0);
                        info.setName(object.getString("name"));
                        info.setId(object.getString("id"));
                        info.setUrl(object.getString("icon"));
                        UserInfoCache.getInstance(context).putData(object.getString("id"), info);//存入缓存
                        // TODO: 2017/5/26   可以考虑新开线程存储数据，或者用线程池，如果阻塞时间过长的话。
//                        IMDBManager.getInstance(context).insertUserInfo(info);//存入数据库
                        IMDBManager.getInstance(context, SharedPreferencesUtils.getInstance().getSharedPreferences().getString(ConstantUtils.SP_MYID, "")).insertUserInfo(info);//存入数据库
                        BroadcastManager.getInstance(context).sendBroadcast(MessageManager.REFRESH_USERINFO, response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        try {
            Log.e("TAG", "onFailure: 获取用户信息失败" + errorResponse.toString());
        } catch (Exception e) {

        }
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {

    }
}
