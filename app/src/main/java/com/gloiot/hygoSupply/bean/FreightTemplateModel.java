package com.gloiot.hygoSupply.bean;

import android.databinding.ObservableBoolean;

import java.io.Serializable;


/**
 * 作者：Ljy on 2017/5/6.
 * 功能：我的——我的资料
 */


public class FreightTemplateModel implements Serializable {

    private String name = "";//名称
    private ObservableBoolean freeExpress;//是否包邮
    private String id = "";
    private String priceType = "";//计价方式(类型)
    private String unit = "";//单位
    private String freight = "";//运费
    private String add = "";//加添
    private String addNum = "";//加额
    private String limiters = "";//限定条件
    private String limitersType = "";//限定类型
    private String strandard = "";//规格
    private ObservableBoolean isLimitMoney;//是否限制条件（true:选择限制，false:不选择限制 ）
    private ObservableBoolean isLimitNum;//是否是数量限制（true:数量限制，false:金额限制 ）
    private String unitShow = "件";//运费设置显示的单位（件，kg，m³）

    public String getUnitShow() {
        return unitShow;
    }

    public void setUnitShow(String unitShow) {
        this.unitShow = unitShow;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getAddNum() {
        return addNum;
    }

    public void setAddNum(String addNum) {
        this.addNum = addNum;
    }

    public String getLimiters() {
        return limiters;
    }

    public void setLimiters(String limiters) {
        this.limiters = limiters;
    }

    public String getLimitersType() {
        return limitersType;
    }

    public void setLimitersType(String limitersType) {
        this.limitersType = limitersType;
    }

    public ObservableBoolean getFreeExpress() {
        return freeExpress;
    }

    public void setFreeExpress(ObservableBoolean freeExpress) {
        this.freeExpress = freeExpress;
    }

    public ObservableBoolean getIsLimitMoney() {
        return isLimitMoney;
    }

    public void setIsLimitMoney(ObservableBoolean isLimitMoney) {
        this.isLimitMoney = isLimitMoney;
    }

    public ObservableBoolean getIsLimitNum() {
        return isLimitNum;
    }

    public void setIsLimitNum(ObservableBoolean isLimitNum) {
        this.isLimitNum = isLimitNum;
    }

    public String getStrandard() {
        return strandard;
    }

    public void setStrandard(String strandard) {
        this.strandard = strandard;
    }

}
