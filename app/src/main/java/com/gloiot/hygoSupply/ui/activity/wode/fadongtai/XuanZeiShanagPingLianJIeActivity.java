package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class XuanZeiShanagPingLianJIeActivity extends BaseActivity implements
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.iv_toptitle_back)
    ImageView ivToptitleBack;
    @Bind(R.id.tv_sousuo)
    TextView tvSousuo;
    @Bind(R.id.et_sousuo_info)
    EditText etSousuoInfo;
    @Bind(R.id.tv_fenlei_zonghe)
    TextView tvFenleiZonghe;
    @Bind(R.id.iv_fenlei_zonghe)
    ImageView ivFenleiZonghe;
    @Bind(R.id.ll_fenlei_zonghe)
    LinearLayout llFenleiZonghe;
    @Bind(R.id.tv_fenlei_xiaoliang)
    TextView tvFenleiXiaoliang;
    @Bind(R.id.ll_fenlei_xiaoliang)
    LinearLayout llFenleiXiaoliang;
    @Bind(R.id.tv_fenlei_jiage)
    TextView tvFenleiJiage;
    @Bind(R.id.iv_fenlei_jiage)
    ImageView ivFenleiJiage;
    @Bind(R.id.ll_fenlei_jiage)
    LinearLayout llFenleiJiage;
    @Bind(R.id.ll_fenlei_biaoti)
    LinearLayout llFenleiBiaoti;
    @Bind(R.id.lv_shangpinglianjie)
    ListView lvShangpinglianjie;
    @Bind(R.id.but_save_lianjie)
    Button butSaveLianjie;
    @Bind(R.id.rl_no_product)
    RelativeLayout rlNoProduct;
    @Bind(R.id.refreshlayout_fg_shangpin_guanli)
    RefreshLayout refreshlayoutFgShangpinGuanli;
    private CommonAdapter adapter;
    private List<ShangpinGuanliBean> allShangpinGuanli;
    private String phone;
    private int page;
    private String condition;
    private boolean refresh;
    private int xiaoshouNum;
    private int selectPosition = -1;
    //PopupWindow
    private Fenlei_ZonghePop fenlei_zonghePop;
    private boolean isTuiJianShangPin; // 是否是从消息-推荐商品进入

    private String type = "";

    @Override
    public int initResource() {
        return R.layout.activity_xuanzeishanagpinglianjie;
    }

    @Override
    public void initData() {
        isTuiJianShangPin = getIntent().getBooleanExtra("tuijian", false);
        type = getIntent().getStringExtra("type");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        condition = "综合排序";
        allShangpinGuanli = new ArrayList<>();
        refreshlayoutFgShangpinGuanli.setOnRefreshListener(this);
        refreshlayoutFgShangpinGuanli.setOnLoadListener(this);
        adapter = new CommonAdapter<ShangpinGuanliBean>(this, R.layout.item_dynamic_product_choice, allShangpinGuanli) {
            @Override
            public void convert(ViewHolder holder, final ShangpinGuanliBean shangpinGuanliBean, final int position) {
                final ImageView picView = holder.getView(R.id.iv_dynamic_product_picture);
                holder.setText(R.id.tv_dynamic_product_name, shangpinGuanliBean.getShangpinmingcheng());
                holder.setText(R.id.tv_dynamic_product_price, "￥ " + shangpinGuanliBean.getDanjia());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.setDisplayImageOptions(picView, shangpinGuanliBean.getSuoluetu(), 4);
                    }
                });
                if (selectPosition == position) {
                    holder.setImageResource(R.id.iv_dynamic_product_choice, R.mipmap.icon_the_selected);
                } else {
                    holder.setImageResource(R.id.iv_dynamic_product_choice, R.mipmap.icon_not_to_choose);
                }
                holder.setOnClickListener(R.id.iv_dynamic_product_choice, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition = position;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lvShangpinglianjie.setAdapter(adapter);
        requestHandleArrayList.add(requestAction.shop_wl_choiceshop(this, phone, String.valueOf(page), condition, "", type));

        if (isTuiJianShangPin) {
            butSaveLianjie.setText("发送链接");
        }

        tvSousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSousuoInfo.getText().toString())) {
                    MToastUtils.showToast("请输入搜索内容");
                } else {
                    allShangpinGuanli.clear();
                    adapter.notifyDataSetChanged();
                    selectPosition = -1;
                    requestHandleArrayList.add(requestAction.shop_wl_choiceshop(XuanZeiShanagPingLianJIeActivity.this, phone, String.valueOf(page), condition, etSousuoInfo.getText().toString(), type));
                }
            }
        });

        butSaveLianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition == -1) {
                    MToastUtils.showToast("请选择商品");
                } else {
                    ShangpinGuanliBean shangpinGuanliBean = allShangpinGuanli.get(selectPosition);
                    String id = shangpinGuanliBean.getId();
                    String url = shangpinGuanliBean.getSuoluetu();
                    String name = shangpinGuanliBean.getShangpinmingcheng();
                    String price = shangpinGuanliBean.getDanjia();
                    if (isTuiJianShangPin) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id);
                        bundle.putString("icon", url);
                        bundle.putString("title", name);
                        bundle.putString("money", price);
                        bundle.putString("extra", "");

                        ChatUiIM.getInstance().sendShangpin(bundle);
                    } else {
                        Intent intent = new Intent();
                        // TODO: 2017/8/17  这里等返回数量
                        String num = "1";
                        intent.putExtra("url", url);
                        intent.putExtra("id", id);
                        intent.putExtra("name", name);
                        intent.putExtra("price", price);
                        intent.putExtra("num", num);
                        setResult(RESULT_OK, intent);
                    }

                    finish();
                }
            }
        });
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_choiceshop:

                String id;    //  商品ID   销售和下架独有
                String shangpinmingcheng;//商品民称
                String jiesuanjia;//结算价
                String kucun;//库存
                String zhuangtai; //状态    申请独有
                String suoluetu;//缩略图
                String xiaoshou = null;//销售   销售和下架独有
                try {
                    if (refresh) {
                        selectPosition = -1;
                        allShangpinGuanli.clear();
                        refreshlayoutFgShangpinGuanli.setRefreshing(false);
                    } else {
                        refreshlayoutFgShangpinGuanli.setLoading(false);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "requestSuccess: " + e.getMessage());
                }

                xiaoshouNum = Integer.parseInt(jsonObject.getString("条数"));
                if (xiaoshouNum > 0) {  //下架商品有数据
                    rlNoProduct.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject xiaoshouShangpin = jsonArray.getJSONObject(i);
                        id = xiaoshouShangpin.getString("id");
                        shangpinmingcheng = xiaoshouShangpin.getString("商品名称");
                        suoluetu = xiaoshouShangpin.getString("缩略图");
                        jiesuanjia = xiaoshouShangpin.getString("价格");
                        ShangpinGuanliBean shenqingBean = new ShangpinGuanliBean(id, shangpinmingcheng, jiesuanjia, "", "", suoluetu, "");
                        allShangpinGuanli.add(shenqingBean);
                    }
                } else {
                    rlNoProduct.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;

        }
    }


    @Override
    public void onRefresh() {
        refresh = true;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_choiceshop(this, phone, String.valueOf(page), condition, "", type));
        try {
            refreshlayoutFgShangpinGuanli.removeFoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {//上拉加载更多
        try {
            refresh = false;
            if (xiaoshouNum >= 10) {
                page++;
                requestHandleArrayList.add(requestAction.shop_wl_choiceshop(this, phone, String.valueOf(page), condition, "", type));
            } else if (xiaoshouNum > 0) {
                refreshlayoutFgShangpinGuanli.setLoading(false);
                refreshlayoutFgShangpinGuanli.isNoData();
            } else {
                refreshlayoutFgShangpinGuanli.setLoading(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_toptitle_back, R.id.ll_fenlei_zonghe, R.id.ll_fenlei_xiaoliang, R.id.ll_fenlei_jiage})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toptitle_back:
                finish();
                break;
            //中部切换
            case R.id.ll_fenlei_zonghe:
                resetView();
                tvFenleiZonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                ivFenleiZonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                if (fenlei_zonghePop == null) {
                    fenlei_zonghePop = new Fenlei_ZonghePop(this);
                }
                fenlei_zonghePop.showAsDropDown(llFenleiZonghe);
                break;
            case R.id.ll_fenlei_xiaoliang:
                changeView(2);
                break;
            case R.id.ll_fenlei_jiage:
                changeView(3);
                break;
            default:
                break;
        }
    }

    public class Fenlei_ZonghePop extends PopupWindow implements View.OnClickListener {
        private View zonghePopView;
        private RelativeLayout rl_pop_zonghe_paixu, rl_pop_xinpin_youxian;
        private TextView tv_pop_zonghe_paixu, tv_pop_xinpin_youxian;
        private ImageView iv_pop_zonghe_paixu, iv_pop_xinpin_youxian;

        public Fenlei_ZonghePop(Context context) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            zonghePopView = inflater.inflate(R.layout.pop_fenlei_zonghe, null);

            rl_pop_zonghe_paixu = (RelativeLayout) zonghePopView.findViewById(R.id.rl_pop_zonghe_paixu);
            rl_pop_xinpin_youxian = (RelativeLayout) zonghePopView.findViewById(R.id.rl_pop_xinpin_youxian);
            tv_pop_zonghe_paixu = (TextView) zonghePopView.findViewById(R.id.tv_pop_zonghe_paixu);
            tv_pop_xinpin_youxian = (TextView) zonghePopView.findViewById(R.id.tv_pop_xinpin_youxian);
            iv_pop_zonghe_paixu = (ImageView) zonghePopView.findViewById(R.id.iv_pop_zonghe_paixu);
            iv_pop_xinpin_youxian = (ImageView) zonghePopView.findViewById(R.id.iv_pop_xinpin_youxian);
            rl_pop_zonghe_paixu.setOnClickListener(this);
            rl_pop_xinpin_youxian.setOnClickListener(this);

            // 设置SelectPicPopupWindow的View
            this.setContentView(zonghePopView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(1f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_pop_zonghe_paixu:
                    tv_pop_zonghe_paixu.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_pop_xinpin_youxian.setTextColor(getResources().getColor(R.color.cl_999999));
                    iv_pop_zonghe_paixu.setImageResource(R.mipmap.ic_xuanze);
                    iv_pop_xinpin_youxian.setImageResource(R.mipmap.ic_xuanze_no);
                    zonghe_zhuangtai = 0;
                    dismiss();
                    break;
                case R.id.rl_pop_xinpin_youxian:
                    tv_pop_xinpin_youxian.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tv_pop_zonghe_paixu.setTextColor(getResources().getColor(R.color.cl_999999));
                    iv_pop_xinpin_youxian.setImageResource(R.mipmap.ic_xuanze);
                    iv_pop_zonghe_paixu.setImageResource(R.mipmap.ic_xuanze_no);
                    zonghe_zhuangtai = 1;
                    dismiss();
                    break;
            }
        }
    }

    /**
     * PopouWindow设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    /**
     * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
            changeView(1);
        }
    }

    private void qingQiuShuJu(String str) {
        etSousuoInfo.setText("");
        if (condition.equals(str)) {
            return;
        } else {
            //加载数据
            page = 0;
            xiaoshouNum = 0;
            allShangpinGuanli.clear();
            adapter.notifyDataSetChanged();
            selectPosition = -1;
            requestHandleArrayList.add(requestAction.shop_wl_choiceshop(this, phone, String.valueOf(page), str, "", type));
        }
        condition = str;
    }

    //0为综合排序，1为新品优先
    private int zonghe_zhuangtai = 0;
    // 0为未选择价格排序；1为从高到底；-1为从低到高
    private int jiage_zhuangtai = 0;

    public void changeView(int i) {
        resetView();
        switch (i) {
            case 1:
                if (zonghe_zhuangtai == 0) {
                    tvFenleiZonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tvFenleiZonghe.setText("综合");
                    ivFenleiZonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("综合排序");
                } else if (zonghe_zhuangtai == 1) {
                    tvFenleiZonghe.setTextColor(getResources().getColor(R.color.cl_E33333));
                    tvFenleiZonghe.setText("新品");
                    ivFenleiZonghe.setImageResource(R.mipmap.ic_fenlei_zonghe1);
                    jiage_zhuangtai = 0;
                    qingQiuShuJu("新品优先");
                }
                break;
            case 2:
                tvFenleiXiaoliang.setTextColor(getResources().getColor(R.color.cl_E33333));
                jiage_zhuangtai = 0;
                qingQiuShuJu("销量");
                break;
            case 3:
                tvFenleiJiage.setTextColor(getResources().getColor(R.color.cl_E33333));
                if (jiage_zhuangtai == 0) {
                    jiage_zhuangtai = -1;
                    ivFenleiJiage.setImageResource(R.mipmap.ic_fenlei_jiage2);
                    qingQiuShuJu("价格低");
                } else if (jiage_zhuangtai == 1) {
                    jiage_zhuangtai = -1;
                    ivFenleiJiage.setImageResource(R.mipmap.ic_fenlei_jiage2);
                    qingQiuShuJu("价格低");
                } else if (jiage_zhuangtai == -1) {
                    jiage_zhuangtai = 1;
                    ivFenleiJiage.setImageResource(R.mipmap.ic_fenlei_jiage1);
                    qingQiuShuJu("价格高");
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化界面
     */
    private void resetView() {
        tvFenleiZonghe.setTextColor(getResources().getColor(R.color.cl_333333));
        tvFenleiXiaoliang.setTextColor(getResources().getColor(R.color.cl_333333));
        tvFenleiJiage.setTextColor(getResources().getColor(R.color.cl_333333));
        ivFenleiZonghe.setImageResource(R.mipmap.ic_fenlei_zonghe0);
        ivFenleiJiage.setImageResource(R.mipmap.ic_fenlei_jiage0);
    }


}
