package com.gloiot.chatsdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hygo00 on 17/6/16.
 * 图片消息
 */

public class ImMsgImgBean extends ImMsgBean {

    private String img;
    private String url;
    private String imgExtra;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgExtra() {
        return imgExtra;
    }

    public void setImgExtra(String imgExtra) {
        this.imgExtra = imgExtra;
    }


    public ImMsgImgBean typeCast(ImMsgBean imMsgBean) {
        ImMsgImgBean imMsgImgBean = new ImMsgImgBean();
        imMsgImgBean.setId(imMsgBean.getId());
        imMsgImgBean.setMsgid(imMsgBean.getMsgid());
        imMsgImgBean.setSendid(imMsgBean.getSendid());
        imMsgImgBean.setReceiveid(imMsgBean.getReceiveid());
        imMsgImgBean.setSessiontype(imMsgBean.getSessiontype());
        imMsgImgBean.setMsgtype(imMsgBean.getMsgtype());
        imMsgImgBean.setMessage(imMsgBean.getMessage());
        imMsgImgBean.setPushdata(imMsgBean.getPushdata());
        imMsgImgBean.setSendTime(imMsgBean.getSendTime());
        imMsgImgBean.setMessageState(imMsgBean.getMessageState());
        imMsgImgBean.setExtra(imMsgBean.getExtra());

        try {
            JSONObject message = new JSONObject(imMsgBean.getMessage());
            imMsgImgBean.setImg(message.getString("img"));
            imMsgImgBean.setUrl(message.getString("url"));
            imMsgImgBean.setImgExtra(message.getString("extra"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imMsgImgBean;
    }
}
