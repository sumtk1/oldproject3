package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品推广类别
 */


public class ShopTimeAndPricePaster extends PasterImpl {
    private ShopPromotionModel promotionalModel;

    public ShopTimeAndPricePaster(Context context, Paster paster, ShopPromotionModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_price_and_time);
        if (pasterView != null) {
            parent.addView(pasterView);

            RelativeLayout rl_promotional_price = (RelativeLayout) pasterView.findViewById(R.id.rl_promotional_price);
            RelativeLayout rl_promotional_time = (RelativeLayout) pasterView.findViewById(R.id.rl_promotional_time);
            RelativeLayout rl_promotional_pay_account = (RelativeLayout) pasterView.findViewById(R.id.rl_promotional_pay_account);
            View divider = pasterView.findViewById(R.id.divider);
            TextView tv_promotional_price = (TextView) pasterView.findViewById(R.id.tv_promotional_price);
            TextView tv_promotional_pay_account = (TextView) pasterView.findViewById(R.id.tv_promotional_pay_account);
            TextView tv_promotional_remark = (TextView) pasterView.findViewById(R.id.tv_promotional_remark);
            EditText et_promotional_time = (EditText) pasterView.findViewById(R.id.et_promotional_time);
            //如果没有选择位置
            if (TextUtils.isEmpty(promotionalModel.getSecondType())) {
                rl_promotional_pay_account.setVisibility(View.GONE);
                rl_promotional_price.setVisibility(View.GONE);
                rl_promotional_time.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            } else {
                rl_promotional_pay_account.setVisibility(View.VISIBLE);
                rl_promotional_price.setVisibility(View.VISIBLE);
                rl_promotional_time.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            }

            tv_promotional_price.setText(promotionalModel.getPrice() + "");
            tv_promotional_pay_account.setText("商家收益（" + promotionalModel.getEarnings() + ")");
            tv_promotional_remark.setText(promotionalModel.getRemark());
            if (promotionalModel.getTime() == 0) {
                et_promotional_time.setText("");
            } else {
                et_promotional_time.setText(promotionalModel.getTime() + "");
            }

            et_promotional_time.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        promotionalModel.setTime(0);
                    } else {
                        promotionalModel.setTime(Integer.parseInt(s.toString()));
                    }
                    promotionalModel.setTotal(promotionalModel.getPrice() * promotionalModel.getTime());
                    promotionalModel.getUpdataPage().updataPage("onlyTotal");
                }
            });


        }
    }


}
