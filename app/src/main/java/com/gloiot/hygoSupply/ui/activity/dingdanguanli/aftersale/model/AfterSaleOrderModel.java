package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model;

/**
 * 作者：Ljy on 2017/9/20.
 *
 */


public class AfterSaleOrderModel {
    private String id;
    private String orderId;
    private String status;
    private String afterSaleSatus="";
    private String afterSaleType="";
    private String consignee;
    private String nickName;
    private String deadline;
    private String totalCost;
    private String num;
    private AfterSaleProductModel productModel;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAfterSaleSatus() {
        return afterSaleSatus;
    }

    public void setAfterSaleSatus(String afterSaleSatus) {
        this.afterSaleSatus = afterSaleSatus;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public AfterSaleProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(AfterSaleProductModel productModel) {
        this.productModel = productModel;
    }

    public String getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(String afterSaleType) {
        this.afterSaleType = afterSaleType;
    }
}
