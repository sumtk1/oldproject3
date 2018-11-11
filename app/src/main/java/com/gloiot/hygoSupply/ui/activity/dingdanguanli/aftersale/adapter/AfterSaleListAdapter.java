package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TongYiTuiHuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleOrderModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ImageSpanUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Ljy on 2017/9/20.
 * 功能：我的——我的资料
 */


public class AfterSaleListAdapter extends BaseQuickAdapter<AfterSaleOrderModel, BaseViewHolder> {

    public AfterSaleListAdapter(@LayoutRes int layoutResId, @Nullable List<AfterSaleOrderModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AfterSaleOrderModel item) {
        helper.setText(R.id.tv_item_after_sale_order_num, "数量：" + item.getProductModel().getNum());
        helper.setText(R.id.tv_item_after_sale_order_price, "合计：￥" + item.getTotalCost());
        helper.setText(R.id.tv_item_after_sale_order_size, item.getProductModel().getSize());
        ImageView iv_deliver = helper.getView(R.id.iv_item_after_sale_order_deliver);
        CommonUtils.setDisplayImageOptions((ImageView) helper.getView(R.id.iv_item_after_sale_order_img), item.getProductModel().getImage(), 4);
        //显示发货状态
        if ("买家已付款".equals(item.getAfterSaleSatus())) {
            iv_deliver.setVisibility(View.VISIBLE);
            CommonUtils.setDisplayImage(iv_deliver, "", 0, R.mipmap.ic_weifahuo);
        } else if ("卖家已发货".equals(item.getAfterSaleSatus())) {
            iv_deliver.setVisibility(View.VISIBLE);
            CommonUtils.setDisplayImage(iv_deliver, "", 0, R.mipmap.ic_yifahuo);
        } else {
            iv_deliver.setVisibility(View.GONE);
        }
        //显示全球购标识
        if (!"全球购".equals(item.getProductModel().getType()) && !"全球购-自营".equals(item.getProductModel().getType())) {
            helper.setText(R.id.tv_item_after_sale_order_product_name, item.getProductModel().getName());
        } else {
            TextView textView = helper.getView(R.id.tv_item_after_sale_order_product_name);
            ImageSpanUtil.getInstance().setImage(mContext, textView, R.mipmap.ic_quanqiugou, item.getProductModel().getName());
        }

        helper.setText(R.id.tv_item_after_sale_order_name, item.getConsignee());

        switch (item.getAfterSaleSatus()) {
            case "请退款":
            case "请处理":
                helper.setText(R.id.btn_after_sale_order_manage, "请处理");
                helper.setGone(R.id.tv_item_after_sale_order_deadline, true);
                helper.setText(R.id.tv_item_after_sale_order_deadline, item.getDeadline());
                helper.setGone(R.id.rl_after_sale_order_manage, true);
                helper.setGone(R.id.tv_item_after_sale_status, false);
                break;
            case "等待卖家收货":
                helper.setText(R.id.btn_after_sale_order_manage, "请确认退款");
                helper.setText(R.id.tv_item_after_sale_order_deadline, item.getDeadline());
                helper.setGone(R.id.tv_item_after_sale_order_deadline, true);
                helper.setGone(R.id.rl_after_sale_order_manage, true);
                helper.setGone(R.id.tv_item_after_sale_status, false);
                break;
            case "退款关闭":
            case "商家拒绝退款申请":
            case "商家拒绝退货申请":
                helper.setText(R.id.tv_item_after_sale_status, "退款关闭");
                helper.setGone(R.id.tv_item_after_sale_status, true);
                helper.setGone(R.id.tv_item_after_sale_order_deadline, false);
                helper.setGone(R.id.rl_after_sale_order_manage, false);
                break;
            case "商家未处理":
                helper.setText(R.id.tv_item_after_sale_status, "超时未处理");
                helper.setGone(R.id.tv_item_after_sale_status, true);
                helper.setGone(R.id.tv_item_after_sale_order_deadline, false);
                helper.setGone(R.id.rl_after_sale_order_manage, false);
                break;
            case "退款成功":
            case "商家已同意退款":
                helper.setText(R.id.tv_item_after_sale_status, "退款成功");
                helper.setGone(R.id.tv_item_after_sale_status, true);
                helper.setGone(R.id.tv_item_after_sale_order_deadline, false);
                helper.setGone(R.id.rl_after_sale_order_manage, false);
                break;
            case "等待买家发货":
                helper.setText(R.id.tv_item_after_sale_status, "等待买家发货");
                helper.setGone(R.id.tv_item_after_sale_status, true);
                helper.setGone(R.id.tv_item_after_sale_order_deadline, false);
                helper.setGone(R.id.rl_after_sale_order_manage, false);
                break;
            default:
                helper.setGone(R.id.tv_item_after_sale_status, false);
                helper.setGone(R.id.tv_item_after_sale_order_deadline, false);
                helper.setGone(R.id.rl_after_sale_order_manage, false);
                break;
        }
        helper.addOnClickListener(R.id.btn_after_sale_order_manage);
    }
}
