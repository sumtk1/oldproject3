package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingDanBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import butterknife.Bind;

public class DingdanXiangqingActivity extends BaseActivity {
    @Bind(R.id.iv_dingdan_xiangqing_touxiang)
    ImageView ivDingdanXiangqingTouxiang;
    @Bind(R.id.tv_dingdan_xiangqing_nicheng)
    TextView tvDingdanXiangqingNicheng;
    @Bind(R.id.tv_dingdan_xiangqing_jine)
    TextView tvDingdanXiangqingJine;
    @Bind(R.id.tv_dingdan_xiangqing_zhuangtai)
    TextView tvDingdanXiangqingZhuangtai;
    @Bind(R.id.tv_dingdan_xiangqing_shuoming)
    TextView tvDingdanXiangqingShuoming;
    @Bind(R.id.tv_dingdan_xiangqing_dizhi)
    TextView tvDingdanXiangqingDizhi;
    @Bind(R.id.tv_dingdan_xiangqing_time)
    TextView tvDingdanXiangqingTime;
    @Bind(R.id.tv_dingdan_xiangqing_dingdanhao)
    TextView tvDingdanXiangqingDingdanhao;
    @Bind(R.id.tv_dingdan_xiangqing_zhifu_fangshi)
    TextView tvDingdanXiangqingZhifuFangshi;

    private DingDanBean dingDanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_dingdan_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "订单详情", "");
        Intent intent = this.getIntent();
        dingDanBean = (DingDanBean) intent.getSerializableExtra("订单");
        CommonUtils.setDisplayImageOptions(ivDingdanXiangqingTouxiang, dingDanBean.getTouxiang(), 0);
        tvDingdanXiangqingNicheng.setText(dingDanBean.getNicheng());
        tvDingdanXiangqingJine.setText(dingDanBean.getShifu_jine());
        tvDingdanXiangqingZhuangtai.setText(dingDanBean.getZhuangtai());
        tvDingdanXiangqingShuoming.setText(dingDanBean.getShangpin_shuoming());
        tvDingdanXiangqingDizhi.setText(dingDanBean.getShouhuo_dizhi());
        tvDingdanXiangqingTime.setText(dingDanBean.getChuangjian_time());
        tvDingdanXiangqingDingdanhao.setText(dingDanBean.getDingdan_id());
        tvDingdanXiangqingZhifuFangshi.setText(dingDanBean.getZhifu_fangshi());
    }

}
