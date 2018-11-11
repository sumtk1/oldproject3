package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.ChakanWuliuActicity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.MToast;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class ExpressAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;

    public ExpressAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public ExpressAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商家信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_after_sale_express);
        if (null != attachView) {
            TextView status = (TextView) attachView.findViewById(R.id.tv_after_sale_status);
            TextView info = (TextView) attachView.findViewById(R.id.tv_after_sale_info);
            TextView time = (TextView) attachView.findViewById(R.id.tv_after_sale_time);
            final TextView check = (TextView) attachView.findViewById(R.id.tv_after_sale_check);
            if (null != afterSaleModel) {
                if (!TextUtils.isEmpty(afterSaleModel.getExpressCompany()) && !TextUtils.isEmpty(afterSaleModel.getExpressId())) {
                    info.setText(afterSaleModel.getExpressCompany() + ":  " + afterSaleModel.getExpressId());
                    time.setText(afterSaleModel.getExpressTime());
                    status.setText(afterSaleModel.getExpressStatus());
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChakanWuliuActicity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", afterSaleModel.getOrderId());
                            bundle.putString("saleId", afterSaleModel.getId());
                            bundle.putString("suolvtu", afterSaleModel.getProductImg());
                            bundle.putString("shangpinId", afterSaleModel.getProductId());
                            bundle.putString("state", "售后");
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    parent.addView(attachView);
                }
//                if (afterSaleModel.getStatus().contains("待确认收货")) {
//                    info.setText(afterSaleModel.getExpressCompany() + ":  " + afterSaleModel.getExpressId());
//                    time.setText(afterSaleModel.getExpressTime());
//                    check.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            MToastUtils.showToast("点击物流");
//                        }
//                    });
//                    parent.addView(attachView);
//                } else if ("退款成功".equals(afterSaleModel.getStatus()) && "退款退货".equals(afterSaleModel.getAfterSaleType())) {
//                    info.setText(afterSaleModel.getExpressCompany() + ":  " + afterSaleModel.getExpressId());
//                    time.setText(afterSaleModel.getExpressTime());
//                    check.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            MToastUtils.showToast("点击物流");
//                        }
//                    });
//                    parent.addView(attachView);
//                }
            }
        }
    }
}
