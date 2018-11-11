package com.gloiot.chatsdk.chatui.keyboard.extend;

/**
 * 输入键盘加号扩展
 */
public class ExtendBean {

    private int id;
    private int icon; // 图标
    private String funcName; // 名称

    public int getIcon() {
        return icon;
    }

    public String getFuncName() {
        return funcName;
    }

    public int getId() {
        return id;
    }

    public ExtendBean(int icon, String funcName){
        this.icon = icon;
        this.funcName = funcName;
    }
}
