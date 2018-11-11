package com.gloiot.hygoSupply.ui.activity.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;


public class WebActivity extends BaseActivity {

    WebFragment webFragment;

    @Override
    public int initResource() {
        return R.layout.activity_webview;
    }

    @Override
    public void initData() {
        Bundle bundle = new Bundle();
        bundle.putString("prepay_id", getIntent().getExtras().getString("prepay_id", ""));
        bundle.putString("appid", getIntent().getExtras().getString("appid", ""));
        bundle.putString("url", getIntent().getExtras().getString("url", ""));
        bundle.putBoolean("showTitle", true);
        FragmentTransaction trans1 = getSupportFragmentManager().beginTransaction();
        webFragment = new WebFragment();
        webFragment.setArguments(bundle);
        trans1.replace(R.id.webtest_fl, webFragment);
        trans1.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return webFragment.onKeyDown(keyCode, super.onKeyDown(keyCode, event));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webFragment.ActivityResult(requestCode, resultCode, data);
    }
}
