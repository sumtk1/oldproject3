package com.gloiot.hygoSupply.ui.activity.promotional.factory;

import android.content.Context;

import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.Paster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.PointsPromotionalPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ProductPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.TimePaster;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：活动工厂，活动类型-----积分抵扣
 */


public class PointPromotionalFactory extends PasterFactory<PromotionalModel> {

    @Override
    public Paster create(Context context, Paster paster, PromotionalModel promotionalModel) {
        if (paster != null && promotionalModel != null) {
            paster = new ProductPaster(context, paster, promotionalModel);
            paster = new PointsPromotionalPaster(context, paster, promotionalModel);
        }
        return paster;
    }


}
