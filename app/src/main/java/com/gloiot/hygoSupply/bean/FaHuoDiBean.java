package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by  ZhangKai on 2016/8/16 15:21
 * 发货地实体类
 */
public class FaHuoDiBean implements Serializable{
    private String id;
    private String fahuoren;
    private String phone;
    private String dizhi;
    private String zhuangtai;


    public FaHuoDiBean(String id, String fahuoren, String phone, String dizhi, String zhuangtai) {
        this.id = id;
        this.fahuoren = fahuoren;
        this.phone = phone;
        this.dizhi = dizhi;
        this.zhuangtai = zhuangtai;
    }

    @Override
    public String toString() {
        return "FaHuoDiBean{" +
                "id='" + id + '\'' +
                ", fahuoren='" + fahuoren + '\'' +
                ", phone='" + phone + '\'' +
                ", dizhi='" + dizhi + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                '}';
    }

    public FaHuoDiBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFahuoren() {
        return fahuoren;
    }

    public void setFahuoren(String fahuoren) {
        this.fahuoren = fahuoren;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDizhi() {
        return dizhi;
    }

    public void setDizhi(String dizhi) {
        this.dizhi = dizhi;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }
}
