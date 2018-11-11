package com.gloiot.chatsdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hygo00 on 17/2/27.
 * 语音消息
 */

public class ImMsgRecoderBean extends ImMsgBean {

    // 语音消息时长
    private String recoderTime;
    // 语音消息数据
    private String recoderData;
    // 语音消息展示长度
    private int recoderLenght;
    // 语音消息播放时的动画是否播放
    private boolean recoderAnim;


    public String getRecoderTime() {
        return recoderTime;
    }

    public void setRecoderTime(String recoderTime) {
        this.recoderTime = recoderTime;
    }

    public String getRecoderData() {
        return recoderData;
    }

    public void setRecoderData(String recoderData) {
        this.recoderData = recoderData;
    }

    public int getRecoderLenght() {
        return recoderLenght;
    }

    public void setRecoderLenght(int recoderLenght) {
        this.recoderLenght = recoderLenght;
    }

    public boolean isRecoderAnim() {
        return recoderAnim;
    }

    public void setRecoderAnim(boolean recoderAnim) {
        this.recoderAnim = recoderAnim;
    }

    public ImMsgRecoderBean typeCast(ImMsgBean imMsgBean) {
        ImMsgRecoderBean imMsgRecoderBean = new ImMsgRecoderBean();
        imMsgRecoderBean.setId(imMsgBean.getId());
        imMsgRecoderBean.setMsgid(imMsgBean.getMsgid());
        imMsgRecoderBean.setSendid(imMsgBean.getSendid());
        imMsgRecoderBean.setReceiveid(imMsgBean.getReceiveid());
        imMsgRecoderBean.setSessiontype(imMsgBean.getSessiontype());
        imMsgRecoderBean.setMsgtype(imMsgBean.getMsgtype());
        imMsgRecoderBean.setMessage(imMsgBean.getMessage());
        imMsgRecoderBean.setPushdata(imMsgBean.getPushdata());
        imMsgRecoderBean.setSendTime(imMsgBean.getSendTime());
        imMsgRecoderBean.setMessageState(imMsgBean.getMessageState());
        imMsgRecoderBean.setExtra(imMsgBean.getExtra());

        try {
            JSONObject message = new JSONObject(imMsgBean.getMessage());
            imMsgRecoderBean.setRecoderTime(message.getString("recoderTime"));
            imMsgRecoderBean.setRecoderData(message.getString("recoderData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imMsgRecoderBean;
    }
}
