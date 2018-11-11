package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by  ZhangKai on 2016/8/19 15:12
 * 订单实体类
 */
public class YouHuiQuanBean implements Serializable {
    private String id  ;//优惠券id
    private String zhonglei  ;//优惠券类型
    private String youhuiquanmingcheng ;
    private String miane  ; //优惠券金额
    private String fahangliang  ; //优惠券总量
    private String shengxiaoshijian  ;
    private String shixiaoshijian ;
    private String zhuangtai ;
    private String yiling ;//优惠券领取量
    private String yishiyong ;//优惠券已使用量
    private String shiyongtiaojian; //使用条件

    public String getShiyongtiaojian() {
        return shiyongtiaojian;
    }

    public void setShiyongtiaojian(String shiyongtiaojian) {
        this.shiyongtiaojian = shiyongtiaojian;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getYishiyong() {
        return yishiyong;
    }

    public void setYishiyong(String yishiyong) {
        this.yishiyong = yishiyong;
    }

    public String getZhonglei() {
        return zhonglei;
    }

    public void setZhonglei(String zhonglei) {
        this.zhonglei = zhonglei;
    }

    public String getYouhuiquanmingcheng() {
        return youhuiquanmingcheng;
    }

    public void setYouhuiquanmingcheng(String youhuiquanmingcheng) {
        this.youhuiquanmingcheng = youhuiquanmingcheng;
    }

    public String getMiane() {
        return miane;
    }

    public void setMiane(String miane) {
        this.miane = miane;
    }

    public String getFahangliang() {
        return fahangliang;
    }

    public void setFahangliang(String fahangliang) {
        this.fahangliang = fahangliang;
    }

    public String getShengxiaoshijian() {
        return shengxiaoshijian;
    }

    public void setShengxiaoshijian(String shengxiaoshijian) {
        this.shengxiaoshijian = shengxiaoshijian;
    }

    public String getShixiaoshijian() {
        return shixiaoshijian;
    }

    public void setShixiaoshijian(String shixiaoshijian) {
        this.shixiaoshijian = shixiaoshijian;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getYiling() {
        return yiling;
    }

    public void setYiling(String yiling) {
        this.yiling = yiling;
    }


}
