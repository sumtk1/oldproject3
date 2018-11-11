package com.gloiot.chatsdk.bean;

/**
 * Created by hygo00 on 17/5/23.
 */

public class UserInfo {

    String id;        // 用户id
    String name;      // 用户名称
    String url;       // 用户头像
    String noteName;       // 用户备注
    String realName;       // 用户备注
//    asdadasdasdasdd

    public UserInfo() {
    }

    public UserInfo(String id, String name, String url, String noteName) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.noteName = noteName;
    }

    public UserInfo(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", noteName='" + noteName + '\'' +
                '}';
    }
}
