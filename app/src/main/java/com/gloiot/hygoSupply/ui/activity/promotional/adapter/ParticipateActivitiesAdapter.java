package com.gloiot.hygoSupply.ui.activity.promotional.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleOrderModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ParticipateActivitiesModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ImageSpanUtil;

import java.util.List;

/**
 * 作者：Ljy on 2017/9/20.
 * 功能：折扣--adapter
 */


public class ParticipateActivitiesAdapter extends BaseQuickAdapter<ParticipateActivitiesModel, BaseViewHolder> {

    public ParticipateActivitiesAdapter(@LayoutRes int layoutResId, @Nullable List<ParticipateActivitiesModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ParticipateActivitiesModel item) {
        helper.setText(R.id.tv_item_participate_activitys_name, item.getProductDescribe());
        helper.setText(R.id.tv_item_participate_activitys_price, "￥" + item.getProcuctPrice());
        helper.setText(R.id.tv_item_participate_activitys_point, item.getDeductionPercent() + "%");
        ImageView productImg = helper.getView(R.id.iv_item_participate_activitys_image);
        ImageView typeImg = helper.getView(R.id.iv_item_participate_activitys_type);
        CommonUtils.setDisplayImageOptions(productImg, item.getProductImage(), 0);
        helper.addOnClickListener(R.id.tv_item_participate_activitys_edit);
        helper.addOnClickListener(R.id.tv_item_participate_activitys_delete);
    }
}
