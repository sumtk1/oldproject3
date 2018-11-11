package com.gloiot.hygoSupply.ui.activity.promotional.factory;

import android.content.Context;

import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.Paster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.PointsPromotionalPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ProductPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ShopProductPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ShopSecondTypeChoicePaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ShopTimeAndPricePaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ShopTypeChoicePaster;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：活动工厂，活动类型-----积分抵扣
 */


public class ShopPromotionalFactory extends PasterFactory<ShopPromotionModel> {

    @Override
    public Paster create(Context context, Paster paster, ShopPromotionModel promotionalModel) {
        if (paster != null && promotionalModel != null) {
            paster = new ShopProductPaster(context, paster, promotionalModel);
            paster = new ShopTypeChoicePaster(context, paster, promotionalModel);
            paster = new ShopSecondTypeChoicePaster(context, paster, promotionalModel);
            paster = new ShopTimeAndPricePaster(context, paster, promotionalModel);
        }
        return paster;
    }


}
