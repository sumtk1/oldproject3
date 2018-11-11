package com.gloiot.hygoSupply.ui.activity.wode.setting.zhanghuyuanquan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AnquanShezhiActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_my_info_xiugaipwd, rl_my_info_bankcard, rl_anquan_shezhi_shimingrenzhen, rl_anquan_shezhi_phone;
    private TextView tv_anquan_shezhi_phone, tv_anquan_shezhi_shiming_renzheng;
    private String phone, phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_anquan_shezhi;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar((Activity) mContext, true, "安全设置", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        phone2 = phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
        tv_anquan_shezhi_phone.setText(phone2);
        rl_my_info_xiugaipwd.setOnClickListener(this);
        requestHandleArrayList.add(requestAction.shop_sj_sz(AnquanShezhiActivity.this, phone));//实名认证判断是否通过了

    }

    public void initComponent() {
        rl_my_info_xiugaipwd = (RelativeLayout) findViewById(R.id.rl_my_info_xiugaipwd);
//        rl_my_info_bankcard = (RelativeLayout) findViewById(R.id.rl_my_info_bankcard);
        tv_anquan_shezhi_phone = (TextView) findViewById(R.id.tv_anquan_shezhi_phone);
        rl_anquan_shezhi_phone = (RelativeLayout) findViewById(R.id.rl_anquan_shezhi_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_info_xiugaipwd: //修改密码
                startActivity(new Intent(mContext, MyChangePwdActivity.class));
                break;
            default:
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_shimingrenzhengpanduan:
                Log.e("实名认证判断请求成功", response.toString());
                switch (response.getString("实名验证")) {
                    case "未认证":
                        tv_anquan_shezhi_shiming_renzheng.setText("未认证");
                        rl_anquan_shezhi_shimingrenzhen.setOnClickListener(this);
                        break;
                    case "已认证":
                        tv_anquan_shezhi_shiming_renzheng.setText("已认证");
                        break;
                    case "审核中":
                        tv_anquan_shezhi_shiming_renzheng.setText("审核中");

                        break;
                    case "认证失败":
                        tv_anquan_shezhi_shiming_renzheng.setText("审核中");
                        rl_anquan_shezhi_shimingrenzhen.setOnClickListener(this);
                        break;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data != null) {
                    Log.e("result", data.getExtras().get("shimingrenzheng").toString());
                    if (data.getExtras().get("shimingrenzheng").equals("success")) {
                        tv_anquan_shezhi_shiming_renzheng.setText("审核中");
                    }
                } else {
                    Log.e("安全设置", "实名认证途中退出返回");
                }
                break;
        }
    }

}
