package com.zyd.wlwsdk.server.network;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hygo00 on 2016/7/15.
 * showLoad值分别为0，1，2
 * 0：网络请求开始显示，网络请求成功销毁
 * 1：网络请求开始显示，网络请求成功不销毁
 * 2：网络请求开始不显示，网络请求成功销毁
 * 网络请求接口类
 */
public interface OnDataListener {

    /**
     * 开始请求
     *
     * @param requestTag 请求标志
     * @param showLoad Load显示
     */
    void onStart(int requestTag, int showLoad);

    /**
     * 请求成功
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     * @param showLoad Load显示
     */
    void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException;

    /**
     * 请求成功
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     * @param showLoad Load显示
     */
    void onSuccess(int requestTag, JSONObject response, int showLoad);

    /**
     * 请求失败
     * @param requestTag    请求标志
     * @param errorResponse 错误请求返回
     * @param showLoad Load显示
     */
    void onFailure(int requestTag, JSONObject errorResponse, int showLoad);


    /**
     * 取消请求监听
     * @param requestTag
     * @param showLoad Load显示
     */
    void onCancel(int requestTag, int showLoad);
}
