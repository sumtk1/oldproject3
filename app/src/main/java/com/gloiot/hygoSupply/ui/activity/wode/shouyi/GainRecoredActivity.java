package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.databinding.ActivityGainRecoredBinding;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DingDanXiangQing_NewActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.JSONUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 作者：Ljy on 2017/9/12.
 * 功能：收益记录详情
 */


public class GainRecoredActivity extends BaseActivity {

    private ActivityGainRecoredBinding binding;
    private GainRecoredModel model;
    private String id;
    private GainRecoredAdapter adapter;
    private TextView title;
    private TextView tv_sqfl;
    private TextView tv_sjdzsy;
    private TextView tv_money;

    @Override
    public int initResource() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gain_recored);
        return 0;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "记录详情", "");
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_sqfl = (TextView) findViewById(R.id.tv_sqfl);
        tv_sjdzsy = (TextView) findViewById(R.id.tv_sjdzsy);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getIntent().getStringExtra("title"));
        if ("商品退款退货".equals(getIntent().getStringExtra("title"))) {
            tv_sqfl.setVisibility(View.GONE);
            tv_sjdzsy.setVisibility(View.GONE);
        }
        WoDeShouYiXiangQingActivity.isRefresh = false;
        model = new GainRecoredModel();
        adapter = new GainRecoredAdapter(this, model.getImgUrl());
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvGainRecordSingle.setLayoutManager(ms);
        binding.rvGainRecordSingle.setAdapter(adapter);
        id = getIntent().getStringExtra("id");
        requestHandleArrayList.add(requestAction.shop_gain_recording_xq(this, phone, id, getIntent().getStringExtra("title")));
        binding.rlGainRecordDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GainRecoredActivity.this, DingDanXiangQing_NewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("订单id", model.getOrderNum());
                bundle.putString("订单状态", model.getStatus());
                bundle.putBoolean("费率", true);
                bundle.putBoolean("售后", false);
                bundle.putBoolean("物流", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_gain_recording_xq:
//        "商品名称":"阿张6",
//                "id":"26575",
//                "订单id":"13728796639_20170912152431701",
//                "付款时间":"2017-09-12 15:24:36",
//                "合计":"23.20",
//                "收取费率":"20.00",
//                "收益":"3.20"
                model.setStatus(JSONUtlis.getString(response, "status"));
                model.setProductName(response.getString("商品名称"));
                model.setId(response.getString("id"));
                model.setOrderNum(response.getString("订单id"));
                model.setTotal(response.getString("合计"));
                model.setRate(response.getString("收取费率"));
                model.setPaymentTime(response.getString("付款时间"));
                model.setRealEarnings(response.getString("收益"));
                model.setRealEarningsTop("￥" + response.getString("收益"));
                JSONArray images = response.getJSONArray("imgs");
                if (images.length() > 0) {
                    for (int i = 0; i < images.length(); i++) {
                        JSONObject image = images.getJSONObject(i);
                        model.getImgUrl().add(image.getString("图片"));
                    }
                }
                if (model.getImgUrl().size() > 1) {
                    binding.llGainRecordSingle.setVisibility(View.GONE);
                    binding.rvGainRecordSingle.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else if (model.getImgUrl().size() > 0) {
                    binding.llGainRecordSingle.setVisibility(View.VISIBLE);
                    binding.rvGainRecordSingle.setVisibility(View.GONE);
                    CommonUtils.setDisplayImage(binding.ivGainRecordSingle, model.getImgUrl().get(0), 0, 0);
                } else {
                    binding.llGainRecordSingle.setVisibility(View.GONE);
                }
                binding.setModel(model);

                if ("商品退款退货".equals(getIntent().getStringExtra("title"))) {
                    model.setRate("");
                    model.setRealEarnings("");
                    tv_money.setTextColor(getResources().getColor(R.color.cl_29b69d));
                }
                break;
            default:
        }
    }
}
