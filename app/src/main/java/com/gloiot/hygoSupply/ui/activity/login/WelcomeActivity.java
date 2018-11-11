package com.gloiot.hygoSupply.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.serrver.network.RequestAction;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.MainActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.JSONUtlis;

import org.json.JSONObject;

import java.util.HashMap;

public class WelcomeActivity extends BaseActivity {
    private String phoneId, phoneName, phoneType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData() {

        requestAction.getPayWebUrl(this, -1, 3);

        // 设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        super.onSuccess(requestTag, response, showLoad);
        if (requestTag == RequestAction.TAG_PAYWEBURL) {
            editor.putString(ConstantUtils.WEB_JS, JSONUtlis.getString(response, "内容"));
            editor.putString(ConstantUtils.WEBPAY_URL, JSONUtlis.getString(response, "webpay_url"));
            editor.putString(ConstantUtils.WEBCASHIER_URL, JSONUtlis.getString(response, "webcashier_url"));
            editor.commit();
        }
        getPhoneInfo();
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        getPhoneInfo();
    }

    //获取手机信息
    private void getPhoneInfo() {
        if (preferences.getString(ConstantUtils.SP_PHENEID, "").isEmpty()) {
            phoneId = "";
            phoneName = "Android " + android.os.Build.MODEL;
            phoneType = "Android";
            editor.putInt(ConstantUtils.SP_PHONEHEIGHT, CommonUtils.getScreenHeight(this));
            editor.putString(ConstantUtils.SP_PHENEID, phoneId);
            editor.putString(ConstantUtils.SP_PHONEINFO_KV, "\n手机ID=" + phoneId + "\n手机名称=" + phoneName + "\n手机型号=" + phoneType);
            editor.putBoolean(ConstantUtils.SP_CHANGEUSERIMG, true);
            editor.putBoolean(ConstantUtils.SP_CHANGEUSERNICHENG, true);
            editor.commit();

            //得到验证码
            ConstantUtils.random = preferences.getString(ConstantUtils.SP_RANDOMCODE, "");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("_版本", "商家");
            hashMap.put("版本号", ConstantUtils.VERSION);
            hashMap.put("手机ID", phoneId);
            hashMap.put("手机名称", phoneName);
            hashMap.put("手机型号", phoneType);
            Log.e("SP_PHONEINFO_JSON", ConstantUtils.SP_PHONEINFO_JSON);
            CommonUtils.saveMap(ConstantUtils.SP_PHONEINFO_JSON, hashMap);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String type = preferences.getString(ConstantUtils.SP_LOGINTYPE, "");
                String randomcode = preferences.getString(ConstantUtils.SP_RANDOMCODE, "");
                ConstantUtils.random = randomcode;
                Log.e("TAG", "run:登录 "+type);
                if ("成功".equals(type)) {
                    editor.putString(ConstantUtils.SP_ZHIYIINGZHUANTAI, "1");
                    editor.commit();
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    WelcomeActivity.this.finish();
                }
            }
        }, 1000);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
