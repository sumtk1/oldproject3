package com.gloiot.hygoSupply.ui.activity.dingdanguanli;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.NoDoubleClickUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TongYiTuiHuoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.money)
    TextView Money;
    @Bind(R.id.tv_tuihuodizhi)
    TextView tvTuihuodizhi;
    @Bind(R.id.tv_sales_tiem)
    TextView tv_sales_tiem;
    @Bind(R.id.tv_tiem_1)
    TextView tv_tiem_1;
    @Bind(R.id.tv_tiem_2)
    TextView tv_tiem_2;
    @Bind(R.id.tv_tiem_3)
    TextView tv_tiem_3;
    @Bind(R.id.tv_tuihuo_tiem)
    TextView tv_tuihuo_tiem;
    @Bind(R.id.tv_tuihuo_name)
    TextView tv_tuihuo_name;
    @Bind(R.id.tv_tuikuanbianhao)
    TextView tv_tuikuanbianhao;
    @Bind(R.id.tv_tongyituihuo_jinge)
    TextView tv_tongyituihuo_jinge;
    @Bind(R.id.rl_tongyi_tuikuan)
    RelativeLayout rl_tongyi_tuikuan;
    @Bind(R.id.rl_maijiaqueren)
    RelativeLayout rl_maijiaqueren;
    @Bind(R.id.tv_shangjia_tongyi)
    TextView tvShangjiaTongyi;
    private String dingdanID, Tuihuanren, shangpingID, tuikuanjinger, id;
    private DingdanGuanliBean mDingdanGuanliShangpinBean;
    private String reviewTime;
    private int position;
    private boolean isTuihuo;
    private boolean isFinish = true;


    @Override
    public int initResource() {
        return R.layout.activity_tongyituihuo;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        mDingdanGuanliShangpinBean = (DingdanGuanliBean) intent.getSerializableExtra("item");
        Tuihuanren = mDingdanGuanliShangpinBean.get退款人帐号();
        DingdanGuanliShangpinBean shangpinBean = mDingdanGuanliShangpinBean.get订单管理商品集合().get(position);
        dingdanID = mDingdanGuanliShangpinBean.get订单id();
        shangpingID = shangpinBean.get商品id();
        id = shangpinBean.getId();

        reviewTime = mDingdanGuanliShangpinBean.getReviewTime();
        CommonUtils.setTitleBar((Activity) mContext, true, "等待商家确认并换货".equals(shangpinBean.getShouhou_status()) ? "确认换货" : "确认退货", "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (null != XieShangTuiKuanActivity.instance) {
                    XieShangTuiKuanActivity.instance.finish();
                }
            }
        });
        if ("等待商家确认并换货".equals(shangpinBean.getShouhou_status())) {
            tvShangjiaTongyi.setText("确认换货");
        } else {
            tvShangjiaTongyi.setText("确认退货");
        }

        if ("商家已同意换货申请".equals(shangpinBean.getShouhou_status()) || "商家已同意退货申请".equals(shangpinBean.getShouhou_status()) || "等待商家确认并退款".equals(shangpinBean.getShouhou_status()) || "等待商家确认并换货".equals(shangpinBean.getShouhou_status())) {
            Money.setText("系统已冻结卖家资金" + shangpinBean.get价格() + "元，确保退货资金安全");
            tvTuihuodizhi.setText("卖家确认退货地址：" + mDingdanGuanliShangpinBean.get收货地址());
            tuikuanjinger = shangpinBean.get价格();
            tv_sales_tiem.setText(intent.getStringExtra("tiem"));
            tv_tiem_1.setText(intent.getStringExtra("tiem"));
            tv_tiem_2.setText(intent.getStringExtra("tiem"));
            tv_tiem_3.setText(intent.getStringExtra("tiem"));
            tv_tuihuo_tiem.setText(intent.getStringExtra("tiem"));
        } else {
            tv_sales_tiem.setText(intent.getStringExtra("tiem"));
            tv_tiem_1.setText(intent.getStringExtra("tiem"));
            tv_tiem_2.setText(intent.getStringExtra("tiem"));
            tv_tiem_3.setText(intent.getStringExtra("tiem"));
            tv_tuihuo_tiem.setText(intent.getStringExtra("tiem"));
            requestHandleArrayList.add(requestAction.shop_wl_tyth(TongYiTuiHuoActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), dingdanID, shangpingID, id));
        }
    }

    @OnClick({R.id.tv_shangjia_tongyi, R.id.tv_shangjia_guanfang})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shangjia_tongyi://同意退货
                if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
                DialogUtlis.twoBtnNormal(mContext, "是否确认退货", "提示", false, "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialog();

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isFinish) {
                            return;
                        } else if (!isTuihuo) {
                            requestHandleArrayList.add(requestAction.Return_wl_goods(TongYiTuiHuoActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), Tuihuanren, dingdanID, shangpingID, id));
                        } else {
                            MToastUtils.showToast("请勿重复确认退货");
                        }
                        isFinish = false;
                        DialogUtlis.dismissDialog();
                    }
                });

                break;
            case R.id.tv_shangjia_guanfang:
                Intent intent1;
                intent1 = new Intent(TongYiTuiHuoActivity.this, QingxuanzeKefuActivity.class);
                if ("00天00小时00分钟".equals(mDingdanGuanliShangpinBean.getCount_down())) {
                    intent1.putExtra("isAvailable", true);
                }
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_tyth:
                tuikuanjinger = response.getString("退款金额");
                String shouhuoren = response.getString("收货人");
                String shoujihao = response.getString("手机号");
                String tuihuodizhi = response.getString("退货地址");
                Tuihuanren = response.getString("退款账号");
                Money.setText("已冻结卖家资金" + tuikuanjinger + "元，确保退货资金安全");
                tvTuihuodizhi.setText("卖家确认退货地址：" + tuihuodizhi);
                break;
            case ConstantUtils.TAG_Return_wl_goods:
                isFinish = true;
                if (response.getString("状态").equals("成功")) {
                    rl_maijiaqueren.setVisibility(View.VISIBLE);
                    tv_tongyituihuo_jinge.setText("退款金额：" + tuikuanjinger + "元");
                    tv_tuikuanbianhao.setText("编号：" + dingdanID);
                    isTuihuo = true;
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        isFinish = true;
    }
}
