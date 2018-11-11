package com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/8/12.
 * 功能：发商品model
 */


public class DynamicProductModel {
    private boolean isMulit;//是否多款商品
    private String link = "";//链接（单款）
    private String title = "";//标题
    private String titleImg = "";//主图片（单款）
    private String id = "";//商品id（单款）


    private List<ProductModel> productModelsMulit = new ArrayList<>();
    private List<ProductModel> productModelsSingle = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMulit() {
        return isMulit;
    }

    public void setMulit(boolean mulit) {
        isMulit = mulit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public List<ProductModel> getProductModelsMulit() {
        return productModelsMulit;
    }

    public void setProductModelsMulit(List<ProductModel> productModelsMulit) {
        this.productModelsMulit = productModelsMulit;
    }

    public List<ProductModel> getProductModelsSingle() {
        return productModelsSingle;
    }

    public void setProductModelsSingle(List<ProductModel> productModelsSingle) {
        this.productModelsSingle = productModelsSingle;
    }


    public String check() {
        if (isMulit()) {
            if (TextUtils.isEmpty(title)) {
                return "请填写商品标题";
            } else if (getProductModelsMulit().size() < 3) {
                return "请添加至少三个或三个以上商品";
            } else {
                return "ok";
            }
        } else {
            if (TextUtils.isEmpty(link)) {
                return "请选择商品链接";
            } else if (TextUtils.isEmpty(title)) {
                return "请填写商品标题";
            } else if (TextUtils.isEmpty(titleImg)) {
                return "请添加商品图片";
            } else if (getProductModelsSingle().size() < 1) {
                return "请添加内容";
            } else {
                return "ok";
            }
        }
    }
}
