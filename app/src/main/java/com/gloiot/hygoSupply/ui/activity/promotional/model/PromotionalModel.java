package com.gloiot.hygoSupply.ui.activity.promotional.model;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：促销活动model(积分抵扣)
 */


public class PromotionalModel {
    private boolean isChoicedProduct;//是否选择了商品
    private String productImage;
    private String productDescribe;
    private String procuctId;
    private String oldProcuctId;//编辑之前的商品id
    private String procuctPrice;
    private String deductionPercent;//抵扣比例
    private String oldDeductionPercent;//编辑之前的抵扣比例
    private String normalDeductionPercent;//平台正常的抵扣比例
    private String pointsExplain;//积分抵扣说明
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String type;//活动类型

    /*
    统一价
     */
    private String uniform_num;//统一价数量
    private String uniform_price;//统一价价格

    /*
    单独价
     */
    private String separate_num;//单独价数量
    private String separate_price_one;//第一件价格
    private String separate_price_two;//第二件价格
    private String separate_price_three;//第三件价格
    /*
   折扣价
    */
    private String discount_num;//折扣价数量
    private String discount_price_one;//第一件折扣
    private String discount_price_two;//第二件折扣
    private String discount_price_three;//第三件折扣

    public String getDiscount_num() {
        return discount_num;
    }

    public void setDiscount_num(String discount_num) {
        this.discount_num = discount_num;
    }

    public String getDiscount_price_one() {
        return discount_price_one;
    }

    public void setDiscount_price_one(String discount_price_one) {
        this.discount_price_one = discount_price_one;
    }

    public String getDiscount_price_two() {
        return discount_price_two;
    }

    public void setDiscount_price_two(String discount_price_two) {
        this.discount_price_two = discount_price_two;
    }

    public String getDiscount_price_three() {
        return discount_price_three;
    }

    public void setDiscount_price_three(String discount_price_three) {
        this.discount_price_three = discount_price_three;
    }

    public String getSeparate_num() {
        return separate_num;
    }

    public void setSeparate_num(String separate_num) {
        this.separate_num = separate_num;
    }

    public String getSeparate_price_one() {
        return separate_price_one;
    }

    public void setSeparate_price_one(String separate_price_one) {
        this.separate_price_one = separate_price_one;
    }

    public String getSeparate_price_two() {
        return separate_price_two;
    }

    public void setSeparate_price_two(String separate_price_two) {
        this.separate_price_two = separate_price_two;
    }

    public String getSeparate_price_three() {
        return separate_price_three;
    }

    public void setSeparate_price_three(String separate_price_three) {
        this.separate_price_three = separate_price_three;
    }

    public String getUniform_price() {
        return uniform_price;
    }

    public void setUniform_price(String uniform_price) {
        this.uniform_price = uniform_price;
    }

    public String getUniform_num() {
        return uniform_num;
    }

    public void setUniform_num(String uniform_num) {
        this.uniform_num = uniform_num;
    }

    public String getNormalDeductionPercent() {
        return normalDeductionPercent;
    }

    public void setNormalDeductionPercent(String normalDeductionPercent) {
        this.normalDeductionPercent = normalDeductionPercent;
    }

    public boolean isChoicedProduct() {
        return isChoicedProduct;
    }

    public void setChoicedProduct(boolean choicedProduct) {
        isChoicedProduct = choicedProduct;
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

    public String getPointsExplain() {
        return pointsExplain;
    }

    public void setPointsExplain(String pointsExplain) {
        this.pointsExplain = pointsExplain;
    }

    public String getOldProcuctId() {
        return oldProcuctId;
    }

    public void setOldProcuctId(String oldProcuctId) {
        this.oldProcuctId = oldProcuctId;
    }

    public String getOldDeductionPercent() {
        return oldDeductionPercent;
    }

    public void setOldDeductionPercent(String oldDeductionPercent) {
        this.oldDeductionPercent = oldDeductionPercent;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
