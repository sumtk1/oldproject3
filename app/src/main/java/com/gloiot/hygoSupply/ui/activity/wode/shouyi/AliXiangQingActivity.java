package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.view.View;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class AliXiangQingActivity extends BaseActivity {

    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_jiaoyi_type)
    TextView tvJiaoyiType;
    @Bind(R.id.tv_zhanghu)
    TextView tvZhanghu;
    @Bind(R.id.tv_shijian)
    TextView tvShijian;

    @Override
    public int initResource() {
        return R.layout.activity_alixiangqing;
    }

    @Override
    public void initData() {

        CommonUtils.setTitleBar(this, "账单详情");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        String id = getIntent().getStringExtra("id");
        requestHandleArrayList.add(requestAction.shop_gain_recording_ali_xq(AliXiangQingActivity.this, phone, id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_gain_recording_ali_xq:
                tvMoney.setText(response.getString("money"));
                tvShijian.setText(response.getString("time"));
                tvZhanghu.setText(response.getString("aliacc"));

                switch (response.getString("status")) {
                    case "已成功":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_sucess);
                        break;
                    case "审核中":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_shenghe);
                        break;
                    case "提现失败已退回":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_fail);
                        break;
                    default:
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_shenghe);
                        break;
                }
                break;
            default:
        }

    }
}
