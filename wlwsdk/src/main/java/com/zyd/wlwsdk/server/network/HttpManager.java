package com.zyd.wlwsdk.server.network;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.zyd.wlwsdk.server.network.utlis.EnDecryptUtlis;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by hygo00 on 2016/8/9.
 * 网络请求管理
 */
public class HttpManager {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String URL;

    private static HttpManager httpManager;

    public static void init(String url) {
        URL = url;
    }

    public static HttpManager getInstance() {
        if (httpManager == null) {
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    public static RequestHandle doPost(final int requestTag, RequestParams params, final OnDataListener onDataListener, final int showLoad) {
        return doPost(null, requestTag, params, onDataListener, showLoad, 10);
    }

    /**
     *
     * @param url               服务器地址
     * @param requestTag        请求标识
     * @param params            请求参数
     * @param onDataListener    请求回调
     * @param showLoad          是否显示加载圈
     * @param time              超时时长
     * @return
     */
    public static RequestHandle doPost(String url, final int requestTag, RequestParams params, final OnDataListener onDataListener, final int showLoad, int time) {
        String nowURL = url == null ? URL : url;
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 1000 * time);
        return client.post(nowURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onDataListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onDataListener.onFailure(requestTag, errorResponse, showLoad);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                onDataListener.onCancel(requestTag, showLoad);
            }
        });
    }

    public static RequestHandle webDoPost(final int requestTag, String webUrl, String func, JSONObject data, String randomCode, final NetworkListener networkListener, final int showLoad) {

        RequestParams params = new RequestParams();
        params.add("func", func);
        params.add("words", randomCode + EnDecryptUtlis.aesEncrypt(data.toString(), randomCode));

        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 10000);
        return client.post(webUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                networkListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                networkListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                networkListener.onFailure(requestTag, errorResponse, showLoad);
            }
        });
    }

    public static RequestHandle doGet(String url, final int requestTag, final OnDataListener onDataListener, final int showLoad) {
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 10000);
        return client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onDataListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onDataListener.onFailure(requestTag, errorResponse, showLoad);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                onDataListener.onCancel(requestTag, showLoad);
            }
        });
    }

    /**
     * @param requestTag
     * @param params
     * @param onDataListener
     * @param showLoad            加载框状态， 0表示弹出收起，1表示只弹出，2表示只收起
     * @param pullToRefreshLayout
     * @param requsetType         当数据位0时表示普通请求，为1时表示刷新请求，为2时表示加载请求
     * @return
     */
    public static RequestHandle doPost(final int requestTag, RequestParams params, final OnDataListener onDataListener, final int showLoad,
                                       final PullToRefreshLayout pullToRefreshLayout, final int requsetType) {
        // 重连次数,超时时间
        client.setMaxRetriesAndTimeout(0, 10000);
        return client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                onDataListener.onStart(requestTag, showLoad);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (requsetType == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
//                else if (requsetType == 2){
//                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
                onDataListener.onSuccess(requestTag, response, showLoad);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (requsetType == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                } else if (requsetType == 2) {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                onDataListener.onFailure(requestTag, errorResponse, showLoad);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                onDataListener.onCancel(requestTag, showLoad);
            }
        });
    }


}
