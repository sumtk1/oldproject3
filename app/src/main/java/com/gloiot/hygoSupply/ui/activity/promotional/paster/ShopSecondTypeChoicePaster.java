package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品推广二级类别
 */


public class ShopSecondTypeChoicePaster extends PasterImpl {
    private ShopPromotionModel promotionalModel;
    private int position = 1;

    public ShopSecondTypeChoicePaster(Context context, Paster paster, ShopPromotionModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_choice);
        if (pasterView != null) {
            parent.addView(pasterView);
            final TextView tv_promotional_type_choice = (TextView) pasterView.findViewById(R.id.tv_promotional_type_choice);
            final TextView tv_promotional_type_choice_txt = (TextView) pasterView.findViewById(R.id.tv_promotional_type_choice_txt);
            RelativeLayout rl_promotional_type_choice = (RelativeLayout) pasterView.findViewById(R.id.rl_promotional_type_choice);
            if (TextUtils.isEmpty(promotionalModel.getType())) {
                rl_promotional_type_choice.setVisibility(View.GONE);
            } else {
                rl_promotional_type_choice.setVisibility(View.VISIBLE);
                tv_promotional_type_choice_txt.setText(promotionalModel.getType());
            }

            if (!TextUtils.isEmpty(promotionalModel.getSecondType())) {
                tv_promotional_type_choice.setText(promotionalModel.getSecondType());
            }
            final String title;
            final String[] type;
            if ("轮播图".equals(promotionalModel.getType())) {
                type = promotionalModel.getType2();
                title = "选择轮播图位置";
            } else {
                type = promotionalModel.getType1();
                title = "选择推荐商品位置";
            }
            tv_promotional_type_choice.setHint(title);
            tv_promotional_type_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (type != null) {
                        DialogUtlis.towBtnLoopView(context, title, type, position, new MyDialogBuilder.LoopViewCallBack() {
                            @Override
                            public void callBack(int data) {
                                position = data;
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tv_promotional_type_choice.setText(type[position]);
                                promotionalModel.setSecondType(type[position]);
                                promotionalModel.setPrice(promotionalModel.getPriceInfo().get(type[position]));
                                promotionalModel.setTotal(promotionalModel.getPrice() * promotionalModel.getTime());
                                DialogUtlis.dismissDialog();
                                promotionalModel.getUpdataPage().updataPage("");
                            }
                        });
                    }
                }
            });
        }
    }


}
