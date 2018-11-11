package com.zyd.wlwsdk.thirdpay;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * Created by hygo00 on 2017/9/11.
 */

public class WXPay {

    public static WXPay instance;
    public WXPayCallBack wxPayCallBack;
    private IWXAPI api;

    public WXPayCallBack getWxPayCallBack() {
        return wxPayCallBack;
    }

    public void startPay(Context context, JSONObject payData, String appId, WXPayCallBack wxPayCallBack) {
        this.wxPayCallBack = wxPayCallBack;
        api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);

        PayReq req = new PayReq();
        req.appId = appId;
        try {
            Log.e("WXPay",payData.toString());
            JSONObject jsonObject = payData;
            req.partnerId = jsonObject.getString("partnerid");
            req.prepayId = jsonObject.getString("prepayid");
            req.nonceStr = jsonObject.getString("noncestr");
            req.timeStamp = jsonObject.getString("timestamp");
            req.packageValue = jsonObject.getString("package");
            req.sign = jsonObject.getString("sign");
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            wxPayCallBack.payError();
        }
    }

    public static WXPay getInstance() {
        if (instance == null){
            instance = new WXPay();
        }
        return instance;
    }

    public interface WXPayCallBack {
        void paySuccess();

        void payError();
    }
}
