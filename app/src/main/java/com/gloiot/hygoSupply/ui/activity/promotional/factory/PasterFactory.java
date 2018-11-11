package com.gloiot.hygoSupply.ui.activity.promotional.factory;

import android.content.Context;

import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.Paster;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：我的——我的资料
 */


public abstract class PasterFactory<T> {
    public abstract Paster create(Context context, Paster paster, T promotionalModel);
}
