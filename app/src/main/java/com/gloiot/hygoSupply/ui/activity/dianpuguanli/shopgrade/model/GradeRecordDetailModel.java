package com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model;

/**
 * 作者：Ljy on 2017/10/11.
 * 功能：我的——我的资料
 */


public class GradeRecordDetailModel {
    private String id;
    private String img;
    private String name;
    private String decribe;
    private int point;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public String getDecribe() {
        return decribe;
    }

    public void setDecribe(String decribe) {
        this.decribe = decribe;
    }
}
