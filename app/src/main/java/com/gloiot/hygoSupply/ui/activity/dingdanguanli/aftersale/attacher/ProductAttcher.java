package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;
import com.gloiot.hygoSupply.ui.activity.wode.shouyi.GainRecoredAdapter;
import com.gloiot.hygoSupply.utlis.CommonUtils;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class ProductAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;
    private GainRecoredAdapter adapter;

    public ProductAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public ProductAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商品信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_aftersale_product);
        if (null != attachView) {
            parent.addView(attachView);
            TextView name = (TextView) attachView.findViewById(R.id.tv_aftersale_product_name);
            TextView size = (TextView) attachView.findViewById(R.id.tv_aftersale_product_size);
            TextView num = (TextView) attachView.findViewById(R.id.tv_aftersale_product_num);
            TextView reason = (TextView) attachView.findViewById(R.id.tv_aftersale_product_reason);
            TextView money = (TextView) attachView.findViewById(R.id.tv_aftersale_product_money);
            TextView time = (TextView) attachView.findViewById(R.id.tv_aftersale_product_time);
            TextView orderId = (TextView) attachView.findViewById(R.id.tv_aftersale_product_orderid);
            TextView explain = (TextView) attachView.findViewById(R.id.tv_aftersale_product_explain);
            ImageView image = (ImageView) attachView.findViewById(R.id.iv_aftersale_product_image);
            TextView voucherTextView = (TextView) attachView.findViewById(R.id.tv_after_sale_voucher);
            RecyclerView voucherRecyclerView = (RecyclerView) attachView.findViewById(R.id.rv_after_sale_voucher);

            if (null != afterSaleModel) {
                name.setText(afterSaleModel.getName());
                size.setText(afterSaleModel.getSize());
                num.setText(afterSaleModel.getNum());
                reason.setText(afterSaleModel.getReason());
                money.setText("退款金额:    " + afterSaleModel.getMoney());
                time.setText(afterSaleModel.getTime());
                orderId.setText("订单编号:    " + afterSaleModel.getOrderId());
                explain.setText(afterSaleModel.getExplain());
                CommonUtils.setDisplayImage(image, afterSaleModel.getProductImg(), 0, 0);
                if (afterSaleModel.getVoucherImages().size() > 0) {
                    adapter = new GainRecoredAdapter(context, afterSaleModel.getVoucherImages());
                    LinearLayoutManager ms = new LinearLayoutManager(context);
                    ms.setOrientation(LinearLayoutManager.HORIZONTAL);
                    voucherRecyclerView.setLayoutManager(ms);
                    voucherRecyclerView.setAdapter(adapter);
                } else {
                    voucherRecyclerView.setVisibility(View.GONE);
                    voucherTextView.setVisibility(View.GONE);
                }

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShangpinDianpuActivity.class);
                        intent.putExtra("id", afterSaleModel.getProductId());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
