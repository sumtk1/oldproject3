package com.gloiot.hygoSupply.ui.activity.evaluation.model;

/**
 * 作者：Ljy on 2017/8/24.
 * 功能：我的——我的资料
 */


public class ReplyModel {
    boolean isClient;
    String content;

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
