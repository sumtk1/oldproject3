package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TongYiTuiHuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TuikuanxiangqingActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.adapter.AfterSaleListAdapter;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleOrderModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleProductModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.dingdanfragment.DingdanGuanliFragment;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/9/19.
 * 功能：我的——我的资料
 */


public class AfterSaleListActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<AfterSaleOrderModel> modelList;
    private AfterSaleListAdapter adapter;
    private RefreshLayout refreshlayout_after_sale_list;
    private String listNum;
    private int page;
    private boolean isPull;

    @Override
    public int initResource() {
        return R.layout.activity_after_sale_list;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "售后管理", "");
        recyclerView = (RecyclerView) this.findViewById(R.id.rv_after_sale_list);
        refreshlayout_after_sale_list = (RefreshLayout) this.findViewById(R.id.refreshlayout_after_sale_list);
        modelList = new ArrayList<>();
        initAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshlayout_after_sale_list.setOnLoadListener(this);
        refreshlayout_after_sale_list.setOnRefreshListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_wl_Reservice(AfterSaleListActivity.this, phone, "退款/售后", "0"));
    }

    public void initAdapter() {
        adapter = new AfterSaleListAdapter(R.layout.item_after_sale_order, modelList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final AfterSaleOrderModel model = modelList.get(position);
                Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleActivity.class);
                intent.putExtra("orderId", model.getOrderId());
                intent.putExtra("id", model.getProductModel().getId());
                intent.putExtra("afterSaleType", model.getAfterSaleType());
                intent.putExtra("account", model.getProductModel().getAccount());
                intent.putExtra("productId", model.getProductModel().getProductId());
                intent.putExtra("status", model.getStatus());
                intent.putExtra("nickName", model.getNickName());
//                MToastUtils.showToast(model.getAfterSaleSatus() + "2");
                startActivity(intent);
//                switch (model.getAfterSaleSatus()) {
//                    case "等待商家处理退款申请":
//                    case "请处理":
//                    case "等待商家处理退货":
//                        Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleActivity.class);
//                        intent.putExtra("orderId", model.getOrderId());
//                        intent.putExtra("id", model.getProductModel().getId());
//                        startActivity(intent);
//                        break;
//                    case "等待商家确认并退款":
//                    case "商家已同意退货申请":
//                        Intent intent1 = new Intent(AfterSaleListActivity.this, AfterSaleActivity.class);
//                        intent1.putExtra("orderId", model.getOrderId());
//                        intent1.putExtra("id", model.getProductModel().getId());
//                        startActivity(intent1);
//                        break;
//                    default:
//                        Intent intent2 = new Intent(AfterSaleListActivity.this, TuikuanxiangqingActivity.class);
////                        intent2.putExtra("item", (Serializable) dingdanGuanliBean);
//                        startActivity(intent2);
//                        break;
//                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final AfterSaleOrderModel model = modelList.get(position);
                Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleActivity.class);
                intent.putExtra("orderId", model.getOrderId());
                intent.putExtra("id", model.getProductModel().getId());
                intent.putExtra("afterSaleType", model.getAfterSaleType());
                intent.putExtra("account", model.getProductModel().getAccount());
                intent.putExtra("productId", model.getProductModel().getProductId());
                intent.putExtra("status", model.getStatus());
                intent.putExtra("nickName", model.getNickName());
//                MToastUtils.showToast(model.getAfterSaleSatus() + "1");
                startActivity(intent);
            }
        });

        adapter.setUpFetchEnable(true);
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        if (isPull == false) {
            refreshlayout_after_sale_list.setRefreshing(false);
            modelList.clear();
        } else {
            Log.e(TAG, "requestSuccess: onload"+response.getString("条数"));
            refreshlayout_after_sale_list.setLoading(false);
        }
        try {
            listNum = response.getString("条数");
        } catch (Exception e) {

        }
        JSONArray array = response.getJSONArray("一级列表");
        Log.e(TAG, "requestSuccess: "+array.length());
        if (array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject orderInfo = array.getJSONObject(i);
                AfterSaleOrderModel afterSaleOrderModel = new AfterSaleOrderModel();
//                afterSaleOrderModel.setId(orderInfo.getString("id"));
                afterSaleOrderModel.setOrderId(orderInfo.getString("订单id"));
                afterSaleOrderModel.setStatus(orderInfo.getString("状态"));
                afterSaleOrderModel.setAfterSaleSatus(orderInfo.getString("售后状态"));
                afterSaleOrderModel.setNickName(orderInfo.getString("昵称"));
                afterSaleOrderModel.setAfterSaleType(orderInfo.getString("退款类型"));
                afterSaleOrderModel.setConsignee(orderInfo.getString("收货人"));
                afterSaleOrderModel.setDeadline(orderInfo.getString("天") + "天" + orderInfo.getString("小时") + "小时" + orderInfo.getString("分钟") + "分钟");
                afterSaleOrderModel.setTotalCost(orderInfo.getString("合计"));
                JSONArray prouductlist = orderInfo.getJSONArray("二级列表");
                if (prouductlist.length() > 0) {
                    JSONObject product = prouductlist.getJSONObject(0);
                    AfterSaleProductModel procuctModel = new AfterSaleProductModel();
                    procuctModel.setId(product.getString("id"));
                    procuctModel.setProductId(product.getString("商品id"));
                    procuctModel.setSize(product.getString("种类详细"));
                    procuctModel.setName(product.getString("商品名称"));
                    procuctModel.setPrice(product.getString("价格"));
                    procuctModel.setNum(product.getString("商品数量"));
                    procuctModel.setDeliveryCost(product.getString("快递费"));
                    procuctModel.setImage(product.getString("缩略图"));
                    procuctModel.setType(product.getString("类型"));
                    procuctModel.setAccount(product.getString("退款账号"));
                    afterSaleOrderModel.setProductModel(procuctModel);
                }
                modelList.add(afterSaleOrderModel);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_Reservice(AfterSaleListActivity.this, phone, "退款/售后", String.valueOf(page)));
    }

    @Override
    public void onLoad() {
        if (Integer.parseInt(listNum) > 9) {
            isPull = true;
            page++;
            Log.e(TAG, "onLoad: "+"执行");
            requestHandleArrayList.add(requestAction.shop_wl_Reservice(AfterSaleListActivity.this, phone, "退款/售后", String.valueOf(page)));
        } else if (Integer.parseInt(listNum) > 0) {
            refreshlayout_after_sale_list.setLoading(false);
        } else {
            refreshlayout_after_sale_list.setLoading(false);
        }
    }
}
