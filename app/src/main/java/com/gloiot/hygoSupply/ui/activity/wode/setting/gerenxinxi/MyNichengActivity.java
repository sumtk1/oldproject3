package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

public class MyNichengActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_gerenxinxi_xiugainicheng;
    private ImageView iv_xiugainicheng_shanchu;
    private Button btn_queren;
    private String nicheng;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_xiu_gai_ni_cheng;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar((Activity) mContext, true, "设置昵称", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        btn_queren.setOnClickListener(this);
        iv_xiugainicheng_shanchu.setOnClickListener(this);
    }

    public void initComponent() {
        et_gerenxinxi_xiugainicheng = (EditText) findViewById(R.id.et_gerenxinxi_xiugainicheng);
        iv_xiugainicheng_shanchu = (ImageView) findViewById(R.id.iv_xiugainicheng_shanchu);
        btn_queren = (Button) findViewById(R.id.btn_queren);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_xiugainicheng_shanchu:
                et_gerenxinxi_xiugainicheng.getText().clear();
                break;
            case R.id.btn_queren:
                nicheng = et_gerenxinxi_xiugainicheng.getText().toString();
                if (nicheng == null || nicheng.equals("")) {
                    MToast.showToast("请输入昵称");
                } else {
                    requestHandleArrayList.add(requestAction.shop_set(MyNichengActivity.this, phone, "", nicheng, ""));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_set: //个人信息请求成功
                Log.e("requestSuccess", "requestSuccess: " + response);
                editor.putString(ConstantUtils.SP_USERNICHENG, response.getString("昵称"));
                editor.putBoolean(ConstantUtils.SP_CHANGEUSERNICHENG, true);
                editor.commit();
                finish();
                break;
        }
    }

}
