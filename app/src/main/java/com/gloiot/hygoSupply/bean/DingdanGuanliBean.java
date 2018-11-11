package com.gloiot.hygoSupply.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13939 on 2016/11/3.
 */

public class DingdanGuanliBean implements Serializable {
    private String 订单id;
    private String 订单编号;
    private String 状态;
    private String 店铺名;
    private String 收货人;
    private String 收货人手机号;
    private String 收货地址;
    private String 快递费;
    private String 合计;
    private String 数量;
    private String 退款人帐号;
    private String count_down;
    private String 昵称;
    private boolean offline;


    private List<String> shouhouList = new ArrayList<>();

    public List<String> getShouhouList() {
        return shouhouList;
    }

    public void setShouhouList(List<String> shouhouList) {
        this.shouhouList = shouhouList;
    }

    private String reviewTime = "";
    private int unshippednNum;//未发货数量

    public int getUnshippednNum() {
        return unshippednNum;
    }

    public void setUnshippednNum(int unshippednNum) {
        this.unshippednNum = unshippednNum;
    }

    public String getCount_down() {
        return count_down;
    }

    public void setCount_down(String count_down) {
        this.count_down = count_down;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String get退款人帐号() {
        return 退款人帐号;
    }

    public void set退款人帐号(String 退款人帐号) {
        this.退款人帐号 = 退款人帐号;
    }

    public String get昵称() {
        return 昵称;
    }

    public void set昵称(String 昵称) {
        this.昵称 = 昵称;
    }

    public String getShouhou_status() {
        return shouhou_status;
    }

    public void setShouhou_status(String shouhou_status) {
        this.shouhou_status = shouhou_status;
    }

    private String shouhou_status = "";

    public String get数量() {
        return 数量;
    }

    public void set数量(String 数量) {
        this.数量 = 数量;
    }

    public String get合计() {
        return 合计;
    }

    public void set合计(String 合计) {
        this.合计 = 合计;
    }

    public boolean isNeedWuliu() {
        return isNeedWuliu;
    }

    public void setNeedWuliu(boolean needWuliu) {
        isNeedWuliu = needWuliu;
    }

    private boolean isNeedWuliu;

    public String get快递费() {
        return 快递费;
    }

    public void set快递费(String 快递费) {
        this.快递费 = 快递费;
    }


    private ArrayList<DingdanGuanliShangpinBean> 订单管理商品集合;

    public DingdanGuanliBean() {
    }

    public DingdanGuanliBean(String 订单id, String 订单编号, String 状态, String 店铺名, String 收货人, String 收货人手机号, String 收货地址, ArrayList<DingdanGuanliShangpinBean> 订单管理商品集合) {
        this.订单id = 订单id;
        this.订单编号 = 订单编号;
        this.状态 = 状态;
        this.店铺名 = 店铺名;
        this.收货人 = 收货人;
        this.收货人手机号 = 收货人手机号;
        this.收货地址 = 收货地址;
        this.订单管理商品集合 = 订单管理商品集合;
    }

    public String get订单id() {
        return 订单id;
    }

    public void set订单id(String 订单id) {
        this.订单id = 订单id;
    }

    public String get订单编号() {
        return 订单编号;
    }

    public void set订单编号(String 订单编号) {
        this.订单编号 = 订单编号;
    }

    public String get状态() {
        return 状态;
    }

    public void set状态(String 状态) {
        this.状态 = 状态;
    }

    public String get店铺名() {
        return 店铺名;
    }

    public void set店铺名(String 店铺名) {
        this.店铺名 = 店铺名;
    }

    public String get收货人() {
        return 收货人;
    }

    public void set收货人(String 收货人) {
        this.收货人 = 收货人;
    }

    public String get收货人手机号() {
        return 收货人手机号;
    }

    public void set收货人手机号(String 收货人手机号) {
        this.收货人手机号 = 收货人手机号;
    }

    public String get收货地址() {
        return 收货地址;
    }

    public void set收货地址(String 收货地址) {
        this.收货地址 = 收货地址;
    }

    public ArrayList<DingdanGuanliShangpinBean> get订单管理商品集合() {
        return 订单管理商品集合;
    }

    public void set订单管理商品集合(ArrayList<DingdanGuanliShangpinBean> 订单管理商品集合) {
        this.订单管理商品集合 = 订单管理商品集合;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @Override
    public String toString() {
        return "DingdanGuanliBean{" +
                "订单id='" + 订单id + '\'' +
                ", 订单编号='" + 订单编号 + '\'' +
                ", 状态='" + 状态 + '\'' +
                ", 店铺名='" + 店铺名 + '\'' +
                ", 收货人='" + 收货人 + '\'' +
                ", 收货人手机号='" + 收货人手机号 + '\'' +
                ", 收货地址='" + 收货地址 + '\'' +
                ", 快递费='" + 快递费 + '\'' +
                ", 合计='" + 合计 + '\'' +
                ", 数量='" + 数量 + '\'' +
                ", 帐号='" + 退款人帐号 + '\'' +
                ", 昵称='" + 昵称 + '\'' +
                ", isNeedWuliu=" + isNeedWuliu +
                ", 订单管理商品集合=" + 订单管理商品集合 +
                '}';
    }
}
