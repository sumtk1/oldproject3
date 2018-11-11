package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.XuanZeiShanagPingLianJIeActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：店铺推广商品信息页
 */


public class ShopProductPaster extends PasterImpl {
    private ShopPromotionModel promotionalModel;
    public static final int REQUESTCODE = 60000;//请求码，尽量确保唯一

    public ShopProductPaster(Context context, Paster paster, ShopPromotionModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_product);
        if (pasterView != null) {
            parent.addView(pasterView);
            TextView tv_promotional_waring = (TextView) pasterView.findViewById(R.id.tv_promotional_waring);
            ImageView iv_promotional_addimg = (ImageView) pasterView.findViewById(R.id.iv_promotional_addimg);
            ImageView iv_promotional_product = (ImageView) pasterView.findViewById(R.id.iv_promotional_product);
            TextView tv_promotional_describe = (TextView) pasterView.findViewById(R.id.tv_promotional_describe);
            FrameLayout fl_promotional_display = (FrameLayout) pasterView.findViewById(R.id.fl_promotional_display);

            if (promotionalModel.isChoicedProduct()) {    //已经选择了商品
                tv_promotional_waring.setVisibility(View.GONE);
                iv_promotional_addimg.setVisibility(View.GONE);
                fl_promotional_display.setVisibility(View.VISIBLE);
                CommonUtils.setDisplayImage(iv_promotional_product, promotionalModel.getProductImage(), 0, 0);
                tv_promotional_describe.setText(promotionalModel.getProductDescribe());
                iv_promotional_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, XuanZeiShanagPingLianJIeActivity.class);
                        intent.putExtra("type", "shopPromotion");
                        ((Activity) context).startActivityForResult(intent, REQUESTCODE);
                    }
                });
            } else {//未选择商品
                tv_promotional_waring.setVisibility(View.VISIBLE);
                iv_promotional_addimg.setVisibility(View.VISIBLE);
                fl_promotional_display.setVisibility(View.GONE);
                iv_promotional_addimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, XuanZeiShanagPingLianJIeActivity.class);
                        intent.putExtra("type", "shopPromotion");
                        ((Activity) context).startActivityForResult(intent, REQUESTCODE);
                    }
                });
            }
        }
    }
}
