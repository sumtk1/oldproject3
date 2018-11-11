package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品信息页
 */


public class UniformPricePaster extends PasterImpl {
    private PromotionalModel promotionalModel;


    public UniformPricePaster(Context context, Paster paster, PromotionalModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_uniform_price);
        if (pasterView != null) {
            parent.addView(pasterView);
            TextView tv_promotional_price = (TextView) pasterView.findViewById(R.id.tv_promotional_price);
            final TextView tv_promotional_uniform_price = (TextView) pasterView.findViewById(R.id.tv_promotional_uniform_price);
            final TextView tv_promotional_uniform_num = (TextView) pasterView.findViewById(R.id.tv_promotional_uniform_num);
            ImageView iv_promotional_price_add = (ImageView) pasterView.findViewById(R.id.iv_promotional_price_add);
            ImageView iv_promotional_price_sub = (ImageView) pasterView.findViewById(R.id.iv_promotional_price_sub);
            ImageView iv_promotional_num_add = (ImageView) pasterView.findViewById(R.id.iv_promotional_num_add);
            ImageView iv_promotional_num_sub = (ImageView) pasterView.findViewById(R.id.iv_promotional_num_sub);


            tv_promotional_price.setText(promotionalModel.getProcuctPrice());
            tv_promotional_uniform_price.setText(promotionalModel.getUniform_price());
            tv_promotional_uniform_num.setText(promotionalModel.getUniform_num());


            iv_promotional_price_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPriceAdd(tv_promotional_uniform_price);
                }
            });
            iv_promotional_price_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPriceSub(tv_promotional_uniform_price);
                }
            });
            iv_promotional_num_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumAdd(tv_promotional_uniform_num);
                }
            });
            iv_promotional_num_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumSub(tv_promotional_uniform_num);
                }
            });


        }
    }


    private void setPriceAdd(TextView tv_promotional_uniform_price) {
        double uniformPrice = Double.parseDouble(promotionalModel.getUniform_price());
        double price = Double.parseDouble(promotionalModel.getProcuctPrice());
        if (uniformPrice < price) {
            uniformPrice += 1;
            tv_promotional_uniform_price.setText(uniformPrice + "");
            promotionalModel.setUniform_price(uniformPrice + "");
        }
    }

    private void setPriceSub(TextView tv_promotional_uniform_price) {
        double uniformPrice = Double.parseDouble(promotionalModel.getUniform_price());
        double price = Double.parseDouble(promotionalModel.getProcuctPrice());
        if ((uniformPrice - 1) > 0) {
            uniformPrice -= 1;
            tv_promotional_uniform_price.setText(uniformPrice + "");
            promotionalModel.setUniform_price(uniformPrice + "");
        }
    }


    private void setNumSub(TextView tv_promotional_uniform_num) {
        double uniformNum = Double.parseDouble(promotionalModel.getUniform_num());
        if (uniformNum > 1 && uniformNum < 10000) {
            uniformNum -= 1;
            tv_promotional_uniform_num.setText(uniformNum + "");
            promotionalModel.setUniform_num(uniformNum + "");
        }
    }

    private void setNumAdd(TextView tv_promotional_uniform_num) {
        double uniformNum = Double.parseDouble(promotionalModel.getUniform_num());
        if (uniformNum > 1 && uniformNum < 10000) {
            uniformNum += 1;
            tv_promotional_uniform_num.setText(uniformNum + "");
            promotionalModel.setUniform_num(uniformNum + "");
        }
    }


}
