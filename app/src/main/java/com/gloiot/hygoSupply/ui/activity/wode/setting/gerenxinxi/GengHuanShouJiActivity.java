package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;


/**
 * Created by RudyJun on 2017/2/10.
 */

public class GengHuanShouJiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_shouji)
    TextView tv_shouji;
    @Bind(R.id.btn_genghuan)
    Button btn_genghuan;

    private Dialog dialog;
    private String phone;
    private String shoujihao;
    private EditText et_mima;
    private Button btn_queren,btn_quxiao;

    @Override
    public int initResource() {
        return R.layout.activity_genghuan_shouji;
    }

    @Override
    public void initData() {
        initComponent();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        shoujihao = getIntent().getStringExtra("手机号");
        tv_shouji.setText(shoujihao);
    }

    public void initComponent() {
        CommonUtils.setTitleBar(this, true, "更换手机号码", "");
        tv_shouji.setText(preferences.getString(ConstantUtils.SP_PHENEID, ""));
        dialog = new Dialog(this, R.style.dialog_untran);
        dialog.setContentView(R.layout.dialog_yanzheng_mima);

        btn_queren = (Button) dialog.findViewById(R.id.btn_queren);
        btn_quxiao = (Button) dialog.findViewById(R.id.btn_quxiao);
        et_mima = (EditText) dialog.findViewById(R.id.et_mima);

        btn_queren.setOnClickListener(this);
        btn_quxiao.setOnClickListener(this);
        btn_genghuan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_queren:
                String mima = et_mima.getText().toString();
                if (mima.length() == 0) {
                    MToast.showToast("请输入密码");
                    return;
                }
                requestHandleArrayList.add(requestAction.shop_sp_wd(this, phone, CommonUtils.Md5(mima)));
                break;
            case R.id.btn_genghuan:
                dialog.show();
                break;
            case R.id.btn_quxiao:
                et_mima.setText("");
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sp_wd:
                if ("成功".equals(response.getString("状态"))) {
                    dialog.dismiss();
                    startActivity(new Intent(GengHuanShouJiActivity.this, ShuRuShouJiActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
