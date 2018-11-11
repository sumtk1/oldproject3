/**
 * Summary: js脚本所能执行的函数空间
 * Version 1.0
 * Date: 13-11-20
 * Time: 下午4:40
 * Copyright: Copyright (c) 2013
 */

package com.zyd.wlwsdk.webview;

import android.text.TextUtils;
import android.webkit.WebView;


import com.zyd.wlwsdk.utlis.L;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JS调用原生方法接口
 */
public class JavaScriptInterface {
    public static String mFailingUrl;
    private WebView mWebView;
    private WebCallback webCallback;

    public final static String TAG_CLOSEWEB = "closeweb";           // 关闭网页
    public final static String TAG_UNABLEGOBACK = "unablegoback";   // 返回键退出网页
    public final static String TAG_ADDRESSBOOK = "addressBook";     // 获取通讯录
    public final static String TAG_LOGIN = "login";                 // 调起登录
    public final static String TAG_SHOPINFO = "shopinfo";           // 商品详情
    public final static String TAG_GETPHOTO = "getphoto";           // 获取手机图片
    public final static String TAG_VIDEO = "video";                 // 视频播放器
    public final static String TAG_WEBVIEW = "webview";             // 重新打开WEBVIEW
    public final static String TAG_APPTYPE = "appType";             // APP类型
    public final static String TAG_PAGETYPE = "PageType";           // 页面类型
    public final static String TAG_APPVERSION = "appVersion";       // APP版本
    public final static String TAG_TITLE = "title";                 // 改变webview标题
    public final static String TAG_GETDATA = "getData";             // 原生请求接口获取数据
    public final static String TAG_TOCASHIER = "toCashier";         // 跳转收银台
    public final static String TAG_TOPAY = "toPay";                 // 支付

    public final static String TAG_WXPAY = "wx";                    // 微信支付
    public final static String TAG_ALIPAY = "alipay";               // 支付宝支付
    public final static String TAG_PWDBOX = "pwd_box";              // 支付密码输入框
    public final static String TAG_UNIONPAY = "UPPay";              // 银联支付


    public JavaScriptInterface(WebView webView, WebCallback webCallback) {
        mWebView = webView;
        this.webCallback = webCallback;
    }

    // 网页请求回调
    public interface WebCallback {
        void webCallback(String type, JSONObject jsonObject);
    }

    // 支付
    @android.webkit.JavascriptInterface
    public void pay(String jsonObject) {
        L.e("--", "pay" + jsonObject);
        try {
            JSONObject j = new JSONObject(jsonObject);
            String payType = j.getString("pay_type");
            webCallback.webCallback(payType, j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 刷新
    @android.webkit.JavascriptInterface
    public void refresh() {
        mWebView.loadUrl(TextUtils.isEmpty(mFailingUrl) ? "" : mFailingUrl);
    }


    // func各种方法
    @android.webkit.JavascriptInterface
    public void func(final String jsonObject) {
        try {
            int tag = 0;
            JSONObject j = new JSONObject(jsonObject);
            L.e("------------", j+"");
            webCallback.webCallback(j.getString("type"), j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}