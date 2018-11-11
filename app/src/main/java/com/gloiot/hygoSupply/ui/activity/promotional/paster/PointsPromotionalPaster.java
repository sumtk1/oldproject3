package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.XuanZeiShanagPingLianJIeActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import butterknife.Bind;

import static com.gloiot.hygoSupply.R.id.tv_promotional_percent;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：积分抵扣活动页
 */


public class PointsPromotionalPaster extends PasterImpl {
    private PromotionalModel promotionalModel;


    public PointsPromotionalPaster(Context context, Paster paster, PromotionalModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_points);
        if (pasterView != null) {
            parent.addView(pasterView);
            final EditText et_promotional_percent = (EditText) pasterView.findViewById(R.id.et_promotional_percent);
            TextView tv_promotional_price = (TextView) pasterView.findViewById(R.id.tv_promotional_price);
            TextView tv_promotional_explain = (TextView) pasterView.findViewById(R.id.tv_promotional_explain);
            et_promotional_percent.setText(promotionalModel.getDeductionPercent());
            tv_promotional_price.setText(promotionalModel.getProcuctPrice());
            tv_promotional_explain.setText(promotionalModel.getPointsExplain());
            et_promotional_percent.setHint(promotionalModel.getNormalDeductionPercent());
            et_promotional_percent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //第一位为零，后面只能输入点
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            et_promotional_percent.setText(s.subSequence(0, 1));
                            et_promotional_percent.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    promotionalModel.setDeductionPercent(s.toString());
                }
            });
        }
    }

}

