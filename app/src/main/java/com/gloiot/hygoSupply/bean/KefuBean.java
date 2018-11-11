package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * 功能：客服实体类
 * Created by ZXL on 2017/7/26.
 */
public class KefuBean implements Serializable {
    private String account; //账号
    private String nickname; //昵称

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "KefuBean{" +
                "account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
