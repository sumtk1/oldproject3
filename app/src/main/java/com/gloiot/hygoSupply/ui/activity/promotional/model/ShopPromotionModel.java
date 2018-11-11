package com.gloiot.hygoSupply.ui.activity.promotional.model;

import com.gloiot.hygoSupply.ui.activity.promotional.paster.UpdataPage;

import java.util.Map;

/**
 * 作者：Ljy on 2017/12/21.
 * 功能：商品推广model
 */


public class ShopPromotionModel {
    private boolean isChoicedProduct;//是否选择了商品
    private String productImage;
    private String productDescribe;
    private String procuctId;
    private String type="";//推广类型
    private String secondType;//二级推广类型（图片位置）
    private String remark;//备注
    private Double price;//每天价格
    private int time;//天数
    private Double total = 0.0;//总价
    private Double earnings = 0.0;//收益账户金额
    private UpdataPage updataPage;//收益账户金额
    private Map<String, Double> priceInfo;
    private String[] type1 ;
    private String[] type2 ;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public UpdataPage getUpdataPage() {
        return updataPage;
    }

    public void setUpdataPage(UpdataPage updataPage) {
        this.updataPage = updataPage;
    }

    public Map<String, Double> getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(Map<String, Double> priceInfo) {
        this.priceInfo = priceInfo;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String[] getType1() {
        return type1;
    }

    public void setType1(String[] type1) {
        this.type1 = type1;
    }

    public String[] getType2() {
        return type2;
    }

    public void setType2(String[] type2) {
        this.type2 = type2;
    }
}
