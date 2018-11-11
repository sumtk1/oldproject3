package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanXiangQingSPBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleActivity;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.ImageSpanUtil;
import com.gloiot.hygoSupply.utlis.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hygo03 on 2016/12/5.
 */

public class DingDanXiangQing_NewActivity extends BaseActivity {

    @Bind(R.id.tv_DDXX_dingdanNum)
    TextView tvDDXXDingdanNum;
    @Bind(R.id.tv_DDXX_zhifufangshi)
    TextView tvDDXXZhifufangshi;
    @Bind(R.id.tv_DDXX_fukuanshijian)
    TextView tvDDXXFukuanshijian;
    @Bind(R.id.tv_DDXX_yunfei)
    TextView tv_DDXX_yunfei;
    @Bind(R.id.tv_DDXX_heji)
    TextView tv_DDXX_heji;
    //@Bind(R.id.tv_DDXX_shouhuoren)
//    TextView tvDDXXShouhuoren;
//    @Bind(R.id.tv_DDXX_shouhuodizhi)
//    TextView tvDDXXShouhuodizhi;
//    @Bind(R.id.tv_DDXX_liangxidianhua)
//    TextView tvDDXXLiangxidianhua;
    @Bind(R.id.lv_DDXX_shangpin)
    ListView lv_DDXX_shangpin;
    @Bind(R.id.rl_dingdan_xiangqing_pay_time)
    RelativeLayout rl_dingdan_xiangqing_pay_time;
    TextView tv_DDXX_shagnjiadizhi, tv_DDXX_shouhuoren, tv_DDXX_shagnjiadianhua;

    CommonAdapter adapter = null;
    List<DingdanXiangQingSPBean> alist = new ArrayList<>();
    List<DingdanXiangQingSPBean> fahuolist = new ArrayList<>();
    DingdanGuanliBean bean;
    //订单状态
    String dingdanzhuangtai = "";
    //订单id
    String dingdan_id = "";
    //是否需要物流
    boolean isNeedWuliu;
    boolean isAfterSale;
    @Bind(R.id.tv_DDXX_discounts)
    TextView tvDDXXDiscounts;
    @Bind(R.id.rl_dingdan_xiangqing_discounts)
    RelativeLayout rlDingdanXiangqingDiscounts;
    @Bind(R.id.btn_dingdan_xiangqing_deliver_goods)
    Button btnDingdanXiangqingDeliverGoods;
    @Bind(R.id.btn_dingdan_xiangqing_deliver_goods_optional)
    Button btnDingdanXiangqingDeliverGoodsOptional;
    @Bind(R.id.btn_dingdan_xiangqing_deliver_goods_buyer)
    Button btnDingdanXiangqingDeliverGoodsBuyer;
    String shouhou;
    String currentFragment;
    @Bind(R.id.tv_dingdan_xiangqing_dikouleiixng)
    TextView tvDingdanXiangqingDikouleiixng;
    @Bind(R.id.tv_dingdan_xiangqing_dikoujine)
    TextView tvDingdanXiangqingDikoujine;
    @Bind(R.id.rl_dingdan_xiangqing_dikou)
    RelativeLayout rlDingdanXiangqingDikou;
    @Bind(R.id.tv_DDXX_dingdanBeizhu)
    TextView tvDDXXDingdanBeizhu;
    @Bind(R.id.rl_DDXX_dingdanBeizhu)
    RelativeLayout rlDDXXDingdanBeizhu;

    //订单买家账号、名称
    private String dingdan_account, dingdan_name;
    private boolean isShowFeilv;
    private boolean isOffline;

    @Override
    public int initResource() {
//        return R.layout.activity_dingdan_xiangqing_new;
        return R.layout.activity_dingdan_xiangqing_07;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "订单详情", "");
        String phone = preferences.getString(ConstantUtils.SP_MYID, "");
        dingdanzhuangtai = (String) getIntent().getExtras().get("订单状态");
        dingdan_id = (String) getIntent().getExtras().get("订单id");
        isNeedWuliu = (boolean) getIntent().getExtras().get("物流");
        isAfterSale = (boolean) getIntent().getExtras().get("售后");
        isOffline = getIntent().getBooleanExtra("isOffline", false);
        try {
            isShowFeilv = (boolean) getIntent().getExtras().get("费率");
        } catch (Exception e) {
            isShowFeilv = false;
        }

        currentFragment = getIntent().getStringExtra("current");
        if ("买家已付款".equals(dingdanzhuangtai) && !isAfterSale) {
            ViewUtil.getInstance().setVisible(this, R.id.rl_dingdan_xiangqing_deliver_goods, true);
        } else {
            ViewUtil.getInstance().setVisible(this, R.id.rl_dingdan_xiangqing_deliver_goods, false);
        }
        bean = (DingdanGuanliBean) getIntent().getSerializableExtra("beanlist");
        btnDingdanXiangqingDeliverGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuerenFahuoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", dingdan_id);
                bundle.putBoolean("isNeedWuliu", isNeedWuliu);
                bundle.putSerializable("commodityList", (Serializable) fahuolist);
                intent.putExtra("isOffline", isOffline);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        btnDingdanXiangqingDeliverGoodsOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DanduFahuoActivity.class);
                intent.putExtra("id", dingdan_id);
                intent.putExtra("isOffline", isOffline);
                startActivityForResult(intent, 2);
            }
        });
        btnDingdanXiangqingDeliverGoodsBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (alist.size() > 0) { // 订单里的商品集合的长度大于0
                    bundle.putString("topType", Constant.CHAT_TOP_TYPE);// 是否有悬浮，""代表没有悬浮，悬浮类别:imgText = 图文
                    bundle.putString("id", alist.get(0).getSp_id());
                    bundle.putString("icon", alist.get(0).getSp_tupian());
                    bundle.putString("title", alist.get(0).getSp_mingcheng());
                    bundle.putString("money", alist.get(0).getSp_danjia());
                    bundle.putBoolean("single", alist.size() > 1 ? false : true); // 订单包含两种商品及以上值为false
                    bundle.putString("extra", "");
                    bundle.putString("orderId", dingdan_id);
                    bundle.putString("orderState", dingdanzhuangtai);
                }
                Intent intent = new Intent(mContext, ConversationActivity.class);
                intent.putExtra("receiveId", dingdan_account);
                intent.putExtra("name", dingdan_name);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
        requestHandleArrayList.add(requestAction.shop_s_ddgl_Info(this, phone, dingdan_id, dingdanzhuangtai));
        View head_view = LayoutInflater.from(this).inflate(R.layout.listview_headview_dingdanxiangqing, null);
        tv_DDXX_shouhuoren = (TextView) head_view.findViewById(R.id.tv_DDXX_shouhuoren);
        tv_DDXX_shagnjiadianhua = (TextView) head_view.findViewById(R.id.tv_DDXX_shagnjiadianhua);
        tv_DDXX_shagnjiadizhi = (TextView) head_view.findViewById(R.id.tv_DDXX_shagnjiadizhi);
        lv_DDXX_shangpin.addHeaderView(head_view);
//        setListViewHeightBasedOnChildren(lv_DDXX_shangpin);
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_s_ddgl_Info:
                dingdan_account = response.getString("账号");
                dingdan_name = response.getString("昵称");
                JSONArray jsonArray = response.getJSONArray("商品");
                response.getString("创建时间");
                tvDDXXDingdanNum.setText(response.getString("订单id"));
                String dikouleixing = response.getString("抵扣类型");
                if (!TextUtils.isEmpty(dikouleixing)) {
                    rlDingdanXiangqingDikou.setVisibility(View.VISIBLE);
                    tvDingdanXiangqingDikouleiixng.setText(response.getString("抵扣类型") + "抵扣");
                    tvDingdanXiangqingDikoujine.setText(response.getString("抵扣金额"));
                } else {
                    rlDingdanXiangqingDikou.setVisibility(View.GONE);
                }
                String beizhu = response.getString("买家留言");
                if (TextUtils.isEmpty(beizhu)) {
                    rlDDXXDingdanBeizhu.setVisibility(View.GONE);
                } else {
                    rlDDXXDingdanBeizhu.setVisibility(View.VISIBLE);
                    tvDDXXDingdanBeizhu.setText(beizhu);
                }

                try {
                    String fukuanshijian = response.getString("付款时间");
                    if (!TextUtils.isEmpty(fukuanshijian)) {
                        tvDDXXFukuanshijian.setText(fukuanshijian);
                        rl_dingdan_xiangqing_pay_time.setVisibility(View.VISIBLE);
                    } else {
                        rl_dingdan_xiangqing_pay_time.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    rl_dingdan_xiangqing_pay_time.setVisibility(View.GONE);
                }
                try {
                    if (!"0.00".equals(response.getString("优惠额"))) {
                        rlDingdanXiangqingDiscounts.setVisibility(View.VISIBLE);
                        tvDDXXDiscounts.setText(response.getString("优惠额"));
                    }
                } catch (Exception e) {

                }
                tv_DDXX_yunfei.setText(response.getString("快递费"));
                tv_DDXX_heji.setText(response.getString("合计"));
                tv_DDXX_shouhuoren.setText(response.getString("收货人"));
                tv_DDXX_shagnjiadizhi.setText(response.getString("收货地址"));
                tv_DDXX_shagnjiadianhua.setText(response.getString("收货人手机号"));
                String zhifufangshi = response.getString("支付方式");
                if (TextUtils.isEmpty(zhifufangshi)) {
                    ViewUtil.getInstance().setVisible(this, R.id.rv_dingdan_xiangqing_zhifufangshi, false);
                } else {
                    ViewUtil.getInstance().setVisible(this, R.id.rv_dingdan_xiangqing_zhifufangshi, true);
                    tvDDXXZhifufangshi.setText(zhifufangshi);
                }
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        DingdanXiangQingSPBean spBean = new DingdanXiangQingSPBean();
                        spBean.setSp_type(jsonObject.getString("类型"));
                        spBean.setSp_productType(jsonObject.getString("商品类型"));
                        spBean.setSp_id(jsonObject.getString("商品id"));
                        spBean.setSp_shuliang(jsonObject.getString("商品数量"));
                        spBean.setSp_guige(jsonObject.getString("商品规格"));
                        spBean.setSp_tupian(jsonObject.getString("缩略图"));
                        spBean.setSp_mingcheng(jsonObject.getString("商品名称"));
                        spBean.setSp_danjia(jsonObject.getString("价格"));
                        spBean.setSp_status(jsonObject.getString("状态"));
                        spBean.setId(jsonObject.getString("id"));
                        spBean.setSp_wuliu_status(jsonObject.getString("物流状态"));
                        try {
                            spBean.setSp_aftersale_status(jsonObject.getString("售后状态"));
                        } catch (Exception e) {

                        }
                        try {
                            spBean.setSp_feilv("费率:" + jsonObject.getString("提点"));
                        } catch (Exception e) {

                        }

                        alist.add(spBean);
                        if ("买家已付款".equals(spBean.getSp_status())) {
                            fahuolist.add(spBean);
                        }
                    }
                    if (fahuolist.size() > 1) {
                        btnDingdanXiangqingDeliverGoods.setText("统一发货");
                        btnDingdanXiangqingDeliverGoodsOptional.setVisibility(View.VISIBLE);
                    } else {
                        btnDingdanXiangqingDeliverGoods.setText("确认发货");
                        btnDingdanXiangqingDeliverGoodsOptional.setVisibility(View.GONE);
                    }
                }
                adapter = new CommonAdapter<DingdanXiangQingSPBean>(this, R.layout.item_ddxq_shangpin, alist) {
                    @Override
                    public void convert(ViewHolder holder, final DingdanXiangQingSPBean spBean, final int position) {
                        holder.setText(R.id.tv_item_ddxq_color, spBean.getSp_guige());
                        holder.setText(R.id.tv_item_ddxq_num, "x" + spBean.getSp_shuliang());
                        holder.setText(R.id.tv_item_ddxq_jiage, "￥" + spBean.getSp_danjia());
                        if (!"全球购".equals(spBean.getSp_type())) {
                            holder.setText(R.id.tv_item_ddxq_shangpinming, spBean.getSp_mingcheng());
                        } else {
                            TextView textView = holder.getView(R.id.tv_item_ddxq_shangpinming);
                            ImageSpanUtil.getInstance().setImage(DingDanXiangQing_NewActivity.this, textView, R.mipmap.ic_quanqiugou, spBean.getSp_mingcheng());
                        }
                        ImageView iv_fahuo = holder.getView(R.id.iv_item_dingdan_xiangqing_fahuo);
                        if ("买家已付款".equals(spBean.getSp_status())) {
                            CommonUtils.setDisplayImage(iv_fahuo, "", 0, R.mipmap.ic_weifahuo);
                        } else if ("卖家已发货".equals(spBean.getSp_status())) {
                            CommonUtils.setDisplayImage(iv_fahuo, "", 0, R.mipmap.ic_yifahuo);
                        } else {
                        }

                        if (isShowFeilv) {
                            holder.setVisible2(R.id.tv_item_ddxq_feilv, true);
                            holder.setText(R.id.tv_item_ddxq_feilv, spBean.getSp_feilv());
                        } else {
                            holder.setVisible2(R.id.tv_item_ddxq_feilv, false);
                        }
                        final String status = spBean.getSp_aftersale_status();
                        if (!TextUtils.isEmpty(status) && !isShowFeilv) {
                            holder.setVisible2(R.id.btn_item_ddxq_chuli, true);
                            switch (status) {
                                case "等待商家处理退款申请":
                                case "等待商家处理退货":
                                    holder.setText(R.id.btn_item_ddxq_chuli, "请处理");
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, true);
                                    break;
                                case "等待商家确认并退款":
                                    holder.setText(R.id.btn_item_ddxq_chuli, "请确认退款");
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, true);
                                    break;
                                case "退款关闭":
                                case "商家拒绝退款申请":
                                    holder.setText(R.id.tv_item_ddxq_chuli, "退款关闭");
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, true);
                                    break;
                                case "商家未处理":
                                    holder.setText(R.id.tv_item_ddxq_chuli, "超时未处理");
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, true);
                                    break;
                                case "退款成功":
                                case "商家已同意退款":
                                    holder.setText(R.id.tv_item_ddxq_chuli, "退款成功");
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, true);
                                    break;
                                case "商家已同意退货申请":
                                    holder.setText(R.id.tv_item_ddxq_chuli, "等待买家发货");
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, true);
                                    break;
                                case "商家拒绝退货申请":
                                    holder.setText(R.id.tv_item_ddxq_chuli, "已拒绝申请");
                                    holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                                    holder.setVisible2(R.id.tv_item_ddxq_chuli, true);
                                    break;
                                default:

                            }
                            holder.setOnClickListener(R.id.btn_item_ddxq_chuli, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (status) {
                                        case "等待商家处理退款申请":
                                        case "等待商家处理退货":
//                                            Intent intent = new Intent(DingDanXiangQing_NewActivity.this, XieShangTuiKuanActivity.class);
//                                            intent.putExtra("item", (Serializable) bean);
//                                            intent.putExtra("position", position);
//                                            startActivity(intent);
                                            Intent intent = new Intent(DingDanXiangQing_NewActivity.this, AfterSaleActivity.class);
                                            intent.putExtra("orderId", bean.get订单id());
                                            intent.putExtra("id", bean.get订单管理商品集合().get(position).getId());
                                            startActivity(intent);
                                            break;
                                        case "等待商家确认并退款":
                                        case "商家同意退货申请":
//                                            Intent intent1 = new Intent(DingDanXiangQing_NewActivity.this, TongYiTuiHuoActivity.class);
//                                            intent1.putExtra("item", (Serializable) bean);
//                                            intent1.putExtra("position", position);
//                                            intent1.putExtra("tiem", bean.getReviewTime());
//                                            startActivity(intent1);

                                            Intent intent1 = new Intent(DingDanXiangQing_NewActivity.this, AfterSaleActivity.class);
                                            intent1.putExtra("orderId", bean.get订单id());
                                            intent1.putExtra("id", bean.get订单管理商品集合().get(position).getId());
                                            startActivity(intent1);
                                            break;

                                        default:
                                    }
                                }
                            });

                        } else {
                            holder.setVisible2(R.id.btn_item_ddxq_chuli, false);
                        }
                        switch (dingdanzhuangtai) {
                            case "卖家已发货":
                            case "交易完成":
                            case "":
                                if (TextUtils.isEmpty(status)) {
                                    if ("卖家已发货".equals(spBean.getSp_status()) || "交易完成".equals(spBean.getSp_status())) {
                                        if ("是".equals(spBean.getSp_wuliu_status())) {
                                            holder.setVisible2(R.id.tv_item_ddxq_wuliu, true);
                                            holder.setOnClickListener(R.id.tv_item_ddxq_wuliu, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(mContext, ChakanWuliuActicity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("id", dingdan_id);
                                                    bundle.putString("saleId", alist.get(position).getId());
                                                    bundle.putString("suolvtu", spBean.getSp_tupian());
                                                    bundle.putString("shangpinId", spBean.getSp_id());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                        }

                                    }
                                }

                            default:
                        }
                        CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_item_sp_pic), spBean.getSp_tupian(), 4);
                    }
                };
                lv_DDXX_shangpin.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_DDXX_shangpin);
                break;
            default:
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1://确认发货后
                setResult(1);
                finish();
                break;
            default:
        }
    }


    /**
     * 计算listview的高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight;
        totalHeight = getListviewHeight(listView);
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//            Log.e(TAG, "setListViewHeightBasedOnChildren: "+totalHeight);
//        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private int getListviewHeight(ListView pull) {

        ListAdapter listAdapter = pull.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;

        int totalHeight = 0;
        int listViewWidth = w_screen; //- dip2px(this, 16);                                         //listView在布局时的宽度,
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, pull);
            listItem.measure(widthSpec, 0);

            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
        }
        // 减掉底部分割线的高度
        int historyHeight = totalHeight
                + (pull.getDividerHeight() * listAdapter.getCount() - 1);

        return historyHeight;

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
