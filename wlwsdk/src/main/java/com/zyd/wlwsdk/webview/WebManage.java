package com.zyd.wlwsdk.webview;

import android.webkit.WebView;


import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.L;

import org.json.JSONObject;

/**
 * Created by hygo00 on 2017/9/8.
 * web方法管理类
 */

public class WebManage implements JavaScriptInterface.WebCallback {

    public static final String TAG_WEBMANAGE_SUCCEESS = "success"; // 登录界面是否登录返回
    public static final String TAG_WEBMANAGE_FAIL = "fail"; // 登录界面是否登录返回

    private WebView webView;
    private String guid;

    public void setWebView( WebView webView){
        this.webView = webView;
    }


    public void callBack(String content) {
        L.e("WebManage_guid", guid);
        if (TAG_WEBMANAGE_SUCCEESS.equals(content)) {
            webView.loadUrl("javascript:p_golbal['" + guid + "']('{\"状态\":\"成功\"}')");
        } else if (TAG_WEBMANAGE_FAIL.equals(content)) {
            webView.loadUrl("javascript:p_golbal['" + guid + "']('{\"状态\":\"失败\"}')");
        } else {
            webView.loadUrl("javascript:p_golbal['" + guid + "']('" + content + "')");
        }
        guid = "";
    }

    @Override
    public void webCallback(String type, JSONObject jsonObject) {
        guid = JSONUtlis.getString(jsonObject, "guid"); // 网页调用原生方法生成的唯一识别id
        switch (type) {
            // 微信支付
            case JavaScriptInterface.TAG_WXPAY:
                webManageCall.web_wxPay(jsonObject);
                break;
            // 支付宝支付
            case JavaScriptInterface.TAG_ALIPAY:
                webManageCall.web_aliPay(jsonObject);
                break;
            // 输入密码框
            case JavaScriptInterface.TAG_PWDBOX:
                webManageCall.web_pwdBox(jsonObject);
                break;
            // 关闭网页浏览器
            case JavaScriptInterface.TAG_CLOSEWEB:
                webManageCall.web_closeWeb(jsonObject);
                break;
            // 返回键关闭网页浏览器
            case JavaScriptInterface.TAG_UNABLEGOBACK:
                webManageCall.web_unableGoBack(jsonObject);
                break;
            // 获取通讯录联系人
            case JavaScriptInterface.TAG_ADDRESSBOOK:
                webManageCall.web_addressbook(jsonObject);
                break;
             // 跳转登录页
            case JavaScriptInterface.TAG_LOGIN:
                webManageCall.web_login(jsonObject);
                break;
            // 跳转商品详情
            case JavaScriptInterface.TAG_SHOPINFO:
                webManageCall.web_shopInfo(jsonObject);
                break;
            // 获取手机图片
            case JavaScriptInterface.TAG_GETPHOTO:
                webManageCall.web_getPhoto(jsonObject);
                break;
            // 播放视频
            case JavaScriptInterface.TAG_VIDEO:
                webManageCall.web_video(jsonObject);
                break;
            // 跳转新浏览器
            case JavaScriptInterface.TAG_WEBVIEW:
                webManageCall.web_webview(jsonObject);
                break;
            // 获取app类型
            case JavaScriptInterface.TAG_APPTYPE:
                webManageCall.web_appType(jsonObject);
                break;
            // 页面类型（pay有倒计时控件）
            case JavaScriptInterface.TAG_PAGETYPE:
                webManageCall.web_pageType(jsonObject);
                break;
            // app版本号
            case JavaScriptInterface.TAG_APPVERSION:
                webManageCall.web_appVersion(jsonObject);
                break;
            // 标题
            case JavaScriptInterface.TAG_TITLE:
                webManageCall.web_webTitle(jsonObject);
                break;
            // 银联支付
            case JavaScriptInterface.TAG_UNIONPAY:
                webManageCall.web_unionPay(jsonObject);
                break;
            // js掉原生请求接口
            case JavaScriptInterface.TAG_GETDATA:
                break;
            // 跳转收银台
            case JavaScriptInterface.TAG_TOCASHIER:
                webManageCall.web_toCashier(jsonObject);
                break;
            // 支付
            case JavaScriptInterface.TAG_TOPAY:
                webManageCall.web_toPay(jsonObject);
                break;
        }
    }


    WebManageCall webManageCall;

    public void setWebManageCall(WebManageCall webManageCall) {
        this.webManageCall = webManageCall;
    }

    public interface WebManageCall {
        void web_wxPay(JSONObject jsonObject);

        void web_aliPay(JSONObject jsonObject);

        void web_pwdBox(JSONObject jsonObject);

        void web_closeWeb(JSONObject jsonObject);

        void web_unableGoBack(JSONObject jsonObject);

        void web_addressbook(JSONObject jsonObject);

        void web_login(JSONObject jsonObject);

        void web_video(JSONObject jsonObject);

        void web_webview(JSONObject jsonObject);

        void web_shopInfo(JSONObject jsonObject);

        void web_getPhoto(JSONObject jsonObject);

        void web_appType(JSONObject jsonObject);

        void web_appVersion(JSONObject jsonObject);

        void web_webTitle(JSONObject jsonObject);

        void web_unionPay(JSONObject jsonObject);

        void web_pageType(JSONObject jsonObject);

        void web_toCashier(JSONObject jsonObject);

        void web_toPay(JSONObject jsonObject);
    }
}
