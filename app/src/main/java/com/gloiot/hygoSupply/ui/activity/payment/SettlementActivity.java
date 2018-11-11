package com.gloiot.hygoSupply.ui.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.serrver.network.RequestAction;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 结算界面
 */
public class SettlementActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {

    // 收款方的用户图片
    @Bind(R.id.iv_payee_img1)
    ImageView iv_payee_img1;
    // 收款方的用户名字
    @Bind(R.id.tv_payee_name)
    TextView tv_payee_name;
    // 转款金额
    @Bind(R.id.et_amount_collected)
    EditText et_amount_collected;
    // 备注内容
    @Bind(R.id.edit_remarks)
    EditText edit_remarks;
    // 确定按钮
    @Bind(R.id.btn_bg_click1)
    Button btn_bg_click1;
    // 积分账户金额
    @Bind(R.id.tv_settlement_zhanhu)
    TextView tv_settlement_zhanhu;

    private String orderNum = ""; //转款订单号
    private String onlyID = ""; // 收款人onlyID
    private String zhanghuMoney = ""; // 积分账户金额
    private String zhanghu = "";
    private String phone;

    @Override
    public int initResource() {
        return R.layout.activity_settlement;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "转账金額", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        onlyID = getIntent().getStringExtra("onlyID");
        requestHandleArrayList.add(requestAction.ChaoJIShangJiaInfo(SettlementActivity.this, onlyID, phone)); // 获取收款方信息
        setRequestErrorCallback(this);
        CommonUtils.setNumPoint(et_amount_collected, 2);
    }

    Double m1, m2;

    @OnClick({R.id.btn_bg_click1})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bg_click1:
                // 判断如果为空重新请求获取收款方信息
                if (TextUtils.isEmpty(zhanghuMoney) && TextUtils.isEmpty(orderNum)) {
                    requestHandleArrayList.add(requestAction.ChaoJIShangJiaInfo(SettlementActivity.this, onlyID, phone)); // 获取收款方信息
                } else if (TextUtils.isEmpty(et_amount_collected.getText().toString())) {
                    MToast.showToast("请输入转账金额");
                } else if (Double.parseDouble(et_amount_collected.getText().toString()) <= 0) {
                    MToast.showToast("请填写正确金额");
                } else {
                    try {
                        m1 = Double.parseDouble(zhanghuMoney);  // 积分账户余额
                        m2 = Double.parseDouble(et_amount_collected.getText().toString()); // 转账积分
                    } catch (Exception e) {
                        m1 = 0.0;
                        m2 = 0.0;
                    }

                    if (m1 < m2) {
                        MToast.showToast("账户余额不足");
                    } else {
                        DialogUtlis.oneBtnPwd(SettlementActivity.this, m2 + "", new DialogUtlis.PasswordCallback() {
                            @Override
                            public void callback(String data) {
                                requestHandleArrayList.add(requestAction.PayChaoJIShangJia(SettlementActivity.this, onlyID, zhanghu, m2 + "", orderNum, data, edit_remarks.getText().toString(), phone));
                            }
                        });
                    }
                }
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("Success请求回调", response.toString());
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_CHAOJISHANGJIAINFO:
                String name = response.getString("名称");
                String pic = response.getString("头像");
                zhanghuMoney = response.getString("金额");
                zhanghu = response.getString("付款账户");
                orderNum = response.getString("转账订单号");

                tv_settlement_zhanhu.setText(zhanghu + "(" + zhanghuMoney + ")");
                CommonUtils.setDisplayImage(iv_payee_img1, pic, 0, R.mipmap.ic_portrait_default);
                tv_payee_name.setText(name);
                break;
            case ConstantUtils.TAG_PAYCHAOJISHANGJIA:
                Intent it = new Intent(SettlementActivity.this, PaymentResultsActivity.class);
                it.putExtra("money", et_amount_collected.getText().toString());
                startActivity(it);
                finish();
                break;
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        Log.e("Error请求回调", response.toString());
        switch (requestTag) {
            case RequestAction.TAG_PAYCHAOJISHANGJIA:
                String status = response.getString("状态");
                if (status.equals("您输入的密码不正确!")) {
                    DialogUtlis.oneBtnNormal(SettlementActivity.this, "提示", status, "确定", false, null);
                } else {
                    MToast.showToast(status);
                    Intent it = new Intent(SettlementActivity.this, PaymentResultsActivity.class);
                    it.putExtra("money", "-1");
                    startActivity(it);
                    finish();
                }
                break;
            default:
                MToast.showToast(response.getString("状态"));
                break;
        }
    }
}
