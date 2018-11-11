package com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.ViewProductActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.ShangPinShangChuanActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.ScreenUtil;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;
import com.gloiot.hygoSupply.widget.MySwipeMenuListView;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 功能：审核中的fragment
 * 创建人：zxl on 2017/5/9.版本1.0.1
 */
public class ShenHeZhongFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private String currentFragment;//判断当前是哪个Fragment
    private SharedPreferences preferences;
    private String phone;
    List<ShangpinGuanliBean> allShangpinGuanli = new ArrayList<>();
    private CommonAdapter adapter;
    private RefreshLayout refreshlayout_fg_shangpin_guanli;
    private RelativeLayout rl_fg_shangpin_guanli;
    private MySwipeMenuListView lv_fg_shangpin_guanli;
    private int shengqingPage = 0, xiaoshouPage = 0, xiajiaPage = 0, //申请页数  销售页数 下架页数
            shengqingNum = 0; //申请数  销售数 下架数
    private Boolean refresh;  //true为刷新或第一次加载状态   false加载更多状态
    private final String shenhezhong = "审核中";
    public static boolean isRefush;
    SwipeMenuCreator zuohuadongCreator;
    private EditText et_shangpin_guanli_search;
    private ImageView iv_delete_icon;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (shenhezhong.equals(currentFragment)) {//在销售商品界面点了下架商品后,在下架商品页面变为可见时，刷新已预加载的下架商品页面
                refresh = true;
                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), "0"));
            } else {
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefush) {
            onRefresh();
            isRefush = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shangpin_guanli, container, false);
        ButterKnife.bind(this, view);
        initComponent(view);
        initData();
        return view;
    }


    public void initData() {
        mContext = getActivity();
        refresh = true;
        currentFragment = getArguments().getString("text");
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        setZuoHuadong();
        initAdapter();
        initSearch();
        requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), String.valueOf(xiajiaPage)));
        refreshlayout_fg_shangpin_guanli.setOnRefreshListener(this);
        refreshlayout_fg_shangpin_guanli.setOnLoadListener(this);
    }

    public void initComponent(View view) {
        refreshlayout_fg_shangpin_guanli = (RefreshLayout) view.findViewById(R.id.refreshlayout_fg_shangpin_guanli);
        lv_fg_shangpin_guanli = (MySwipeMenuListView) view.findViewById(R.id.lv_fg_shangpin_guanli);
        rl_fg_shangpin_guanli = (RelativeLayout) view.findViewById(R.id.rl_fg_shangpin_guanli);
        lv_fg_shangpin_guanli.setmRefreshLayout(refreshlayout_fg_shangpin_guanli);
        et_shangpin_guanli_search = (EditText) view.findViewById(R.id.et_shangpin_guanli_search);
        iv_delete_icon = (ImageView) view.findViewById(R.id.iv_delete_icon);
    }

    //搜索栏
    private void initSearch() {
        et_shangpin_guanli_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!"".equals(et_shangpin_guanli_search.getText().toString().trim())) {
//                        requestHandleArrayList.add(requestAction.shop_sj_sou(WodeDianpuActivity.this, phone, et_sousuo.getText().toString()));
                        MToastUtils.showToast("开始搜索啦");
                    } else {
                        MToast.showToast("请输入搜索内容");
                    }
                    return true;
                }
                return false;
            }
        });

        iv_delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_shangpin_guanli_search.setText("");
            }
        });
    }
    public void setListVisble() {
        if (allShangpinGuanli == null || allShangpinGuanli.size() == 0) {
            rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
        } else {
            rl_fg_shangpin_guanli.setVisibility(View.GONE);
        }
    }

    public void setZuoHuadong() {
        zuohuadongCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                Log.e("TAG", "create: " + menu.getViewType());
                switch (menu.getViewType()) {
                    case 0:
                        setXiajia(menu);
                        break;
                    case 1:

                        break;
                }
            }
        };
    }

    public void setXiajia(SwipeMenu menu) {
        SwipeMenuItem item = new SwipeMenuItem(getActivity());
        // set item width
        item.setWidth(ScreenUtil.getInstance().dip2px(getActivity(), 60));
        // 添加删除按钮图片
        item.setTitle("删除");
        item.setTitleSize(14);
        item.setBackground(new ColorDrawable((ContextCompat.getColor(getActivity(), R.color.cl_ff4e44))));
        item.setTitleColor(Color.WHITE);
//                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.c_333333)));
        // add to menu
        menu.addMenuItem(item);
    }

    public void initAdapter() {
        adapter = new CommonAdapter<ShangpinGuanliBean>(getActivity(), R.layout.item_shangpin_guanli, allShangpinGuanli) {
            @Override
            public void convert(ViewHolder holder, final ShangpinGuanliBean shangpinGuanliBean, final int position) {
                final ImageView picView = holder.getView(R.id.item_shangpin_guanli_leftpic);
                if ("审核中".equals(currentFragment)) {
                    holder.setText(R.id.item_shangpin_guanli_name, shangpinGuanliBean.getShangpinmingcheng());
                    holder.setText(R.id.item_shangpin_guanli_price, "￥ " + shangpinGuanliBean.getDanjia());
                    holder.setText(R.id.item_shangpin_guanli_kucun, "库存：  " + shangpinGuanliBean.getKucun());
                    holder.setVisible2(R.id.item_shangpin_guanli_quanqiugou, shangpinGuanliBean.isQuanqiugou());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
                        }
                    });
                    holder.setVisible(R.id.btn_item_shangpin_guanli_shenhe_status, true);
                    if ("审核中".equals(shangpinGuanliBean.getZhuangtai())) {
                        holder.setBackgroundRes(R.id.btn_item_shangpin_guanli_shenhe_status, R.drawable.bg_btn_shangpin_guanli_fillet04);
                    } else {
                        holder.setBackgroundRes(R.id.btn_item_shangpin_guanli_shenhe_status, R.drawable.bg_btn_shangpin_guanli_fillet05);
                    }
                    holder.setText(R.id.btn_item_shangpin_guanli_shenhe_status, shangpinGuanliBean.getZhuangtai());

                    holder.setOnClickListener(R.id.btn_item_shangpin_guanli_shenhe_status, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("审核失败".equals(shangpinGuanliBean.getZhuangtai())) {

                                DialogUtlis.twoBtnNormal(mContext, shangpinGuanliBean.getZuofeiyuanyin(), "审核失败", true, "确定", "去修改", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialogNoAnimator();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialogNoAnimator();
                                        Intent intent = new Intent(mContext, ShangPinShangChuanActivity.class);
                                        intent.putExtra("type", "edit");
                                        intent.putExtra("productId", shangpinGuanliBean.getId());
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                Intent intent = new Intent(mContext, ViewProductActivity.class);
                                intent.putExtra("type", "edit");
                                intent.putExtra("showbottom", "apply");
                                intent.putExtra("productId", allShangpinGuanli.get(position).getId());
                                startActivity(intent);
                            }
                        }
                    });

//                    holder.setOnClickListener(R.id.rl_item_shangpin_guanli, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if ("审核中".equals(allShangpinGuanli.get(position).getZhuangtai())) {
//                                Intent intent = new Intent(mContext, ViewProductActivity.class);
//                                intent.putExtra("type", "edit");
//                                intent.putExtra("showbottom", "apply");
//                                intent.putExtra("productId", allShangpinGuanli.get(position).getId());
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(mContext, ViewProductActivity.class);
//                                intent.putExtra("type", "edit");
//                                intent.putExtra("showbottom", "refuse");
//                                intent.putExtra("refuseProduct", allShangpinGuanli.get(position));
//                                intent.putExtra("productId", allShangpinGuanli.get(position).getId());
//                                startActivity(intent);
//                            }
//                        }
//                    });

                }
                //点击标题右边上下架处理
                holder.setVisible2(R.id.item_shangpin_guanli_selctedpic, false);
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                if ("审核失败".equals(allShangpinGuanli.get(position).getZhuangtai())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        lv_fg_shangpin_guanli.setAdapter(adapter);
        lv_fg_shangpin_guanli.setMenuCreator(zuohuadongCreator);
        lv_fg_shangpin_guanli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("审核中".equals(allShangpinGuanli.get(position).getZhuangtai())) {
                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("showbottom", "apply");
                    intent.putExtra("productId", allShangpinGuanli.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("type", "edit");
                    intent.putExtra("showbottom", "refuse");
                    intent.putExtra("refuseProduct", allShangpinGuanli.get(position));
                    intent.putExtra("productId", allShangpinGuanli.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        lv_fg_shangpin_guanli.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 删除
                        if ("审核失败".equals(allShangpinGuanli.get(position).getZhuangtai())) {
                            DialogUtlis.twoBtnNormal(getActivity(), "是否删除该商品信息？", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestHandleArrayList.add(requestAction.shop_wl_delshops(ShenHeZhongFragment.this, phone, allShangpinGuanli.get(position).getId()));
                                }
                            });
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        setListVisble();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sx_manages:
                Log.e("商品管理请求成功", jsonObject.toString());
                String id;    //  商品ID   销售和下架独有
                String shangpinmingcheng;//商品民称
                String jiesuanjia;//结算价
                String kucun;//库存
                String zhuangtai; //状态    申请独有
                String suoluetu;//缩略图
                String zuofeiyuanyin;//作废原因
                String zuofeishijian;//作废时间
                String lurushijian;//录入时间
                if (refresh) {
                    Log.e("清除了数据", "true");
                    allShangpinGuanli.clear();
                    refreshlayout_fg_shangpin_guanli.setRefreshing(false); //取消刷新动画
                } else {
                    refreshlayout_fg_shangpin_guanli.setLoading(false);  ////取消加载动画
                }
                if ("审核中".equals(currentFragment)) {
                    shengqingNum = 0;
                    shengqingNum = Integer.parseInt(jsonObject.getString("申请商品条数"));
                    Log.e("shengqingNum", shengqingNum + "");
                    if (shengqingNum > 0) {
                        rl_fg_shangpin_guanli.setVisibility(View.GONE);//申请商品有数据
                        Log.e("shengqingNum", shengqingNum + "------");
                        JSONArray jsonArray = jsonObject.getJSONArray("申请商品");
                        Log.e("申请商品数组长度", jsonArray.length() + "");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject shenqingShangpin = jsonArray.getJSONObject(i);
                            id = shenqingShangpin.getString("id");
                            shangpinmingcheng = shenqingShangpin.getString("商品名称");
                            kucun = shenqingShangpin.getString("库存");
                            zhuangtai = shenqingShangpin.getString("状态");
                            suoluetu = shenqingShangpin.getString("缩略图");
                            jiesuanjia = shenqingShangpin.getString("建议零售价");
                            ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, "", kucun, zhuangtai, suoluetu, "");
                            try {
                                zuofeiyuanyin = shenqingShangpin.getString("作废原因");
                                zuofeishijian = shenqingShangpin.getString("作废时间");
                                shenqingBean.setZuofeiyuanyin(zuofeiyuanyin);
                                shenqingBean.setZuofeishijian(zuofeishijian);
                            } catch (Exception e) {

                            }

                            lurushijian = shenqingShangpin.getString("录入时间");
                            shenqingBean.setDanjia(jiesuanjia);
                            shenqingBean.setLurushijian(lurushijian);
                            if ("全球购".equals(shenqingShangpin.getString("类型")) || "全球购-自营".equals(shenqingShangpin.getString("类型"))) {
                                shenqingBean.setQuanqiugou(true);
                            }
                            allShangpinGuanli.add(shenqingBean);

                        }
                    } else {
                        rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    setListVisble();
                }
                break;
            case ConstantUtils.TAG_shop_wl_delshops:
                MToastUtils.showToast("删除成功");
                DialogUtlis.dismissDialogNoAnimator();
                onRefresh();
                break;
        }
        if (shengqingNum > 9) {
            refreshlayout_fg_shangpin_guanli.setOnLoadListener(this);
        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        refresh = true;
        shengqingPage = 0;
        requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", "0", "0"));
        try {
            refreshlayout_fg_shangpin_guanli.removeFoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {//上拉加载更多
        Log.e("onLoad", "=======");
        try {
            refresh = false;
            switch (currentFragment) {
                case "审核中":
                    if (shengqingNum >= 10) {
                        shengqingPage++;
                        requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), "0", "0"));
                    } else if (shengqingNum > 0) {
                        refreshlayout_fg_shangpin_guanli.setLoading(false);
                        refreshlayout_fg_shangpin_guanli.isNoData();
                    } else {
                        refreshlayout_fg_shangpin_guanli.setLoading(false);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
