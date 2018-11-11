package com.gloiot.hygoSupply.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2016/9/5.
 */
public class WuliuBean implements Serializable {
    private String time;
    private String context;
    private String ftime;
    private Boolean isFirst;

    public WuliuBean(){};

    public WuliuBean(String time, String context, String ftime) {
        this.time = time;
        this.context = context;
        this.ftime = ftime;
        this.isFirst=false;
    }

    public Boolean getFirst() {
        return isFirst;
    }

    public void setFirst(Boolean first) {
        isFirst = first;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    @Override
    public String toString() {
        return "WuliuBean{" +
                "time='" + time + '\'' +
                ", context='" + context + '\'' +
                ", ftime='" + ftime + '\'' +
                '}';
    }
}
