package com.gloiot.hygoSupply.ui.activity.promotional;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleListActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.adapter.AfterSaleListAdapter;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleOrderModel;
import com.gloiot.hygoSupply.ui.activity.promotional.adapter.ParticipateActivitiesAdapter;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ParticipateActivitiesModel;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/10/31.
 * 功能：参与活动商品列表界面
 */


public class ParticipateActivities extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private LinearLayout ll_new_activities;
    private List<ParticipateActivitiesModel> modelList;
    private ParticipateActivitiesAdapter adapter;
    private RefreshLayout refreshlayout_parcitipate_activities;
    private String listNum;
    private int page;
    private boolean isPull;
    private String deductionPercent;
    private String activitiesExplain;


    @Override
    public int initResource() {
        return R.layout.activity_parcitipace_activities;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_wl_activity(ParticipateActivities.this, phone, page + ""));

    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "参与活动", "");
        deductionPercent = getIntent().getStringExtra("deductionPercent");
        activitiesExplain = getIntent().getStringExtra("activitiesExplain");
        recyclerView = (RecyclerView) this.findViewById(R.id.rv_parcitipate_activities);
        ll_new_activities = (LinearLayout) this.findViewById(R.id.ll_new_activities);
        refreshlayout_parcitipate_activities = (RefreshLayout) this.findViewById(R.id.refreshlayout_parcitipate_activities);
        modelList = new ArrayList<>();
        initAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshlayout_parcitipate_activities.setOnLoadListener(this);
        refreshlayout_parcitipate_activities.setOnRefreshListener(this);
        ll_new_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activities;
                activities = new Intent(mContext, PromotionalAvtivities.class);
                activities.putExtra("deductionPercent", deductionPercent);
                activities.putExtra("activitiesExplain", activitiesExplain);
                startActivity(activities);
            }
        });
    }

    public void initAdapter() {
        adapter = new ParticipateActivitiesAdapter(R.layout.item_participate_activitys, modelList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.tv_item_participate_activitys_edit:
                        Intent activities;
                        activities = new Intent(mContext, PromotionalAvtivities.class);
                        activities.putExtra("deductionPercent", deductionPercent);
                        activities.putExtra("activitiesExplain", activitiesExplain);
                        activities.putExtra("id", modelList.get(position).getProcuctId());
                        activities.putExtra("percent", modelList.get(position).getDeductionPercent());
                        activities.putExtra("image", modelList.get(position).getProductImage());
                        activities.putExtra("describe", modelList.get(position).getProductDescribe());
                        activities.putExtra("price", modelList.get(position).getProcuctPrice());
                        activities.putExtra("type", "edit");
                        startActivity(activities);
                        break;
                    case R.id.tv_item_participate_activitys_delete:
                        DialogUtlis.twoBtnNormal(mContext, "删除之后该商品将会退出活动", "提示", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                requestHandleArrayList.add(requestAction.shop_wl_Editorial_activities(ParticipateActivities.this, phone, "1", "", modelList.get(position).getProcuctId(), "", ""));
                            }
                        });
                        break;

                }
            }
        });
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_activity:
                if (isPull == false) {
                    refreshlayout_parcitipate_activities.setRefreshing(false);
                    modelList.clear();
                } else {
                    refreshlayout_parcitipate_activities.setLoading(false);
                }
                try {
                    listNum = response.getString("条数");
                } catch (Exception e) {

                }
                JSONArray array = response.getJSONArray("列表");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        ParticipateActivitiesModel model = new ParticipateActivitiesModel();
                        model.setProcuctPrice(jsonObject.getString("价格"));
                        model.setProcuctId(jsonObject.getString("id"));
                        model.setProductImage(jsonObject.getString("缩略图"));
                        model.setDeductionPercent(jsonObject.getString("折扣价"));
                        model.setProductDescribe(jsonObject.getString("商品名称"));
                        modelList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case ConstantUtils.TAG_shop_wl_Editorial_activities:
                MToastUtils.showToast("删除成功");
                onRefresh();
                break;

        }


    }

    @Override
    public void onRefresh() {
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_activity(ParticipateActivities.this, phone, page + ""));
    }

    @Override
    public void onLoad() {
        if (Integer.parseInt(listNum) > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_activity(ParticipateActivities.this, phone, page + ""));
        } else if (Integer.parseInt(listNum) > 0) {
            refreshlayout_parcitipate_activities.setLoading(false);
        } else {
            refreshlayout_parcitipate_activities.setLoading(false);
        }
    }
}
