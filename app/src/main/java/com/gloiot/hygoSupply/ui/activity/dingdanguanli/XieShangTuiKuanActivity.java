package com.gloiot.hygoSupply.ui.activity.dingdanguanli;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.NoDoubleClickUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 协商退货退款
 */
public class XieShangTuiKuanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.im_shangping_tupian)
    ImageView imShangpingTupian;
    @Bind(R.id.tv_shangping_name)
    TextView tvShangpingName;
    @Bind(R.id.tv_shangping_jinger)
    TextView tvShangpingJinger;
    @Bind(R.id.tv_shangping_type)
    TextView tvShangpingType;
    @Bind(R.id.tv_tuihuo_tiem)
    TextView tv_tuihuo_tiem;
    @Bind(R.id.tv_tuihuo_name)
    TextView tv_tuihuo_name;
    @Bind(R.id.tv_tongyituihuo_jinge)
    TextView tv_tongyituihuo_jinge;
    @Bind(R.id.rl_tongyi_tuikuan)
    RelativeLayout rl_tongyi_tuikuan;
    @Bind(R.id.tv_tiem)
    TextView tvTiem;
    @Bind(R.id.tv_sales_tiem)
    TextView tvSalesTiem;
    @Bind(R.id.tv_buyer_name)
    TextView tvBuyerName;
    @Bind(R.id.tv_yuanying)
    TextView tvYuanying;
    @Bind(R.id.tv_jingqian)
    TextView tvJingqian;
    @Bind(R.id.tv_shuoming)
    TextView tvShuoming;
    @Bind(R.id.tv_danhao)
    TextView tvDanhao;
    @Bind(R.id.tv_shangjia_tongyi)
    TextView tvShangjiaTongyi;
    @Bind(R.id.tv_jintuikuan)
    TextView tvJintuikuan;
    //退货退款功能
    @Bind(R.id.ll_tuikuan_pingzheng)
    LinearLayout llTuikuanPingzheng;
    @Bind(R.id.im_tuikuan_pingzheng_1)
    ImageView im_tuikuan_pingzheng_1;
    @Bind(R.id.im_tuikuan_pingzheng_2)
    ImageView im_tuikuan_pingzheng_2;
    @Bind(R.id.im_tuikuan_pingzheng_3)
    ImageView im_tuikuan_pingzheng_3;
    //仅退款功能
    @Bind(R.id.tv_xieshang_zhuangtai)
    TextView tv_xieshang_zhuangtai;
    @Bind(R.id.tv_shuoming_01)
    TextView tv_shuoming_01;
    @Bind(R.id.tv_shuoming_02)
    TextView tv_shuoming_02;
    @Bind(R.id.tv_shuoming_03)
    TextView tv_shuoming_03;
    @Bind(R.id.tv_timeremaining)
    TextView tv_timeremaining;
    @Bind(R.id.ll_tuikuan_zhaopian)
    LinearLayout llTuikuanZhaopian;
    @Bind(R.id.im_tuikuan_zhaopian_1)
    ImageView im_tuikuan_zhaopian_1;
    @Bind(R.id.im_tuikuan_zhaopian_2)
    ImageView im_tuikuan_zhaopian_2;
    @Bind(R.id.im_tuikuan_zhaopian_3)
    ImageView im_tuikuan_zhaopian_3;
    @Bind(R.id.ll_xieshangtuihuoyaoqiu)
    LinearLayout llXieshangtuiyaoqiu;
    @Bind(R.id.ll_xieshangjintuikuan)
    LinearLayout llXieshangjintuikuan;
    @Bind(R.id.tv_jujuetuihuo_yuanyin)
    TextView tvJujuetuihuoYuanyin;
    @Bind(R.id.tv_jujuetuihuo_shuoming)
    TextView tvJujuetuihuoShuoming;
    @Bind(R.id.rl_jujue_tuikuan)
    RelativeLayout rlJujueTuikuan;
    @Bind(R.id.scro_tuikuan_view)
    ScrollView scroTuikuanView;
    private String type, dingdanID, shangpingID, Tuihuanren, tuikuanjie, tiem, id;
    private DingdanGuanliBean mDingdanGuanliShangpinBean;
    private ImageView[] imageViews, imageViews1;
    public static XieShangTuiKuanActivity instance = null;
    private boolean isJujue, isTuiKuan, isFinish = true;
    private int position;

    @Override
    public int initResource() {
        return R.layout.activity_xieshangtuikuan;
    }

    @Override
    public void initData() {
        instance = this;
        imageViews = new ImageView[3];
        imageViews[0] = im_tuikuan_pingzheng_1;
        imageViews[1] = im_tuikuan_pingzheng_2;
        imageViews[2] = im_tuikuan_pingzheng_3;
        imageViews1 = new ImageView[3];//仅退款功能
        imageViews1[0] = im_tuikuan_zhaopian_1;
        imageViews1[1] = im_tuikuan_zhaopian_2;
        imageViews1[2] = im_tuikuan_zhaopian_3;
        Intent intent = getIntent();
        mDingdanGuanliShangpinBean = (DingdanGuanliBean) intent.getSerializableExtra("item");
        position = intent.getIntExtra("position", 0);
        DingdanGuanliShangpinBean shangpinBean = mDingdanGuanliShangpinBean.get订单管理商品集合().get(position);
        CommonUtils.setDisplayImageOptions(imShangpingTupian, shangpinBean.get缩略图(), 4);
        Tuihuanren = mDingdanGuanliShangpinBean.get退款人帐号();
        type = shangpinBean.getShouhou_status();
        tvShangpingName.setText(shangpinBean.get商品名称());
        tvShangpingJinger.setText(shangpinBean.get价格());
        dingdanID = mDingdanGuanliShangpinBean.get订单id();
        shangpingID = shangpinBean.get商品id();
        id = shangpinBean.getId();
        tvShangpingType.setText(type);
        tv_timeremaining.setText("剩" + mDingdanGuanliShangpinBean.getCount_down());
        if (type.equals("等待商家处理退款申请")) {
            requestHandleArrayList.add(requestAction.TuiKuanShenQing(XieShangTuiKuanActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), "退款", dingdanID, shangpingID, id));
            CommonUtils.setTitleBar((Activity) mContext, true, "协商仅退款", "");
            tvShangjiaTongyi.setText("同意退款");
            tv_xieshang_zhuangtai.setText("请处理退款");
            tv_shuoming_01.setText("1、如果您同意，将直接退款给买家。");
            tv_shuoming_02.setText("2、如果您拒绝，买家可以要求环游购介入处理，如核实是您的责任，将会影响您店铺的纠纷退款率。");
            tv_shuoming_03.setText("3、逾期未处理，买家可以申请环游购介入处理");
        } else if (type.equals("等待商家处理退货")) {
            requestHandleArrayList.add(requestAction.TuiKuanShenQing(XieShangTuiKuanActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), "退款退货", dingdanID, shangpingID, id));
            CommonUtils.setTitleBar((Activity) mContext, true, "协商退货退款", "");
            tvShangjiaTongyi.setText("同意退货");
            tv_xieshang_zhuangtai.setText("请处理退货申请");
            tv_shuoming_01.setText("1.如您同意，请将正确退货地址发给买家");
            tv_shuoming_02.setText("2.如您拒绝，买家可以要求官方介入");
            tv_shuoming_03.setText("3.如逾期未处理，视为同意，系统将发送默认退货地址给买家。");
        } else if (type.equals("等待商家处理换货")) {
            requestHandleArrayList.add(requestAction.TuiKuanShenQing(XieShangTuiKuanActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), "退款退货", dingdanID, shangpingID, id));
            CommonUtils.setTitleBar((Activity) mContext, true, "协商退货退款", "");
            tvShangjiaTongyi.setText("同意换货");
            tv_xieshang_zhuangtai.setText("请处理退货申请");
            tv_shuoming_01.setText("1.如您同意，请将正确退货地址发给买家");
            tv_shuoming_02.setText("2.如您拒绝，买家可以要求官方介入");
            tv_shuoming_03.setText("3.如逾期未处理，视为同意，系统将发送默认退货地址给买家。");
        }

    }

    @OnClick({R.id.tv_shangjia_jujue, R.id.tv_shangjia_guanfang, R.id.tv_shangjia_tongyi})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shangjia_jujue://拒绝
                if (isJujue) {
                    MToastUtils.showToast("请勿重复拒绝申请");
                } else if (isTuiKuan) {
                    MToastUtils.showToast("您已退款成功！");
                } else {
                    Intent intent = new Intent(XieShangTuiKuanActivity.this, JuJueShenQingActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("dingdanID", dingdanID);
                    intent.putExtra("shangpinId", shangpingID);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, 1);
                }

                break;
            case R.id.tv_shangjia_guanfang://官方介入
//                intent = new Intent(XieShangTuiKuanActivity.this, ShenqingGuanFangJieRuActivity.class);
                Intent intent1;
                intent1 = new Intent(XieShangTuiKuanActivity.this, QingxuanzeKefuActivity.class);
                if ("00天00小时00分钟".equals(mDingdanGuanliShangpinBean.getCount_down())) {
                    intent1.putExtra("isAvailable", true);
                }
                startActivity(intent1);
                break;
            case R.id.tv_shangjia_tongyi://同意(两种状态)
                if (type.equals("等待商家处理退款申请")) {


                    DialogUtlis.twoBtnNormal(mContext, "是否确认退款", "提示", false, "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
                            if (!isFinish) {
                                return;
                            }

                            if (!isJujue) {
                                requestHandleArrayList.add(requestAction.Manual_wl_refund(XieShangTuiKuanActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), Tuihuanren, dingdanID, shangpingID, id));
                            } else {
                                MToastUtils.showToast("该退款申请已被拒绝");
                            }
                            isFinish = false;
                            DialogUtlis.dismissDialog();
                        }
                    });

                } else {
                    if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击

                    DialogUtlis.twoBtnNormal(mContext, "是否确认退货", "提示", false, "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isJujue) {
                                Intent intent2 = new Intent(XieShangTuiKuanActivity.this, TongYiTuiHuoActivity.class);
                                intent2.putExtra("item", mDingdanGuanliShangpinBean);
                                intent2.putExtra("Tuihuanren", Tuihuanren);
                                intent2.putExtra("tiem", tiem);
                                startActivity(intent2);
                            } else {
                                MToastUtils.showToast("该退货申请已被拒绝");
                            }
                            DialogUtlis.dismissDialog();
                        }
                    });

                }
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_record:
                Log.e("--已请求--", "已请求");
                if (response.getString("状态").equals("成功")) {
                    tvTiem.setText(response.getString("录入时间"));//客户旁边时间
                    tvSalesTiem.setText(response.getString("录入时间"));//系统旁边时间
                    tiem = response.getString("录入时间");
                    tvDanhao.setText(response.getString("订单id"));
                    tvBuyerName.setText(response.getString("退款姓名"));
                    JSONArray jsonArray = response.getJSONArray("凭证");
                    if (jsonArray.length() == 0) {
                        llTuikuanPingzheng.setVisibility(View.GONE);
                        llTuikuanZhaopian.setVisibility(View.GONE);
                    }
                    if (!type.equals("等待商家处理退款申请")) {
                        llXieshangtuiyaoqiu.setVisibility(View.VISIBLE);
                        Tuihuanren = response.getString("退款账号");
                        tvYuanying.setText(response.getString("退款原因"));
                        tvJingqian.setText(response.getString("退款金额"));
                        tvShuoming.setText(response.getString("退款说明"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i >= 3) {
                                break;
                            }
                            llTuikuanZhaopian.setVisibility(View.VISIBLE);
                            CommonUtils.setDisplayImageOptions(imageViews[i], ((JSONObject) jsonArray.get(i)).getString("图片"), 0);
                        }
                    } else {
                        llXieshangjintuikuan.setVisibility(View.VISIBLE);
                        tuikuanjie = response.getString("退款金额");
                        tvJintuikuan.setText("发起了仅退款申请\n退款原因："
                                + response.getString("退款原因") + "\n退款金额："
                                + tuikuanjie + "元。" + "\n退款说明：" + response.getString("退款说明") + "\n物流状态：" + response.getString("物流状态"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i >= 3) {
                                break;
                            }
                            llTuikuanPingzheng.setVisibility(View.VISIBLE);
                            CommonUtils.setDisplayImageOptions(imageViews1[i], ((JSONObject) jsonArray.get(i)).getString("图片"), 0);

                        }
                    }
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
            case ConstantUtils.TAG_Manual_wl_refund:
                isFinish = true;
                if (response.getString("状态").equals("成功")) {
                    tv_tuihuo_tiem.setVisibility(View.VISIBLE);
                    tv_tuihuo_name.setVisibility(View.VISIBLE);
                    tv_tuihuo_tiem.setText(response.getString("时间"));
                    rl_tongyi_tuikuan.setVisibility(View.VISIBLE);
                    tv_tongyituihuo_jinge.setText("退款金额：" + tuikuanjie + "元");
                    MToastUtils.showToast("退款成功");
                    isTuiKuan = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        scroTuikuanView.fullScroll(ScrollView.FOCUS_DOWN);
                                    } catch (Exception e) {

                                    }

                                }
                            });
                        }
                    }).start();

                }
                break;
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
        isFinish = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String zhuangtai = data.getStringExtra("type");
                    String shuoming = data.getStringExtra("shuoming");
                    String yuanyin = data.getStringExtra("yuanyin");
                    if (!TextUtils.isEmpty(zhuangtai)) {
                        if ("成功".equals(zhuangtai)) {
                            MToast.showToast("拒绝成功");
                            rlJujueTuikuan.setVisibility(View.VISIBLE);
                            tvJujuetuihuoShuoming.setText("拒绝说明：" + shuoming);
                            tvJujuetuihuoYuanyin.setText("拒绝原因：" + yuanyin);
                            isJujue = true;
//                            tvShangpingType.setText("退款关闭");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                scroTuikuanView.fullScroll(ScrollView.FOCUS_DOWN);
                                            } catch (Exception e) {

                                            }

                                        }
                                    });
                                }
                            }).start();

//                            finish();
                        }
                    } else {
                        return;
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        setResult(2);
        super.onDestroy();

    }

}
