package com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class ShiMingRenZhengActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.et_real_name)
    EditText etRealName;
    @Bind(R.id.et_sfz_num)
    EditText etSfzNum;
    @Bind(R.id.but_realname_tijiao)
    Button butRealnameTijiao;

    @Override
    public int initResource() {
        return R.layout.activity_shimingrenzheng;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "实名认证", "");
        if (!etRealName.getText().toString().equals("") && !etSfzNum.getText().toString().equals("")) {
            butRealnameTijiao.setBackgroundColor(Color.parseColor("#ff7f29"));
            butRealnameTijiao.setOnClickListener(this);
        } else {
            butRealnameTijiao.setEnabled(false);
            butRealnameTijiao.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        //监听输入事件
        etRealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable1) {
                if (editable1.toString().equals("") || editable1.toString().length() < 2) {
                    butRealnameTijiao.setEnabled(false);
                    butRealnameTijiao.setBackgroundColor(Color.parseColor("#cccccc"));
                } else {
                    if (!etSfzNum.getText().toString().equals("")) {
                        butRealnameTijiao.setEnabled(true);
                        butRealnameTijiao.setBackgroundColor(Color.parseColor("#ff7f29"));
                    }
                }

            }
        });
        etSfzNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    butRealnameTijiao.setEnabled(false);
                    butRealnameTijiao.setBackgroundColor(Color.parseColor("#cccccc"));
                } else {
                    if (!etRealName.getText().toString().equals("") && etRealName.getText().toString().length() >= 2) {
                        butRealnameTijiao.setEnabled(true);
                        butRealnameTijiao.setBackgroundColor(Color.parseColor("#ff7f29"));
                    }
                }
            }
        });
    }

    @OnClick({R.id.but_realname_tijiao})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_realname_tijiao:
                String name = etRealName.getText().toString().trim();
                String num = etSfzNum.getText().toString().trim();
                String zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
                String shoujihao = preferences.getString(ConstantUtils.SP_USERPHONE, "");
                final String regx = "[0-9]{17}([0-9]|x|X)";
                if (TextUtils.isEmpty(name) || name.length() < 2) {
                    MToast.showToast("请填写正确的姓名");
                } else if (TextUtils.isEmpty(num) || !num.matches(regx)) {
                    MToast.showToast("请填写正确的身份证号");
                } else {
                    requestHandleArrayList.add(requestAction.real_name(ShiMingRenZhengActivity.this, zhanghao, shoujihao, num, name));

                }
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_real_name:
                if (response.getString("状态").equals("成功")) {
                    editor.putString(ConstantUtils.SP_SHIMINGYANZHENG, response.getString("实名认证")).commit();
                    MToast.showToast(response.getString("实名认证"));
                    finish();
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }
}
