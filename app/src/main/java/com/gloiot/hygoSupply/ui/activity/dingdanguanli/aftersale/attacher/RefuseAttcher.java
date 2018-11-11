package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class RefuseAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;

    public RefuseAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public RefuseAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商家信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_after_sale_refuse);
        if (null != attachView) {
            TextView reason = (TextView) attachView.findViewById(R.id.tv_after_sale_reason);
            TextView explain = (TextView) attachView.findViewById(R.id.tv_after_sale_explain);
            if (null != afterSaleModel) {
                Log.e("TAG", "attach: " + afterSaleModel.getRefuseReason());
                if (!TextUtils.isEmpty(afterSaleModel.getRefuseReason())) {
                    reason.setText("拒绝原因:" + afterSaleModel.getRefuseReason());
                    explain.setText("拒绝说明:" + afterSaleModel.getRefuseExplain());
                    parent.addView(attachView);
                }

            }
        }
    }
}
