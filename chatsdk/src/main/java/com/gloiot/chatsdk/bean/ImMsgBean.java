package com.gloiot.chatsdk.bean;

import java.io.Serializable;

/**
 * Created by hygo02 on 2017/5/18.
 * 作用：系统消息实体
 */

public class ImMsgBean implements Serializable {

    public static final String FROM_USER_MSG = "文本_0";      //接收文字消息
    public static final String TO_USER_MSG = "文本_1";        //发送文字消息
    public static final String FROM_USER_IMG = "图片_0";      //接收图片消息
    public static final String TO_USER_IMG = "图片_1";        //发送图片消息
    public static final String FROM_USER_VOICE = "语音_0";    //接收语音消息
    public static final String TO_USER_VOICE = "语音_1";      //发送语音消息
    public static final String FROM_USER_IMGTEXT = "图文_0";  //接收图文消息
    public static final String TO_USER_IMGTEXT = "图文_1";    //发送图文消息
    public static final String FROM_USER_SHANGPIN = "商品_0";  //接收商品消息
    public static final String TO_USER_SHANGPIN = "商品_1";    //发送商品消息
    public static final String FROM_USER_SHOPPING = "购物_0";  //接收购物消息
    public static final String TO_USER_SHOPPING = "购物_1";    //发送购物消息

    private int id;
    private String msgid;
    private String sendid;
    private String receiveid;
    private String sessiontype;
    private String msgtype;
    private String message;
    private String pushdata;
    private String sendTime;
    private int messageState;
    private String extra;

    public ImMsgBean(){}

    public ImMsgBean(int id, String msgid, String sendid, String receiveid, String sessiontype, String msgtype, String message, String pushdata, String sendTime, int messageState, String extra) {
        this.id = id;
        this.msgid = msgid;
        this.sendid = sendid;
        this.receiveid = receiveid;
        this.sessiontype = sessiontype;
        this.msgtype = msgtype;
        this.message = message;
        this.pushdata = pushdata;
        this.sendTime = sendTime;
        this.messageState = messageState;
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }

    public String getSessiontype() {
        return sessiontype;
    }

    public void setSessiontype(String sessiontype) {
        this.sessiontype = sessiontype;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPushdata() {
        return pushdata;
    }

    public void setPushdata(String pushdata) {
        this.pushdata = pushdata;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    @Override
    public String toString() {
        return "ImMsgBean{" +
                "id=" + id +
                ", msgid='" + msgid + '\'' +
                ", sendid='" + sendid + '\'' +
                ", receiveid='" + receiveid + '\'' +
                ", sessiontype='" + sessiontype + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", message='" + message + '\'' +
                ", pushdata='" + pushdata + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", messageState='" + messageState + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}
