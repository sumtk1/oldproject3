package com.gloiot.hygoSupply.ui.activity.promotional.model;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品推广model
 */


public class ShopPromotionRecordModel {

    private String productImage;
    private String productDescribe;
    private String procuctId;
    private String price;//每日费用
    private String type;//推广类型
    private String time;//推广时间
    private String recordTime;//推广发起时间
    private String payType;//支付类型


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescribe() {
        return productDescribe;
    }

    public void setProductDescribe(String productDescribe) {
        this.productDescribe = productDescribe;
    }

    public String getProcuctId() {
        return procuctId;
    }

    public void setProcuctId(String procuctId) {
        this.procuctId = procuctId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
