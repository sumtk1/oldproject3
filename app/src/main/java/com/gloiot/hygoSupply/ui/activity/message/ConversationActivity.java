package com.gloiot.hygoSupply.ui.activity.message;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.fragment.ConversationFragment;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.L;


public class ConversationActivity extends BaseActivity {

    private UserInfo userInfo = null; // 传过来的用户信息
    private ConversationFragment conversationFragment;  // 聊天Fragment
    private boolean requestPermissionFalg = false; // 记录语音权限请求结果

    @Override
    public int initResource() {
        return R.layout.activity_conversation;
    }

    @Override
    public void initData() {
        requestPermission();
        String receiveId = getIntent().getExtras().getString("receiveId", "");
        String name = getIntent().getExtras().getString("name", "");
        CommonUtils.setTitleBar(this, name);

        userInfo = UserInfoCache.getInstance(mContext).getUserInfo(receiveId, preferences.getString(ConstantUtils.SP_MYID, ""));
//        userInfo = UserInfoCache.getInstance(mContext).getUserInfo(receiveId);
        if (userInfo != null) {
            CommonUtils.setTitleBar(this, TextUtils.isEmpty(userInfo.getName()) ? receiveId : userInfo.getName());
        }

        Bundle bundle = new Bundle();
        bundle.putString("receiveid", receiveId);
        bundle.putString("userid", preferences.getString(ConstantUtils.SP_MYID, ""));
        bundle.putString("random", preferences.getString(ConstantUtils.SP_RANDOMCODE, ""));
        bundle.putString("nick", preferences.getString(ConstantUtils.SP_USERNICHENG, ""));

        bundle.putBundle("data", getIntent().getExtras().getBundle("data"));

        L.e("12321", "" + getIntent().getExtras().getBundle("data"));

        FragmentTransaction trans1 = getFragmentManager().beginTransaction();
        conversationFragment = new ConversationFragment();
        conversationFragment.setArguments(bundle);
        trans1.replace(R.id.container, conversationFragment);
        trans1.commitAllowingStateLoss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        conversationFragment.onActivityResult(requestCode, resultCode, data); // 返回的数据传给Fragment
    }

    /**
     * 请求语音,SD卡权限，点击发送语音的按钮时触发
     * @return   权限请求成功时返回true
     */
    public boolean requestPermission() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                requestPermissionFalg = true;
            }
        }, R.string.perm_contacts, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
//        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        return requestPermissionFalg;
    }
}
