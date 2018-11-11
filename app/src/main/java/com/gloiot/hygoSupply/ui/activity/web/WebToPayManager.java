package com.gloiot.hygoSupply.ui.activity.web;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.server.network.NetworkListener;
import com.zyd.wlwsdk.thirdpay.AliPay;
import com.zyd.wlwsdk.thirdpay.WXPay;
import com.zyd.wlwsdk.unionpay.UnionPay;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MD5Utlis;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.webview.WebManage;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.zyd.wlwsdk.webview.WebManage.TAG_WEBMANAGE_FAIL;
import static com.zyd.wlwsdk.webview.WebManage.TAG_WEBMANAGE_SUCCEESS;

/**
 * Created by hygo00 on 2017/9/25.
 * 网页下单支付管理
 */

public class WebToPayManager implements NetworkListener {

    private Context mContext;
    private WebManage webManage;
    private JSONObject jsonObject;
    private NetworkListener networkListener;
    private WebFragment fragment;

    private final int TAG_1_JIFEN = 1;      // 1层积分支付
    private final int TAG_1_WX = 2;         // 1层微信支付
    private final int TAG_1_ALIPAY = 3;     // 1层支付宝支付
    private final int TAG_1_BANK = 4;       // 1层银联支付
    private final int TAG_2_WX = 5;         // 2层微信支付
    private final int TAG_2_ALIPAY = 6;     // 2层支付宝支付
    private final int TAG_2_BANK = 7;       // 2层银联支付


    WebToPayManager(Context context, WebFragment fragment, WebManage webManage, JSONObject jsonObject) {
        this.mContext = context;
        this.fragment = fragment;
        this.webManage = webManage;
        this.jsonObject = jsonObject;
        networkListener = this;
    }

    /**
     * 跳转收银台
     *
     * @param preferences
     * @param mContext
     * @param jsonObject
     * @return
     */
    public static Intent toCashier(SharedPreferences preferences, Context mContext, JSONObject jsonObject) {
        // 拼接收银台地址
        String url = preferences.getString(ConstantUtils.WEBCASHIER_URL, "") +
                "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") +
                "&prepay_id=" + JSONUtlis.getString(jsonObject, "prepay_id") +
                "&appid=" + JSONUtlis.getString(jsonObject, "appid") +
                "&sign=" + JSONUtlis.getString(jsonObject, "sign");

        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("prepay_id", JSONUtlis.getString(jsonObject, "prepay_id"));
        intent.putExtra("appid", JSONUtlis.getString(jsonObject, "appid"));
        return intent;
    }

    /**
     * 支付请求接口
     *
     * @param toPayUrl   支付url地址
     * @param onlyID     唯一id
     * @param randomCode 随机码
     * @param prepay_id  预支付id
     * @param appid      appid
     */
    public void toPay(String toPayUrl, String onlyID, String randomCode, String prepay_id, String appid) {
        JSONObject json = null;
        try {
            json = jsonObject.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        json = JSONUtlis.putString(json, "onlyID", onlyID);
        json = JSONUtlis.putString(json, "prepay_id", prepay_id);
        json = JSONUtlis.putString(json, "appid", appid);
        Log.e("web", json + "");

        String payType1 = JSONUtlis.getString(json, "pay_type1");
        String payType2 = JSONUtlis.getString(json, "pay_type2");
        String money = JSONUtlis.getString(json, "money");

        if (!TextUtils.isEmpty(payType1) && TextUtils.isEmpty(payType2)) {
            // 单层支付(红利)
            showPwdBox(TAG_1_JIFEN, toPayUrl, json, money, randomCode);
        } else if (TextUtils.isEmpty(payType1) && !TextUtils.isEmpty(payType2)) {
            // 单层支付(微信、支付宝、银联)
            switch (payType2) {
                case "微信支付":
                    HttpManager.webDoPost(TAG_1_WX, toPayUrl, "p_cashier_pay", json, randomCode, networkListener, -1);
                    break;
                case "支付宝":
                    HttpManager.webDoPost(TAG_1_ALIPAY, toPayUrl, "p_cashier_pay", json, randomCode, networkListener, -1);
                    break;
                case "银联支付":
                    HttpManager.webDoPost(TAG_1_BANK, toPayUrl, "p_cashier_pay", json, randomCode, networkListener, -1);
                    break;
            }
        } else if (!TextUtils.isEmpty(payType1) && !TextUtils.isEmpty(payType2)) {
            // 双层支付
            switch (payType2) {
                case "微信支付":
                    showPwdBox(TAG_2_WX, toPayUrl, json, money, randomCode);
                    break;
                case "支付宝":
                    showPwdBox(TAG_2_ALIPAY, toPayUrl, json, money, randomCode);
                    break;
                case "银联支付":
                    showPwdBox(TAG_2_BANK, toPayUrl, json, money, randomCode);
                    break;
            }
        }
    }

    /**
     * 一层支付弹出输入支付密码框
     *
     * @param tag        支付标志
     * @param toPayUrl   支付url地址
     * @param jsonObject
     * @param money      显示的金额
     * @param randomCode 随机码
     */
    private void showPwdBox(final int tag, final String toPayUrl, final JSONObject jsonObject, String money, final String randomCode) {
        DialogUtlis.oneBtnPwd(mContext, money, new DialogUtlis.PasswordCallback() {
            @Override
            public void callback(String data) {
                JSONObject json = jsonObject;
                json = JSONUtlis.putString(json, "password", MD5Utlis.Md5(data));
                HttpManager.webDoPost(tag, toPayUrl, "p_cashier_pay", json, randomCode, networkListener, -1);
            }
        });
    }

    /**
     * 二层支付（微信、支付宝、银联）
     *
     * @param paytype  二层支付类型
     * @param response
     */
    private void pay2(String paytype, JSONObject response) {
        switch (paytype) {
            case "wx":
                WXPay wxPay = WXPay.getInstance();
                wxPay.startPay(mContext, response, ConstantUtils.WXAPPID, new WXPay.WXPayCallBack() {
                    @Override
                    public void paySuccess() {
                        fragment.stopTimer();
                        webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
                    }

                    @Override
                    public void payError() {
                        webManage.callBack(TAG_WEBMANAGE_FAIL);
                    }
                });
                break;
            case "alipay":
                new AliPay(mContext, JSONUtlis.getString(response, "return_info")) {
                    @Override
                    public void paySuccess() {
                        fragment.stopTimer();
                        webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
                    }

                    @Override
                    public void payError(boolean flag) {
                        MToast.showToast(flag ? "支付取消" : "支付失败");
                    }
                };
                break;
            case "bank":
                UnionPay unionPay = UnionPay.getInstance();
                unionPay.startPay(mContext, JSONUtlis.getString(response, "tn_code", "123"), ConstantUtils.UnionPayMode, new UnionPay.UnionPayCallBack() {
                    @Override
                    public void paySuccess() {
                        fragment.stopTimer();
                        webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
                    }

                    @Override
                    public void payError(String state) {
                        webManage.callBack("{\"状态\":\"" + state + "\"}");
                    }
                });
                break;
        }
    }

    @Override
    public void onStart(int requestTag, int showLoad) {
        Log.e("web-onStart:", requestTag + "");
        LoadDialog.show(mContext);
    }

    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        Log.e("web-onSuccess:" + requestTag, response + "");
        LoadDialog.dismiss(mContext);
        if (TAG_1_JIFEN == requestTag) {
            if ("一层成功".equals(JSONUtlis.getString(response, "状态"))) {
                fragment.stopTimer();
                webManage.callBack(TAG_WEBMANAGE_SUCCEESS);
            } else {
                webManage.callBack("{\"状态\":\"" + JSONUtlis.getString(response, "状态") + "\"}");
            }
        } else {
            if ("二层成功".equals(JSONUtlis.getString(response, "状态"))) {
                if (TAG_1_WX == requestTag || TAG_2_WX == requestTag) {
                    pay2("wx", response);
                } else if (TAG_1_ALIPAY == requestTag || TAG_2_ALIPAY == requestTag) {
                    pay2("alipay", response);
                } else if (TAG_1_BANK == requestTag || TAG_2_BANK == requestTag) {
                    pay2("bank", response);
                }
            } else {
                webManage.callBack("{\"状态\":\"" + JSONUtlis.getString(response, "状态") + "\"}");
            }
        }
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        Log.e("web-onFailure:", requestTag + "");
        LoadDialog.dismiss(mContext);
        webManage.callBack(TAG_WEBMANAGE_FAIL);
    }
}
