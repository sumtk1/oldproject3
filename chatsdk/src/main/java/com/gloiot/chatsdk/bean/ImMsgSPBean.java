package com.gloiot.chatsdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建者 zengming.
 * 功能：商品 (图片 + 文字)消息
 */

public class ImMsgSPBean extends ImMsgBean {

    private String spId;
    private String spIcon;    // 商品图片
    private String spTitle;   // 商品标题
    private String spMoney;   // 商品价格
    private String spUrl;     // 商品链接
    private String extra;     // 附加

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpIcon() {
        return spIcon;
    }

    public void setSpIcon(String spIcon) {
        this.spIcon = spIcon;
    }

    public String getSpTitle() {
        return spTitle;
    }

    public void setSpTitle(String spTitle) {
        this.spTitle = spTitle;
    }

    public String getSpMoney() {
        return spMoney;
    }

    public void setSpMoney(String spMoney) {
        this.spMoney = spMoney;
    }

    public String getSpUrl() {
        return spUrl;
    }

    public void setSpUrl(String spUrl) {
        this.spUrl = spUrl;
    }

    @Override
    public String getExtra() {
        return extra;
    }

    @Override
    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ImMsgSPBean typeCast(ImMsgBean imMsgBean) {
        ImMsgSPBean imMsgSPBean = new ImMsgSPBean();
        imMsgSPBean.setId(imMsgBean.getId());
        imMsgSPBean.setMsgid(imMsgBean.getMsgid());
        imMsgSPBean.setSendid(imMsgBean.getSendid());
        imMsgSPBean.setReceiveid(imMsgBean.getReceiveid());
        imMsgSPBean.setSessiontype(imMsgBean.getSessiontype());
        imMsgSPBean.setMsgtype(imMsgBean.getMsgtype());
        imMsgSPBean.setMessage(imMsgBean.getMessage());
        imMsgSPBean.setPushdata(imMsgBean.getPushdata());
        imMsgSPBean.setSendTime(imMsgBean.getSendTime());
        imMsgSPBean.setMessageState(imMsgBean.getMessageState());
        imMsgSPBean.setExtra(imMsgBean.getExtra());

        try {
            JSONObject message = new JSONObject(imMsgBean.getMessage());
            imMsgSPBean.setSpId(message.getString("spId"));
            imMsgSPBean.setSpIcon(message.getString("spIcon"));
            imMsgSPBean.setSpTitle(message.getString("spTitle"));
            imMsgSPBean.setSpMoney(message.getString("spMoney"));
            imMsgSPBean.setSpUrl(message.getString("spUrl"));
            imMsgSPBean.setExtra(message.getString("extra"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imMsgSPBean;
    }
}
