package com.gloiot.hygoSupply.ui.activity.dingdanguanli.dingdanfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DanduFahuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DingDanXiangQing_NewActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.QuerenFahuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TongYiTuiHuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TuikuanxiangqingActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.XieShangTuiKuanActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.ImageSpanUtil;
import com.gloiot.hygoSupply.widget.RefreshLayout;
import com.gloiot.hygoSupply.widget.SelectOilCardPopupWindow;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MyCommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DingdanGuanliFragment extends BaseFragment implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    public static boolean isRefreshQuanbu = false;
    public static boolean isRefreshDaifahuo = false;
    public static boolean isRefreshYifahuo = false;
    public static boolean isRefreshWeifukuan = false;
    public static boolean isRefreshYiwancheng = false;
    public static boolean isRefreshTuihuotuikuan = false;
    @Bind(R.id.lv_fg_dingdan_guanli_quanbu)
    ListView lv_fg_dingdan_guanli;
    @Bind(R.id.refreshlayout_fg_dingdan_guanli_quanbu)
    RefreshLayout refreshlayout_fg_dingdan_guanli_quanbu;
    @Bind(R.id.rl_dingdan_guanli_zanwu)
    RelativeLayout rl_dingdan_guanli_zanwu;
    @Bind(R.id.tv_fg_ddgl_head)
    TextView tv_fg_ddgl_head;

    private Context mContext;
    private String title, phone;
    private int page = 0;
    private String currentFragment;
    private CommonAdapter commonAdapter;
    private Boolean isPull;  // 是否上拉加载
    private String tiaoshu = "-1";
    private ArrayList<DingdanGuanliBean> dingdanGuanlilist = new ArrayList<>();
    private List<Double> yunfeiList;
    private DecimalFormat df;
    private View list_head_view;
    private int updataPosition = -1;
    private List<String[]> KuaidigongsiSet = new ArrayList<>();
    private SelectOilCardPopupWindow menuWindow;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dingdan_guanli_quanbu, container, false);
        ButterKnife.bind(this, view);

        isPull = false;
        mContext = getActivity();
        yunfeiList = new ArrayList<>();
        currentFragment = getArguments().getString("text");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        if ("待发货".equals(currentFragment)) {
            list_head_view = view.findViewById(R.id.ll_fg_ddgl_head_view);
            requestHandleArrayList.add(requestAction.shop_wl_OrderTime(DingdanGuanliFragment.this, phone));
        }
        requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));

        lv_fg_dingdan_guanli = (ListView) view.findViewById(R.id.lv_fg_dingdan_guanli_quanbu);
        rl_dingdan_guanli_zanwu = (RelativeLayout) view.findViewById(R.id.rl_dingdan_guanli_zanwu);
        refreshlayout_fg_dingdan_guanli_quanbu = (RefreshLayout) view.findViewById(R.id.refreshlayout_fg_dingdan_guanli_quanbu);
        refreshlayout_fg_dingdan_guanli_quanbu.setOnRefreshListener(this);
        refreshlayout_fg_dingdan_guanli_quanbu.setOnLoadListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("退款/售后".equals(currentFragment) || "全部".equals(currentFragment)) {
            page = 0;
            requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_refund:
            case ConstantUtils.TAG_shop_wl_RefundTime:
                //全部订单
                updateData(jsonObject);
                break;
            case ConstantUtils.TAG_shop_wl_OrderTime:
                JSONArray array = jsonObject.getJSONArray("列表");
                KuaidigongsiSet.clear();
                selectPosition = -10;
                KuaidigongsiSet.add(new String[]{"全部"});
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    KuaidigongsiSet.add(new String[]{object.getString("时间")});
                }
                if (menuWindow == null) {
                    menuWindow = new SelectOilCardPopupWindow(mContext, itemsOnClick, KuaidigongsiSet);
                }
                break;
            case ConstantUtils.TAG_shop_s_ddgl_Info:
                Log.e("TAG", "requestSuccess: " + updataPosition);
                if (updataPosition > -1) {
                    JSONArray jsonArray = jsonObject.getJSONArray("商品");
                    DingdanGuanliBean dingdanGuanliBean = dingdanGuanlilist.get(updataPosition);
                    double price = 0;
                    if (jsonArray.length() > 0) {
                        ArrayList<DingdanGuanliShangpinBean> fahuolist = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            DingdanGuanliShangpinBean spBean = new DingdanGuanliShangpinBean();
                            spBean.set类型(jsonObject1.getString("类型"));
                            spBean.set商品类型(jsonObject1.getString("商品类型"));
                            spBean.set商品id(jsonObject1.getString("商品id"));
                            spBean.set商品数量(jsonObject1.getString("商品数量"));
                            spBean.set种类详细(jsonObject1.getString("商品规格"));
                            spBean.set缩略图(jsonObject1.getString("缩略图"));
                            spBean.set商品名称(jsonObject1.getString("商品名称"));
                            spBean.set价格(jsonObject1.getString("价格"));
                            spBean.setStatus(jsonObject1.getString("状态"));
                            spBean.setId(jsonObject1.getString("id"));
                            try {
                                spBean.setShouhou_status(jsonObject.getString("售后状态"));
                            } catch (Exception e) {

                            }
                            if ("买家已付款".equals(spBean.getStatus())) {
                                fahuolist.add(spBean);
                                price += Double.parseDouble(spBean.get价格());
                            }
                        }
                        if (fahuolist.size() > 1) {
                            dingdanGuanliBean.set订单管理商品集合(fahuolist);
                            dingdanGuanliBean.set合计((Double.parseDouble(dingdanGuanliBean.get快递费()) + price) + "");
                            commonAdapter.notifyDataSetChanged();
                            updataPosition = -1;
                        } else {
                            dingdanGuanlilist.remove(updataPosition);
                            updataPosition = -1;
                        }
                    }
                }
                break;
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.iv_cancel:

                    break;
                // 确认
                case R.id.tv_confirm:
                    requestHandleArrayList.add(requestAction.shop_wl_RefundTime(DingdanGuanliFragment.this, phone, time));
                    break;
                default:
                    break;
            }
        }
    };

    private View lastSelectView;//记录上次选择的Item
    private String time;
    private int selectPosition = -10;

    private void SelectKuaidi() {
        menuWindow.showAtLocation(getActivity().findViewById(R.id.ll_test), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        if (listView == null) {
            listView = menuWindow.setListViewSingle2(mContext);
            final MyCommonAdapter commonAdapter = new MyCommonAdapter<String[]>(mContext, R.layout.item_select_kuaidigongsi, KuaidigongsiSet) {
                @Override
                public void convert(ViewHolder holder, final String[] strings, int position) {
                    holder.setText(R.id.item_select_kuaidigongsi, strings[0]);
                    if (position == selectPosition) {
                        holder.setBackgroundColor(R.id.ll_select_kuaidi_gongsi, Color.rgb(128, 128, 128));
                    } else {
                        holder.setBackgroundColor(R.id.ll_select_kuaidi_gongsi, Color.rgb(255, 255, 255));
                    }
                }
            };
            listView.setAdapter(commonAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectPosition = position;
                    view.setBackgroundColor(Color.rgb(128, 128, 128));
                    if ("全部".equals(KuaidigongsiSet.get(position)[0])) {
                        time = "";
                    } else {
                        time = KuaidigongsiSet.get(position)[0];
                    }


                    if (lastSelectView != null) {
                        lastSelectView.setBackgroundColor(Color.rgb(255, 255, 255));
                    }
                    lastSelectView = view;
                }
            });
            menuWindow.setMaxHeight(listView);
        }
    }

    public void showTimeList() {
        if (menuWindow != null) {
            SelectKuaidi();
        }
    }


    public void updateData(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("状态").equals("成功")) {
                if (isPull == false) {
                    refreshlayout_fg_dingdan_guanli_quanbu.setRefreshing(false);
                    dingdanGuanlilist.clear();
                } else {
                    refreshlayout_fg_dingdan_guanli_quanbu.setLoading(false);
                }
                try {
                    tiaoshu = jsonObject.getString("条数");
                } catch (Exception e) {

                }
                if ("0".equals(tiaoshu)) {
                    rl_dingdan_guanli_zanwu.setVisibility(View.VISIBLE);
                    tiaoshu = "0";
                } else {
                    boolean isNeedWuliu = false;
                    rl_dingdan_guanli_zanwu.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("一级列表");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        String shouhou_zhuangtai = "";
                        String count_down = "";
                        int shuliang = 0;
                        double kuaidifei = 0;
                        double zongfeiyong = 0;
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(j);
                        Log.e("first", jsonObject1.toString());
                        DingdanGuanliBean dingdanGuanli = new DingdanGuanliBean();
                        dingdanGuanli.set订单id(jsonObject1.getString("订单id"));
                        dingdanGuanli.set订单编号(jsonObject1.getString("订单编号"));
                        dingdanGuanli.set状态(jsonObject1.getString("状态"));
                        dingdanGuanli.set店铺名(jsonObject1.getString("店铺名"));
                        dingdanGuanli.set收货人(jsonObject1.getString("收货人"));
                        dingdanGuanli.set收货人手机号(jsonObject1.getString("收货人手机号"));
                        dingdanGuanli.set收货地址(jsonObject1.getString("退货地址"));
                        dingdanGuanli.set合计(jsonObject1.getString("合计"));
                        dingdanGuanli.set退款人帐号(jsonObject1.getString("账号"));
                        dingdanGuanli.set昵称(jsonObject1.getString("昵称"));
                        dingdanGuanli.setOffline("是".equals(jsonObject1.getString("线下交易")));
                        try {
                            dingdanGuanli.setReviewTime(jsonObject1.getString("审核时间"));
                        } catch (Exception e) {

                        }
                        try {
                            count_down = jsonObject1.getString("天") + "天" + jsonObject1.getString("小时") + "小时" + jsonObject1.getString("分钟") + "分钟";
                            dingdanGuanli.setCount_down(count_down);
                        } catch (Exception e) {
                        }

                        if (!"作废".equals(dingdanGuanli.get状态()) || "全部".equals(currentFragment)) {
                            if (jsonObject1.getJSONArray("二级列表").length() > 0) {
                                //未发货数量
                                int unshippednNum = 0;
                                ArrayList<DingdanGuanliShangpinBean> dingdanGuanliShangpinlist = new ArrayList<>();
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("二级列表");
                                for (int z = 0; z < jsonArray1.length(); z++) {
                                    JSONObject obj_shangpin = jsonArray1.getJSONObject(z);
                                    DingdanGuanliShangpinBean dingdanGuanliShangpin = new DingdanGuanliShangpinBean();
                                    dingdanGuanliShangpin.set商品id(obj_shangpin.getString("商品id"));
                                    dingdanGuanliShangpin.setId(obj_shangpin.getString("id"));
                                    dingdanGuanliShangpin.setType(obj_shangpin.getString("类型"));
                                    dingdanGuanliShangpin.setStatus(obj_shangpin.getString("状态"));
                                    if ("买家已付款".equals(obj_shangpin.getString("状态"))) {
                                        unshippednNum++;
                                    }
                                    dingdanGuanliShangpin.set种类详细(obj_shangpin.getString("种类详细"));
                                    dingdanGuanliShangpin.set商品名称(obj_shangpin.getString("商品名称"));
                                    dingdanGuanliShangpin.set商品数量(obj_shangpin.getString("商品数量"));
                                    dingdanGuanliShangpin.set快递费(obj_shangpin.getString("快递费"));
                                    shuliang += Integer.parseInt(obj_shangpin.getString("商品数量"));
                                    if (obj_shangpin.getString("商品类型").equals("实物商品")) {
                                        isNeedWuliu = true;
                                    }
                                    if (!TextUtils.isEmpty(obj_shangpin.getString("售后状态"))) {
                                        dingdanGuanliShangpin.setShouhou_status(obj_shangpin.getString("售后状态"));
                                        shouhou_zhuangtai = obj_shangpin.getString("售后状态");
                                    }
                                    dingdanGuanli.getShouhouList().add(obj_shangpin.getString("售后状态"));
                                    dingdanGuanliShangpin.set商品类型(obj_shangpin.getString("商品类型"));
                                    if (Double.parseDouble(obj_shangpin.getString("快递费")) > kuaidifei) {
                                        kuaidifei = Double.parseDouble(obj_shangpin.getString("快递费"));
                                    }
                                    dingdanGuanliShangpin.set缩略图(obj_shangpin.getString("缩略图"));
                                    dingdanGuanliShangpin.set价格(obj_shangpin.getString("价格"));
                                    dingdanGuanliShangpinlist.add(dingdanGuanliShangpin);

                                }
                                dingdanGuanli.setUnshippednNum(unshippednNum);
                                dingdanGuanli.set订单管理商品集合(dingdanGuanliShangpinlist);
                                dingdanGuanli.set快递费(kuaidifei + "");
                                dingdanGuanli.setNeedWuliu(isNeedWuliu);
                                dingdanGuanli.set数量(shuliang + "");
                                dingdanGuanli.setShouhou_status(shouhou_zhuangtai);
                                dingdanGuanli.setCount_down(count_down);
                                dingdanGuanlilist.add(dingdanGuanli);
                            }
                        }

                    }
                }
                if (isPull == false) {
//                            final double zongfeiyong1 = zongfeiyong;
                    commonAdapter = new CommonAdapter<DingdanGuanliBean>(mContext, R.layout.item_dingdan_guanli, dingdanGuanlilist) {
                        @Override
                        public void convert(final ViewHolder holder, final DingdanGuanliBean dingdanGuanliBean) {
                            holder.setText(R.id.tv_item_dingdan_guanli_zhuangtai, dingdanGuanliBean.get状态());
                            if ("待收货".equals(currentFragment)) {
                                holder.setText(R.id.tv_item_dingdan_guanli_zhuangtai, "买家已付款");
                            }
                            Double jiage = 0.0;
                            Double kuaidifei = 0.0;

                            if (dingdanGuanliBean.get订单管理商品集合().size() > 0) {
                                for (int i = 0; i < dingdanGuanliBean.get订单管理商品集合().size(); i++) {

                                    try {
                                        DingdanGuanliShangpinBean dingdanGuanliShangpin = dingdanGuanliBean.get订单管理商品集合().get(i);
                                        jiage += Double.parseDouble(dingdanGuanliShangpin.get价格()) *
                                                Integer.parseInt(dingdanGuanliShangpin.get商品数量());
                                        kuaidifei += Double.parseDouble(dingdanGuanliShangpin.get快递费());
                                    } catch (Exception e) {

                                    }

//                                            kuaidifei += Double.parseDouble(dingdanGuanliBean.get订单管理商品集合().get(i).get快递费()) *
//                                                    Integer.parseInt(dingdanGuanliBean.get订单管理商品集合().get(i).get商品数量());

                                }
                            }
                            //截取小数点后两位以前的值
//                            df = new DecimalFormat("######0.00");
                            holder.setText(R.id.tv_item_dingdan_guanli_jiage, "合计：￥" + dingdanGuanliBean.get合计());
                            holder.setText(R.id.tv_item_dingdan_guanli_shuliang, "数量：" + dingdanGuanliBean.get数量());
                            //获取内部listview并赋值 商品信息
                            ListView lv_item_dingdan_guanli_shangpin = holder.getView(R.id.lv_item_dingdan_guanli_shangpin);
                            lv_item_dingdan_guanli_shangpin.setAdapter(new CommonAdapter<DingdanGuanliShangpinBean>(mContext
                                    , R.layout.item_dingdan_guanli_lv, dingdanGuanliBean.get订单管理商品集合()) {
                                @Override
                                public void convert(ViewHolder holder, final DingdanGuanliShangpinBean dingdanGuanliShangpin) {
                                    ImageView iv_suolvtu = holder.getView(R.id.iv_item_dingdan_guanli_shangpin_suolvetu);
                                    ImageView iv_fahuo = holder.getView(R.id.iv_item_dingdan_guanli_shangpin_fahuo);
                                    CommonUtils.setDisplayImageOptions(iv_suolvtu, dingdanGuanliShangpin.get缩略图(), 4);
                                    if ("买家已付款".equals(dingdanGuanliShangpin.getStatus())) {
                                        CommonUtils.setDisplayImage(iv_fahuo, "", 0, R.mipmap.ic_weifahuo);
                                    } else if ("卖家已发货".equals(dingdanGuanliShangpin.getStatus())) {
                                        CommonUtils.setDisplayImage(iv_fahuo, "", 0, R.mipmap.ic_yifahuo);
                                    } else {
                                    }
                                    holder.setText(R.id.tv_item_dingdan_guanli_shangpin_zhonglei_xiangxi, dingdanGuanliShangpin.get种类详细());
                                    holder.setClickable(R.id.rl_item_dingdan_guanli_auto, false);
                                    if (!"全球购".equals(dingdanGuanliShangpin.getType()) && !"全球购-自营".equals(dingdanGuanliShangpin.getType())) {
//                                        holder.setVisible2(R.id.iv_item_dingdan_guanli_shangpin_quanqiugou,true);
                                        holder.setText(R.id.tv_item_dingdan_guanli_shangpin_shangpinming, dingdanGuanliShangpin.get商品名称());
                                    } else {
//                                        holder.setVisible2(R.id.iv_item_dingdan_guanli_shangpin_quanqiugou,false);
                                        TextView textView = holder.getView(R.id.tv_item_dingdan_guanli_shangpin_shangpinming);
                                        ImageSpanUtil.getInstance().setImage(getActivity(), textView, R.mipmap.ic_quanqiugou, dingdanGuanliShangpin.get商品名称());
                                    }
                                    if ("退款/售后".equals(currentFragment)) {
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_shangpin_jiaoyi_status, false);
                                        holder.setText(R.id.tv_item_dingdan_guanli_shangpin_jiaoyi_status, dingdanGuanliBean.get状态());
                                        dingdanGuanliBean.setShouhou_status(dingdanGuanliShangpin.getShouhou_status());
                                    } else {
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_shangpin_jiaoyi_status, false);
                                    }
                                }
                            });

                            setListViewHeightBasedOnChildren(lv_item_dingdan_guanli_shangpin);


                            final String status = dingdanGuanliBean.getShouhou_status();
                            holder.setText(R.id.tv_item_dingdan_guanli_shangjiaming, dingdanGuanliBean.get收货人());
                            if ("全部".equals(currentFragment)) {
                                if (!TextUtils.isEmpty(status) && "退款关闭".equals(status) && "商家拒绝退款申请".equals(status) && "商家拒绝退货申请".equals(status)) {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                    holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                    holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, true);
                                    holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, false);
                                    holder.setText(R.id.tv_item_dingdan_guanli_name, dingdanGuanliBean.get收货人());
                                    holder.setVisible2(R.id.tv_item_dingdan_guanli_status, true);
                                    holder.setText(R.id.tv_item_dingdan_guanli_status, "售后申请中");
                                } else {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                    holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                    holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, false);
                                    holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, true);
                                }
                            } else if (!TextUtils.isEmpty(dingdanGuanliBean.getShouhou_status()) && "退款/售后".equals(currentFragment)) {
                                holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, true);
                                holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, false);
                                holder.setText(R.id.tv_item_dingdan_guanli_name, dingdanGuanliBean.get收货人());

                                switch (status) {
                                    case "等待商家处理退款申请":
                                    case "等待商家处理退货":
                                        holder.setText(R.id.btn_item_dingdan_chuli, "请处理");
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, true);
                                        holder.setText(R.id.tv_item_dingdan_guanli_daojishi, dingdanGuanliBean.getCount_down());
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, true);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, false);
                                        break;
                                    case "等待商家确认并退款":
                                        holder.setText(R.id.btn_item_dingdan_chuli, "请确认退款");
                                        holder.setText(R.id.tv_item_dingdan_guanli_daojishi, dingdanGuanliBean.getCount_down());
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, true);
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, true);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, false);
                                        break;
                                    case "退款关闭":
                                    case "商家拒绝退款申请":
                                    case "商家拒绝退货申请":
                                        if ("待发货".equals(currentFragment)) {
                                            holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                            holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, false);
                                            holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, true);
                                            holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);

                                            if (dingdanGuanliBean.getUnshippednNum() > 1) {
                                                holder.setText(R.id.btn_item_dingdan_querenfahuo, "统一发货");
                                                holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, true);//显示单独发货按钮
                                            } else {
                                                holder.setText(R.id.btn_item_dingdan_querenfahuo, "确认发货");
                                                holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);//显示单独发货按钮
                                            }

                                            holder.setOnClickListener(R.id.btn_item_dingdan_dandufahuo, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (dingdanGuanliBean.get状态().equals("") || dingdanGuanliBean.get状态().equals("买家已付款"))
                                                        danduFahuo(dingdanGuanliBean.get订单id(), dingdanGuanliBean.isOffline(), holder.getmPosition());
                                                }
                                            });
                                        } else if ("已发货".equals(currentFragment)) {
                                            holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                            holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, false);
                                            holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, true);
                                            holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);
                                            holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                            holder.setText(R.id.btn_item_dingdan_querenfahuo, "订单详情");
                                        } else {
                                            holder.setText(R.id.tv_item_dingdan_guanli_status, "退款关闭");
                                            holder.setVisible2(R.id.tv_item_dingdan_guanli_status, true);
                                            holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, false);
                                            holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                        }
                                        break;
                                    case "商家未处理":
                                        holder.setText(R.id.tv_item_dingdan_guanli_status, "超时未处理");
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, true);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, false);
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                        break;
                                    case "退款成功":
                                    case "商家已同意退款":

                                        holder.setText(R.id.tv_item_dingdan_guanli_status, "退款成功");
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, true);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, false);
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                        break;
                                    case "商家已同意退货申请":
                                        holder.setText(R.id.tv_item_dingdan_guanli_status, "等待买家发货");
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, true);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, false);
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                        break;
                                    default:
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_status, false);
                                        holder.setVisible2(R.id.tv_item_dingdan_guanli_daojishi, false);
                                        holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                        break;
                                }


                                holder.setOnClickListener(R.id.btn_item_dingdan_chuli, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        switch (TextUtils.isEmpty(status) ? "" : status) {
                                            case "等待商家处理退款申请":
                                            case "等待商家处理退货":
//                                                Intent intent = new Intent(getActivity(), XieShangTuiKuanActivity.class);
//                                                intent.putExtra("item", (Serializable) dingdanGuanliBean);
//                                                startActivity(intent);
                                                Intent intent = new Intent(getActivity(), AfterSaleActivity.class);
                                                intent.putExtra("orderId", dingdanGuanliBean.get订单id());
                                                intent.putExtra("id", dingdanGuanliBean.get订单管理商品集合().get(0).getId());
                                                startActivity(intent);
                                                break;
                                            case "等待商家确认并退款":
                                            case "商家已同意退货申请":
                                                Intent intent1 = new Intent(getActivity(), TongYiTuiHuoActivity.class);
                                                intent1.putExtra("item", (Serializable) dingdanGuanliBean);
                                                intent1.putExtra("tiem", dingdanGuanliBean.getReviewTime());
                                                startActivity(intent1);
                                                break;
                                        }
                                    }
                                });

                                if ("已发货".equals(currentFragment) || "待付款".equals(currentFragment) || "待发货".equals(currentFragment)) {
                                    holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                }
                                if ("退款/售后".equals(currentFragment)) {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                }

                            } else {
                                holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                holder.setVisible2(R.id.rl_item_dingdan_guanli_shouhou_top, false);
                                holder.setVisible2(R.id.rv_item_dingdan_guanli_shangpin, true);
                                if (dingdanGuanliBean.get状态().equals("等待买家付款")) {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                } else if (dingdanGuanliBean.get状态().equals("买家已付款")) {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);

                                    if (dingdanGuanliBean.getUnshippednNum() > 1) {
                                        holder.setText(R.id.btn_item_dingdan_querenfahuo, "统一发货");
                                        holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, true);//显示单独发货按钮
                                    } else {
                                        holder.setText(R.id.btn_item_dingdan_querenfahuo, "确认发货");
                                        holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);//显示单独发货按钮
                                    }

                                    holder.setOnClickListener(R.id.btn_item_dingdan_dandufahuo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (dingdanGuanliBean.get状态().equals("") || dingdanGuanliBean.get状态().equals("买家已付款"))
                                                danduFahuo(dingdanGuanliBean.get订单id(), dingdanGuanliBean.isOffline(), holder.getmPosition());
                                        }
                                    });
                                } else if (dingdanGuanliBean.get状态().equals("卖家已发货")) {
                                    holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);
                                    holder.setVisible2(R.id.btn_item_dingdan_querenfahuo, true);
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);
                                    holder.setText(R.id.btn_item_dingdan_querenfahuo, "订单详情");
                                } else if (dingdanGuanliBean.get状态().equals("交易完成")) {
                                    holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);
                                    holder.setVisible2(R.id.btn_item_dingdan_querenfahuo, true);
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);
                                    holder.setText(R.id.btn_item_dingdan_querenfahuo, "订单详情");
                                } else if (dingdanGuanliBean.get状态().equals("")) {
                                    if ("全部".equals(currentFragment)) {
                                        holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);

                                        if (dingdanGuanliBean.getUnshippednNum() > 1) {
                                            holder.setText(R.id.btn_item_dingdan_querenfahuo, "统一发货");
                                            holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, true);//显示单独发货按钮
                                        } else {
                                            holder.setText(R.id.btn_item_dingdan_querenfahuo, "确认发货");
                                            holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);//显示单独发货按钮
                                        }

                                        holder.setOnClickListener(R.id.btn_item_dingdan_dandufahuo, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (dingdanGuanliBean.get状态().equals("") || dingdanGuanliBean.get状态().equals("买家已付款"))
                                                    danduFahuo(dingdanGuanliBean.get订单id(), dingdanGuanliBean.isOffline(), holder.getmPosition());
                                            }
                                        });
                                    } else if ("已发货".equals(currentFragment)) {
                                        holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);
                                        holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                        holder.setText(R.id.btn_item_dingdan_querenfahuo, "订单详情");
                                    } else if ("待发货".equals(currentFragment)) {
                                        holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, true);
                                        if (dingdanGuanliBean.getUnshippednNum() > 1) {
                                            holder.setText(R.id.btn_item_dingdan_querenfahuo, "统一发货");
                                            holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, true);//显示单独发货按钮
                                        } else {
                                            holder.setText(R.id.btn_item_dingdan_querenfahuo, "确认发货");
                                            holder.setVisible2(R.id.btn_item_dingdan_dandufahuo, false);//显示单独发货按钮
                                        }
                                        holder.setOnClickListener(R.id.btn_item_dingdan_dandufahuo, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (dingdanGuanliBean.get状态().equals("") || dingdanGuanliBean.get状态().equals("买家已付款"))
                                                    danduFahuo(dingdanGuanliBean.get订单id(), dingdanGuanliBean.isOffline(), holder.getmPosition());
                                            }
                                        });

                                    }

                                } else if (dingdanGuanliBean.get状态().equals("作废")) {
                                    holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                }

                                if ("已发货".equals(currentFragment) || "待付款".equals(currentFragment) || "待发货".equals(currentFragment)) {
                                    holder.setVisible2(R.id.rl_item_dingdan_chuli, false);
                                }
                                if ("退款/售后".equals(currentFragment)) {
                                    holder.setVisible2(R.id.rl_dingdan_guanli_queren_fahuo, false);
                                }
                            }
//                            }
                            lv_item_dingdan_guanli_shangpin.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            updataPosition = holder.getmPosition();
                                            if ("全部".equals(currentFragment)) {
                                                if (dingdanGuanliBean.get订单管理商品集合().size() > 1 || TextUtils.isEmpty(status)) {
                                                    Intent intent = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    if (TextUtils.isEmpty(status) || "退款关闭".equals(status) || "商家拒绝退款申请".equals(status) || "商家拒绝退货申请".equals(status)) {
                                                        bundle.putBoolean("售后", false);
                                                    } else {
                                                        bundle.putBoolean("售后", true);
                                                    }
                                                    intent.putExtra("current", currentFragment);
                                                    intent.putExtra("isOffline", dingdanGuanliBean.isOffline());

                                                    intent.putExtra("beanlist", (Serializable) dingdanGuanliBean);
                                                    bundle.putString("订单id", dingdanGuanliBean.get订单id());
                                                    bundle.putString("订单状态", dingdanGuanliBean.get状态());
                                                    bundle.putBoolean("物流", dingdanGuanliBean.isNeedWuliu());
                                                    intent.putExtras(bundle);
                                                    startActivityForResult(intent, 1);
                                                } else {
                                                    Intent intent = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    if ("退款关闭".equals(status) || "商家拒绝退款申请".equals(status) || "商家拒绝退货申请".equals(status)) {
                                                        bundle.putBoolean("售后", false);
                                                    } else {
                                                        bundle.putBoolean("售后", true);
                                                    }
                                                    intent.putExtra("current", currentFragment);
                                                    intent.putExtra("isOffline", dingdanGuanliBean.isOffline());
                                                    intent.putExtra("beanlist", (Serializable) dingdanGuanliBean);
                                                    bundle.putString("订单id", dingdanGuanliBean.get订单id());
                                                    bundle.putString("订单状态", dingdanGuanliBean.get状态());
                                                    bundle.putBoolean("物流", dingdanGuanliBean.isNeedWuliu());
                                                    intent.putExtras(bundle);
                                                    startActivityForResult(intent, 1);

                                                }
                                            } else if ("退款/售后".equals(currentFragment)) {
                                                switch (TextUtils.isEmpty(status) ? "" : status) {
                                                    case "等待商家处理退款申请":
                                                    case "等待商家处理退货":
//                                                        Intent intent = new Intent(getActivity(), XieShangTuiKuanActivity.class);
//                                                        intent.putExtra("item", (Serializable) dingdanGuanliBean);
//                                                        startActivity(intent);
                                                        Intent intent = new Intent(getActivity(), AfterSaleActivity.class);
                                                        intent.putExtra("orderId", dingdanGuanliBean.get订单id());
                                                        intent.putExtra("id", dingdanGuanliBean.get订单管理商品集合().get(0).getId());
                                                        startActivity(intent);
                                                        break;
                                                    case "等待商家确认并退款":
                                                    case "商家已同意退货申请":
//                                                        Intent intent1 = new Intent(getActivity(), TongYiTuiHuoActivity.class);
//                                                        intent1.putExtra("item", (Serializable) dingdanGuanliBean);
//                                                        intent1.putExtra("tiem", dingdanGuanliBean.getReviewTime());
//                                                        startActivity(intent1);
                                                        Intent intent1 = new Intent(getActivity(), AfterSaleActivity.class);
                                                        intent1.putExtra("orderId", dingdanGuanliBean.get订单id());
                                                        intent1.putExtra("id", dingdanGuanliBean.get订单管理商品集合().get(0).getId());
                                                        startActivity(intent1);
                                                        break;
                                                    default:
                                                        Intent intent2 = new Intent(getActivity(), TuikuanxiangqingActivity.class);
                                                        intent2.putExtra("item", (Serializable) dingdanGuanliBean);
                                                        startActivity(intent2);
                                                        break;
                                                }
                                            } else {
                                                Intent intent = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
                                                Bundle bundle = new Bundle();
                                                if (TextUtils.isEmpty(status) || "退款关闭".equals(status) || "商家拒绝退款申请".equals(status) || "商家拒绝退货申请".equals(status)) {
                                                    bundle.putBoolean("售后", false);
                                                } else {
                                                    bundle.putBoolean("售后", true);
                                                }
                                                bundle.putString("订单id", dingdanGuanliBean.get订单id());
                                                intent.putExtra("isOffline", dingdanGuanliBean.isOffline());
                                                bundle.putString("订单状态", dingdanGuanliBean.get状态());
                                                bundle.putBoolean("物流", dingdanGuanliBean.isNeedWuliu());
                                                intent.putExtra("current", currentFragment);
                                                intent.putExtras(bundle);
                                                startActivityForResult(intent, 1);
                                            }
                                        }
                                    }
                            );

                            holder.setOnClickListener(R.id.btn_item_dingdan_querenfahuo, new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            updataPosition = holder.getmPosition();
                                            switch (dingdanGuanliBean.get状态()) {
                                                case "买家已付款":
                                                    Intent intent = new Intent(mContext, QuerenFahuoActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("id", dingdanGuanliBean.get订单id());
                                                    bundle.putBoolean("isOffline", dingdanGuanliBean.isOffline());
                                                    bundle.putSerializable("list", dingdanGuanliBean.get订单管理商品集合());
                                                    intent.putExtras(bundle);
                                                    startActivityForResult(intent, 1);
                                                    break;
                                                case "卖家已发货":
                                                case "交易完成":
                                                    Intent intent3 = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
                                                    Bundle bundle2 = new Bundle();
                                                    bundle2.putBoolean("售后", false);
                                                    bundle2.putString("订单id", dingdanGuanliBean.get订单id());
                                                    bundle2.putString("订单状态", dingdanGuanliBean.get状态());
                                                    bundle2.putBoolean("物流", dingdanGuanliBean.isNeedWuliu());
                                                    intent3.putExtra("current", currentFragment);
                                                    intent3.putExtras(bundle2);
                                                    startActivityForResult(intent3, 1);
                                                    break;
                                                case "":
                                                    if ("全部".equals(currentFragment) || "待发货".equals(currentFragment)) {
                                                        Intent intent1 = new Intent(mContext, QuerenFahuoActivity.class);
                                                        Bundle bundle1 = new Bundle();
                                                        bundle1.putString("id", dingdanGuanliBean.get订单id());
                                                        bundle1.putBoolean("isOffline", dingdanGuanliBean.isOffline());
                                                        bundle1.putSerializable("list", dingdanGuanliBean.get订单管理商品集合());
                                                        intent1.putExtras(bundle1);
                                                        startActivityForResult(intent1, 1);
                                                    } else if ("已发货".equals(currentFragment)) {
                                                        Intent intent2 = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
                                                        Bundle bundle3 = new Bundle();
                                                        bundle3.putBoolean("售后", false);
                                                        bundle3.putString("订单id", dingdanGuanliBean.get订单id());
                                                        bundle3.putString("订单状态", dingdanGuanliBean.get状态());
                                                        bundle3.putBoolean("物流", dingdanGuanliBean.isNeedWuliu());
                                                        intent2.putExtra("current", currentFragment);
                                                        intent2.putExtras(bundle3);
                                                        startActivityForResult(intent2, 1);
                                                    }

                                                    break;
                                            }
                                        }

                                    }
                            );

                            // 联系买家
                            holder.setOnClickListener(R.id.btn_item_dingdan_lianximaijia, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.intentIMActivity(getActivity(), dingdanGuanliBean);
                                }
                            });

                            //item点击事件
//                            holder.setOnClickListener(R.id.btn_item_dingdan_guanli_dingdan_xiangqing, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(getActivity(), DingDanXiangQing_NewActivity.class);
//                                    intent.putExtra("订单id", dingdanGuanliBean.get订单id());
//                                    startActivity(intent);
//                                }
//                            });
                        }

                    };
                    lv_fg_dingdan_guanli.setAdapter(commonAdapter);
//                            MToastUtils.showToast(getActivity(), "加载成功");

                } else {
                    commonAdapter.notifyDataSetChanged();
                }
                if (dingdanGuanlilist.size() == 0) {
                    rl_dingdan_guanli_zanwu.setVisibility(View.VISIBLE);
                    if ("待发货".equals(currentFragment)) {
                        list_head_view.setVisibility(View.GONE);
                    }
                } else {
                    if ("待发货".equals(currentFragment)) {
                        if (!TextUtils.isEmpty(jsonObject.getString("消息"))) {
                            tv_fg_ddgl_head.setText(jsonObject.getString("消息"));
                        }
                        list_head_view.setVisibility(View.VISIBLE);
                    }
                    rl_dingdan_guanli_zanwu.setVisibility(View.GONE);
                }
            } else {
                //无数据
                DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("状态"));
            }
            if (Integer.parseInt(tiaoshu) > 9) {
                refreshlayout_fg_dingdan_guanli_quanbu.setOnLoadListener(this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "onActivityResult: " + requestCode + "-------" + resultCode);
        switch (resultCode) {
            case 1://确认发货后返回刷新数据 (需要更新的fragment：全部，待发货，已发货)
                isRefreshYifahuo = true;
                isRefreshDaifahuo = true;
//                Log.e("onActivityResult", "onActivityResult: ");
//                if (currentFragment.equals("全部")) {
//
//                    isRefreshDaifahuo = true;
//                } else {
//                    isRefreshQuanbu = true;
//                }
                if ("待发货".equals(currentFragment)) {
                    Log.e("TAG", "updataPosition: " + updataPosition);
                    try {
                        if (updataPosition > -1) {
                            isRefreshDaifahuo = false;
                            if (dingdanGuanlilist.size() >= updataPosition + 1) {
                                DingdanGuanliBean dingdanGuanliBean = dingdanGuanlilist.get(updataPosition);
                                requestHandleArrayList.add(requestAction.shop_s_ddgl_Info(this, phone
                                        , dingdanGuanliBean.get订单id(), dingdanGuanliBean.get状态()));
                            }
                        } else {
                            dingdanGuanlilist.clear();
                            isRefreshDaifahuo = false;
                            requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                        }
                        return;
                    } catch (Exception e) {

                    }
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                }

                break;
            case 2:
                isRefreshTuihuotuikuan = true;
                Log.e("onActivityResult", "onActivityResult: ");
//                if (currentFragment.equals("全部")) {
//
//                    isRefreshDaifahuo = true;
//                } else {
//                    isRefreshQuanbu = true;
//                }
                page = 0;
                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                break;
        }
    }


    @Override
    public void onLoad() {
        if (Integer.parseInt(tiaoshu) > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
        } else if (Integer.parseInt(tiaoshu) > 0) {
            try {
                refreshlayout_fg_dingdan_guanli_quanbu.setLoading(false);
                refreshlayout_fg_dingdan_guanli_quanbu.isNoData();
            } catch (Exception e) {
            }
        } else {
            refreshlayout_fg_dingdan_guanli_quanbu.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_OrderTime(DingdanGuanliFragment.this, phone));
        requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
        refreshlayout_fg_dingdan_guanli_quanbu.removeFoot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        page = 0;
        if (isVisibleToUser) {

            if ("已发货".equals(currentFragment) && isRefreshYifahuo) {
                isRefreshYifahuo = false;
                dingdanGuanlilist.clear();
                page = 0;
                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                return;
            }
            if ("待付款".equals(currentFragment) && isRefreshWeifukuan) {
                isRefreshWeifukuan = false;
                dingdanGuanlilist.clear();

                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                return;
            }

            if ("全部".equals(currentFragment) && isRefreshQuanbu) {
                isRefreshQuanbu = false;
                dingdanGuanlilist.clear();
                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                return;
            }
            if ("待发货".equals(currentFragment) && isRefreshDaifahuo) {
                dingdanGuanlilist.clear();
                isRefreshDaifahuo = false;
                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                return;
            }
            if ("退款/售后".equals(currentFragment) && isRefreshTuihuotuikuan) {
                isRefreshTuihuotuikuan = false;
//                isRefreshTuihuotuikuan = false;

                requestHandleArrayList.add(requestAction.shop_wl_refund(DingdanGuanliFragment.this, phone, currentFragment, String.valueOf(page)));
                return;
            }
        }
    }

    //scrollview 嵌套listview数据显示冲突问题

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void danduFahuo(String dingdan_id, boolean isOffline, int position) {
        updataPosition = position;
        Intent intent = new Intent(getActivity(), DanduFahuoActivity.class);
        intent.putExtra("id", dingdan_id);
        intent.putExtra("isOffline", isOffline);
        startActivityForResult(intent, 8);
    }


    @Override
    public void statusUnusual(JSONObject response) throws JSONException {
        if ("亲，没有此商品哦".equals(response.getString("状态"))) {
            try {
                dingdanGuanlilist.remove(updataPosition);
                commonAdapter.notifyDataSetChanged();
                updataPosition = -1;
            } catch (Exception e) {

            }

        } else {
            super.statusUnusual(response);
        }


    }
}
