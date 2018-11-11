package com.gloiot.hygoSupply.ui.activity.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.App;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.MainActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.et_login_phone)
    EditText et_login_phone;
    @Bind(R.id.et_login_passwrod)
    EditText et_login_passwrod;
    @Bind(R.id.tv_login_forgetpassword)
    TextView tv_login_forgetpassword;
    @Bind(R.id.btn_login_login)
    Button btn_login_login;
    private Boolean isExit = false;
    private boolean isChongZhi = false;
    private String myPhoneInfo, loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        editor.putBoolean(ConstantUtils.SP_CHANGEUSERIMG, true);
        editor.putBoolean(ConstantUtils.SP_CHANGEUSERNICHENG, true);
        editor.commit();
    }

    @Override
    public void initData() {
        myPhoneInfo = preferences.getString(ConstantUtils.SP_PHONEINFO_KV, "");
        CommonUtils.setTitleBar((Activity) mContext, false, "登录", "");
        tv_login_forgetpassword.setOnClickListener(this);
        btn_login_login.setOnClickListener(this);
        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);
        loginType = preferences.getString(ConstantUtils.SP_LOGINTYPE, "退出");
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_ZHANGHAO, ""))) {
            et_login_phone.setText(preferences.getString(ConstantUtils.SP_ZHANGHAO, ""));
        }
    }


    @OnClick({R.id.tv_login_forgetpassword, R.id.btn_login_login})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_forgetpassword: //忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_login_login: //登录
                if (login_yanzheng()) {
                    requestHandleArrayList.add(requestAction.userLogin(LoginActivity.this,
                            et_login_phone.getText().toString(),
                            CommonUtils.Md5(et_login_passwrod.getText().toString())));
                }
                break;
            default:

        }
    }


    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ("成功".equals(loginType)) {
            finish();
        } else {
            if (!isExit) {
                isExit = true;
                MToast.showToast("再按一次退出程序");
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                App.getInstance().mActivityStack.AppExit();
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    //页面数据验证
    private boolean login_yanzheng() {
        if (et_login_phone.getText().toString().equals("") && et_login_passwrod.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入账号和密码");
            return false;
        } else if (et_login_passwrod.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "密码不能为空,请输入密码");
            return false;
        } else if (et_login_passwrod.getText().toString().length() < 6) {
            DialogUtlis.oneBtnNormal(mContext, "密码不能小于6位,请重新输入密码");
            return false;
        } else if (!CommonUtils.checkNoChinese(et_login_passwrod.getText().toString())) {
            DialogUtlis.oneBtnNormal(mContext, "密码不能输入中文,请重新输入密码");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_login:
                //存账号
                editor.putString(ConstantUtils.SP_ZHANGHAO, et_login_phone.getText().toString());
                editor.putString(ConstantUtils.SP_MYID, jsonObject.getString("账号"));
                editor.putString(ConstantUtils.SP_USERNAME, jsonObject.getString("姓名"));
                editor.putString(ConstantUtils.SP_RANDOMCODE, jsonObject.getString("随机码"));
                editor.putString(ConstantUtils.SP_USERPWD, et_login_passwrod.getText().toString());
                editor.putString(ConstantUtils.SP_USERSCTYPE, jsonObject.getString("商家类别"));
                editor.putString(ConstantUtils.SP_USERIMG, jsonObject.getString("头像"));
                editor.putString(ConstantUtils.SP_USERNICHENG, jsonObject.getString("昵称"));
                editor.putString(ConstantUtils.SP_USERSEX, jsonObject.getString("性别"));
                editor.putString(ConstantUtils.SP_YOUWUDIANPU, jsonObject.getString("店铺状态"));
                editor.putString(ConstantUtils.SP_DIANPUID, jsonObject.getString("店铺id"));
                editor.putString(ConstantUtils.SP_USERPHONE, jsonObject.getString("手机号"));
                editor.putString(ConstantUtils.SP_ZIYING, jsonObject.getString("自营"));
                editor.putString(ConstantUtils.SP_MYPWD, jsonObject.getString("支付密码"));
                editor.putString(ConstantUtils.SP_ONLYID, jsonObject.getString("唯一id"));
                editor.putString(ConstantUtils.SP_SUPERMERCHANT, jsonObject.getString("超级商家"));
                editor.putString(ConstantUtils.SP_DIANPU_ID, jsonObject.getString("店铺id"));
                editor.putString(ConstantUtils.SP_SHIFOUBANGDINGWX, jsonObject.getString("绑定微信"));
                editor.putString(ConstantUtils.SP_SENDMESSAGE, jsonObject.getString("发送消息"));
                editor.putString(ConstantUtils.SP_LOGINTYPE, "成功");
                editor.commit();
                ConstantUtils.random = jsonObject.getString("随机码");
                ConstantUtils.dianpuId = jsonObject.getString("店铺id");
                try {
                    imDB(jsonObject.getString("账号"));
                    imSocket();
                } catch (Exception e) {

                }

                //SQLite
                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
                sqLiteDatabase.execSQL("CREATE TABLE if not exists qiehuan_zhanghao (zhanghao VARCHAR PRIMARY KEY, mima VARCHAR, imgUrl VARCHAR)");
                ContentValues contentValues = new ContentValues();
                contentValues.put("zhanghao", et_login_phone.getText().toString());
                contentValues.put("mima", et_login_passwrod.getText().toString());
                contentValues.put("imgUrl", JSONUtlis.getString(jsonObject, "头像"));
                sqLiteDatabase.replace("qiehuan_zhanghao", null, contentValues);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                this.finish();
                break;
        }
    }

    private void imDB(String account) {
        IMDBManager.getInstance(mContext, account);
//        IMDBManager.getInstance(mContext).getHygoHelperInstance(account, "HygoSupplyIm", 1);
    }

    // socket认证
    private void imSocket() {
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_USERPHONE, ""))) {
            SocketListener.getInstance().connectionRenZheng(preferences.getString(ConstantUtils.SP_USERPHONE, "") + "_商家", preferences.getString(ConstantUtils.SP_RANDOMCODE, ""));
        }
    }

}
