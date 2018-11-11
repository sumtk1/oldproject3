package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by ZXL on 2016/11/3.
 */

public class DingdanXiangQingSPBean implements Serializable {


    private String sp_id;


    private String id;
    private String sp_shuliang;
    private String sp_guige;
    private String sp_tupian;
    private String sp_mingcheng;
    private String sp_danjia;
    private String sp_type;
    private String sp_status;
    private String sp_aftersale_status;
    private String sp_wuliu_status;
    private String sp_feilv;


    public String getSp_feilv() {
        return sp_feilv;
    }

    public void setSp_feilv(String sp_feilv) {
        this.sp_feilv = sp_feilv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSp_aftersale_status() {
        return sp_aftersale_status;
    }

    public void setSp_aftersale_status(String sp_aftersale_status) {
        this.sp_aftersale_status = sp_aftersale_status;
    }

    public String getSp_status() {
        return sp_status;
    }

    public void setSp_status(String sp_status) {
        this.sp_status = sp_status;
    }

    public String getSp_productType() {
        return sp_productType;
    }

    public void setSp_productType(String sp_productType) {
        this.sp_productType = sp_productType;
    }

    private String sp_productType;
    private boolean is_select_ed = false;

//    "商品id":"1201",
//            "商品数量":"1.0000",
//            "商品规格":"规格:红",
//            "图片数量":"1",
//            "图片":"http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/01B6EBBD-368C-454A-A5AF-A9F08C35CE213943505982.jpg",
//            "商品名称":"自营",
//            "单价":"80"

    public boolean is_select_ed() {
        return is_select_ed;
    }

    public void setIs_select_ed(boolean is_select_ed) {
        this.is_select_ed = is_select_ed;
    }

    public String getSp_type() {
        return sp_type;
    }

    public void setSp_type(String sp_type) {
        this.sp_type = sp_type;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getSp_shuliang() {
        return sp_shuliang;
    }

    public void setSp_shuliang(String sp_shuliang) {
        this.sp_shuliang = sp_shuliang;
    }

    public String getSp_guige() {
        return sp_guige;
    }

    public void setSp_guige(String sp_guige) {
        this.sp_guige = sp_guige;
    }

    public String getSp_tupian() {
        return sp_tupian;
    }

    public void setSp_tupian(String sp_tupian) {
        this.sp_tupian = sp_tupian;
    }

    public String getSp_mingcheng() {
        return sp_mingcheng;
    }

    public void setSp_mingcheng(String sp_mingcheng) {
        this.sp_mingcheng = sp_mingcheng;
    }

    public String getSp_danjia() {
        return sp_danjia;
    }

    public void setSp_danjia(String sp_danjia) {
        this.sp_danjia = sp_danjia;
    }

    public String getSp_wuliu_status() {
        return sp_wuliu_status;
    }

    public void setSp_wuliu_status(String sp_wuliu_status) {
        this.sp_wuliu_status = sp_wuliu_status;
    }
}
