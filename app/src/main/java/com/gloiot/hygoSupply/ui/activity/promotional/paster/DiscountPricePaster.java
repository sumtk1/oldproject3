package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品信息页
 */


public class DiscountPricePaster extends PasterImpl {
    private PromotionalModel promotionalModel;


    public DiscountPricePaster(Context context, Paster paster, PromotionalModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_discount_price);
        if (pasterView != null) {
            parent.addView(pasterView);
            TextView tv_promotional_price = (TextView) pasterView.findViewById(R.id.tv_promotional_price);
            final TextView tv_promotional_discount_num = (TextView) pasterView.findViewById(R.id.tv_promotional_discount_num);
            ImageView iv_promotional_num_add = (ImageView) pasterView.findViewById(R.id.iv_promotional_num_add);
            ImageView iv_promotional_num_sub = (ImageView) pasterView.findViewById(R.id.iv_promotional_num_sub);
            EditText et_promotional_discount_one = (EditText) pasterView.findViewById(R.id.et_promotional_discount_one);
            EditText et_promotional_discount_two = (EditText) pasterView.findViewById(R.id.et_promotional_discount_two);
            EditText et_promotional_discount_three = (EditText) pasterView.findViewById(R.id.et_promotional_discount_three);

            tv_promotional_price.setText(promotionalModel.getProcuctPrice());
            tv_promotional_discount_num.setText(promotionalModel.getDiscount_num());

            iv_promotional_num_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumAdd(tv_promotional_discount_num);
                }
            });
            iv_promotional_num_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumSub(tv_promotional_discount_num);
                }
            });
            setTextWatcher(et_promotional_discount_one, 1);
            setTextWatcher(et_promotional_discount_two, 2);
            setTextWatcher(et_promotional_discount_three, 3);
        }
    }


    private void setNumSub(TextView tv_promotional_uniform_num) {
        double uniformNum = Double.parseDouble(promotionalModel.getDiscount_num());
        if (uniformNum > 1 && uniformNum < 10000) {
            uniformNum -= 1;
            tv_promotional_uniform_num.setText(uniformNum + "");
            promotionalModel.setDiscount_num(uniformNum + "");
        }
    }

    private void setNumAdd(TextView tv_promotional_uniform_num) {
        double uniformNum = Double.parseDouble(promotionalModel.getDiscount_num());
        if (uniformNum > 1 && uniformNum < 10000) {
            uniformNum += 1;
            tv_promotional_uniform_num.setText(uniformNum + "");
            promotionalModel.setDiscount_num(uniformNum + "");
        }
    }

    private void setTextWatcher(final EditText editText, final int num) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //第一位为零，后面只能输入点
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (num) {
                    case 1:
                        promotionalModel.setDiscount_price_one(s.toString());
                        break;
                    case 2:
                        promotionalModel.setDiscount_price_two(s.toString());
                        break;
                    case 3:
                        promotionalModel.setDiscount_price_three(s.toString());
                        break;
                }

            }
        });

    }

}
