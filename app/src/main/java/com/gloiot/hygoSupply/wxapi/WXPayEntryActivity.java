package com.gloiot.hygoSupply.wxapi;

import android.content.Intent;


import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zyd.wlwsdk.thirdpay.WXPay;
import com.zyd.wlwsdk.utlis.MToast;
;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

	@Override
	public int initResource() {
		return R.layout.pay_result;
	}

	@Override
	public void initData() {
		api = WXAPIFactory.createWXAPI(this, ConstantUtils.WXAPPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (String.valueOf(resp.errCode).equals("0")){
				WXPay.WXPayCallBack wxPayCallBack = WXPay.getInstance().getWxPayCallBack();
				wxPayCallBack.paySuccess();
				editor.putString(ConstantUtils.SP_PAYTYPE, "成功");
				editor.commit();
			}
			finish();
		}

		switch(resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				MToast.showToast("取消!");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				MToast.showToast("被拒绝");
				break;
			default:
				MToast.showToast("失败!");
				break;
		}
		finish();
	}
}