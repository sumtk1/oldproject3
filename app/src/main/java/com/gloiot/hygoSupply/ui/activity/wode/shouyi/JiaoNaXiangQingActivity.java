package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.util.Log;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.JSONUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 缴纳详情
 *
 * @author ljz
 */
public class JiaoNaXiangQingActivity extends BaseActivity {


    @Bind(R.id.tv_jnxq_title)
    TextView tvJnxqTitle;       // 标题
    @Bind(R.id.tv_jnxq_money)
    TextView tvJnxqMoney;       // 金额
    @Bind(R.id.tv_jnxq_shijian)
    TextView tvJnxqShijian;     // 时间
    @Bind(R.id.tv_jnxq_beizhu)
    TextView tvJnxqBeizhu;      // 备注

    @Override
    public int initResource() {
        return R.layout.activity_jiaonaxiangqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "账单详情", "");

        String id = getIntent().getStringExtra("id");
        requestHandleArrayList.add(requestAction.shop_gain_recording_bank_xq(JiaoNaXiangQingActivity.this, phone, id, "缴纳信誉保证金"));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_gain_recording_bank_xq:
                String money = JSONUtlis.getString(response, "money");
                // 根据正负改变字体颜色
                if (money.contains("-")) {
                    tvJnxqMoney.setTextColor(getResources().getColor(R.color.cl_29b69d));
                } else {
                    tvJnxqMoney.setTextColor(getResources().getColor(R.color.cl_ff6666));
                }
                tvJnxqMoney.setText(money);
                tvJnxqTitle.setText(JSONUtlis.getString(response, "explain"));
                tvJnxqShijian.setText(JSONUtlis.getString(response, "time"));
                tvJnxqBeizhu.setText(JSONUtlis.getString(response, "notes"));
                break;
            default:
        }
    }
}
