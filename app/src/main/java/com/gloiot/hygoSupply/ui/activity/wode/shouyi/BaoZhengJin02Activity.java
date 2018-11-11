package com.gloiot.hygoSupply.ui.activity.wode.shouyi;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MD5Utlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 缴纳信誉保证金界面
 *
 * @author ljz
 */
public class BaoZhengJin02Activity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_bzj_money)
    TextView tvBzjMoney;        // 缴纳保证金金额
    @Bind(R.id.tv_bzj_money2)
    TextView tvBzjMoney2;       // 保证金总额
    @Bind(R.id.tv_bzj_jnzh)
    TextView tvBzjJnzh;         // 缴纳账户金额
    @Bind(R.id.et_bzj_money)
    EditText etBzjMoney;        // 输入缴纳的金额
    @Bind(R.id.cb_bzj_xuzhi)
    CheckBox cbBzjXuzhi;        // 勾选《信誉保证金须知》
    @Bind(R.id.tv_toptitle_right)
    TextView tvToptitleRight;

    private String jiaoNaZH;    // 缴纳账户
    private String daiJjiaoNa;  // 待缴纳金额

    @Override
    public int initResource() {
        return R.layout.activity_wode_shouyi_baozhengjin02;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "缴纳信誉保证金", "缴纳记录");
        requestHandleArrayList.add(requestAction.shop_bzj_jine(this, preferences.getString(ConstantUtils.SP_MYID, "")));
    }

    @OnClick({R.id.tv_bzj_xuzhi, R.id.btn_bzj, R.id.tv_toptitle_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bzj_xuzhi:
                // 《信誉保证金须知》
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", ConstantUtils.BAOZHENGJINXUZHI_URL);
                startActivity(intent);
                break;
            case R.id.btn_bzj:
                // 按钮去缴纳
                final String money = etBzjMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    MToast.showToast("请输入缴纳的金额");
                    break;
                }
                if (!cbBzjXuzhi.isChecked()) {
                    MToast.showToast("请勾选信誉保证金须知");
                    break;
                }
                Log.e("123123", jiaoNaZH + "--"+money);
                if (processParseDouble(jiaoNaZH) < processParseDouble(money)) {
                    MToast.showToast("账户余额不足");
                    break;
                }
                if ("0".equals(money)) {
                    MToast.showToast("请填写有效缴纳金额");
                    break;
                }
                if (processParseDouble(daiJjiaoNa) < processParseDouble(money)) {
                    MToast.showToast("缴纳金额不得大于待缴纳金额");
                    break;
                }
                if ("0".equals(daiJjiaoNa)) {
                    MToast.showToast("缴纳金额已满无需再缴纳");
                    break;
                }
                DialogUtlis.oneBtnPwd(this, "¥" + money, new DialogUtlis.PasswordCallback() {
                    @Override
                    public void callback(String data) {
                        requestHandleArrayList.add(requestAction.shop_bzj_jiaona(BaoZhengJin02Activity.this, preferences.getString(ConstantUtils.SP_MYID, ""), money, daiJjiaoNa, MD5Utlis.Md5(data)));
                    }
                });
                break;
            case R.id.tv_toptitle_right:
                startActivity(new Intent(BaoZhengJin02Activity.this, JiaoNaJiLuActivity.class));
                break;
            default:
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_bzj_jine:
                daiJjiaoNa = JSONUtlis.getString(response, "待缴保证金", "0");
                jiaoNaZH = JSONUtlis.getString(response, "商城收益", "0");

                tvBzjMoney.setText(daiJjiaoNa);
                tvBzjMoney2.setText("需缴纳(￥" + JSONUtlis.getString(response, "总缴保证金", "0") + ")");
                tvBzjJnzh.setText("（￥" + jiaoNaZH + ")");
                break;
            case ConstantUtils.TAG_shop_bzj_jiaona:
                requestHandleArrayList.add(requestAction.shop_bzj_jine(this, preferences.getString(ConstantUtils.SP_MYID, "")));
                MToast.showToast("缴纳成功");
                etBzjMoney.setText("");
                break;
            default:
        }
    }

    /**
     * String 转 int
     *
     * @param data String
     * @return int
     */
    private double processParseDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {
            return 0;
        }
    }
}
