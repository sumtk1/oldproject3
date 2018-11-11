package com.gloiot.chatsdk.chatui.keyboard.recorder;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.chatsdk.R;


/**
 * Created by hygo00 on 17/2/23.
 * 发送语音Dialog
 */

public class DialogManager {

    private Dialog mDialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;

    private Context mContext;

    /**
     * 构造方法 传入上下文
     */
    public DialogManager(Context context) {
        this.mContext = context;
    }

    // 显示录音的对话框
    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder,null);
        mDialog.setContentView(view);

        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
        mLable = (TextView) view.findViewById(R.id.id_recorder_dialog_label);

        mDialog.show();
    }

    // 显示正在录音时
    public void recording(){
        if(mDialog != null && mDialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.recorder);
            mLable.setText("手指上滑，取消发送");
            mLable.setBackgroundResource(0);
        }
    }

    // 显示想取消
    public void wantToCancel() {
        if(mDialog != null && mDialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.cancel);
            mLable.setText("松开手指，取消发送");
            mLable.setBackgroundResource(R.drawable.dialog_recorder_cancel_bg);
        }
    }


    // 显示时间过短
    public void tooShort() {
        if(mDialog != null && mDialog.isShowing()){ //显示状态
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.mipmap.voice_to_short);
            mLable.setText("录音时间过短");
            mLable.setBackgroundResource(0);
        }
    }

    // 取消对话框
    public void dimissDialog() {

        if(mDialog != null && mDialog.isShowing()){ //显示状态
            mDialog.dismiss();
            mDialog = null;
        }
    }

    // 显示更新音量级别
    public void updateVoiceLevel(int level) {
        if(mDialog != null && mDialog.isShowing()){ //显示状态

            //设置图片的id
            int resId = mContext.getResources().getIdentifier("v"+level, "mipmap", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
}
