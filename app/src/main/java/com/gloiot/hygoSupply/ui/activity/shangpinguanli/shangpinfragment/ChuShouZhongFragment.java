package com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.WodeDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.ViewProductActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.ShangPinShangChuanActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.BroadCastActionUtil;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.ScreenUtil;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;
import com.gloiot.hygoSupply.widget.MySwipeMenuListView;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.gloiot.hygoSupply.widget.XiaJiaDialog;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 功能：出售中的fragment
 * 创建人：zxl on 2017/5/9.版本1.0.1
 */

public class ChuShouZhongFragment extends BaseFragment implements
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private ImageView bottom_leftImg;
    private TextView bottom_RightTxt;
    private String currentFragment;//判断当前是哪个Fragment
    private Context mContext;
    private SharedPreferences preferences;
    private String phone;
    List<ShangpinGuanliBean> allShangpinGuanli = new ArrayList<>();
    private CommonAdapter adapter;
    private RefreshLayout refreshlayout_fg_shangpin_guanli;
    private RelativeLayout rl_fg_shangpin_guanli;
    private MySwipeMenuListView lv_fg_shangpin_guanli;
    private int shengqingPage = 0, xiaoshouPage = 0, xiajiaPage = 0;//申请页数  销售页数 下架页数
    public static int xiaoshouNum = 0; //出售数
    private Boolean refresh;  //true为刷新或第一次加载状态   false加载更多状态
    List<ShangpinGuanliBean> xuanzhongArray = new ArrayList<>(); //上下架选中的list集合商品
    List<ShangpinGuanliBean> zuohuadong_list = new ArrayList<>(); //左滑动单选下架选中的list集合商品
    private final String chushouzhong = "出售中";
    private boolean lefthuadong_danxuan = false;
    SwipeMenuCreator zuohuadongCreator = null;
    SwipeMenuCreator jinzhiZuohuadongCreator = null;
    public static boolean isRefush;


    private EditText et_shangpin_guanli_search;
    private ImageView iv_delete_icon;


    @Override
    public void onResume() {
        super.onResume();
        if (isRefush) {
            onRefresh();
            isRefush = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (chushouzhong.equals(currentFragment)) {//在销售商品界面点了下架商品后,在下架商品页面变为可见时，刷新已预加载的下架商品页面
                if (getActivity() != null) {
                    xuan_zhuangtai(-1);
                }
                refresh = true;
                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), "0"));
            } else if (chushouzhong.equals(currentFragment)) {

            } else {

            }
        } else {
            if (getActivity() != null) {
                xuan_zhuangtai(-1);
                adapter.notifyDataSetChanged();
                fragment = "-1";
            }
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
        setZuoHuadong();
        setJinZhiZuohuadong();
        currentFragment = getArguments().getString("text");
        preferences = SharedPreferencesUtils.getInstance().getSharedPreferences();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        initAdapter(zuohuadongCreator);
        requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), String.valueOf(xiajiaPage)));
        refreshlayout_fg_shangpin_guanli.setOnRefreshListener(this);
        refreshlayout_fg_shangpin_guanli.setOnLoadListener(this);
        bottom_leftImg = (ImageView) getActivity().findViewById(R.id.spgl_quanxuan_img);
        bottom_RightTxt = (TextView) getActivity().findViewById(R.id.spgl_xiajia_txt);
        registerBroadCast();
        setListVisble();
        initSearch();
    }

    public void initComponent(View view) {
        refreshlayout_fg_shangpin_guanli = (RefreshLayout) view.findViewById(R.id.refreshlayout_fg_shangpin_guanli);
        lv_fg_shangpin_guanli = (MySwipeMenuListView) view.findViewById(R.id.lv_fg_shangpin_guanli);
        lv_fg_shangpin_guanli.setmRefreshLayout(refreshlayout_fg_shangpin_guanli);
        rl_fg_shangpin_guanli = (RelativeLayout) view.findViewById(R.id.rl_fg_shangpin_guanli);
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


    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sx_manages:
                Log.e("出售中商品请求成功", jsonObject.toString());
                String id;    //  商品ID   销售和下架独有
                String shangpinmingcheng;//商品民称
                String jiesuanjia;//结算价
                String kucun;//库存
                String zhuangtai; //状态    申请独有
                String suoluetu;//缩略图
                String xiaoshou = null;//销售   销售和下架独有
                if (refresh) {
                    Log.e("清除了数据", "true");
                    initAdapter(zuohuadongCreator);
                    allShangpinGuanli.clear();
                    refreshlayout_fg_shangpin_guanli.setRefreshing(false);
                } else {
                    refreshlayout_fg_shangpin_guanli.setLoading(false);
                }
                if (chushouzhong.equals(currentFragment)) {
                    xiaoshouNum = 0;
                    xiaoshouNum = Integer.parseInt(jsonObject.getString("销售商品条数"));
                    if (xiaoshouNum > 0) {  //出售商品有数据
                        rl_fg_shangpin_guanli.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("销售商品");
                        Log.e("出售中商品数组长度", jsonArray.length() + "");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject xiaoshouShangpin = jsonArray.getJSONObject(i);
                            Log.e("出售中商品", xiaoshouShangpin.toString());

                            id = xiaoshouShangpin.getString("id");
                            shangpinmingcheng = xiaoshouShangpin.getString("商品名称");
                            kucun = xiaoshouShangpin.getString("库存");

                            suoluetu = xiaoshouShangpin.getString("缩略图");

                            if (xiaoshouShangpin.getString("销售").equals("")) {
                                xiaoshou = "0";
                            } else {
                                xiaoshou = xiaoshouShangpin.getString("销售");
                            }
                            jiesuanjia = xiaoshouShangpin.getString("建议零售价");
                            ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, jiesuanjia, kucun, "", suoluetu, xiaoshou);
                            if ("全球购".equals(xiaoshouShangpin.getString("类型")) || "全球购-自营".equals(xiaoshouShangpin.getString("类型"))) {
                                shenqingBean.setQuanqiugou(true);
                            }
                            allShangpinGuanli.add(shenqingBean);
                        }
                    } else {
                        rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();
                setListVisble();
                break;
            case ConstantUtils.TAG_shop_wl_shelevs://商品下架请求
                Log.e("商品下架请求成功", jsonObject.toString());
                MToastUtils.showToast("下架成功");
                if (lefthuadong_danxuan) {
                    xiaoshouNum = xiaoshouNum - 1;
                    allShangpinGuanli.removeAll(zuohuadong_list);
                    adapter.notifyDataSetChanged();
                    lefthuadong_danxuan = false;
                } else {
                    try {
                        xiaoshouNum = xiaoshouNum - xuanzhongArray.size();
                        refreshlayout_fg_shangpin_guanli.removeFoot();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    allShangpinGuanli.removeAll(xuanzhongArray);
                    adapter.notifyDataSetChanged();
                    if (allShangpinGuanli.size() <= 0) {
                        mTitleListener.initShangjiaShuxing();
                    } else {
                        bottom_leftImg.setImageResource(R.mipmap.icon_not_to_choose);
                        bottom_RightTxt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.xiajia_default_bg));
                    }
                    xuanzhongArray.clear();
                }
                setListVisble();
                break;
        }
    }

    public void initAdapter(SwipeMenuCreator creator) {
        adapter = new CommonAdapter<ShangpinGuanliBean>(getActivity(), R.layout.item_shangpin_guanli, allShangpinGuanli) {
            @Override
            public void convert(ViewHolder holder, final ShangpinGuanliBean shangpinGuanliBean, final int position) {
                final ImageView picView = holder.getView(R.id.item_shangpin_guanli_leftpic);
                final ImageView selectedImg = holder.getView(R.id.item_shangpin_guanli_selctedpic);
                if (chushouzhong.equals(currentFragment)) {
                    holder.setText(R.id.item_shangpin_guanli_name, shangpinGuanliBean.getShangpinmingcheng());
                    holder.setText(R.id.item_shangpin_guanli_price, "￥ " + shangpinGuanliBean.getDanjia());
                    holder.setText(R.id.item_shangpin_guanli_kucun, "库存:  " + shangpinGuanliBean.getKucun());
                    holder.setText(R.id.item_shangpin_guanli_sale, "销量:  " + shangpinGuanliBean.getXiaoshou());
                    holder.setVisible2(R.id.item_shangpin_guanli_sale, true);

                    holder.setVisible2(R.id.item_shangpin_guanli_quanqiugou, shangpinGuanliBean.isQuanqiugou());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
                        }
                    });
                }
                //点击标题右边上下架处理
                if (0 == shangpinGuanliBean.getIs_selected()) {
                    selectedImg.setVisibility(View.VISIBLE);
                    holder.setImageResource(R.id.item_shangpin_guanli_selctedpic, R.mipmap.icon_not_to_choose);
                } else if (1 == shangpinGuanliBean.getIs_selected()) {
                    selectedImg.setVisibility(View.VISIBLE);
                    holder.setImageResource(R.id.item_shangpin_guanli_selctedpic, R.mipmap.icon_the_selected);
                } else {
                    selectedImg.setVisibility(View.GONE);
                }
                holder.setOnClickListener(R.id.item_shangpin_guanli_selctedpic, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shangpinGuanliBean.getIs_selected() == 0) {
                            shangpinGuanliBean.setIs_selected(1);
                            xuanzhongArray.add(shangpinGuanliBean);
                        } else if (shangpinGuanliBean.getIs_selected() == 1) {
                            shangpinGuanliBean.setIs_selected(0);
                            xuanzhongArray.remove(shangpinGuanliBean);
                        }
                        if (xuanzhongArray.size() == allShangpinGuanli.size()) {
                            bottom_leftImg.setImageResource(R.mipmap.icon_the_selected);
                        } else {
                            bottom_leftImg.setImageResource(R.mipmap.icon_not_to_choose);
                        }
                        if (xuanzhongArray.size() > 0) {
                            bottom_RightTxt.setBackgroundColor(getActivity().getResources().getColor(R.color.cl_ff7f29));
                        } else {
                            bottom_RightTxt.setBackgroundColor(getActivity().getResources().getColor(R.color.xiajia_default_bg));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

//                holder.setOnClickListener(R.id.item_shangpin_guanli_top, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }
        };
        lv_fg_shangpin_guanli.setAdapter(adapter);
        lv_fg_shangpin_guanli.setMenuCreator(creator);
        lv_fg_shangpin_guanli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ViewProductActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("showbottom", "sell");
                intent.putExtra("productId", allShangpinGuanli.get(position).getId());
                startActivity(intent);
            }
        });
        lv_fg_shangpin_guanli.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // 编辑
                        Intent intent = new Intent(mContext, ShangPinShangChuanActivity.class);
                        intent.putExtra("type", "edit");
                        intent.putExtra("productId", allShangpinGuanli.get(position).getId());
                        startActivity(intent);
                        break;
                    case 1:
                        // 下架
                        zuohuadong_list.clear();
                        zuohuadong_list.add(allShangpinGuanli.get(position));
                        lefthuadong_danxuan = true;
                        xiajia_dialog();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        setListVisble();
    }

    public void setZuoHuadong() {
        zuohuadongCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        setBianji(menu);
                        setXiajia(menu);
                        break;
                    case 1:
                        break;
                }
            }
        };
    }

    public void setJinZhiZuohuadong() {
        jinzhiZuohuadongCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
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
        item.setTitle("下架");
        item.setTitleSize(14);
        item.setBackground(new ColorDrawable((ContextCompat.getColor(getActivity(), R.color.cl_ff4e44))));
        item.setTitleColor(Color.WHITE);
//                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.c_333333)));
        // add to menu
        menu.addMenuItem(item);
    }

    public void setBianji(SwipeMenu menu) {
        SwipeMenuItem item = new SwipeMenuItem(getActivity());
        // set item width
        item.setWidth(ScreenUtil.getInstance().dip2px(getActivity(), 60));
        // 添加删除按钮图片
        item.setTitle("编辑");
        item.setTitleSize(14);
        item.setTitleColor(Color.WHITE);
        item.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.cl_ff980b)));
        // add to menu
        menu.addMenuItem(item);
    }


    @Override
    public void onRefresh() {
        mTitleListener.initShangjiaShuxing();
        xuanzhongArray.clear();
        refresh = true;
        xiaoshouPage = 0;
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
            if (xiaoshouNum >= 10) {
                xiaoshouPage++;
                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", String.valueOf(xiaoshouPage), "0"));
            } else if (xiaoshouNum > 0) {
                refreshlayout_fg_shangpin_guanli.setLoading(false);
                refreshlayout_fg_shangpin_guanli.isNoData();
            } else {
                refreshlayout_fg_shangpin_guanli.setLoading(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    XiaJiaDialog mXiaJiaDialog;
    String xiajiaContent;

    private void xiajia_dialog() {
        mXiaJiaDialog = new XiaJiaDialog(getActivity(), R.style.dialogshow, new XiaJiaDialog.ClickListenerInterface() {
            @Override
            public void cancle() {
                mXiaJiaDialog.dismiss();
                lefthuadong_danxuan = false;
                zuohuadong_list.clear();
            }

            @Override
            public void doConfirm(String xiajia_yuanyin) {
                mXiaJiaDialog.clearEdidtext();
                mXiaJiaDialog.dismiss();
                String zhanghao = preferences.getString(ConstantUtils.SP_ZHANGHAO, "");
                if (lefthuadong_danxuan) {
                    requestHandleArrayList.add(requestAction.shop_wl_shelevs(ChuShouZhongFragment.this, zhanghao, zuohuadong_list, xiajia_yuanyin));
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_shelevs(ChuShouZhongFragment.this, zhanghao, xuanzhongArray, xiajia_yuanyin));
                }
            }

            @Override
            public void addEditContentListten(String content, int length) {
                xiajiaContent = content;
            }
        });
        mXiaJiaDialog.show();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getActivity().unregisterReceiver(xiajiaChangReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void xuan_zhuangtai(int selected) {
        xuanzhongArray.clear();
        for (int i = 0; i < allShangpinGuanli.size(); i++) {
            allShangpinGuanli.get(i).setIs_selected(selected);
            if (1 == selected) {
                xuanzhongArray.add(allShangpinGuanli.get(i));
            }
        }
        if (1 != selected) {
            bottom_RightTxt.setBackgroundColor(getActivity().getResources().getColor(R.color.xiajia_default_bg));
            bottom_leftImg.setImageResource(R.mipmap.icon_not_to_choose);
        } else {
            bottom_RightTxt.setBackgroundColor(getActivity().getResources().getColor(R.color.cl_ff7f29));
        }
    }

    public void setListVisble() {
        if (allShangpinGuanli == null || allShangpinGuanli.size() == 0) {
            rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
        } else {
            rl_fg_shangpin_guanli.setVisibility(View.GONE);
        }
    }


    public interface ChuShouZhongFragmentBackCalled {
        void initShangjiaShuxing();
    }

    private ChuShouZhongFragmentBackCalled mTitleListener;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mTitleListener = (ChuShouZhongFragmentBackCalled) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnArticleSelectedListener");
        }
    }


    private String fragment = "0";//点击的当前fragment
    //定义一个过滤器；
    private IntentFilter intentFilter;

    //定义一个广播监听器；
    private XiajiaChangReceiver xiajiaChangReceiver;

    public void registerBroadCast() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastActionUtil.XIAJIA_ACTION);
        intentFilter.addAction(BroadCastActionUtil.XUANZHONG_ACTION);
        intentFilter.addAction(BroadCastActionUtil.XIAJIA_TIJIAO_ACTION);
        xiajiaChangReceiver = new XiajiaChangReceiver();
        mContext.registerReceiver(xiajiaChangReceiver, intentFilter);
    }

    class XiajiaChangReceiver extends BroadcastReceiver {

        //重写onReceive方法，该方法的实体为，接收到广播后的执行代码；
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String start_shangjia = intent.getStringExtra("start_shangjia");
            fragment = intent.getStringExtra("fragment");
            if (!"1".equals(fragment)) return;
            if (BroadCastActionUtil.XIAJIA_ACTION.equals(action)) {
                if ("-1".equals(start_shangjia)) {
                    xuan_zhuangtai(-1);//取消下架模式
                    initAdapter(zuohuadongCreator);
                } else {
                    xuan_zhuangtai(0);//开启下架模式
                    initAdapter(jinzhiZuohuadongCreator);
                }
            } else if (BroadCastActionUtil.XUANZHONG_ACTION.equals(action)) {
                if ("1".equals(fragment)) {
                    if (xuanzhongArray.size() == allShangpinGuanli.size()) {
                        xuan_zhuangtai(0);//取消全选模式
                        bottom_leftImg.setImageResource(R.mipmap.icon_not_to_choose);
                    } else {
                        xuan_zhuangtai(1);//全选模式
                        bottom_leftImg.setImageResource(R.mipmap.icon_the_selected);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (BroadCastActionUtil.XIAJIA_TIJIAO_ACTION.equals(action)) {
                if (xuanzhongArray.size() == 0) return;
                if (xiajiaContent == null) {
                    xiajia_dialog();
                } else {
                    mXiaJiaDialog.show();
                }
            }
        }
    }

}
