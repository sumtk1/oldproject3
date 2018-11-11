package com.gloiot.hygoSupply.ui.activity.remenzixun;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaoxl on 2017/2/17.
 */

public class ReMenZiXunActivity extends BaseActivity implements View.OnClickListener {
    private WebView rmzx_webview;
    private String zhanghao;

    @Override
    public int initResource() {
        return R.layout.activity_remen_zixun;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar((Activity) mContext, true, "热门资讯", "");
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        String url = getIntent().getStringExtra("url");
        initWebview();
        rmzx_webview.loadUrl(url);
    }

    public void initComponent() {
        rmzx_webview = (WebView) findViewById(R.id.rmzx_webview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fuwushang_phone:
                break;
        }

    }

    public void initWebview() {
        rmzx_webview.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//启用支持javascript
        WebSettings settings = rmzx_webview.getSettings();
        settings.setJavaScriptEnabled(true);


    }
}
