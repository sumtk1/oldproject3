package com.zyd.wlwsdk.unionpay;

import android.content.Context;
import android.content.Intent;

import com.unionpay.UPPayAssistEx;

/**
 * 创建者 zengming.
 * 功能：解析银联支付返回结果
 */
public class UnionPay {

    public static UnionPay instance;

    public UnionPayCallBack unionPayCallBack;

    public void setUnionPayCallBack(UnionPayCallBack unionPayCallBack) {
        this.unionPayCallBack = unionPayCallBack;
    }

    public static UnionPay getInstance() {
        if (instance == null) {
            instance = new UnionPay();
        }
        return instance;
    }

    public void startPay(Context context, String tn, String unionPayMode, UnionPayCallBack unionPayCallBack) {
        UPPayAssistEx.startPay(context, null, null, tn, unionPayMode);
        setUnionPayCallBack(unionPayCallBack);
    }

    /**
     * @param data 银联支付完成后返回的结果集
     * @return 返回结果，
     */
    public void UnionPayResult(Intent data) {
        if (unionPayCallBack == null) {
            return;
        }
        if (data == null || instance == null) {
            unionPayCallBack.payError("失败");
            return;
        }

        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = "";
        try {
            if (data.getExtras().containsKey("pay_result")) {
                str = data.getExtras().getString("pay_result");
            }
        } catch (Exception e) {
            return;
        }

        if (str.equalsIgnoreCase("success")) {
            unionPayCallBack.paySuccess();
        } else if (str.equalsIgnoreCase("fail")) {
            unionPayCallBack.payError("失败");
        } else if (str.equalsIgnoreCase("cancel")) {
            unionPayCallBack.payError("取消");
        } else {
            unionPayCallBack.payError("失败");
        }
    }


    public interface UnionPayCallBack {
        void paySuccess();

        void payError(String state);
    }
}