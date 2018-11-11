package com.gloiot.hygoSupply.ui.activity.promotional;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.adapter.ShopPromotionRecordAdapter;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ParticipateActivitiesModel;
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

public class ShopPromotionRecordActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tv_shop_promotion_norecord)
    TextView tvShopPromotionNorecord;
    @Bind(R.id.rv_shop_promotion_record)
    RecyclerView rvShopPromotionRecord;
    @Bind(R.id.refreshlayout_shop_promotion_record)
    RefreshLayout refreshlayoutShopPromotionRecord;
    private int page;
    private boolean isPull;
    private String listNum;
    private List<ShopPromotionRecordModel> modelList;
    private ShopPromotionRecordAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_shop_promotion_record;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "推广记录", "");
        modelList = new ArrayList<>();
        adapter = new ShopPromotionRecordAdapter(R.layout.item_shop_promotion_record, modelList);
        rvShopPromotionRecord.setLayoutManager(new LinearLayoutManager(this));
        rvShopPromotionRecord.setAdapter(adapter);
        refreshlayoutShopPromotionRecord.setOnLoadListener(this);
        refreshlayoutShopPromotionRecord.setOnRefreshListener(this);

        requestHandleArrayList.add(requestAction.shop_wl_extension_record(ShopPromotionRecordActivity.this, phone, page + ""));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_extension_record:
                if (isPull == false) {
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
                    tvShopPromotionNorecord.setVisibility(View.VISIBLE);
                } else {
                    tvShopPromotionNorecord.setVisibility(View.GONE);
                }
                JSONArray array = response.getJSONArray("列表");
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        ShopPromotionRecordModel model = new ShopPromotionRecordModel();
                        model.setPrice(jsonObject.getString("单价"));
                        model.setProductImage(jsonObject.getString("缩略图"));
                        model.setProductDescribe(jsonObject.getString("商品名称"));
                        model.setType(jsonObject.getString("类别"));
                        model.setRecordTime(jsonObject.getString("录入时间"));
                        model.setTime(jsonObject.getString("天数"));
                        model.setPayType("商家收益");
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
        requestHandleArrayList.add(requestAction.shop_wl_extension_record(ShopPromotionRecordActivity.this, phone, page + ""));
    }

    @Override
    public void onLoad() {
        if (Integer.parseInt(listNum) > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_extension_record(ShopPromotionRecordActivity.this, phone, page + ""));
        } else if (Integer.parseInt(listNum) > 0) {
            refreshlayoutShopPromotionRecord.setLoading(false);
        } else {
            refreshlayoutShopPromotionRecord.setLoading(false);
        }
    }
}
