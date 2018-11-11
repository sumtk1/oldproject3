package com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.adapter.ShopGradeRecoredAdapter;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.model.ShopGradeRecordModel;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2017/10/11.
 * 功能：店铺——店铺等级
 */


public class ShopGradeActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.iv_shop_grade_icon)
    ImageView ivShopGradeIcon;
    @Bind(R.id.tv_shop_grade_progress)
    TextView tvShopGradeProgress;
    @Bind(R.id.tv_shop_grade_next)
    TextView tvShopGradeNext;
    @Bind(R.id.ll_shop_grade_progress)
    LinearLayout llShopGradeProgress;
    @Bind(R.id.rv_shop_grade_records)
    RecyclerView rvShopGradeRecords;
    @Bind(R.id.tv_shop_grade_points)
    TextView tvShopGradePoints;
    @Bind(R.id.refreshlayout_shop_grade)
    RefreshLayout refreshlayoutShopGrade;
    @Bind(R.id.tv_shop_grade_point_explain)
    TextView tvShopGradePointExplain;
    private String shopGrade;
    private List<ShopGradeRecordModel> shopGradeRecordModels;
    private ShopGradeRecoredAdapter adapter;
    private int page;
    private boolean isPull;
    private String listNum;

    @Override
    public int initResource() {
        return R.layout.activity_shop_grade;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "店铺等级", "");
        shopGrade = getIntent().getStringExtra("shopGrade");
        shopGradeRecordModels = new ArrayList<>();
        adapter = new ShopGradeRecoredAdapter(R.layout.item_shop_grade_records, shopGradeRecordModels);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent shopGrades = new Intent(mContext, GradeDetailActivity.class);
                shopGrades.putExtra("id", shopGradeRecordModels.get(position).getId());
                startActivity(shopGrades);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onLoad();
            }
        }, rvShopGradeRecords);

        rvShopGradeRecords.setLayoutManager(new LinearLayoutManager(this));
        rvShopGradeRecords.setAdapter(adapter);
        refreshlayoutShopGrade.setOnLoadListener(this);
        refreshlayoutShopGrade.setOnRefreshListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_sp_grade(this, phone, shopGrade, "0"));
    }

    @OnClick(R.id.tv_shop_grade_next)
    public void onViewClicked() {
        Intent intent3 = new Intent(mContext, WebActivity.class);
        intent3.putExtra("url", CommonUtils.jointUrl(ConstantUtils.SHOPGRADEINSTRUCTION_URL));
        startActivity(intent3);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        if (isPull == false) {
            adapter.setEnableLoadMore(true);
            refreshlayoutShopGrade.setRefreshing(false);
            shopGradeRecordModels.clear();
        } else {
            adapter.loadMoreComplete();
            refreshlayoutShopGrade.setLoading(false);
        }
        try {
            listNum = response.getString("条数");
        } catch (Exception e) {

        }
        String url = response.getString("等级徽章");
        CommonUtils.setDisplayImage(ivShopGradeIcon, url, 0, 0);
        JSONArray records = response.getJSONArray("列表");
        if (records.length() > 0) {
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                ShopGradeRecordModel model = new ShopGradeRecordModel();
                model.setId(record.getString("订单销售id"));
                model.setTime(record.getString("录入时间"));
                model.setPoint(record.getString("荣誉积分"));
                shopGradeRecordModels.add(model);
            }
        }
        adapter.notifyDataSetChanged();
        String title = response.getString("title");
        try {
            int position = title.indexOf("\n");
            String point = title.substring(6, position);
            tvShopGradePoints.setText(point);
            String describe = title.substring(position + 1, title.length() - 4);
            tvShopGradeProgress.setText(describe);
            String next = title.substring(title.length() - 4);
            tvShopGradeNext.setText(next);
        } catch (Exception e) {
            tvShopGradePointExplain.setText("");
            tvShopGradePoints.setText("");
            String next = title.substring(title.length() - 4);
            tvShopGradeNext.setText(next);
            String describe = title.substring(0, title.length() - 4);
            tvShopGradeProgress.setText(describe);
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        adapter.loadMoreFail();
    }

    @Override
    public void onRefresh() {
        isPull = false;
        page = 0;
        adapter.setEnableLoadMore(false);
        requestHandleArrayList.add(requestAction.shop_sp_grade(this, phone, shopGrade, String.valueOf(page)));
    }

    @Override
    public void onLoad() {
        if (Integer.parseInt(listNum) > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_sp_grade(this, phone, shopGrade, String.valueOf(page)));
        } else if (Integer.parseInt(listNum) > 0) {
            refreshlayoutShopGrade.setLoading(false);
            adapter.loadMoreEnd();
        } else {
            refreshlayoutShopGrade.setLoading(false);
            adapter.loadMoreEnd();
        }
    }
}
