package com.gloiot.hygoSupply.ui.activity.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.widget.TimeButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;


public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_forget_password_newpassword)
    EditText et_forget_password_newpassword;
    @Bind(R.id.et_forget_password_renewpassword)
    EditText et_forget_password_renewpassword;
    @Bind(R.id.et_forget_password_phone)
    EditText et_forget_password_phone;
    @Bind(R.id.et_forget_password_yanzhengma)
    EditText et_forget_password_yanzhengma;
    @Bind(R.id.tv_forget_password_getyanzhengma)
    TimeButton tv_forget_password_getyanzhengma;
    public static int currentTime = 0;
    private TextView tv_forget_password_getyanzhengma1;
    public static Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_forget_password_getyanzhengma.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        tv_forget_password_getyanzhengma.onDestroy();
        super.onDestroy();
    }

    @Override
    public int initResource() {
        return R.layout.activity_forget_password;
    }


    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "忘记密码", "");
        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);
        tv_forget_password_getyanzhengma1 = (TextView) findViewById(R.id.tv_forget_password_getyanzhengma1);
        tv_forget_password_getyanzhengma1.setOnClickListener(this);
        if (currentTime > 0) {
//            tv_forget_password_getyanzhengma.setText(currentTime + "");
            if(!thread.isInterrupted()){
                thread.interrupt();
                startCountDown();
            }
        }
    }

    @OnClick({R.id.tv_forget_password_getyanzhengma, R.id.btn_forget_password_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_password_getyanzhengma: //获取验证码
                if (havephone_yanzheng()) {
                    if ("获取验证码".equals(tv_forget_password_getyanzhengma.getText())) {
                        requestHandleArrayList.add(requestAction.sms(ForgetPasswordActivity.this, et_forget_password_phone.getText().toString()));
                    }
                }
                break;
            case R.id.tv_forget_password_getyanzhengma1: //获取验证码
                if (havephone_yanzheng()) {
                    Log.e("TAG", "onClick: 点击了");
                    if ("获取验证码".equals(tv_forget_password_getyanzhengma1.getText())) {
                        requestHandleArrayList.add(requestAction.sms(ForgetPasswordActivity.this, et_forget_password_phone.getText().toString()));
                    }
                }
                break;
            case R.id.btn_forget_password_confirm: //修改密码按钮
                if (forgetPassword_yanzheng()) {
                    requestHandleArrayList.add(requestAction.forgetPassword(
                            ForgetPasswordActivity.this,
                            et_forget_password_phone.getText().toString(),
                            et_forget_password_yanzhengma.getText().toString(),
                            CommonUtils.Md5(et_forget_password_newpassword.getText().toString().replaceAll(" ", "").replaceAll("'", ""))));
                }
                break;
            default:

        }
    }

    private boolean havephone_yanzheng() {
        if (et_forget_password_phone.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入手机号", "知道了");
            return false;
        } else if (et_forget_password_phone.getText().length() != 11) {
            DialogUtlis.oneBtnNormal(mContext, "请输入正确的手机号", "知道了");
            return false;
        } else {
            return true;
        }
    }

    //忘记密码验证
    private boolean forgetPassword_yanzheng() {
        if (et_forget_password_phone.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入手机号", "知道了");
            return false;
        } else if (et_forget_password_phone.getText().length() != 11) {
            DialogUtlis.oneBtnNormal(mContext, "请输入正确手机号", "知道了");
            return false;
        } else if (et_forget_password_yanzhengma.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请填写验证码", "知道了");
            return false;
        } else if (et_forget_password_newpassword.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入新密码", "知道了");
            return false;
        } else if (et_forget_password_newpassword.getText().length() < 6) {
            DialogUtlis.oneBtnNormal(mContext, "新密码不少于6位", "知道了");
            return false;
        } else if (et_forget_password_renewpassword.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入确认密码", "知道了");
            return false;
        } else if (!et_forget_password_newpassword.getText().toString().equals(et_forget_password_renewpassword.getText().toString())) {
            DialogUtlis.oneBtnNormal(mContext, "请输入相同的密码", "知道了");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccess(int requestTag, JSONObject jsonObject, int showLoad) {
        super.onSuccess(requestTag, jsonObject, showLoad);

        switch (requestTag) {
            case ConstantUtils.TAG_sms:     //验证码请求成功
                //"状态":"短信发送成功","内容":"稍后将采用语音的方式发送验证码，请注意接听来电！"
                try {
                    if (jsonObject.getString("状态").equals("成功")) {
                        DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("内容"), "知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialogNoAnimator();
                                startCountDown();
                            }
                        });
                    } else {
                        DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("状态"), "知道了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ConstantUtils.TAG_test_password:  //修改密码请求成功
                try {
                    if (jsonObject.getString("状态").equals("成功")) {
                        DialogUtlis.oneBtnNormal(mContext, "修改密码成功", "知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogUtlis.dismissDialogNoAnimator();
                                finish();
                            }
                        });
                    } else {
                        DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("状态"), "知道了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean isDestory = false;


    public void startCountDown() {
        Log.e("TAG", "startCountDown: " + "开始");
      thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (!thread.isInterrupted()) {
                    for (int i = currentTime == 0 ? 120 : currentTime; i > 0; i--) {
                        Log.e("TAG", "startCountDown: " + "开始" + i);
                        final int num = i;
                        currentTime = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isDestory) {
                                    Log.e("TAG", "startCountDown: " + "开始更新界面" + num);
                                    tv_forget_password_getyanzhengma1.setText(num + "");
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            currentTime = 0;

                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDestory) {
                                tv_forget_password_getyanzhengma1.setText("获取验证码");
                            }
                            currentTime = 0;
                        }
                    });
                    thread.interrupt();
                }
            }
        });
        thread.start();
    }
}
