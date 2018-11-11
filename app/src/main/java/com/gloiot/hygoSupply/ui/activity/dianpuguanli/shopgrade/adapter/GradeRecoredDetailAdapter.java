package com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model.GradeRecordDetailModel;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model.ShopGradeRecordModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/10/11.
 * 功能：荣誉分记录adapter
 */


public class GradeRecoredDetailAdapter extends BaseQuickAdapter<GradeRecordDetailModel, BaseViewHolder> {
    public GradeRecoredDetailAdapter(@LayoutRes int layoutResId, @Nullable List<GradeRecordDetailModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GradeRecordDetailModel item) {
        helper.setText(R.id.tv_record_detail_name, item.getName() + "|" + item.getDecribe());
        CommonUtils.setDisplayImage((ImageView) helper.getView(R.id.iv_record_detail_img), item.getImg(), 0, 0);
        //设置评分星星个数
        RatingBar bar = helper.getView(R.id.tv_record_detail_ratingbar);
        bar.setRating(item.getPoint());
    }
}
