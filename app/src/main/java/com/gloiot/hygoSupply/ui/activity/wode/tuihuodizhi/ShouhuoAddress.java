package com.gloiot.hygoSupply.ui.activity.wode.tuihuodizhi;

import java.io.Serializable;

/**
 * 收货地址
 * Created by LinKorea on 2017/5/23.
 */

public class ShouhuoAddress implements Serializable {
    private String id;
    private String shouhuoren;
    private String phoneNum;
    private String province;
    private String city;
    private String district;
    private String detailedAddress;//详细地址
    private String defaultAddress;//是否是默认地址，取值：是/否
    private String shouhuoArea;//收货地区

    public ShouhuoAddress(String id, String shouhuoren, String phoneNum, String province, String city, String district, String detailedAddress, String defaultAddress, String shouhuoArea) {
        this.id = id;
        this.shouhuoren = shouhuoren;
        this.phoneNum = phoneNum;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detailedAddress = detailedAddress;
        this.defaultAddress = defaultAddress;
        this.shouhuoArea = shouhuoArea;
    }

    public String getShouhuoArea() {
        return shouhuoArea;
    }

    public void setShouhuoArea(String shouhuoArea) {
        this.shouhuoArea = shouhuoArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShouhuoren() {
        return shouhuoren;
    }

    public void setShouhuoren(String shouhuoren) {
        this.shouhuoren = shouhuoren;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    @Override
    public String toString() {
        return "ShouhuoAddress{" +
                "id='" + id + '\'' +
                ", shouhuoren='" + shouhuoren + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", defaultAddress='" + defaultAddress + '\'' +
                '}';
    }
}
