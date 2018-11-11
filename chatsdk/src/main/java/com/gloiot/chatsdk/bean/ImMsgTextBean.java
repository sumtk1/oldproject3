package com.gloiot.chatsdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hygo00 on 17/2/27.
 * 文字消息
 */

public class ImMsgTextBean extends ImMsgBean {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public ImMsgTextBean typeCast(ImMsgBean imMsgBean) {
        ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
        imMsgTextBean.setId(imMsgBean.getId());
        imMsgTextBean.setMsgid(imMsgBean.getMsgid());
        imMsgTextBean.setSendid(imMsgBean.getSendid());
        imMsgTextBean.setReceiveid(imMsgBean.getReceiveid());
        imMsgTextBean.setSessiontype(imMsgBean.getSessiontype());
        imMsgTextBean.setMsgtype(imMsgBean.getMsgtype());
        imMsgTextBean.setMessage(imMsgBean.getMessage());
        imMsgTextBean.setPushdata(imMsgBean.getPushdata());
        imMsgTextBean.setSendTime(imMsgBean.getSendTime());
        imMsgTextBean.setMessageState(imMsgBean.getMessageState());
        imMsgTextBean.setExtra(imMsgBean.getExtra());

        try {
            JSONObject message = new JSONObject(imMsgBean.getMessage());
            imMsgTextBean.setText(message.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imMsgTextBean;
    }
}
