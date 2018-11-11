package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.WoDeShouYiBean;
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
import butterknife.OnClick;

/**
 * @author ljz(2017.12.08修改)
 *         <p>
 *         描述：我的收益
 *         修改;1.新增缴纳保证金 2.新增充值 3.界面调整
 */
public class WoDeShouYiXiangQingActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.tv_shouyi_jnstate)
    TextView tvShouyiJnstate;
    private RefreshLayout wode_shouyi_refreshlayout;
    private RelativeLayout rl_wode_shangping_fenrun;
    private ListView wode_shouyi_listview;
    private CommonAdapter commonAdapter;
    private List<WoDeShouYiBean> listDatas = new ArrayList<>(5);
    private TextView tv_wode_shouyi, tv_shangpingfenrun;
    private String phone;
    private WoDeShouYiBean woDeShouYiBean;
    private Boolean isPull = false;  // 是否上拉加载
    private int page = 0;
    private int tiaoshu;
    public static boolean isRefresh = false;
    private int tuiGuan_YongJin;
    private String jiaonaState;     // （上个页面传值）缴纳状态

    @Override
    public int initResource() {
        return R.layout.activity_wode_shouyi_new;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar(this, true, "我的收益", null);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        requestHandleArrayList.add(requestAction.shop_wl_sp(this, phone, 0));

        // 根据上个页面传值，判断是否需要缴纳保证金
        jiaonaState = getIntent().getStringExtra("jiaonaState");
        if ("是".equals(jiaonaState)){
            tvShouyiJnstate.setText("已缴纳完保证金");
        } else {
            tvShouyiJnstate.setText("缴纳保证金");
        }
    }

    public void initComponent() {
        wode_shouyi_refreshlayout = (RefreshLayout) findViewById(R.id.wode_shouyi_refreshlayout);
        rl_wode_shangping_fenrun = (RelativeLayout) findViewById(R.id.rl_wode_shangping_fenrun);
        wode_shouyi_refreshlayout.setOnRefreshListener(this);
        wode_shouyi_refreshlayout.setOnLoadListener(this);
        tv_wode_shouyi = (TextView) findViewById(R.id.tv_wode_shouyi);
        tv_shangpingfenrun = (TextView) findViewById(R.id.tv_shangpingfenrun);
        wode_shouyi_listview = (ListView) findViewById(R.id.wode_shouyi_listview);
        tv_shangpingfenrun.setOnClickListener(this);
        commonAdapter = new CommonAdapter<WoDeShouYiBean>(mContext, R.layout.item_wode_shouyi, listDatas) {
            @Override
            public void convert(ViewHolder holder, WoDeShouYiBean woDeShouYiBean) {
                TextView t = (TextView) holder.getConvertView().findViewById(R.id.item_wode_shouyi_price);
                ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.iv_wode_shouyi_picture);
                ImageView imaView_type = (ImageView) holder.getConvertView().findViewById(R.id.img_wode_shouyi_type);
                holder.setText(R.id.item_wode_shouyi_title, woDeShouYiBean.getWode_shouyi_shuoming());
                holder.setText(R.id.item_wode_shouyi_time, woDeShouYiBean.getWode_shouyi_time());

                if (woDeShouYiBean.getWode_shouyi_jifen().length() > 0) {
                    if (woDeShouYiBean.getWode_shouyi_jifen().substring(0, 1).equals("-")) {
                        holder.getView(R.id.item_wode_shouyi_zhuangtai).setVisibility(View.GONE);
                        holder.setText(R.id.item_wode_shouyi_price, woDeShouYiBean.getWode_shouyi_jifen());
                        t.setTextColor(getResources().getColor(R.color.cl_29b69d));
                    } else {
                        holder.getView(R.id.item_wode_shouyi_zhuangtai).setVisibility(View.GONE);
                        holder.setText(R.id.item_wode_shouyi_price, woDeShouYiBean.getWode_shouyi_jifen());
                        t.setTextColor(getResources().getColor(R.color.cl_ff6666));
                    }
                    CommonUtils.setDisplayImage(imageView, woDeShouYiBean.getImg(), 0, 0);
                    switch (woDeShouYiBean.getWode_shouyi_yinhangka_type()) {
                        case "已成功":
                            imaView_type.setVisibility(View.VISIBLE);
                            imaView_type.setBackgroundResource(R.mipmap.shouyi_sucess);
                            break;
                        case "审核中":
                            imaView_type.setVisibility(View.VISIBLE);
                            imaView_type.setBackgroundResource(R.mipmap.shouyi_shenghe);
                            break;
                        case "提现失败已退回":
                            imaView_type.setVisibility(View.VISIBLE);
                            imaView_type.setBackgroundResource(R.mipmap.shouyi_fail);
                            break;
                        default:
                            imaView_type.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        };
        wode_shouyi_listview.setDivider(null);
        wode_shouyi_listview.setAdapter(commonAdapter);

        wode_shouyi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (listDatas.get(position).getWode_shouyi_shuoming()) {
                    case "提现到银行卡":
                        if (!"".equals(listDatas.get(position).getId()) && listDatas.get(position).getId() != null) {
                            startActivity(new Intent(WoDeShouYiXiangQingActivity.this, ZhuanYHKXiangQingActivity.class).putExtra("id", listDatas.get(position).getId()));
                        }
                        break;
                    case "商品退款退货":
                        if (!"".equals(listDatas.get(position).getId()) && listDatas.get(position).getId() != null) {
                            startActivity(new Intent(WoDeShouYiXiangQingActivity.this, GainRecoredActivity.class).putExtra("id", listDatas.get(position).getId()).putExtra("title", "商品退款退货"));
                        }
                        break;
                    case "商品购物":
                        if (!"".equals(listDatas.get(position).getId()) && listDatas.get(position).getId() != null) {
                            startActivity(new Intent(WoDeShouYiXiangQingActivity.this, GainRecoredActivity.class).putExtra("id", listDatas.get(position).getId()).putExtra("title", "商品购物"));
                        }
                        break;
                    case "缴纳信誉保证金":
                        if (!"".equals(listDatas.get(position).getId()) && listDatas.get(position).getId() != null) {
                            startActivity(new Intent(WoDeShouYiXiangQingActivity.this, JiaoNaXiangQingActivity.class).putExtra("id", listDatas.get(position).getId()));
                        }
                        break;
                    case "提现到支付宝":
                        if (!"".equals(listDatas.get(position).getId()) && listDatas.get(position).getId() != null) {
                            startActivity(new Intent(WoDeShouYiXiangQingActivity.this, AliXiangQingActivity.class).putExtra("id", listDatas.get(position).getId()));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            onRefresh();
        } else {
            isRefresh = true;
        }
    }

    @OnClick({R.id.ll_shouyi_bzj, R.id.ll_shouyi_cz, R.id.ll_shouyi_tx})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shangpingfenrun:
                Intent intent = new Intent(mContext, ShangPingFenRunActivity.class);
                intent.putExtra("shangpingfenrun", tuiGuan_YongJin + "");
                startActivity(intent);
                break;
            case R.id.ll_shouyi_bzj:
                // 缴纳保证金
                startActivity(new Intent(mContext, BaoZhengJin01Activity.class).putExtra("jiaonaState", jiaonaState));
                break;
            case R.id.ll_shouyi_cz:
                // 充值
                startActivity(new Intent(mContext, ChongZhiActivity.class).putExtra("money", tv_wode_shouyi.getText().toString()));
                break;
            case R.id.ll_shouyi_tx:
                // 提现方式
                startActivity(new Intent(mContext, XuanZeTiXianActivity.class));
                break;
            default:
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_Erecord:
                Log.e("我的收益", response.toString());
                if (isPull == false) {
                    wode_shouyi_refreshlayout.setRefreshing(false);
                    listDatas.clear();
                } else {
                    wode_shouyi_refreshlayout.setLoading(false);
                }
                tv_wode_shouyi.setText(response.getString("mall_income"));

                //推广佣金
                tuiGuan_YongJin = response.getInt("points");
                tv_shangpingfenrun.setText("推广佣金（元）" + tuiGuan_YongJin + "  " + "明细   ");
                tiaoshu = response.getInt("num");
                if (tiaoshu > 0) {
                    JSONArray jsonArray = response.getJSONArray("list");
                    for (int i = 0; i < tiaoshu; i++) {
                        woDeShouYiBean = new WoDeShouYiBean();
                        JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                        woDeShouYiBean.setWode_shouyi_jifen(jsonObj.getString("积分"));
                        woDeShouYiBean.setWode_shouyi_shuoming(jsonObj.getString("说明"));
                        woDeShouYiBean.setWode_shouyi_time(jsonObj.getString("录入时间"));
                        woDeShouYiBean.setImg(jsonObj.getString("img"));
                        woDeShouYiBean.setId(jsonObj.getString("id"));
                        woDeShouYiBean.setWode_shouyi_yinhangka_type(jsonObj.getString("status"));
                        listDatas.add(woDeShouYiBean);
                    }
                    commonAdapter.notifyDataSetChanged();
                } else {
                    tiaoshu = 0;
                }

                break;
            default:
        }
    }

    @Override
    public void onLoad() {
        wode_shouyi_refreshlayout.setLoading(false);
        if (tiaoshu > 9) {
            isPull = true;
            page++;
            requestHandleArrayList.add(requestAction.shop_wl_sp(this, phone, page));
        } else if (tiaoshu > 0) {
            wode_shouyi_refreshlayout.setLoading(false);
            wode_shouyi_refreshlayout.isNoData();
        } else {
            wode_shouyi_refreshlayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(0);
        isPull = false;
        page = 0;
        requestHandleArrayList.add(requestAction.shop_wl_sp(this, phone, 0));
        try {
            wode_shouyi_refreshlayout.removeFoot();
        } catch (Exception e) {

        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            wode_shouyi_refreshlayout.setRefreshing(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }


}
