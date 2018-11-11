package com.gloiot.hygoSupply.ui.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.loopj.android.http.AsyncHttpClient.log;

public class ZhifumimaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_zfmm_shojihao)
    EditText et_zfmm_shojihao;
    @Bind(R.id.et_zfmm_yanzhengma)
    EditText et_zfmm_yanzhengma;
    @Bind(R.id.et_zfmm_mima)
    EditText et_zfmm_mima;
    @Bind(R.id.et_zfmm_queren)
    EditText et_zfmm_queren;
    @Bind(R.id.tv_zfmm_huoqu_yzm)
    TextView tv_zfmm_huoqu_yzm;
    private String zhanghao;
    private String fromFlag;
    private static int i = 60;
    boolean is_finish = false;
    Thread timer = null;

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (!is_finish) {
                if (msg.what == -9) {
                    tv_zfmm_huoqu_yzm.setText(i + "秒后重试");
                    tv_zfmm_huoqu_yzm.setClickable(false);
                } else if (msg.what == -8) {
                    tv_zfmm_huoqu_yzm.setText("获取验证码");
                    tv_zfmm_huoqu_yzm.setClickable(true);
                    i = 60;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_zhifumima;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBarWuFengeXian(this, true, "设置支付密码"
                , "", getResources().getColor(R.color.cl_activity_newmain)
                , getResources().getColor(R.color.cl_white), this);
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        ((ImageView) findViewById(R.id.iv_toptitle_back)).setImageResource(R.mipmap.ic_titilebar_back);//ic_titilebar_back
        et_zfmm_shojihao.setText(preferences.getString(ConstantUtils.SP_USERPHONE, ""));
        et_zfmm_mima.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_zfmm_queren.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_zfmm_mima.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
        et_zfmm_queren.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
        et_zfmm_mima.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        et_zfmm_queren.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
    }

    @Override
    public void onResume() {
        super.onResume();
        is_finish = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_finish = true;
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_zfmm_huoqu_yzm, R.id.btn_zfmm_confirm})
    @Override
    public void onClick(View v) {
        String phoneNum = et_zfmm_shojihao.getText().toString().trim();
        switch (v.getId()) {
            case R.id.iv_toptitle_back:
                finish();
                break;
            case R.id.tv_zfmm_huoqu_yzm://获取验证码
                if (TextUtils.isEmpty(phoneNum)) {
                    MToast.showToast("请输入有效手机号");
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_payment(ZhifumimaActivity.this, phoneNum));
                }
                break;
            case R.id.btn_zfmm_confirm:
                String newPassWord = et_zfmm_mima.getText().toString().trim();
                String confirmPassWord = et_zfmm_queren.getText().toString().trim();
                String YZM = et_zfmm_yanzhengma.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    MToast.showToast("请输入有效手机号");
                    return;
                } else if (phoneNum.length() != 11) {
                    MToast.showToast("请输入有效手机号");
                    return;
                } else if (TextUtils.isEmpty(YZM)) {
                    MToast.showToast("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(newPassWord)) {
                    MToast.showToast("请输入支付密码");
                    return;
                } else if (newPassWord.length() < 6) {
                    MToast.showToast("支付密码不能少于6位");
                    return;
                } else if (TextUtils.isEmpty(confirmPassWord)) {
                    MToast.showToast("请输入确认支付密码");
                    return;
                } else if (confirmPassWord.length() < 6) {
                    MToast.showToast("确认密码不能少于6位");
                    return;
                } else if (!newPassWord.equals(confirmPassWord)) {
                    et_zfmm_queren.getText().clear();
                    MToast.showToast("密码不一致，请重新输入");
                    return;
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_setpay(ZhifumimaActivity.this, zhanghao, newPassWord, confirmPassWord, YZM, phoneNum));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_setpay://设置支付密码
                MToast.showToast("设置支付密码成功");
                editor.putString(ConstantUtils.SP_MYPWD, "是");
                editor.commit();
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_payment: //支付验证码
                String content = response.getString("内容");
                MToastUtils.showToast(content);
                if ("发送短信成功".equals(content)) {
                    startThread();
                }
                break;
        }
    }

    public void startThread() {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    SystemClock.sleep(1000);
                }
                handler.sendEmptyMessage(-8);
            }
        });
        timer.start();
    }

}
