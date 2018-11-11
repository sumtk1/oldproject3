package com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model;

import java.io.Serializable;

/**
 * Created by LinKorea on 2017/7/14.
 */

public class ImageTextModel implements Serializable {
    private String neirong;
    private String tupian;

    public ImageTextModel(String neirong, String tupian) {
        this.neirong = neirong;
        this.tupian = tupian;
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian;
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    @Override
    public String toString() {
        return "ImageTextModel{" +
                "neirong='" + neirong + '\'' +
                ", tupian='" + tupian + '\'' +
                '}';
    }

}
