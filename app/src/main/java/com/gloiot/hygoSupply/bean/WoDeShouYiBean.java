package com.gloiot.hygoSupply.bean;

/**
 * Created by hygo02g on 2017/4/20.
 */

public class WoDeShouYiBean {
    private String wode_shouyi_title;//商品名称
    private int wode_shouyi_shuliang;//商品数量
    private String wode_shouyi_time = "";//创建时间
    private String wode_shouyi_price = "";//收益
    private String wode_shouyi_zhuangtai;//状态
    private String wode_shouyi_shuoming = "";//说明（转红利）
    private String wode_shouyi_jifen = "";//积分
    private String img = "";//缩略图
    private String id = "";//id
    private String wode_shouyi_yinhangka_type;//收益转银行卡状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWode_shouyi_title() {
        return wode_shouyi_title;
    }

    public void setWode_shouyi_title(String wode_shouyi_title) {
        this.wode_shouyi_title = wode_shouyi_title;
    }

    public int getWode_shouyi_shuliang() {
        return wode_shouyi_shuliang;
    }

    public void setWode_shouyi_shuliang(int wode_shouyi_shuliang) {
        this.wode_shouyi_shuliang = wode_shouyi_shuliang;
    }

    public String getWode_shouyi_time() {
        return wode_shouyi_time;
    }

    public void setWode_shouyi_time(String wode_shouyi_time) {
        this.wode_shouyi_time = wode_shouyi_time;
    }

    public String getWode_shouyi_price() {
        return wode_shouyi_price;
    }

    public void setWode_shouyi_price(String wode_shouyi_price) {
        this.wode_shouyi_price = wode_shouyi_price;
    }

    public String getWode_shouyi_zhuangtai() {
        return wode_shouyi_zhuangtai;
    }

    public void setWode_shouyi_zhuangtai(String wode_shouyi_zhuangtai) {
        this.wode_shouyi_zhuangtai = wode_shouyi_zhuangtai;
    }

    public String getWode_shouyi_shuoming() {
        return wode_shouyi_shuoming;
    }

    public void setWode_shouyi_shuoming(String wode_shouyi_shuoming) {
        this.wode_shouyi_shuoming = wode_shouyi_shuoming;
    }

    public String getWode_shouyi_jifen() {
        return wode_shouyi_jifen;
    }

    public void setWode_shouyi_jifen(String wode_shouyi_jifen) {
        this.wode_shouyi_jifen = wode_shouyi_jifen;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWode_shouyi_yinhangka_type() {
        return wode_shouyi_yinhangka_type;
    }

    public void setWode_shouyi_yinhangka_type(String wode_shouyi_yinhangka_type) {
        this.wode_shouyi_yinhangka_type = wode_shouyi_yinhangka_type;
    }
}
