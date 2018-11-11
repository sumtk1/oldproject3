package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.ShopPromotionRecordActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.adapter.ShopPromotionRecordAdapter;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionRecordModel;
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

public class SystemMessageRecordActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_system_message_norecord)
    TextView tvSystemMessageNorecord;
    @Bind(R.id.rl_system_message_record)
    RecyclerView rlSystemMessageRecord;
    @Bind(R.id.refreshlayout_shop_promotion_record)
    RefreshLayout refreshlayoutShopPromotionRecord;
    private int page;
    private boolean isPull;
    private String listNum;
    private List<SystemMessageRecordModel> modelList;
    private SystemMessageRecordAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_system_message_record;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "消息记录", "");
        modelList = new ArrayList<>();
        adapter = new SystemMessageRecordAdapter(R.layout.item_system_message_record, modelList);
        rlSystemMessageRecord.setLayoutManager(new LinearLayoutManager(this));
        rlSystemMessageRecord.setAdapter(adapter);
        refreshlayoutShopPromotionRecord.setOnLoadListener(this);
        refreshlayoutShopPromotionRecord.setOnRefreshListener(this);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SystemMessageRecordActivity.this, SystemMessageDetailActivity.class);
                intent.putExtra("id", modelList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_wl_mass_List(SystemMessageRecordActivity.this, phone, page + ""));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_mass_List:
                if (!isPull) {
                    refreshlayoutShopPromotionRecord.setRefreshing(false);
                    modelList.clear();
                } else {
                    refreshlayoutShopPromotionRecord.setLoading(false);
                }
                try {
                    listNum = response.getString("条数");
                } catch (Exception e) {

                }
                if (page == 0 && "0".equals(listNum)) {
                    tvSystemMessageNorecord.setVisibility(View.VISIBLE);
                } else {
                    tvSystemMessageNorecord.setVisibility(View.GONE);
                }
                JSONArray array = response.getJSONArray("列表");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        SystemMessageRecordModel model = new SystemMessageRecordModel();
                        model.setId(jsonObject.getString("id"));
                        model.setTiem(jsonObject.getString("录入时间"));
                        model.setTitle(jsonObject.getString("消息标题"));
                        model.setType(jsonObject.getString("消息类型"));
                        modelList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRefresh() {
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_mass_List(SystemMessageRecordActivity.this, phone, page + ""));
    }

    @Override
    public void onLoad() {
        if (Integer.parseInt(listNum) > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_mass_List(SystemMessageRecordActivity.this, phone, page + ""));
        } else if (Integer.parseInt(listNum) > 0) {
            refreshlayoutShopPromotionRecord.setLoading(false);
        } else {
            refreshlayoutShopPromotionRecord.setLoading(false);
        }
    }
}
