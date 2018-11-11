package com.gloiot.hygoSupply.bean;

/**
 * Created by hygo02 on 2016/7/20 18:02
 * 供货商实体类
 */
public class GongHuoBean {
    private String id = "";
    private String name ="";
    private String jiamengfei ="";
    private Boolean ischeck ;
    private String shuoming ="";

    public GongHuoBean() {

    }

    public GongHuoBean(String id, String name, String jiamengfei, String shuoming, Boolean ischeck) {
        this.id = id;
        this.name = name;
        this.jiamengfei = jiamengfei;
        this.ischeck = ischeck;
        this.shuoming = shuoming;
    }

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

    public String getJiamengfei() {
        return jiamengfei;
    }

    public void setJiamengfei(String jiamengfei) {
        this.jiamengfei = jiamengfei;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getShuoming() {
        return shuoming;
    }

    public void setShuoming(String shuoming) {
        this.shuoming = shuoming;
    }
}
