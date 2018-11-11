package com.gloiot.hygoSupply.ui.activity.shangpinguanli;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Ljy on 2017/8/18.
 * 功能：商品管理——审核结果
 */


public class AuditResultActivity extends BaseActivity {
    String phone, id, name;
    @Bind(R.id.tv_audit_product_name)
    TextView tvAuditProductName;
    @Bind(R.id.tv_audit_release_time)
    TextView tvAuditReleaseTime;
    @Bind(R.id.tv_audit_audit_time)
    TextView tvAuditAuditTime;
    @Bind(R.id.tv_audit_failure_reason)
    TextView tvAuditFailureReason;
    @Bind(R.id.iv_audit_result)
    ImageView ivAuditResult;

    @Override
    public int initResource() {
        return R.layout.activity_audit_result;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "审核结果", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        id = getIntent().getStringExtra("productId");
        name = getIntent().getStringExtra("productName");
        requestHandleArrayList.add(requestAction.shop_wl_FailureReason(AuditResultActivity.this, phone, id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        CommonUtils.setDisplayImage(ivAuditResult, response.getString("缩略图"), 0, 0);
        tvAuditReleaseTime.setText(response.getString("录入时间"));
        tvAuditAuditTime.setText(response.getString("作废时间"));
        tvAuditFailureReason.setText(response.getString("作废原因"));
        tvAuditProductName.setText(name);

    }

    @Override
    public void statusUnusual(JSONObject response) throws JSONException {
        if ("审核作废".equals(response.getString("状态"))) {
//            JSONArray array = response.getJSONArray("缩略图");
//            CommonUtils.setDisplayImage(ivAuditResult, array.getJSONObject(0).getString("图片"), 0, 0);
            CommonUtils.setDisplayImage(ivAuditResult, response.getString("缩略图"), 0, 0);
            tvAuditReleaseTime.setText(response.getString("录入时间"));
            tvAuditAuditTime.setText(response.getString("作废时间"));
            tvAuditFailureReason.setText(response.getString("作废原因"));
            tvAuditProductName.setText(name);
        } else {
            super.statusUnusual(response);
        }
    }
}
