package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeFahuodiActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_change_fahuodi_fahuoren, et_change_fahuodi_phone, et_change_fahuodi_dizhi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_change_fahuodi;
    }

    @Override
    public void initData() {
        initComponent();
        Intent intent = getIntent();
        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);
        if ("添加".equals(intent.getStringExtra("添加修改类型"))) { //添加发货地址
            CommonUtils.setTitleBar((Activity) mContext, true, "添加发货地址", "保存");
        } else { //修改发货地址
            CommonUtils.setTitleBar((Activity) mContext, true, "修改发货地址", "保存");
            et_change_fahuodi_fahuoren.setText(intent.getStringExtra("发货人"));
            et_change_fahuodi_phone.setText(intent.getStringExtra("手机号"));
            et_change_fahuodi_dizhi.setText(intent.getStringExtra("地址"));
        }
    }

    public void initComponent() {
        et_change_fahuodi_fahuoren = (EditText) findViewById(R.id.et_change_fahuodi_fahuoren);
        et_change_fahuodi_phone = (EditText) findViewById(R.id.et_change_fahuodi_phone);
        et_change_fahuodi_dizhi = (EditText) findViewById(R.id.et_change_fahuodi_dizhi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toptitle_right: //完成
                if (change_fahuodi_yanzheng()) {//信息不为空进行保存

                }
                break;
            default:

        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_fahuobianji:    //添加或编辑发货地址
                Log.e("保存地址请求成功", response.toString());
                MToast.showToast("保存成功");
                finish();
                break;
        }
    }

    private boolean change_fahuodi_yanzheng() {
        if (et_change_fahuodi_fahuoren.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请填写发货人信息");
            return false;
        } else if (et_change_fahuodi_phone.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入手机号");
            return false;
        } else if (et_change_fahuodi_phone.getText().toString().length() != 11) {
            DialogUtlis.oneBtnNormal(mContext, "请输入正确的手机号");
            return false;
        } else if (et_change_fahuodi_dizhi.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入详细地址");
            return false;
        } else {
            return true;
        }

    }

}
