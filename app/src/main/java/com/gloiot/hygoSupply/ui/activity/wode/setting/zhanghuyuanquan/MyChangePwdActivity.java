package com.gloiot.hygoSupply.ui.activity.wode.setting.zhanghuyuanquan;

import android.app.Activity;
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

public class MyChangePwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_chenge_pwd_oldpwd, et_chenge_pwd_newpwd, et_chenge_pwd_renewpwd;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void initData() {
        initComponent();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        CommonUtils.setTitleBar((Activity) mContext, true, "修改登录密码", "保存");
        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);
    }

    public void initComponent() {
        mContext = this;
        et_chenge_pwd_oldpwd = (EditText) findViewById(R.id.et_chenge_pwd_oldpwd);
        et_chenge_pwd_newpwd = (EditText) findViewById(R.id.et_chenge_pwd_newpwd);
        et_chenge_pwd_renewpwd = (EditText) findViewById(R.id.et_change_pwd_renewpwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toptitle_right: //保存
                if (change_pwd_yanzheng()) {
                    requestHandleArrayList.add(requestAction.update_pwd(MyChangePwdActivity.this,
                            phone,
                            CommonUtils.Md5(et_chenge_pwd_oldpwd.getText().toString().replaceAll(" ", "").replaceAll("'", "")),
                            CommonUtils.Md5(et_chenge_pwd_newpwd.getText().toString().replaceAll(" ", "").replaceAll("'", "")),
                            CommonUtils.Md5(et_chenge_pwd_renewpwd.getText().toString()).replaceAll(" ", "").replaceAll("'", "")));
                }
                break;
            default:

        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_update_pwd:
                Log.e("修改密码请求成功", response.toString());
                MToast.showToast("密码修改成功!");
                finish();
                break;
        }
    }


    private boolean change_pwd_yanzheng() {
        if (et_chenge_pwd_oldpwd.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入旧密码");
            return false;
        } else if (et_chenge_pwd_oldpwd.getText().toString().length() < 6) {
            DialogUtlis.oneBtnNormal(mContext, "密码少于6位");
            return false;
        } else if (et_chenge_pwd_newpwd.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入新密码");
            return false;
        } else if (et_chenge_pwd_newpwd.getText().toString().length() < 6) {
            DialogUtlis.oneBtnNormal(mContext, "新密码少于6位");
            return false;
        } else if (et_chenge_pwd_renewpwd.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请再次输入新密码");
            return false;
        } else if (!et_chenge_pwd_newpwd.getText().toString().equals(et_chenge_pwd_renewpwd.getText().toString())) {
            Log.e("et_chenge_pwd_newpwd", et_chenge_pwd_newpwd.getText().toString() + " --> " + et_chenge_pwd_renewpwd.getText().toString());
            DialogUtlis.oneBtnNormal(mContext, "确认密码与新密码不一致");
            return false;
        } else {
            return true;
        }

    }

}
