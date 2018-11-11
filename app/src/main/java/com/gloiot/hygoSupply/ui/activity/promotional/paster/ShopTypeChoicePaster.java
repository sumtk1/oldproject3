package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品推广类别
 */


public class ShopTypeChoicePaster extends PasterImpl {
    private ShopPromotionModel promotionalModel;
    private String[] type = new String[]{"轮播图", "推荐商品"};
    private int position = 1;

    public ShopTypeChoicePaster(Context context, Paster paster, ShopPromotionModel promotionalModel) {
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
            if (!TextUtils.isEmpty(promotionalModel.getType())) {
                tv_promotional_type_choice.setText(promotionalModel.getType());
            }
            tv_promotional_type_choice_txt.setText("类别");
            tv_promotional_type_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type != null) {
                        DialogUtlis.towBtnLoopView(context, "选择类别", type, position, new MyDialogBuilder.LoopViewCallBack() {
                            @Override
                            public void callBack(int data) {
                                position = data;
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tv_promotional_type_choice.setText(type[position]);
                                String oldType = promotionalModel.getType();
                                if(!oldType.equals(type[position])){
                                    promotionalModel.setType(type[position]);
                                    promotionalModel.setSecondType("");
                                    promotionalModel.setTime(0);
                                    promotionalModel.setPrice(0.0);
                                    promotionalModel.setTotal(0.0);
                                }
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
