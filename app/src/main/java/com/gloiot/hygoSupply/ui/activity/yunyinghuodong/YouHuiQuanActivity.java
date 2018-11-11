package com.gloiot.hygoSupply.ui.activity.yunyinghuodong;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.YouHuiQuanBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/***
 * Created by zxl on 2017/6/20.
 * 优惠券管理
 */

public class YouHuiQuanActivity extends BaseActivity implements View.OnClickListener
        , RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tv_twotile_1)
    TextView tv_twotile_1;
    @Bind(R.id.tv_twotile_2)
    TextView tv_twotile_2;
    @Bind(R.id.tv_twotile_3)
    TextView tv_twotile_3;
    @Bind(R.id.refreshlayout_youhuiquan)
    RefreshLayout refreshlayout_youhuiquan;
    @Bind(R.id.lv_youhuiquan)
    ListView lv_youhuiquan;
    @Bind(R.id.rl_youhuiquan_zanwu)
    RelativeLayout rl_youhuiquan_zanwu;

    private CommonAdapter adapter = null;
    private List<YouHuiQuanBean> list = new ArrayList<>();
    private final String quanbu = "全部";
    private final String yishiyong = "已使用";
    private final String yitingyong = "已停用";
    private final String jixufashou = "继续发售";
    private final String tingzhi = "停止";
    private String phone = "";
    private String type = quanbu;
    private Boolean refresh = true;
    private int page = 0;
    private int tishu = 0;
    private int click_position;

    @Override
    public int initResource() {
        return R.layout.activity_youhuiquan;
    }

    @Override
    public void initData() {
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        initAdapter();
        tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
        tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
        tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
        refreshlayout_youhuiquan.setOnRefreshListener(this);
        refreshlayout_youhuiquan.setOnLoadListener(this);
    }


    public void initAdapter() {
        adapter = new CommonAdapter<YouHuiQuanBean>(YouHuiQuanActivity.this, R.layout.item_youhuiquan, list) {
            @Override
            public void convert(ViewHolder holder, final YouHuiQuanBean youHuiQuanBean, final int position) {
                //"种类":"店铺","优惠券名称":"零食折扣券",
                // "面额":"50","发行量":"100","生效时间":"2017-06-15",
                // "失效时间":"2017-06-20","状态":"正常","已领":"2"
                if (position == 0)
                    holder.setVisible2(R.id.view_item_yhq_topfill, true);
                else
                    holder.setVisible2(R.id.view_item_yhq_topfill, false);
                holder.setText(R.id.tv_item_yhq_mingcheng, youHuiQuanBean.getYouhuiquanmingcheng());
                holder.setText(R.id.tv_item_yhq_num, youHuiQuanBean.getMiane());

//                holder.setText(R.id.tv_item_yhq_yilingqu,"已领取" + youHuiQuanBean.getYiling() + "张");
//                holder.setText(R.id.tv_item_yhq_yishiyong,"已使用" + youHuiQuanBean.getYishiyong() + "张");
//                holder.setText(R.id.tv_item_yhq_zongliang,"总量" + youHuiQuanBean.getFahangliang() + "张");
                String str_nums = "总量" + youHuiQuanBean.getFahangliang() + "张"
                        + "                已领取" + youHuiQuanBean.getYiling() + "张"
                        + "<font color='#ff7f29'>" + "                已使用" + youHuiQuanBean.getYishiyong() + "张" + "</font>";
                TextView tv = holder.getView(R.id.tv_item_yhq_zongliang);
                tv.setText(Html.fromHtml(str_nums)); //<small>红颜色</small>
                holder.setText(R.id.tv_item_yhq_youxiaoqi, createNewDate(youHuiQuanBean.getShengxiaoshijian(), youHuiQuanBean.getShixiaoshijian()));
                if ("店铺".equals(youHuiQuanBean.getZhonglei())) {
                    holder.setBackgroundRes(R.id.ll_item_youhuixinxi, R.mipmap.youhuiquan_dp);
                } else {
                    holder.setBackgroundRes(R.id.ll_item_youhuixinxi, R.mipmap.youhuiquan_sp);
                }
                if ("已停止".equals(youHuiQuanBean.getZhuangtai())) {
                    holder.setText(R.id.btn_item_yhq_tingzhi, jixufashou);
                } else if ("正常".equals(youHuiQuanBean.getZhuangtai())) {
                    holder.setText(R.id.btn_item_yhq_tingzhi, tingzhi);
                }
                try {
                    if (Integer.parseInt(youHuiQuanBean.getFahangliang()) - Integer.parseInt(youHuiQuanBean.getYiling()) <= 0) {
                        holder.setText(R.id.btn_item_yhq_tingzhi, "发售完毕");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.setOnClickListener(R.id.btn_item_yhq_tingzhi, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //发售完毕为不可点击状态
                        click_position = position;
                        String status = youHuiQuanBean.getZhuangtai();
                        try {
                            if (Integer.parseInt(youHuiQuanBean.getFahangliang()) - Integer.parseInt(youHuiQuanBean.getYiling()) <= 0) {
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        if ("正常".equals(status)) {
                            status = "停止";
                        } else {
                            status = "发售";
                        }
                        requestHandleArrayList.add(requestAction.shop_wl_stopcoupon(YouHuiQuanActivity.this
                                , phone, youHuiQuanBean.getId(), status));
                    }
                });
                if ("无".equals(youHuiQuanBean.getShiyongtiaojian())) {
                    holder.setText(R.id.tv_item_yhq_lingquzhuangtai, "无满额使用");
                } else {
                    holder.setText(R.id.tv_item_yhq_lingquzhuangtai, "满" + youHuiQuanBean.getShiyongtiaojian() + "使用");
                }
            }
        };
        lv_youhuiquan.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh = true;
        requestHandleArrayList.add(requestAction.shop_wl_Coupon(this, phone, "0", type));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_Coupon:
                Log.e(type + "优惠券请求成功", response.toString());
                if (refresh) {
                    Log.e("清除了数据", "true");
                    list.clear();
                    refreshlayout_youhuiquan.setRefreshing(false); //取消刷新动画
                    refresh = false;
                } else {
                    refreshlayout_youhuiquan.setLoading(false);  ////取消加载动画
                }
                page = Integer.parseInt(response.getString("页数"));
                tishu = Integer.parseInt(response.getString("条数"));
                JSONArray jsonArray = response.getJSONArray("列表");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    YouHuiQuanBean youHuiQuanBean = new YouHuiQuanBean();
                    youHuiQuanBean.setId(jsonObject.getString("id"));
                    youHuiQuanBean.setZhonglei(jsonObject.getString("种类"));
                    youHuiQuanBean.setYouhuiquanmingcheng(jsonObject.getString("优惠券名称"));
                    youHuiQuanBean.setMiane(jsonObject.getString("面额"));
//                    if (type.equals(quanbu)) {
                    youHuiQuanBean.setFahangliang(jsonObject.getString("发行量"));
//                    }
                    youHuiQuanBean.setYishiyong(jsonObject.getString("已使用"));
                    youHuiQuanBean.setShiyongtiaojian(jsonObject.getString("使用条件"));
                    youHuiQuanBean.setShengxiaoshijian(jsonObject.getString("生效时间"));
                    youHuiQuanBean.setShixiaoshijian(jsonObject.getString("失效时间"));
                    youHuiQuanBean.setZhuangtai(jsonObject.getString("状态"));
                    youHuiQuanBean.setYiling(jsonObject.getString("已领"));
                    list.add(youHuiQuanBean);
                }
                adapter.notifyDataSetChanged();
                setVisible();
                break;
            case ConstantUtils.TAG_shop_wl_stopcoupon:
//                if (quanbu.equals(type)) {
                YouHuiQuanBean youHuiQuanBean = list.get(click_position);
                if (!"正常".equals(youHuiQuanBean.getZhuangtai())) {
                    youHuiQuanBean.setZhuangtai("正常");
                } else {
                    youHuiQuanBean.setZhuangtai("已停止");
                }
//                } else {
//                    list.remove(click_position);
//                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void setVisible() {
        if (list.size() > 0) {
            lv_youhuiquan.setVisibility(View.VISIBLE);
            rl_youhuiquan_zanwu.setVisibility(View.GONE);
        } else {
            lv_youhuiquan.setVisibility(View.GONE);
            rl_youhuiquan_zanwu.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_twotile_1, R.id.tv_twotile_2, R.id.tv_twotile_3, R.id.iv_twotitle_back, R.id.rl_chuangjiancoupon})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_twotile_1:
                type = quanbu;
                setTitleStyle();
                break;
            case R.id.tv_twotile_2:
                type = yishiyong;
                setTitleStyle();
                break;
            case R.id.tv_twotile_3:
                type = yitingyong;
                setTitleStyle();
                break;
            case R.id.iv_twotitle_back:
                finish();
                break;
            case R.id.rl_chuangjiancoupon:
                Intent intent = new Intent(YouHuiQuanActivity.this, ChuangJianYouHuiQuanActivity.class);
                startActivity(intent);
                break;
        }
    }

    /***
     * 获取拼接新的有效期
     */
    private String createNewDate(String shengxiaoqi, String shixiaoqi) {
        String newriqi;
        String[] shengxiaoqis;
        String[] shixiaoqis;
        try {
            shengxiaoqis = shengxiaoqi.split("-");
            shixiaoqis = shixiaoqi.split("-");
            shengxiaoqi = shengxiaoqi.replace("-", ".");
            if (shengxiaoqis[0].equals(shixiaoqis[0])) {
                newriqi = shengxiaoqi + "-" + shixiaoqis[1] + "." + shixiaoqis[2];
            } else {
                newriqi = shengxiaoqi + "-" + shixiaoqi.replace("-", ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
            newriqi = shengxiaoqi + "-" + shixiaoqi.replace("-", ".");
        }
        return "有效期" + newriqi;
    }

    //点击title变色样式
    public void setTitleStyle() {
        if (quanbu.equals(type)) {
            tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_3_shape_selected);
            tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_3_shape_center);
            tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_3_shape_right);
        } else if (yishiyong.equals(type)) {
            tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_3_shape);
            tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_3_shape_center_selected);
            tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_3_shape_right);
        } else {
            tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_3_shape);
            tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_3_shape_center);
            tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_3_shape_right_selected);
        }
        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresh = true;
        page = 0;
        try {
            requestHandleArrayList.add(requestAction.shop_wl_Coupon(this, phone, String.valueOf(page), type));
            refreshlayout_youhuiquan.removeFoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        Log.e("onLoad", "=======");
        try {
            refresh = false;
            if (tishu >= 10) {
                page++;
                requestHandleArrayList.add(requestAction.shop_wl_Coupon(this, phone, String.valueOf(page), type));
            } else if (tishu > 0) {
                refreshlayout_youhuiquan.setLoading(false);
                refreshlayout_youhuiquan.isNoData();
            } else {
                refreshlayout_youhuiquan.setLoading(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

