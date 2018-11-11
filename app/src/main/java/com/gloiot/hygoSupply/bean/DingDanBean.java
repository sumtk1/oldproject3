package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by  ZhangKai on 2016/8/19 15:12
 * 订单实体类
 */
public class DingDanBean implements Serializable {
    private String touxiang;
    private String nicheng;
    private String dingdan_id;
    private String shifu_jine;
    private String zhuangtai;
    private String shangpin_shuoming;
    private String shouhuo_dizhi;
    private String chuangjian_time;
    private String zhifu_fangshi;
    private String zhanghao;

    @Override
    public String toString() {
        return "DingDanBean{" +
                "touxiang='" + touxiang + '\'' +
                ", nicheng='" + nicheng + '\'' +
                ", dingdan_id='" + dingdan_id + '\'' +
                ", shifu_jine='" + shifu_jine + '\'' +
                ", zhuangtai='" + zhuangtai + '\'' +
                ", shangpin_shuoming='" + shangpin_shuoming + '\'' +
                ", shouhuo_dizhi='" + shouhuo_dizhi + '\'' +
                ", chuangjian_time='" + chuangjian_time + '\'' +
                ", zhifu_fangshi='" + zhifu_fangshi + '\'' +
                ", zhanghao='" + zhanghao + '\'' +
                '}';
    }

    public DingDanBean() {
    }

    public DingDanBean(String touxiang, String nicheng, String dingdan_id, String shifu_jine, String zhuangtai, String shangpin_shuoming, String shouhuo_dizhi, String chuangjian_time, String zhifu_fangshi, String zhanghao) {
        this.touxiang = touxiang;
        this.nicheng = nicheng;
        this.dingdan_id = dingdan_id;
        this.shifu_jine = shifu_jine;
        this.zhuangtai = zhuangtai;
        this.shangpin_shuoming = shangpin_shuoming;
        this.shouhuo_dizhi = shouhuo_dizhi;
        this.chuangjian_time = chuangjian_time;
        this.zhifu_fangshi = zhifu_fangshi;
        this.zhanghao = zhanghao;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getDingdan_id() {
        return dingdan_id;
    }

    public void setDingdan_id(String dingdan_id) {
        this.dingdan_id = dingdan_id;
    }

    public String getShifu_jine() {
        return shifu_jine;
    }

    public void setShifu_jine(String shifu_jine) {
        this.shifu_jine = shifu_jine;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getShangpin_shuoming() {
        return shangpin_shuoming;
    }

    public void setShangpin_shuoming(String shangpin_shuoming) {
        this.shangpin_shuoming = shangpin_shuoming;
    }

    public String getShouhuo_dizhi() {
        return shouhuo_dizhi;
    }

    public void setShouhuo_dizhi(String shouhuo_dizhi) {
        this.shouhuo_dizhi = shouhuo_dizhi;
    }

    public String getChuangjian_time() {
        return chuangjian_time;
    }

    public void setChuangjian_time(String chuangjian_time) {
        this.chuangjian_time = chuangjian_time;
    }

    public String getZhifu_fangshi() {
        return zhifu_fangshi;
    }

    public void setZhifu_fangshi(String zhifu_fangshi) {
        this.zhifu_fangshi = zhifu_fangshi;
    }

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao;
    }
}
