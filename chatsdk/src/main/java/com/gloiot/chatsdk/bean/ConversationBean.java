package com.gloiot.chatsdk.bean;

/**
 * Created by hygo02 on 2017/5/18.
 * 作用：会话列表消息实体
 */

public class ConversationBean {
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_SYSTEM = 2;
    //    public static final int VIEW_TYPE_APPLICATION = 3;
    public static final int VIEW_TYPE_GROUP = 4;
    public static final int VIEW_TYPE_WULIU = 5;

    public static final int VIEW_TYPE_ZHANGDAN = 6;//账单

    private String sendid;
    private String receiveid;
    private String sessiontype;
    private String msgtype;
    private String message;
    private String pushdata;
    private String sendTime;
    private int isNoDiaturb;
    private int noReadNum;
    private int isTop;
    private int timestamp;
    private String extra;

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

    public int getIsNoDiaturb() {
        return isNoDiaturb;
    }

    public void setIsNoDiaturb(int isNoDiaturb) {
        this.isNoDiaturb = isNoDiaturb;
    }

    public int getNoReadNum() {
        return noReadNum;
    }

    public void setNoReadNum(int noReadNum) {
        this.noReadNum = noReadNum;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "ConversationBean{" +
                "sendid='" + sendid + '\'' +
                ", receiveid='" + receiveid + '\'' +
                ", sessiontype='" + sessiontype + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", message='" + message + '\'' +
                ", pushdata='" + pushdata + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", isNoDiaturb=" + isNoDiaturb +
                ", noReadNum=" + noReadNum +
                ", isTop=" + isTop +
                ", timestamp=" + timestamp +
                ", extra='" + extra + '\'' +
                '}';
    }
}
