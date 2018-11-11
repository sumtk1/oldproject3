package com.gloiot.hygoSupply.ui.activity.wode.shouyi;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.JSONUtlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 缴纳记录
 *
 * @author ljz
 */
public class JiaoNaJiLuActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.lv_jiaona_jilu)
    ListView lvJiaonaJilu;          // 列表
    @Bind(R.id.ref_jiaona_jilu)
    RefreshLayout refJiaonaJilu;    // 刷新控件
    @Bind(R.id.tv_jiaona_jilu)
    TextView tvJiaonaJilu;          // 暂无缴纳记录

    private Boolean refresh = false;    // 是否上拉加载
    private int number;                 // 每页条数
    private int page = 0;               // 页数
    private List<String[]> list = new ArrayList<>();       // 0.标题 1.时间 2.金额
    private CommonAdapter commonAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_jiaonajilu;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "缴纳记录", "");
        requestHandleArrayList.add(requestAction.shop_jiaonajilu(JiaoNaJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), page + ""));
        setAdapter();
        refJiaonaJilu.setOnRefreshListener(this);
        refJiaonaJilu.setOnLoadListener(this);
    }


    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_jiaonajilu, list) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                // 0.标题 1.时间 2.金额
                holder.setText(R.id.tv_jiaonajilu_title, strings[0]);
                holder.setText(R.id.tv_jiaonajilu_time, strings[1]);
                holder.setText(R.id.tv_jiaonajilu_money, strings[2]);
                // 根据正负改变字体颜色
                if (strings[2].contains("-")) {
                    holder.setTextColor(R.id.tv_jiaonajilu_money, getResources().getColor(R.color.cl_29b69d));
                } else {
                    holder.setTextColor(R.id.tv_jiaonajilu_money, getResources().getColor(R.color.cl_ff6666));
                }
            }
        };
        lvJiaonaJilu.setAdapter(commonAdapter);
        lvJiaonaJilu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JiaoNaJiLuActivity.this, JiaoNaJiLuXiangQingActivity.class);
                intent.putExtra("title", list.get(position)[0]);
                intent.putExtra("time", list.get(position)[1]);
                intent.putExtra("money", list.get(position)[2]);
                intent.putExtra("zhanghu", list.get(position)[3]);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRefresh() {
        refJiaonaJilu.setRefreshing(false);
        refresh = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_jiaonajilu(JiaoNaJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), page + ""));
        refJiaonaJilu.removeFoot();
    }

    @Override
    public void onLoad() {
        refJiaonaJilu.setLoading(false);
        if (number > 19) {
            refresh = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_jiaonajilu(JiaoNaJiLuActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), page + ""));
            refJiaonaJilu.removeFoot();
        } else if (number > 0) {
            refJiaonaJilu.setLoading(false);
            refJiaonaJilu.isNoData();
        } else {
            refJiaonaJilu.setLoading(false);
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_jiaonajilu:
                if (refresh == false) {
                    refJiaonaJilu.setRefreshing(false);
                    list.clear();
                } else {
                    refJiaonaJilu.setLoading(false);
                }
                number = Integer.parseInt(JSONUtlis.getString(response,"条数"));

                if (number > 0) {
                    tvJiaonaJilu.setVisibility(View.GONE);
                    lvJiaonaJilu.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] a = new String[5];
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        a[0] = JSONUtlis.getString(jsonObject,"说明");
                        a[1] = JSONUtlis.getString(jsonObject,"录入时间");
                        a[2] = JSONUtlis.getString(jsonObject,"积分");
                        a[3] = JSONUtlis.getString(jsonObject,"交易账户");
                        list.add(a);
                    }
                    commonAdapter.notifyDataSetChanged();
                } else {
                    tvJiaonaJilu.setVisibility(View.VISIBLE);
                    lvJiaonaJilu.setVisibility(View.GONE);
                    number = 0;
                }
                break;
            default:
        }
    }
}
