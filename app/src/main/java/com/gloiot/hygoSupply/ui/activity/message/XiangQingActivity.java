package com.gloiot.hygoSupply.ui.activity.message;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/7/12.
 */

public class XiangQingActivity extends BaseActivity {

    @Bind(R.id.rl_shuoming)
    RelativeLayout rl_shuoming;
    @Bind(R.id.rl_shoukuanfan)
    RelativeLayout rl_shoukuanfan;
    @Bind(R.id.rl_dingdanhao)
    RelativeLayout rl_dingdanhao;
    @Bind(R.id.rl_zhifufangshi)
    RelativeLayout rl_zhifufangshi;
    @Bind(R.id.rl_beizhu)
    RelativeLayout rl_beizhu;
    @Bind(R.id.rl_shijian)
    RelativeLayout rl_shijian;
    @Bind(R.id.tv_content)
    TextView tv_content;
    @Bind(R.id.tv_money)
    TextView tv_money;
    @Bind(R.id.tv_jiaoyi_type)
    TextView tv_jiaoyi_type;
    @Bind(R.id.tv_shoukuanfan)
    TextView tv_shoukuanfan;
    @Bind(R.id.tv_shouming)
    TextView tv_shouming;
    @Bind(R.id.tv_dingdanhao)
    TextView tv_dingdanhao;
    @Bind(R.id.tv_beizhu)
    TextView tv_beizhu;
    @Bind(R.id.tv_shijian)
    TextView tv_shijian;
    @Bind(R.id.tv_zhifufangshi)
    TextView tv_zhifufangshi;
    @Bind(R.id.tv_01)
    TextView tv01;
    @Bind(R.id.tv_02)
    TextView tv02;
    @Bind(R.id.tv_03)
    TextView tv03;
    @Bind(R.id.tv_04)
    TextView tv04;
    @Bind(R.id.tv_05)
    TextView tv05;
    @Bind(R.id.tv_06)
    TextView tv06;

    private String type;

    @Override
    public int initResource() {
        return R.layout.activity_jilu_xiangqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "账单详情");

        String id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        if (type.equals("支付宝充值") && type.equals("积分充值")) {
            rl_shoukuanfan.setVisibility(View.GONE);
            rl_dingdanhao.setVisibility(View.GONE);
        } else {

        }
//        requestHandleArrayList.add(requestAction.p_billing_details_three(this, id, type));

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case RequestAction.TAG_DETAILS:
//
//                JSONArray jsonArray = response.getJSONArray("列表");
//                if (jsonArray.length() > 0) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        if (i == 0) {
//                            tv_jiaoyi_type.setText(jsonObject.getString("交易状态"));
//                            tv_money.setText(jsonObject.getString("金额"));
//                            tv_content.setText(jsonObject.getString("业务名"));
//                        } else {
//                            switch (type) {
//                                case "积分转让":
//                                    rl_dingdanhao.setVisibility(View.GONE);
//
//                                    tv_beizhu.setText(jsonObject.getString("备注"));
//                                    tv_shouming.setText(jsonObject.getString("说明"));
//                                    tv_shijian.setText(jsonObject.getString("时间"));
//                                    tv_zhifufangshi.setText(jsonObject.getString("支付方式"));
//                                    tv_shoukuanfan.setText(jsonObject.getString("入账账户"));
//                                    break;
//                                case "积分充值":
//                                    rl_shoukuanfan.setVisibility(View.GONE);
//
//                                    tv_shouming.setText(jsonObject.getString("说明"));
//                                    tv_shijian.setText(jsonObject.getString("时间"));
//                                    tv_zhifufangshi.setText(jsonObject.getString("支付方式"));
//                                    tv_dingdanhao.setText(jsonObject.getString("支付订单号"));
//                                    tv05.setText("赠送积分");
//                                    tv_beizhu.setText(jsonObject.getString("赠送积分"));
//                                    break;
//
//                                case "提现":
//                                    rl_shijian.setVisibility(View.GONE);
//                                    rl_zhifufangshi.setVisibility(View.GONE);
//                                    rl_beizhu.setVisibility(View.GONE);
//                                    tv01.setText("银行名称");
//                                    tv02.setText("银行账户");
//                                    tv03.setText("时间");
//                                    tv_shoukuanfan.setText(jsonObject.getString("银行名称"));
//                                    tv_shouming.setText(jsonObject.getString("银行账户"));
//                                    tv_dingdanhao.setText(jsonObject.getString("时间"));
//                                    break;
//                                case "超级商家":
//                                    rl_zhifufangshi.setVisibility(View.GONE);
//
//                                    tv_beizhu.setText(jsonObject.getString("备注"));
//                                    tv_shouming.setText(jsonObject.getString("说明"));
//                                    tv_shijian.setText(jsonObject.getString("时间"));
//                                    tv_dingdanhao.setText(jsonObject.getString("订单号"));
//                                    tv_shoukuanfan.setText(jsonObject.getString("入账账户"));
//                                    break;
//
//                                case "充值中心":
//                                    rl_shoukuanfan.setVisibility(View.GONE);
//                                    rl_beizhu.setVisibility(View.GONE);
//
//                                    tv_shouming.setText(jsonObject.getString("说明"));
//                                    tv_shijian.setText(jsonObject.getString("时间"));
//                                    tv_dingdanhao.setText(jsonObject.getString("订单号"));
//                                    tv_zhifufangshi.setText(jsonObject.getString("支付方式"));
//                                    break;
//                                // 商金币/积分宝/油费/全部/支付宝充值
//                                case "积分宝":
//                                case "油费":
//                                case "全部":
//                                case "支付宝充值":
//                                    rl_shoukuanfan.setVisibility(View.GONE);
//                                    rl_dingdanhao.setVisibility(View.GONE);
//
//                                    tv_shouming.setText(jsonObject.getString("说明"));
//                                    tv_shijian.setText(jsonObject.getString("时间"));
//                                    tv_beizhu.setText(jsonObject.getString("备注"));
//                                    tv_zhifufangshi.setText(jsonObject.getString("支付方式"));
//                                    break;
//                            }
//
//                        }
//                    }
//                }
//                Log.e("TAG_DETAILS", response.toString());
//                break;
        }
    }
}
