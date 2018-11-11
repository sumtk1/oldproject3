package com.gloiot.hygoSupply.bean;

import java.util.List;

/**
 * 创建者 zengming.
 * 功能：生活模块数据模板
 */
public class Life {

    public final static int SHOWTYPE_0 = 0;
    public final static int SHOWTYPE_1 = 1;
    public final static int SHOWTYPE_2 = 2;

    // 显示类型
    private int showType;
    // 图标
    private String icon;
    // 标题
    private String title;
    // 说明
    private String explain;
    // 图片、跳转网址集合
    private List<String[]> btnLists;

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public List<String[]> getBtnLists() {
        return btnLists;
    }

    public void setBtnLists(List<String[]> btnLists) {
        this.btnLists = btnLists;
    }
}
