package com.gloiot.hygoSupply.ui.activity.promotional.model;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：促销活动model
 */


public class ParticipateActivitiesModel {

    private String productImage;
    private String productDescribe;
    private String procuctId;
    private String procuctPrice;
    private String type;//活动类型
    private String deductionPercent;//抵扣比例

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

    public String getProcuctPrice() {
        return procuctPrice;
    }

    public void setProcuctPrice(String procuctPrice) {
        this.procuctPrice = procuctPrice;
    }

    public String getDeductionPercent() {
        return deductionPercent;
    }

    public void setDeductionPercent(String deductionPercent) {
        this.deductionPercent = deductionPercent;
    }


}
