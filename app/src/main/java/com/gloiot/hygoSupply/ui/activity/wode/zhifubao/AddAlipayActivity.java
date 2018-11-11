package com.gloiot.hygoSupply.ui.activity.wode.zhifubao;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class AddAlipayActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_add_alipay_name)
    TextView tvAddAlipayName;
    @Bind(R.id.et_add_alipay_zhanghao)
    EditText etAddAlipayZhanghao;

    @Override
    public int initResource() {
        return R.layout.activity_add_alipay;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "添加支付宝账户");
        tvAddAlipayName.setText(preferences.getString(ConstantUtils.SP_USERNAME, ""));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

        L.e("requestSuccess: " + requestTag, response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_shop_aliacc_edit:
                MToast.showToast("添加支付宝成功");
                finish();
                break;
            default:
        }
    }


    @OnClick({R.id.btn_add_alipay_confirm})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_alipay_confirm:
                if (etAddAlipayZhanghao.getText().toString().trim().length() == 0) {
                    MToast.showToast("请填写支付宝账号");
                } else {
                    requestHandleArrayList.add(requestAction.shop_aliacc_edit(AddAlipayActivity.this, phone, "add",
                            "", etAddAlipayZhanghao.getText().toString().replaceAll(" ", ""),
                            tvAddAlipayName.getText().toString(), "", preferences.getString(ConstantUtils.SP_ONLYID, "")));

                }
                break;
            default:
        }
    }
}
