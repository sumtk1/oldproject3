package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import com.gloiot.hygoSupply.R;

/**
 * 作者：Ljy on 2018/1/2.
 * 功能：
 */


public class SystemMessageRecordModel {
    private String type;
    private int image;
    private String tiem;
    private String id;
    private String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        switch (type) {
            case "系统维护":
                image = R.mipmap.ic_systemmaintenance_message;
                break;
            case "重要通知":
                image = R.mipmap.ic_important_message;
                break;
            case "通用通知":
                image = R.mipmap.ic_general_notification;
                break;
            case "紧急通知":
                image = R.mipmap.ic_urgent_notice;
                break;
            case "最新通知":
                image = R.mipmap.ic_latest_notice;
                break;
            case "临时通知":
                image = R.mipmap.ic_interim_notice;
                break;
            case "无效消息":
                image = R.mipmap.ic_useless_notice;
                break;
        }
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTiem() {
        return tiem;
    }

    public void setTiem(String tiem) {
        this.tiem = tiem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
