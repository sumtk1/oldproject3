package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class ResultAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;

    public ResultAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public ResultAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商家信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_after_sale_result);
        if (null != attachView) {
            TextView status = (TextView) attachView.findViewById(R.id.tv_after_sale_status);
            TextView explain = (TextView) attachView.findViewById(R.id.tv_after_sale_explain);
            TextView time = (TextView) attachView.findViewById(R.id.tv_after_sale_time);
            ImageView image = (ImageView) attachView.findViewById(R.id.iv_after_sale_image);
            if (null != afterSaleModel) {
                if (!TextUtils.isEmpty(afterSaleModel.getRefuseReason())) {
                    status.setText("退款关闭");
                    explain.setText("您已拒绝了退款申请");
                    time.setText(afterSaleModel.getResultTime());
                    image.setImageResource(R.mipmap.ic_after_sale_result2);
                    parent.addView(attachView);
                } else if ("退款成功".equals(afterSaleModel.getStatus())) {
                    status.setText("退款成功");
                    explain.setText("已退回￥" + afterSaleModel.getMoney() + "给买家");
                    time.setText(afterSaleModel.getResultTime());
                    image.setImageResource(R.mipmap.ic_after_sale_result1);
                    parent.addView(attachView);
                } else if (afterSaleModel.getStatus().contains("超时未处理")) {
                    status.setText("申请关闭");
                    explain.setText("超时未处理");
                    time.setText(afterSaleModel.getResultTime());
                    image.setImageResource(R.mipmap.ic_after_sale_result2);
                    parent.addView(attachView);
                } else if (afterSaleModel.getStatus().contains("买家已撤销申请")) {
                    status.setText("申请关闭");
                    explain.setText("买家已撤销申请");
                    time.setText(afterSaleModel.getResultTime());
                    image.setImageResource(R.mipmap.ic_after_sale_result2);
                    parent.addView(attachView);
                }
            }
        }
    }
}
