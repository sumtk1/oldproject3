package com.zyd.wlwsdk.server.network;

import org.json.JSONObject;

/**
 * Created by hygo00 on 2017/9/11.
 */

public interface NetworkListener {

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
    void onSuccess(int requestTag, JSONObject response, int showLoad);

    /**
     * 请求失败
     * @param requestTag    请求标志
     * @param errorResponse 错误请求返回
     * @param showLoad Load显示
     */
    void onFailure(int requestTag, JSONObject errorResponse, int showLoad);
}
