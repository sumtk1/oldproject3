package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by  ZhangKai on 2016/8/23 10:32
 */
public class ShangpinGuigeBean implements Serializable {
    private String yanse;
    private String chicun;
    private String jianyijia;
    private String gonghuojia;
    private String kucun;
    //如果店铺类型非服装类店铺，用此属性
    private String xiangxi;

    @Override
    public String toString() {
        return "ShangpinGuigeBean{" +
                "yanse='" + yanse + '\'' +
                ", chicun='" + chicun + '\'' +
                ", jianyijia='" + jianyijia + '\'' +
                ", gonghuojia='" + gonghuojia + '\'' +
                ", kucun='" + kucun + '\'' +
                '}';
    }

    public ShangpinGuigeBean() {

    }

    public ShangpinGuigeBean(String yanse, String chicun, String jiesuanjia, String jiage, String kucun) {
        this.yanse = yanse;
        this.chicun = chicun;
        this.jianyijia = jiesuanjia;
        this.gonghuojia = jiage;
        this.kucun = kucun;
    }

    public String getYanse() {
        return yanse;
    }

    public void setYanse(String yanse) {
        this.yanse = yanse;
    }

    public String getChicun() {
        return chicun;
    }

    public void setChicun(String chicun) {
        this.chicun = chicun;
    }

    public String getJianyijia() {
        return jianyijia;
    }

    public void setJianyijia(String jianyijia) {
        this.jianyijia = jianyijia;
    }

    public String getGonghuojia() {
        return gonghuojia;
    }

    public void setGonghuojia(String gonghuojia) {
        this.gonghuojia = gonghuojia;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getXiangxi() {
        return xiangxi;
    }

    public void setXiangxi(String xiangxi) {
        this.xiangxi = xiangxi;
    }
}
