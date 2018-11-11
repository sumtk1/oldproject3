package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：Paster实现类
 */


public class PasterImpl implements Paster {
    public Paster paster;
    public Context context;

    public PasterImpl() {
    }


    public PasterImpl(Context context, Paster paster) {
        this.context = context;
        this.paster = paster;
    }


    @Override
    public void past(ViewGroup parent) {
        if (parent != null) {
            paster.past(parent);
        }
    }

    public View getView(int resId) {
        if (context == null) {
            return null;
        } else {
            return LayoutInflater.from(context).inflate(resId, null, true);
        }
    }
}
