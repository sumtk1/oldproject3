package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品信息页
 */


public class TypeChoicePaster extends PasterImpl {
    private PromotionalModel promotionalModel;
    private String[] type = new String[]{"折扣价", "统一价", "单独价"};
    private int position = 1;

    public TypeChoicePaster(Context context, Paster paster, PromotionalModel promotionalModel) {
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
            tv_promotional_type_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type != null) {
                        DialogUtlis.towBtnLoopView(context, "选择活动类别", type, position, new MyDialogBuilder.LoopViewCallBack() {
                            @Override
                            public void callBack(int data) {
                                position = data;
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tv_promotional_type_choice.setText(type[position]);
                                promotionalModel.setType(type[position]);
                                DialogUtlis.dismissDialog();
                            }
                        });
                    }
                }
            });
        }
    }


}
