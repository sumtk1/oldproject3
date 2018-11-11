package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/9/13.
 * 功能：收益记录model
 */


public class GainRecoredModel {
    private String id;
    private String status;
    private String productName;
    private String orderNum;
    private String paymentTime;
    private String total;
    private String rate;
    private String realEarnings;
    private String realEarningsTop;
    private List<String> imgUrl=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRealEarnings() {
        return realEarnings;
    }

    public void setRealEarnings(String realEarnings) {
        this.realEarnings = realEarnings;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRealEarningsTop() {
        return realEarningsTop;
    }

    public void setRealEarningsTop(String realEarningsTop) {
        this.realEarningsTop = realEarningsTop;
    }
}
