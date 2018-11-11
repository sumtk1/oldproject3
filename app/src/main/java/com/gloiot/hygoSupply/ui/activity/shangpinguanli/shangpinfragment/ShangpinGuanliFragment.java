//package com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.gloiot.hygoSupply.R;
//import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
//import com.gloiot.hygoSupply.ui.activity.BaseFragment;
//import com.gloiot.hygoSupply.utlis.CommonUtils;
//import com.gloiot.hygoSupply.utlis.ConstantUtils;
//import com.gloiot.hygoSupply.widget.RefreshLayout;
//import com.zyd.wlwsdk.adapter.ViewHolder;
//import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
//import com.zyd.wlwsdk.utlis.MToast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//
//public class ShangpinGuanliFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
//    @Bind(R.id.lv_fg_shangpin_guanli)
//    ListView lv_fg_shangpin_guanli;
//    @Bind(R.id.refreshlayout_fg_shangpin_guanli)
//    RefreshLayout refreshlayout_fg_shangpin_guanli;
//    @Bind(R.id.rl_fg_shangpin_guanli)
//    RelativeLayout rl_fg_shangpin_guanli;
//
//    private String currentFragment;//判断当前是哪个Fragment
//    private String phone;
//    List<ShangpinGuanliBean> allShangpinGuanli = new ArrayList<>();
//    private CommonAdapter adapter;
//    private int shengqingPage = 0, xiaoshouPage = 0, xiajiaPage = 0, //申请页数  销售页数 下架页数
//            shengqingNum = 0, xiaoshouNum = 0, xiajiaNum = 0; //申请数  销售数 下架数
//    private Boolean refresh;  //true为刷新或第一次加载状态   false加载更多状态
//    private ShangpinGuanliBean deleteBean;//记录下架的条目用来刷新界面
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_shangpin_guanli, container, false);
//        ButterKnife.bind(this, view);
//
//        mContext = getActivity();
//        refresh = true;
//        currentFragment = getArguments().getString("text");
//        phone = preferences.getString(ConstantUtils.SP_MYID, "");
//        if (!"下架商品".equals(currentFragment)) {
//            requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), String.valueOf(xiajiaPage)));
//        }
//        refreshlayout_fg_shangpin_guanli.setOnRefreshListener(this);
//        refreshlayout_fg_shangpin_guanli.setOnLoadListener(this);
//        return view;
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            if ("下架商品".equals(currentFragment)) {//在销售商品界面点了下架商品后,在下架商品页面变为可见时，刷新已预加载的下架商品页面
//                allShangpinGuanli.clear();
//                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), String.valueOf(xiaoshouPage), "0"));
//            }
//        }
//    }
//
//
//    @Override
//    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
//        super.requestSuccess(requestTag, jsonObject, showLoad);
//        switch (requestTag) {
//            case ConstantUtils.TAG_shop_sx_manages:
//                Log.e("商品管理请求成功", jsonObject.toString());
//                String id;    //  商品ID   销售和下架独有
//                String shangpinmingcheng;//商品民称
//                String jiesuanjia;//结算价
//                String kucun;//库存
//                String zhuangtai; //状态    申请独有
//                String suoluetu;//缩略图
//                String xiaoshou = null;//销售   销售和下架独有
//                if (refresh) {
//                    Log.e("清除了数据", "true");
//                    allShangpinGuanli.clear();
//                    refreshlayout_fg_shangpin_guanli.setRefreshing(false);
//                } else {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                }
//                if ("申请商品".equals(currentFragment)) {
//                    shengqingNum = 0;
//                    shengqingNum = Integer.parseInt(jsonObject.getString("申请商品条数"));
//                    Log.e("shengqingNum", shengqingNum + "");
//                    if (shengqingNum > 0) {
//                        rl_fg_shangpin_guanli.setVisibility(View.GONE);//申请商品有数据
//                        Log.e("shengqingNum", shengqingNum + "------");
//                        JSONArray jsonArray = jsonObject.getJSONArray("申请商品");
//                        Log.e("申请商品数组长度", jsonArray.length() + "");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject shenqingShangpin = jsonArray.getJSONObject(i);
//                            id = shenqingShangpin.getString("id");
//                            shangpinmingcheng = shenqingShangpin.getString("商品名称");
//                            kucun = shenqingShangpin.getString("库存");
//                            zhuangtai = shenqingShangpin.getString("状态");
//                            suoluetu = shenqingShangpin.getString("缩略图");
//                            jiesuanjia = shenqingShangpin.getString("价格");
//                            ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, jiesuanjia, kucun, zhuangtai, suoluetu, "");
//                            allShangpinGuanli.add(shenqingBean);
//                            Log.e("申请商品", "申请商品" + allShangpinGuanli);
//                            Log.e("aaa", allShangpinGuanli.toString());
//
//                        }
//                    } else {
//                        rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
//                    }
//
//                } else if ("销售商品".equals(currentFragment)) {
//                    xiaoshouNum = 0;
//                    xiaoshouNum = Integer.parseInt(jsonObject.getString("销售商品条数"));
//                    if (xiaoshouNum > 0) {  //销售商品有数据
//                        rl_fg_shangpin_guanli.setVisibility(View.GONE);
//                        JSONArray jsonArray = jsonObject.getJSONArray("销售商品");
//                        Log.e("销售商品数组长度", jsonArray.length() + "");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject xiaoshouShangpin = jsonArray.getJSONObject(i);
//                            id = xiaoshouShangpin.getString("id");
//                            shangpinmingcheng = xiaoshouShangpin.getString("商品名称");
//                            kucun = xiaoshouShangpin.getString("库存");
//                            suoluetu = xiaoshouShangpin.getString("缩略图");
//                            xiaoshou = xiaoshouShangpin.getString("销售");
//                            if (xiaoshou.equals("")) {
//                                xiaoshou = "0";
//                            }
//                            jiesuanjia = xiaoshouShangpin.getString("价格");
//                            ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, jiesuanjia, kucun, "", suoluetu, xiaoshou);
//                            allShangpinGuanli.add(shenqingBean);
//                        }
//
//                    } else {
//                        rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
//                    }
//                } else if ("下架商品".equals(currentFragment)) {
//                    xiajiaNum = 0;
//                    xiajiaNum = Integer.parseInt(jsonObject.getString("下架商品条数"));
//                    if (xiajiaNum > 0) {  //下架商品有数据
//                        rl_fg_shangpin_guanli.setVisibility(View.GONE);
//                        JSONArray jsonArray = jsonObject.getJSONArray("下架商品");
//                        Log.e("下架商品数组长度", jsonArray.length() + "");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject xiaoshouShangpin = jsonArray.getJSONObject(i);
//                            Log.e("xiaoshouShangpin", xiaoshouShangpin.toString());
//
//                            id = xiaoshouShangpin.getString("id");
//                            shangpinmingcheng = xiaoshouShangpin.getString("商品名称");
//                            kucun = xiaoshouShangpin.getString("库存");
//
//                            suoluetu = xiaoshouShangpin.getString("缩略图");
//
//                            if (xiaoshouShangpin.getString("销售").equals("")) {
//                                xiaoshou = "0";
//                            } else {
//                                xiaoshou = xiaoshouShangpin.getString("销售");
//                            }
//                            jiesuanjia = xiaoshouShangpin.getString("价格");
//                            ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, jiesuanjia, kucun, "", suoluetu, xiaoshou);
//                            allShangpinGuanli.add(shenqingBean);
//                        }
//                    } else {
//                        rl_fg_shangpin_guanli.setVisibility(View.VISIBLE);
//                    }
//
//                }
//
//                if (refresh) {    //下拉刷新或第一次加载数据
//                    adapter = new CommonAdapter<ShangpinGuanliBean>(getActivity(), R.layout.item_shangpin_guanli, allShangpinGuanli) {
//                        @Override
//                        public void convert(ViewHolder holder, final ShangpinGuanliBean shangpinGuanliBean) {
//                            final ImageView picView = holder.getView(R.id.item_shangpin_guanli_leftpic);
//                            if ("申请商品".equals(currentFragment)) {
//                                holder.setText(R.id.item_shangpin_guanli_name, shangpinGuanliBean.getShangpinmingcheng());
//                                holder.setText(R.id.item_shangpin_guanli_price, "￥ " + shangpinGuanliBean.getDanjia());
//                                holder.setText(R.id.item_shangpin_guanli_xiaoliang, "库存：  " + shangpinGuanliBean.getKucun());
//                                holder.setText(R.id.item_shangpin_guanli_zhuangtai, shangpinGuanliBean.getZhuangtai());
//                                holder.setVisible(R.id.item_shangpin_guanli_kucun, false);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
//                                    }
//                                });
//
//                            } else if ("销售商品".equals(currentFragment)) {
//                                holder.setText(R.id.item_shangpin_guanli_name, shangpinGuanliBean.getShangpinmingcheng());
//                                holder.setText(R.id.item_shangpin_guanli_price, "￥ " + shangpinGuanliBean.getDanjia());
//                                holder.setText(R.id.item_shangpin_guanli_xiaoliang, "销量：  " + shangpinGuanliBean.getxiaoshou());
//                                holder.setVisible(R.id.item_shangpin_guanli_zhuangtai, false);
//                                holder.setText(R.id.item_shangpin_guanli_kucun, "库存：  " + shangpinGuanliBean.getKucun());
//                                holder.setVisible(R.id.item_shangpin_guanli_bianji, false);
//                                holder.setVisible(R.id.item_shangpin_guanli_xiajia, true);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
//                                    }
//                                });
////                                CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
//                                holder.setOnClickListener(R.id.item_shangpin_guanli_xiajia, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Log.e("点击了商品下架", shangpinGuanliBean.getId() + "------");
//
//                                        requestHandleArrayList.add(requestAction.shop_sx_manages_out(ShangpinGuanliFragment.this, phone, shangpinGuanliBean.getId()));
//                                        deleteBean = shangpinGuanliBean;
//                                    }
//                                });
//                            } else if ("下架商品".equals(currentFragment)) {
//                                holder.setText(R.id.item_shangpin_guanli_name, shangpinGuanliBean.getShangpinmingcheng());
//                                holder.setText(R.id.item_shangpin_guanli_price, "￥ " + shangpinGuanliBean.getDanjia());
//                                holder.setText(R.id.item_shangpin_guanli_xiaoliang, "销量：  " + shangpinGuanliBean.getxiaoshou());
//                                holder.setText(R.id.item_shangpin_guanli_zhuangtai, shangpinGuanliBean.getZhuangtai());
//                                holder.setText(R.id.item_shangpin_guanli_kucun, "库存  " + shangpinGuanliBean.getKucun());
//                                holder.setVisible(R.id.item_shangpin_guanli_zhuangtai, false);
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
//                                    }
//                                });
////                                CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
//                            }
//                        }
//                    };
//
//                    lv_fg_shangpin_guanli.setAdapter(adapter);
//                    lv_fg_shangpin_guanli.setVisibility(View.VISIBLE);
//                } else {//上拉加载更多
//                    adapter.notifyDataSetChanged();
//                }
//                break;
//            case ConstantUtils.TAG_shop_sx_manages_out://商品下架请求
//                Log.e("商品下架请求成功", jsonObject.toString());
//                allShangpinGuanli.remove(deleteBean);
//                adapter.notifyDataSetChanged();
//                MToast.showToast(getActivity(), "下架成功");
//                break;
//        }
//    }
//
//
//    @Override
//    public void onRefresh() {
//        refresh = true;
//        switch (currentFragment) {
//            case "申请商品":
//                shengqingPage = 0;
//                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", "0", "0"));
//                break;
//            case "销售商品":
//                xiaoshouPage = 0;
//                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", "0", "0"));
//                break;
//            case "下架商品":
//                xiajiaPage = 0;
//                requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", "0", "0"));
//                break;
//        }
//        refreshlayout_fg_shangpin_guanli.removeFoot();
//
//    }
//
//    @Override
//    public void onLoad() {//上拉加载更多
//        refresh = false;
//        switch (currentFragment) {
//            case "申请商品":
//                if (shengqingNum >= 10) {
//                    shengqingPage++;
//                    requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, String.valueOf(shengqingPage), "0", "0"));
//                } else if (shengqingNum > 0) {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                    refreshlayout_fg_shangpin_guanli.isNoData();
//                } else {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                }
//                break;
//            case "销售商品":
//                if (xiaoshouNum >= 10) {
//                    xiaoshouPage++;
//
//                    requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", String.valueOf(xiaoshouPage), "0"));
//
//                } else if (xiaoshouNum > 0) {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                    refreshlayout_fg_shangpin_guanli.isNoData();
//                } else {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                }
//                break;
//            case "下架商品":
//                if (xiajiaNum >= 10) {
//                    xiajiaPage++;
//                    requestHandleArrayList.add(requestAction.shop_sx_manages(this, phone, "0", "0", String.valueOf(xiajiaPage)));
//                } else if (xiajiaNum > 0) {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                    refreshlayout_fg_shangpin_guanli.isNoData();
//                } else {
//                    refreshlayout_fg_shangpin_guanli.setLoading(false);
//                }
//                break;
//        }
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//}
