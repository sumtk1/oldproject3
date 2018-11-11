package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
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
import java.util.List;

import butterknife.Bind;

public class ShangPingFenRunActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.tv_fenrun_money)
    TextView tvFenrunMoney;
    @Bind(R.id.tv_fenrun_shuoming)
    TextView tvFenrunShuoming;
    @Bind(R.id.wode_fenrun_listview)
    ListView wodeFenrunListview;
    @Bind(R.id.wode_fenrun_refreshlayout)
    RefreshLayout wodeFenrunRefreshlayout;
    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int num, page = 0;
    private Boolean isPull = false;  // 是否上拉加载
    boolean isFirst = false;

    @Override
    public int initResource() {
        return R.layout.activity_shangpingfenrun;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "商品分润", "");
        wodeFenrunRefreshlayout.setOnRefreshListener(this);
        wodeFenrunRefreshlayout.setOnLoadListener(this);
        Intent intent = getIntent();
        tvFenrunMoney.setText(intent.getStringExtra("shangpingfenrun"));
        requestHandleArrayList.add(requestAction.shop_wl_Profit(ShangPingFenRunActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), 0));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_Profit:
                isFirst = true;
                if (isPull == false) {
                    wodeFenrunRefreshlayout.setRefreshing(false);
                    list.clear();
                } else {
                    wodeFenrunRefreshlayout.setLoading(false);
                }
                tvFenrunShuoming.setText(response.getString("说明"));
                num = Integer.parseInt(response.getString("条数"));
                if (num > 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String[] a = new String[3];
                        a[0] = jsonObject.getString("交易类别");
                        a[1] = jsonObject.getString("录入时间");
                        a[2] = jsonObject.getString("积分");
                        list.add(a);
                    }
                    wodeFenrunListview.setAdapter(commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_fenrunjilu, list) {
                        @Override
                        public void convert(ViewHolder holder, String[] strings) {
                            holder.setText(R.id.tv_fenren_mingcheng, strings[0]);
                            holder.setText(R.id.tv_fenrun_lurushijian, strings[1]);
                            holder.setText(R.id.tv_fenrun_jinger, strings[2]);
                            if (strings[2].contains("+")) {
                                holder.setTextColor(R.id.tv_fenrun_jinger, Color.parseColor("#FF7F29"));
                            } else if (strings[2].contains("-")) {
                                holder.setTextColor(R.id.tv_fenrun_jinger, Color.parseColor("#29b69d"));
                            }
                        }
                    });
                } else {
                    num = 0;
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(0);
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_sp(this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), 0));
        wodeFenrunRefreshlayout.removeFoot();
    }

    @Override
    public void onLoad() {
        wodeFenrunRefreshlayout.setLoading(false);
        if (num > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_sp(this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), page));
        } else if (num > 0) {
            wodeFenrunRefreshlayout.setLoading(false);
            wodeFenrunRefreshlayout.isNoData();
        } else {
            wodeFenrunRefreshlayout.setLoading(false);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            wodeFenrunRefreshlayout.setRefreshing(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }

}
