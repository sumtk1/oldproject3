package com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model;

/**
 * 作者：Ljy on 2017/8/12.
 * 功能：商品model
 */


public class ProductModel {
    private boolean isMulit;//是否多款商品
    private String link = "";//链接（多款）
    private String title = "";//标题（多款）
    private String describe = "";//描述
    private String imgUrl = "";//图片地址
    private String id = "";//商品id
    private String price = "";//商品价格
    private String num = "";//商品数量

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMulit() {
        return isMulit;
    }

    public void setMulit(boolean mulit) {
        isMulit = mulit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
