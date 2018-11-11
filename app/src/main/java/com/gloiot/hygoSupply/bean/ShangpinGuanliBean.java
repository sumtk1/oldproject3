package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2016/9/6.
 */
public class ShangpinGuanliBean implements Serializable {
    private String id;    //销售独有
    private String shangpinmingcheng;//商品民称
    private String danjia;//单价
    private String kucun;//库存
    private String zhuangtai; //状态    申请独有
    private String suoluetu;//缩略图
    private String xiaoshou;//销量   销售独有
    private String xiajiayuanyin;//下架原因
    private String zuofeiyuanyin;//作废原因
    private String zuofeishijian;//作废时间
    private String lurushijian;//录入时间

    public boolean isQuanqiugou() {
        return quanqiugou;
    }

    public void setQuanqiugou(boolean quanqiugou) {
        this.quanqiugou = quanqiugou;
    }

    private boolean quanqiugou;//

    private String freightId;//运费模板id

    private int is_selected = -1;// -1是隐藏图标，0是显示未选中的图片，1是选中的图片

    public String getZuofeiyuanyin() {
        return zuofeiyuanyin;
    }

    public String getXiaoshou() {
        return xiaoshou;
    }

    public void setXiaoshou(String xiaoshou) {
        this.xiaoshou = xiaoshou;
    }

    public String getZuofeishijian() {
        return zuofeishijian;
    }

    public void setZuofeishijian(String zuofeishijian) {
        this.zuofeishijian = zuofeishijian;
    }

    public String getLurushijian() {
        return lurushijian;
    }

    public void setLurushijian(String lurushijian) {
        this.lurushijian = lurushijian;
    }

    public void setZuofeiyuanyin(String zuofeiyuanyin) {
        this.zuofeiyuanyin = zuofeiyuanyin;
    }

    public ShangpinGuanliBean() {
    }

    public ShangpinGuanliBean(String id, String shangpinmingcheng, String danjia, String kucun, String zhuangtai, String suoluetu, String xiaoshou) {
        this.id = id;
        this.shangpinmingcheng = shangpinmingcheng;
        this.danjia = danjia;
        this.kucun = kucun;
        this.zhuangtai = zhuangtai;
        this.suoluetu = suoluetu;
        this.xiaoshou = xiaoshou;
    }

    public String getXiajiayuanyin() {
        return xiajiayuanyin;
    }

    public void setXiajiayuanyin(String xiajiayuanyin) {
        this.xiajiayuanyin = xiajiayuanyin;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShangpinmingcheng() {
        return shangpinmingcheng;
    }

    public void setShangpinmingcheng(String shangpinmingcheng) {
        this.shangpinmingcheng = shangpinmingcheng;
    }

    public String getDanjia() {
        return danjia;
    }

    public void setDanjia(String danjia) {
        this.danjia = danjia;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getSuoluetu() {
        return suoluetu;
    }

    public void setSuoluetu(String suoluetu) {
        this.suoluetu = suoluetu;
    }

    public String getxiaoshou() {
        return xiaoshou;
    }

    public void setxiaoshou(String xiaoshou) {
        this.xiaoshou = xiaoshou;
    }

    public String getFreightId() {
        return freightId;
    }

    public void setFreightId(String freightId) {
        this.freightId = freightId;
    }

    @Override
    public String toString() {
        return "ShangpinGuanliBean{" +
                "id='" + id + '\'' +
                ", shangpinmingcheng='" + shangpinmingcheng + '\'' +
                ", danjia='" + danjia + '\'' +
                ", kucun='" + kucun + '\'' +
                ", suoluetu='" + suoluetu + '\'' +
                ", xiaoshou='" + xiaoshou + '\'' +
                '}';
    }
}
