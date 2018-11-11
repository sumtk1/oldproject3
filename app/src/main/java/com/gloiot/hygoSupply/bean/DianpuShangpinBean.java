package com.gloiot.hygoSupply.bean;

/**
 * Created by 13939 on 2016/9/5.
 */
public class DianpuShangpinBean {
    private String id;
    private String suolvetu;
    private String shangpinmingcheng;
    private String Danjia;
    private boolean quanqiugou;

    public DianpuShangpinBean(String id, String suolvetu, String shangpinmingcheng, String danjia) {
        this.id = id;
        this.suolvetu = suolvetu;
        this.shangpinmingcheng = shangpinmingcheng;
        Danjia = danjia;
    }

    public DianpuShangpinBean() {
    }

    public boolean isQuanqiugou() {
        return quanqiugou;
    }

    public void setQuanqiugou(boolean quanqiugou) {
        this.quanqiugou = quanqiugou;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuolvetu() {
        return suolvetu;
    }

    public void setSuolvetu(String suolvetu) {
        this.suolvetu = suolvetu;
    }

    public String getShangpinmingcheng() {
        return shangpinmingcheng;
    }

    public void setShangpinmingcheng(String shangpinmingcheng) {
        this.shangpinmingcheng = shangpinmingcheng;
    }

    public String getDanjia() {
        return Danjia;
    }

    public void setDanjia(String danjia) {
        Danjia = danjia;
    }

    @Override
    public String toString() {
        return "DianpuShangpinBean{" +
                "id='" + id + '\'' +
                ", suolvetu='" + suolvetu + '\'' +
                ", shangpinmingcheng='" + shangpinmingcheng + '\'' +
                ", Danjia='" + Danjia + '\'' +
                '}';
    }
}
