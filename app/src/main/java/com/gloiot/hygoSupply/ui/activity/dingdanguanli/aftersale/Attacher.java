package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygoSupply.R;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：售后——AttachView实现类
 */


public class Attacher implements AttachView {

    protected AttachView attacher;
    protected Context context;

    public Attacher(AttachView attacher, Context context) {
        this.attacher = attacher;
        this.context = context;
    }

    @Override
    public void attach(ViewGroup parent) {
        attacher.attach(parent);
    }

    public View getView(int resId) {
        return LayoutInflater.from(context).inflate(resId, null, true);
    }
}
