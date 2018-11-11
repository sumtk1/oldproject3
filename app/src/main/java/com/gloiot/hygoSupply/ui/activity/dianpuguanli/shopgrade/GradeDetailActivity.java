package com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.adapter.GradeRecoredDetailAdapter;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model.GradeRecordDetailModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DingDanXiangQing_NewActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2017/10/11.
 * 功能：店铺——荣誉分详细
 */


public class GradeDetailActivity extends BaseActivity {


    @Bind(R.id.tv_grade_record_points)
    TextView tvGradeRecordPoints;
    @Bind(R.id.rv_grade_record_detail)
    RecyclerView rvGradeRecordDetail;
    private String id;
    private String orderId;
    private String orderStatus;
    private List<GradeRecordDetailModel> gradeRecordDetailModels;
    private GradeRecoredDetailAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_grade_record;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "荣誉分记录", "");
        id = getIntent().getStringExtra("id");
        gradeRecordDetailModels = new ArrayList<>();
        adapter = new GradeRecoredDetailAdapter(R.layout.item_grade_record_detail, gradeRecordDetailModels);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2017/10/11 跳转记录详情
//                MToastUtils.showToast("点击");
            }
        });
        rvGradeRecordDetail.setLayoutManager(new LinearLayoutManager(this));
        rvGradeRecordDetail.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_sp_gradeDetail(this, phone, id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        gradeRecordDetailModels.clear();
        tvGradeRecordPoints.setText(response.getString("荣誉分"));
        GradeRecordDetailModel model = new GradeRecordDetailModel();
        model.setId(response.getString("商品id"));
        model.setImg(response.getString("缩略图"));
        model.setPoint(Integer.parseInt(response.getString("商品评分")));
        model.setName(response.getString("商品名称"));
        model.setDecribe(response.getString("商品规格"));
        gradeRecordDetailModels.add(model);
        orderId = response.getString("订单id");
        orderStatus = response.getString("订单状态");
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.tv_grade_record_detail_go)
    public void onViewClicked() {

        // TODO: 2017/10/12 跳转订单详情
        Intent intent = new Intent(mContext, DingDanXiangQing_NewActivity.class);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("售后", false);
        bundle2.putString("订单id", orderId);
        bundle2.putString("订单状态", orderStatus);
        bundle2.putBoolean("物流", false);
        intent.putExtra("current", "已完成");
        intent.putExtras(bundle2);
        startActivity(intent);
    }
}
