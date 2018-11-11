package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.web.WebToPayManager;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ljz
 *         <p>
 *         描述：充值界面
 */
public class ChongZhiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_cz_jnzh)
    TextView tvCzJnzh;          // 充值账户
    @Bind(R.id.et_cz_money)
    EditText etCzMoney;         // 充值金额

    @Override
    public int initResource() {
        return R.layout.activity_chongzhi;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "充值", null);
        tvCzJnzh.setText("（￥"+ getIntent().getStringExtra("money")+"）");
    }

    @OnClick({R.id.btn_cz})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cz:
                String czMoney = etCzMoney.getText().toString();
                if (TextUtils.isEmpty(czMoney)){
                    MToast.showToast("请输入充值金额");
                } else if ("0".equals(czMoney)){
                    MToast.showToast("请输入大于零的整数");
                } else {
                    requestHandleArrayList.add(requestAction.shop_chongzhi(this, preferences.getString(ConstantUtils.SP_MYID, ""), czMoney));
                }
                break;
            default:
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_chongzhi:
                startActivity(WebToPayManager.toCashier(preferences, mContext, response));
                finish();
                break;
            default:
        }
    }
}
