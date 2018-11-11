package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 信誉保证金介绍
 *
 * @author ljz
 */
public class BaoZhengJin01Activity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.view_status_bar)
    View viewStatusBar;             // 透明View
    @Bind(R.id.tv_bzj01_money)
    TextView tvBzj01Money;          // 需缴纳金额
    @Bind(R.id.tv_bzj01_tsy)
    TextView tvBzj01Tsy;            // 提示语
    @Bind(R.id.tv_bzj01_title1)
    TextView tvBzj01Title1;         // 特权信息标题1
    @Bind(R.id.tv_bzj01_content1)
    TextView tvBzj01Content1;       // 特权信息1
    @Bind(R.id.tv_bzj01_title2)
    TextView tvBzj01Title2;         // 特权信息标题2
    @Bind(R.id.tv_bzj01_content2)
    TextView tvBzj01Content2;       // 特权信息2
    @Bind(R.id.tv_bzj01_title3)
    TextView tvBzj01Title3;         // 特权信息标题3
    @Bind(R.id.tv_bzj01_content3)
    TextView tvBzj01Content3;       // 特权信息3
    @Bind(R.id.rl_bzj01_back)
    RelativeLayout rlBzj01Back;     // 返回按钮
    @Bind(R.id.rl_bzj_money)
    RelativeLayout rlBzjMoney;      // 待缴纳
    @Bind(R.id.tv_bzj01_djn)
    TextView tvBzj01Djn;            // 待缴纳/需缴纳
    @Bind(R.id.tv_bzj_gxn)
    TextView tvBzjGxn;              // 文字 恭喜您
    @Bind(R.id.tv_bzj01_jilu)
    TextView tvBzj01Jilu;           // 缴纳记录
    @Bind(R.id.btn_bzj01)
    TextView btnBzj01;              // 缴纳保证金按钮

    @Override
    public int initResource() {
        return R.layout.activity_wode_shouyi_baozhengjin01;
    }

    @Override
    public void initData() {
        StatusBarUtil.transparencyBar(this);
        if (Build.VERSION.SDK_INT >= 21) {
            viewStatusBar.setVisibility(View.VISIBLE);
        }
        requestHandleArrayList.add(requestAction.shop_bzj_jieshao(this, preferences.getString(ConstantUtils.SP_MYID, "")));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        requestHandleArrayList.add(requestAction.shop_bzj_jieshao(this, preferences.getString(ConstantUtils.SP_MYID, "")));
    }

    @OnClick({R.id.rl_bzj01_back, R.id.btn_bzj01, R.id.tv_bzj01_jilu})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bzj01_back:
                finish();
                break;
            case R.id.btn_bzj01:
                // 去缴纳
                startActivity(new Intent(mContext, BaoZhengJin02Activity.class));
                break;
            case R.id.tv_bzj01_jilu:
                // 缴纳记录
                startActivity(new Intent(BaoZhengJin01Activity.this, JiaoNaJiLuActivity.class));
                break;
            default:
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_bzj_jieshao:
                JSONArray jsonArray = response.getJSONArray("列表");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    switch (i) {
                        case 0:
                            tvBzj01Title1.setText(JSONUtlis.getString(jsonObject, "标题", ""));
                            tvBzj01Content1.setText(JSONUtlis.getString(jsonObject, "内容", ""));
                            break;
                        case 1:
                            tvBzj01Title2.setText(JSONUtlis.getString(jsonObject, "标题", ""));
                            tvBzj01Content2.setText(JSONUtlis.getString(jsonObject, "内容", ""));
                            break;
                        case 2:
                            tvBzj01Title3.setText(JSONUtlis.getString(jsonObject, "标题", ""));
                            tvBzj01Content3.setText(JSONUtlis.getString(jsonObject, "内容", ""));
                            break;
                        default:
                    }
                }
                tvBzj01Money.setText(JSONUtlis.getString(response, "待缴保证金", "0"));
                tvBzj01Tsy.setText(JSONUtlis.getString(response, "内容", "0"));
                String jnState = JSONUtlis.getString(response, "缴纳状态");
                if ("已缴满".equals(jnState)) {
                    rlBzjMoney.setVisibility(View.GONE);
                    tvBzjGxn.setVisibility(View.VISIBLE);
                    tvBzj01Jilu.setVisibility(View.VISIBLE);
                    btnBzj01.setVisibility(View.GONE);
                    tvBzj01Tsy.setText("已缴纳" + JSONUtlis.getString(response, "保证金", "0") + "元的信誉保证金");
                } else if ("缴纳中".equals(jnState)) {
                    rlBzjMoney.setVisibility(View.VISIBLE);
                    tvBzjGxn.setVisibility(View.GONE);
                    tvBzj01Jilu.setVisibility(View.GONE);
                    btnBzj01.setVisibility(View.VISIBLE);
                    tvBzj01Djn.setText("待缴纳（元）");
                } else {
                    rlBzjMoney.setVisibility(View.VISIBLE);
                    tvBzjGxn.setVisibility(View.GONE);
                    tvBzj01Jilu.setVisibility(View.GONE);
                    btnBzj01.setVisibility(View.VISIBLE);
                    tvBzj01Djn.setText("需缴纳（元）");
                }
                break;
            default:
        }
    }

}
