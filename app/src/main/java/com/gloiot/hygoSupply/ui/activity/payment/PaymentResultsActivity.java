package com.gloiot.hygoSupply.ui.activity.payment;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 支付结果页面
 */
public class PaymentResultsActivity extends BaseActivity implements View.OnClickListener {

    // 支付状态
    @Bind(R.id.tv_title_pay_status)
    TextView tv_title_pay_status;
    // 支付成功与否的图片
    @Bind(R.id.iv_payee_img)
    ImageView iv_payee_img;
    // 支付结果
    @Bind(R.id.tv_pay_result)
    TextView tv_pay_result;
    // 需支付金额
    @Bind(R.id.tv_need_pay_money)
    TextView tv_need_pay_money;
    // 确定按钮
    @Bind(R.id.btn_bg_click)
    Button btn_bg_click;


    @Override
    public int initResource() {
        return R.layout.activity_payment_results;
    }

    @Override
    public void initData() {
        payResultViewStyle(!getIntent().getStringExtra("money").equals("-1"));
    }


    /**
     * 支付结果样式-true:支付成功，false:支付失败
     */
    public void payResultViewStyle(boolean status) {
        if (status) {
            iv_payee_img.setBackgroundResource(R.mipmap.ic_shiming_correct);
            tv_title_pay_status.setText("支付成功");
            tv_pay_result.setText("支付成功");
            tv_need_pay_money.setText("￥" + getIntent().getStringExtra("money"));
        } else {
            iv_payee_img.setBackgroundResource(R.mipmap.ic_shiming_failure);
            tv_title_pay_status.setText("支付失败");
            tv_pay_result.setText("支付失败");
            tv_need_pay_money.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.btn_bg_click})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bg_click:
                finish();
                break;
        }
    }


}
