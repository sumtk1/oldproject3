package com.gloiot.hygoSupply.ui.activity.promotional.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionRecordModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/9/20.
 * 功能：折扣--adapter
 */


public class ShopPromotionRecordAdapter extends BaseQuickAdapter<ShopPromotionRecordModel, BaseViewHolder> {

    public ShopPromotionRecordAdapter(@LayoutRes int layoutResId, @Nullable List<ShopPromotionRecordModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopPromotionRecordModel item) {
        helper.setText(R.id.tv_item_shop_promotion_record_name, item.getProductDescribe());
        helper.setText(R.id.tv_item_shop_promotion_record_unitprice, item.getPrice() + "/天");
        helper.setText(R.id.tv_item_shop_promotion_record_time, "×" + item.getTime());
        helper.setText(R.id.tv_item_shop_promotion_record_paytype, "支付账户:" + item.getPayType());
        helper.setText(R.id.tv_item_shop_promotion_record_paytime, "下单时间:" + item.getRecordTime());
        ImageView productImg = helper.getView(R.id.iv_item_shop_promotion_record_image);
        ImageView typeImg = helper.getView(R.id.iv_item_shop_promotion_record_type);
        CommonUtils.setDisplayImageOptions(productImg, item.getProductImage(), 0);
        if ("轮播图".equals(item.getType())) {
            CommonUtils.setDisplayImage(typeImg, "", 0, R.mipmap.ic_shop_promotion_slideshow);
        } else if ("推荐商品".equals(item.getType())) {
            CommonUtils.setDisplayImage(typeImg, "", 0, R.mipmap.ic_shop_promotion_product);
        }
    }
}
