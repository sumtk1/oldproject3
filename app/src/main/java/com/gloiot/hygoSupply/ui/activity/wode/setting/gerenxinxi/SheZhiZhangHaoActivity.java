package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moxx on 2017/2/15.
 */

public class SheZhiZhangHaoActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_zhanghao;
    private Button btn_queren;
    private String zhanghao;
    private String phone;

    @Override
    public int initResource() {
        return R.layout.activity_shezhi_zhanghao;
    }

    @Override
    public void initData() {
        initComponent();
        phone = preferences.getString(ConstantUtils.SP_MYID , "");
    }

    public void initComponent() {
        CommonUtils.setTitleBar(this, true, "设置账号", "");
        et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
        btn_queren = (Button) findViewById(R.id.btn_queren);
        btn_queren.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_queren:
                zhanghao = et_zhanghao.getText().toString();
                if (zhanghao.length() == 0) {
                    MToast.showToast("账号不能为空");
                } else
//                    requestHandleArrayList.add(requestAction.shop_set_zhanghao(this,phone,zhanghao));
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        Log.e("response", "requestSuccess: " + response);
        switch (requestTag) {
//            case ConstantUtils.TAG_SHOP_S_ACCOUNT:
//                if ("成功".equals(response.getString("状态"))) {
//                    editor.putString(ConstantUtils.SP_MYID, zhanghao);
//                    editor.apply();
//                    MToastUtils.showToast(this, "账号设置成功");
//                    finish();
//                }
//                break;
        }
    }

}
