package com.gloiot.hygoSupply.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.keyboard.XhsEmoticonsKeyBoard;
import com.gloiot.chatsdk.imagepicker.ImagePicker;
import com.gloiot.chatsdk.imagepicker.bean.ImageItem;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.zyd.wlwsdk.utlis.MToast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

import static com.gloiot.chatsdk.chatui.ChatUiIM.REQUEST_CODE_CAMERA;
import static com.gloiot.chatsdk.chatui.ChatUiIM.REQUEST_CODE_PIC;

/**
 * Created by hygo00 on 17/5/23.
 */

public class ConversationFragment extends Fragment implements XhsEmoticonsKeyBoard.RequestVoicePermission {

    private XhsEmoticonsKeyBoard ekBar; // 表情输入键盘
    private ChatUiIM chatUiIM;
    private ArrayList<ImageItem> images = null; // 选择的图片集合
    private Context context;
    private String receiveId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_simple_chat_translucent, container, false);
        ButterKnife.bind(this, view);

        HashMap usreMap = new HashMap();
        receiveId = getArguments().getString("receiveid");
        usreMap.put("receiveId", getArguments().getString("receiveid"));
        usreMap.put("sendId", getArguments().getString("userid") + "_商家");
        usreMap.put("random", getArguments().getString("random"));
        usreMap.put("sendName", getArguments().getString("nick"));

        chatUiIM = ChatUiIM.getInstance();
        chatUiIM.setView(view, getActivity(), usreMap, getArguments().getBundle("data"));
        ekBar = chatUiIM.getEkBar();
        ekBar.setRequestVoicePermission(this);

        if (getArguments().getString("receiveid").contains("service")){
            ekBar.setVoice(false);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清除未读数
        try {
            context = getActivity();
            IMDBManager.getInstance(context).NoReadNumClean(receiveId, new DataBaseCallBack() {
                @Override
                public void operationState(boolean flag) {

                }
            });
        } catch (Exception e) {

        }

        chatUiIM.destroyBroadcast();
    }

    @Override
    public void onPause() {
        super.onPause();
        chatUiIM.cleanNoReadNum();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_PIC) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                chatUiIM.sendImage(images);
            } else if (data != null && requestCode == REQUEST_CODE_CAMERA) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                chatUiIM.sendImage(images);
            } else {
                MToast.showToast("没有数据");
            }
        }
    }

    @Override
    public boolean voiceResult() {
        return ((ConversationActivity)getActivity()).requestPermission();
    }
}
