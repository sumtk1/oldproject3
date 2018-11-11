package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.databinding.ActivityQuerenFahuoBinding;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class ShenqingGuanFangJieRuActivity extends BaseActivity {
    ActivityQuerenFahuoBinding binding;
    @Bind(R.id.tv_sqgfjr_shuoming)
    TextView tv_sqgfjr_shuoming;
    @Bind(R.id.iv_sqgfjr_erweima)
    ImageView iv_sqgfjr_erweima;

    @Override
    public int initResource() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shenqingguanfangjieru);
        return 0;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "申请官方介入", "");
//        requestHandleArrayList.add(requestAction.shop_ddgl_kd(ShenqingGuanFangJieRuActivity.this, phone, dingdanid));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case ConstantUtils.TAG_shop_ddgl_fahuo:
//                MToast.showToast(ShenqingGuanFangJieRuActivity.this, "发货成功");
//                setResult(0);
//                finish();
//                break;
        }
    }

}
