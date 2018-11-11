package com.gloiot.hygoSupply.bean;

/**
 * Created by hygo03 on 2017/4/10.
 */

public class TiemData implements Comparable<TiemData> {
    private Integer data;
    private String riqi;
    private String num;

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TiemData{" +
                "data=" + data +
                ", riqi='" + riqi + '\'' +
                ", num='" + num + '\'' +
                '}';
    }

    @Override
    public int compareTo(TiemData tiemData) {
        return this.getData()-tiemData.getData();
    }
}
