package com.gloiot.hygoSupply.ui.activity.yunyinghuodong;

import java.io.Serializable;

/**
 * Created by  ZhangKai on 2016/8/19 15:12
 * 订单实体类
 */
public class XuanZeQuYuBean implements Serializable {
    private String diming;
    private String id;
    private boolean is_select = false;


    public String getDiming() {
        return diming;
    }

    public void setDiming(String diming) {
        this.diming = diming;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
