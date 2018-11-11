package com.zyd.wlwsdk.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zyd.wlwsdk.R;
import com.zyd.wlwsdk.webview.safewebview.SafeWebView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hygo00 on 2016/10/7.
 */

public class InnerWebView extends SafeWebView {

    private TextView tvTitle;           // 标题
    private ProgressBar pb;             // 进度条
    private ImageView imgTitleClose;    // 关闭按钮
    private String webJs;               // JS注入内容
    private Context context;
    private String URL;

    JavaScriptInterface.WebCallback webCallback;

    public InnerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, View view, String webJs, JavaScriptInterface.WebCallback webCallback, String url) {
        this.context = context;
        this.webJs = webJs;
        this.webCallback = webCallback;
        this.URL = url;
        imgTitleClose = (ImageView) view.findViewById(R.id.img_title_close);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        pb = (ProgressBar) view.findViewById(R.id.pb);

        setWebSettings();
        addJavascriptInterface(new JavaScriptInterface(this, webCallback), "Android");
        setWebChromeClient(new InnerWebChromeClient());
        setWebViewClient(new InnerWebViewClient());
    }

    private void setWebSettings(){
        WebSettings ws = getSettings();
        ws.setDefaultTextEncodingName("utf-8");
        //启用数据库
        ws.setDatabaseEnabled(true);
        String dir = context.getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        ws.setGeolocationEnabled(true);
        //设置定位的数据库路径
        ws.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        //配置权限（同样在WebChromeClient中实现）
        ws.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        ws.setJavaScriptEnabled(true);  //支持js
        ws.setAllowFileAccess(true);  //设置可以访问文件
        ws.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        ws.setSupportZoom(true);  //支持缩放
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        ws.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        ws.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
//        ws.setBuiltInZoomControls(true); //设置支持缩放
//        ws.supportMultipleWindows();  //多窗口
//        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
//        ws.setAppCacheEnabled(true); // 开启缓存机制
//        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        loadUrl(URL);
    }


    public class InnerWebChromeClient extends SafeWebView.SafeWebChromeClient {

        boolean mIsInjectedJS;

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // 设置标题
            tvTitle.setText(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress); // 务必放在方法体的第一行执行；
            //为什么要在这里注入JS
            //1 OnPageStarted中注入有可能全局注入不成功，导致页面脚本上所有接口任何时候都不可用
            //2 OnPageFinished中注入，虽然最后都会全局注入成功，但是完成时间有可能太晚，当页面在初始化调用接口函数时会等待时间过长
            //3 在进度变化时注入，刚好可以在上面两个问题中得到一个折中处理
            //为什么是进度大于25%才进行注入，因为从测试看来只有进度大于这个数字页面才真正得到框架刷新加载，保证100%注入成功
            if (newProgress <= 25) {
                mIsInjectedJS = false;
            } else if (!mIsInjectedJS) {
                if (TextUtils.isEmpty(webJs)) {
                    try {
                        InputStream is = context.getAssets().open("index.txt");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        String text = new String(buffer, "UTF-8");
                        view.loadUrl("javascript:" + text);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    view.loadUrl("javascript:" + webJs);
                }
                mIsInjectedJS = true;
            }
            pb.setMax(100); // 设置进度条
            pb.setProgress(newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            } else {
                pb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result); // 务必放在方法体的最后一行执行，或者用if判断也行；
        }

    }

    private class InnerWebViewClient extends WebViewClient {

        //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {   // 跳转拨打电话界面
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                context.startActivity(intent);
                return true;
            }

            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 判断当前页面是否首次加载进来页面（显示隐藏关闭按钮）
            if (url.contains(URL)) {
                imgTitleClose.setVisibility(View.GONE);
            } else {
                imgTitleClose.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // 页面加载失败跳转页
            JavaScriptInterface.mFailingUrl = failingUrl;
            view.loadUrl("file:///android_asset/warning.html");
        }
    }

}
