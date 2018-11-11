package com.gloiot.hygoSupply.ui.activity.wode.setting.zhanghuyuanquan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class AnquanShezhiActivity1 extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_anquan_shezhi_phone)
    TextView tv_anquan_shezhi_phone;
    @Bind(R.id.rl_my_info_xiugaipwd)
    RelativeLayout rl_my_info_xiugaipwd;
    private String phone, phone2;


    @Override
    public int initResource() {
        return R.layout.activity_anquan_shezhi;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "安全设置", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        phone2 = phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);
        tv_anquan_shezhi_phone.setText(phone2);
        rl_my_info_xiugaipwd.setOnClickListener(this);
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
    }

}
