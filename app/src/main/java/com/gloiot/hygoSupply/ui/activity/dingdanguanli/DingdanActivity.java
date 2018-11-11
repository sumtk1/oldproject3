package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingDanBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DingdanActivity extends BaseActivity implements AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private String phone;
    private ArrayList<DingDanBean> allDingDan = new ArrayList<>();
    private ListView lv_zhangdan_jinri;
    private RelativeLayout rl_dingdan;
    private CommonAdapter adapter;
    private RefreshLayout refreshlayout_fg_jinri_shouyi;
    private Boolean refresh;  //true为刷新或第一次加载状态   false加载更多状态
    private int page = 0;
    private int num = 0;
    private boolean load = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_dingdan;
    }

    @Override
    public void initData() {
        lv_zhangdan_jinri = (ListView) findViewById(R.id.lv_zhangdan_jinri);
        rl_dingdan = (RelativeLayout) findViewById(R.id.rl_dingdan);
        refreshlayout_fg_jinri_shouyi = (RefreshLayout) findViewById(R.id.refreshlayout_fg_jinri_shouyi);

        CommonUtils.setTitleBar((Activity) mContext, true, "今日交易详情", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        lv_zhangdan_jinri.setOnItemClickListener(this);
        refresh = true;

        refreshlayout_fg_jinri_shouyi.setOnRefreshListener(this);
        refreshlayout_fg_jinri_shouyi.setOnLoadListener(this);
        requestHandleArrayList.add(requestAction.shop_sy_ddxq_day(DingdanActivity.this, phone, "0"));
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sy_ddxq_day:    //今日订单
                Log.e("今日订单接口请求成功", response.toString());
                lv_zhangdan_jinri.setVisibility(View.GONE);
                rl_dingdan.setVisibility(View.GONE);
                lv_zhangdan_jinri.setVisibility(View.VISIBLE);
                if (refresh) {
                    Log.e("清除了数据", "true");
                    allDingDan.clear();
                    refreshlayout_fg_jinri_shouyi.setRefreshing(false);
                } else {
                    refreshlayout_fg_jinri_shouyi.setLoading(false);
                }
                num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {

                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        response = (JSONObject) jsonArray.get(j);
                        DingDanBean dingDanBean = new DingDanBean(
                                response.getString("头像"),
                                response.getString("昵称"),
                                response.getString("订单id"),
                                response.getString("实付金额"),
                                response.getString("状态"),
                                response.getString("商品说明"),
                                response.getString("收货地址"),
                                response.getString("创建时间"),
                                response.getString("支付方式"),
                                response.getString("账号"));
                        allDingDan.add(dingDanBean);
                    }
                    if (refresh) {
                        adapter = new CommonAdapter<DingDanBean>(mContext, R.layout.item_dingdan_mingxi, allDingDan) {
                            @Override
                            public void convert(ViewHolder holder, final DingDanBean dingDanBean) {
                                holder.setText(R.id.tv_item_zhangdan_mingxi_jine, dingDanBean.getShifu_jine());
                                holder.setText(R.id.tv_item_zhangdan_mingxi_zhuangtai, dingDanBean.getZhuangtai());
                                holder.setText(R.id.tv_item_zhangdan_mingxi_miaoshu, dingDanBean.getShangpin_shuoming());
                                holder.setText(R.id.tv_item_zhangdan_mingxi_time, dingDanBean.getChuangjian_time());
                            }
                        };
                        lv_zhangdan_jinri.setAdapter(adapter);
                        lv_zhangdan_jinri.setVisibility(View.VISIBLE);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (load) {
                        lv_zhangdan_jinri.setVisibility(View.GONE);
                        rl_dingdan.setVisibility(View.GONE);
                        lv_zhangdan_jinri.setVisibility(View.VISIBLE);
                    } else {
                        rl_dingdan.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DingdanActivity.this, DingdanXiangqingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("订单", allDingDan.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_sy_ddxq_day(DingdanActivity.this, phone, "0"));
        refreshlayout_fg_jinri_shouyi.removeFoot();

    }

    @Override
    public void onLoad() {
        refresh = false;
        load = true;
        if (num >= 10) {
            page++;
            requestHandleArrayList.add(requestAction.shop_sy_ddxq_day(DingdanActivity.this, phone, String.valueOf(page)));
        } else if (num >= 0) {
            refreshlayout_fg_jinri_shouyi.setLoading(false);
            refreshlayout_fg_jinri_shouyi.isNoData();
        } else {
            refreshlayout_fg_jinri_shouyi.setLoading(false);
        }

    }
}
