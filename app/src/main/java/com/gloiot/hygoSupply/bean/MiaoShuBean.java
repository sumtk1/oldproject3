package com.gloiot.hygoSupply.bean;


import java.io.Serializable;

/**
 * Created by Admin on 2016/8/30.
 */
public class MiaoShuBean implements Serializable,Cloneable {

    private  String miaoshu;
    private  String tupian;

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    public MiaoShuBean(String miaoshu, String tupian) {
        this.miaoshu = miaoshu;
        this.tupian = tupian;
    }

    public MiaoShuBean() {
    }

    @Override
    public MiaoShuBean clone(){
        MiaoShuBean model = null;
        try {
            model = (MiaoShuBean) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return model;
    }
}
