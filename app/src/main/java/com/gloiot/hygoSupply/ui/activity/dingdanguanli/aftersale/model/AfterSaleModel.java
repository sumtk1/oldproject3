package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/9/12.
 * 功能：售后详情model
 */


public class AfterSaleModel {
    private String status = "";//当前订单状态（页面显示的状态）
    private String afterSaleStatus = "";//售后状态（后端存储的状态）
    private String buyerInfo = "";
    private String buyerAddress = "";
    private String name = "";
    private String id = "";
    private String size = "";
    private String num = "";
    private String reason = "";
    private String money = "";
    private String time = "";
    private String orderId = "";
    private String explain = "";
    private String notice = "";
    private String productImg = "";
    private String productId = "";
    private String afterSaleType = "";
    private String refuseReason = "";
    private String refuseExplain = "";
    private String resultTime = "";
    private String expressCompany = "";
    private String expressId = "";
    private String expressTime = "";
    private String expressStatus = "";
    private String successTime = "";//退款成功时间
    private String account = "";//退款人账号
    private List<String> voucherImages = new ArrayList<>();
    private List<String[]> progressInfo = new ArrayList<>();

    public String getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(String expressStatus) {
        this.expressStatus = expressStatus;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public List<String> getVoucherImages() {
        return voucherImages;
    }

    public void setVoucherImages(List<String> voucherImages) {
        this.voucherImages = voucherImages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(String buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }


    public String getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(String afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    public List<String[]> getProgressInfo() {
        return progressInfo;
    }

    public void setProgressInfo(List<String[]> progressInfo) {
        this.progressInfo = progressInfo;
    }

    public String getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(String afterSaleType) {
        this.afterSaleType = afterSaleType;
    }


    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getRefuseExplain() {
        return refuseExplain;
    }

    public void setRefuseExplain(String refuseExplain) {
        this.refuseExplain = refuseExplain;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(String expressTime) {
        this.expressTime = expressTime;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
