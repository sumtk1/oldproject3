package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ParticipateActivitiesModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/9/20.
 * 功能：折扣--adapter
 */


public class SystemMessageRecordAdapter extends BaseQuickAdapter<SystemMessageRecordModel, BaseViewHolder> {

    public SystemMessageRecordAdapter(@LayoutRes int layoutResId, @Nullable List<SystemMessageRecordModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageRecordModel item) {
        helper.setText(R.id.tv_system_message_time, item.getTiem());
        helper.setText(R.id.tv_system_message_type, item.getType());
        helper.setText(R.id.tv_system_message_title, item.getTitle());
        ImageView productImg = helper.getView(R.id.iv_system_message_image);
        CommonUtils.setDisplayImage(productImg, "", 0, item.getImage());
    }
}
