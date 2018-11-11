package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by 13939 on 2016/11/3.
 */

public class DingdanGuanliShangpinBean implements Serializable {
    private String 商品id;
    private String id;
    private String 种类详细;
    private String 商品名称;
    private String 商品数量;
    private String 快递费;
    private String 缩略图;
    private String 价格;
    private String type;
    private String shouhou_status;
    private String status;//判断是否发货





    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShouhou_status() {
        return shouhou_status;
    }

    public void setShouhou_status(String shouhou_status) {
        this.shouhou_status = shouhou_status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get类型() {
        return 类型;
    }

    public void set类型(String 类型) {
        this.类型 = 类型;
    }

    private String 类型;

    public String get商品类型() {
        return 商品类型;
    }

    public void set商品类型(String 商品类型) {
        this.商品类型 = 商品类型;
    }

    private String 商品类型;

    public DingdanGuanliShangpinBean() {
    }

    public String get商品id() {
        return 商品id;
    }

    public void set商品id(String 商品id) {
        this.商品id = 商品id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get种类详细() {
        return 种类详细;
    }

    public void set种类详细(String 种类详细) {
        this.种类详细 = 种类详细;
    }

    public String get商品名称() {
        return 商品名称;
    }

    public void set商品名称(String 商品名称) {
        this.商品名称 = 商品名称;
    }

    public String get商品数量() {
        return 商品数量;
    }

    public void set商品数量(String 商品数量) {
        this.商品数量 = 商品数量;
    }

    public String get快递费() {
        return 快递费;
    }

    public void set快递费(String 快递费) {
        this.快递费 = 快递费;
    }

    public String get缩略图() {
        return 缩略图;
    }

    public void set缩略图(String 缩略图) {
        this.缩略图 = 缩略图;
    }

    public String get价格() {
        return 价格;
    }

    public void set价格(String 价格) {
        this.价格 = 价格;
    }

    @Override
    public String toString() {
        return "DingdanGuanliShangpinBean{" +
                "商品id='" + 商品id + '\'' +
                ", id='" + id + '\'' +
                ", 种类详细='" + 种类详细 + '\'' +
                ", 商品名称='" + 商品名称 + '\'' +
                ", 商品数量='" + 商品数量 + '\'' +
                ", 快递费='" + 快递费 + '\'' +
                ", 缩略图='" + 缩略图 + '\'' +
                ", 价格='" + 价格 + '\'' +
                ", type='" + type + '\'' +
                ", shouhou_status='" + shouhou_status + '\'' +
                ", 类型='" + 类型 + '\'' +
                ", 商品类型='" + 商品类型 + '\'' +
                '}';
    }
}
