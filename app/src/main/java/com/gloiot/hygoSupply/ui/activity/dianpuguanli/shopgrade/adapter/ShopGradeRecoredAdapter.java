package com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model.ShopGradeRecordModel;

import java.util.List;

/**
 * 作者：Ljy on 2017/10/11.
 * 功能：我的——我的资料
 */


public class ShopGradeRecoredAdapter extends BaseQuickAdapter<ShopGradeRecordModel, BaseViewHolder> {
    public ShopGradeRecoredAdapter(@LayoutRes int layoutResId, @Nullable List<ShopGradeRecordModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopGradeRecordModel item) {
        helper.setText(R.id.tv_grade_record_time, item.getTime());
        helper.setText(R.id.tv_grade_record_point, item.getPoint());
    }
}
