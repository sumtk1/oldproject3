package com.gloiot.hygoSupply.ui.activity.wode.shoukuan;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.PictureUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by LinKorea on 2017/5/23.
 */

public class ShouKuanJiLuActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.lv_shoukuan_jilu)
    ListView lvShouKuanjilu;
    @Bind(R.id.tv_shoukuan_jilu)
    TextView tvShouKuanjilu;
    @Bind(R.id.refreshlayout_shoukuan_jilu)
    RefreshLayout reShoukuanjilu;

    private List<String[]> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private Boolean refresh = false;  // 是否上拉加载
    private boolean isRefreshing = false;
    private int number;
    private int page = 0;


    @Override
    public int initResource() {
        return R.layout.activity_shoukuanjilu;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "收款记录", "");
        requestHandleArrayList.add(requestAction.shop_wl_address(ShouKuanJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), 0));
        setAdapter();
        reShoukuanjilu.setOnRefreshListener(this);
        reShoukuanjilu.setOnLoadListener(this);
    }


    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_shoukuanjilu, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                PictureUtlis.loadCircularImageViewHolder(mContext, strings[0], R.mipmap.ic_wode, (ImageView) holder.getView(R.id.iv_jilu_icon));
                holder.setText(R.id.tv_jilu_money, strings[1]);
                holder.setText(R.id.tv_jilu_time, strings[2]);
                if (!TextUtils.isEmpty(strings[3])) {
                    holder.setVisible(R.id.ll_jilu_beizhu, true);
                    holder.setText(R.id.tv_jilu_beizhu, strings[3]);
                } else {
                    holder.setVisible(R.id.ll_jilu_beizhu, false);
                }

                holder.setText(R.id.tv_jilu_shuoming, strings[4]);

            }
        };
        lvShouKuanjilu.setAdapter(commonAdapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_address:
                isRefreshing = true;
                if (refresh == false) {
                    reShoukuanjilu.setRefreshing(false);
                    list.clear();
                } else {
                    reShoukuanjilu.setLoading(false);
                }
                if (response.getString("状态").equals("成功")) {
                    number = Integer.parseInt(response.getString("条数"));

                    if (number > 0) {
                        tvShouKuanjilu.setVisibility(View.GONE);
                        lvShouKuanjilu.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("列表");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String[] a = new String[5];
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            a[0] = jsonObject.getString("头像");
                            a[1] = jsonObject.getString("收款金额");
                            a[2] = jsonObject.getString("收款时间");
                            a[3] = jsonObject.getString("收款备注");
                            a[4] = jsonObject.getString("说明");
                            list.add(a);
                        }
                        commonAdapter.notifyDataSetChanged();
                    } else {
                        tvShouKuanjilu.setVisibility(View.VISIBLE);
                        lvShouKuanjilu.setVisibility(View.GONE);
                        number = 0;
                    }
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            reShoukuanjilu.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(0);
        refresh = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_address(ShouKuanJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), 0));
        reShoukuanjilu.removeFoot();
    }

    @Override
    public void onLoad() {
        reShoukuanjilu.setLoading(false);
        if (number > 9) {
            refresh = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_address(ShouKuanJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), page));
            reShoukuanjilu.removeFoot();
        } else if (number > 0) {
            reShoukuanjilu.setLoading(false);
            reShoukuanjilu.isNoData();
        } else {
            reShoukuanjilu.setLoading(false);
        }
    }
}
