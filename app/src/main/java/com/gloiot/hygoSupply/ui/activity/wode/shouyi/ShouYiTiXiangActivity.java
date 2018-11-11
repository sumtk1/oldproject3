package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.yinhangka.WoDeYinHangKaActivity;
import com.gloiot.hygoSupply.ui.activity.wode.zhifubao.AddAlipayActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MD5Utlis;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.LoadDialog;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hygo03 on 2017/11/4.
 */

public class ShouYiTiXiangActivity extends BaseActivity implements View.OnClickListener, BaseActivity.RequestErrorCallback {
    @Bind(R.id.tv_tixiang_jine)
    TextView tvTixiangJine;
    @Bind(R.id.tixiang_shuoming)
    TextView tixiangShuoming;
    @Bind(R.id.et_tixiang_jine)
    EditText etTixiangJine;
    @Bind(R.id.btn_lijitiqu)
    Button btnLijitiqu;
    @Bind(R.id.tv_tianjia_yinhangka)
    TextView tvTianjiaYinhangka;
    @Bind(R.id.tv_tixiang_jin)
    TextView tvTixiangJin;

    private String phone, tiXian_fanShi, gudinghongli, zongjine, bank, bankId;

    private boolean bankIsNoNull = false;
    private String[] bankInfo;
    private List<String> bankIdList = new ArrayList<>();
    private int position = 0;
    private View.OnClickListener tianjiayinghangka;
    //用于数据请求成功标识
    private boolean successInfo = false;
    private JSONObject bankJson;
    private JSONObject zhifubaoJson;
    private JSONObject selectedJson;

    // 判断提现方式
    private String cardType;

    @Override
    public int initResource() {
        return R.layout.activity_shouyi_tixiang;
    }

    @Override
    public void initData() {
        cardType = getIntent().getStringExtra("type");
        CommonUtils.setTitleBar(this, "收益提现");
        setRequestErrorCallback(this);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        requestHandleArrayList.add(requestAction.shop_scsy_tx_Version(ShouYiTiXiangActivity.this, phone));
        requestHandleArrayList.add(requestAction.HuoQvYinHangKa110(ShouYiTiXiangActivity.this, phone, cardType));

        tianjiayinghangka = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShouYiTiXiangActivity.this, WoDeYinHangKaActivity.class));
                finish();
            }
        };

        etTixiangJine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    int jine = Integer.parseInt(s.toString().trim());
                    if (jine > 50000) {
                        MToast.showToast("提现金额不得超过50000");
                        etTixiangJine.setText("");
                    } else if (jine <= 0) {
                        MToast.showToast("提现金额必须大于0");
                        etTixiangJine.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.HuoQvYinHangKa110(ShouYiTiXiangActivity.this, phone, cardType));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_scsy_tx_Version:
                Log.e("shouyitiqu", response.toString());
                tvTixiangJine.setText(response.getString("总金额"));
                bankJson = response.getJSONObject("bank");
                zhifubaoJson = response.getJSONObject("支付宝");
                zongjine = response.getString("总金额");
                successInfo = true;
                break;
            case ConstantUtils.TAG_shop_wl_Bankcard_Version110:
                Log.e("yinhangka", response.toString());
                JSONArray bankList = response.getJSONArray("列表");
                if (bankList.length() > 0) {
                    bankInfo = new String[bankList.length()];
                    bankIdList.clear();
                    for (int i = 0; i < bankList.length(); i++) {
                        JSONObject object = bankList.getJSONObject(i);
                        String banknum = object.getString("卡号");

                        switch (JSONUtlis.getString(object, "类别")) {
                            case "支付宝":
                                if (banknum.length() > 0) {
                                    if (banknum.contains("@")) {
                                        bankInfo[i] = JSONUtlis.getString(object, "账户名") + "(" +
                                                banknum.substring(banknum.lastIndexOf("@") - 3, banknum.length()) + ")";
                                    } else if (banknum.length() == 11) {
                                        bankInfo[i] = JSONUtlis.getString(object, "账户名") + "(" + banknum.substring(0, 3) + "****" + banknum.substring(7, 11) + ")";
                                    } else {
                                        bankInfo[i] = JSONUtlis.getString(object, "账户名") + "(" + banknum.substring(banknum.length() - 4, banknum.length()) + ")";
                                    }
                                } else {
                                    bankInfo[i] = JSONUtlis.getString(object, "账户名");
                                }
                                bankIdList.add(object.getString("id"));
                                break;
                            case "信用卡":
                            case "储蓄卡":
                                if (!TextUtils.isEmpty(banknum) && banknum.length() > 4) {
                                    bankInfo[i] = object.getString("账户名") + "(" + banknum.substring(banknum.length() - 4, banknum.length()) + ")";
                                    bankIdList.add(object.getString("id"));
                                }
                                break;
                            default:
                        }
                    }
                }
                bankIsNoNull = true;
                break;
            case ConstantUtils.TAG_shop_profitExchange_Version:
                if ("成功".equals(response.get("状态").toString())) {
                    MToast.showToast(response.get("title").toString());
                }
                requestHandleArrayList.add(requestAction.shop_scsy_tx_Version(ShouYiTiXiangActivity.this, phone));
                break;
            default:
        }
    }


    @OnClick({R.id.tv_tianjia_yinhangka, R.id.btn_lijitiqu, R.id.tv_toptitle_right})
    @Override
    public void onClick(View v) {
        if (onMoreClick(v)) return;
        switch (v.getId()) {
            case R.id.tv_tianjia_yinhangka:

                // 如果请求银行卡列表失败 重新请求
                if (!bankIsNoNull) {
                    Toast.makeText(mContext, "获取账户列表", Toast.LENGTH_SHORT);
                    requestHandleArrayList.add(requestAction.HuoQvYinHangKa110(ShouYiTiXiangActivity.this, phone, cardType));
                    return;
                }
                //可能存在银行卡重名的情况，这里需要通过选中的position来得到银行卡的id，所以重载了oneBtnSingleChoice()方法和MyDialogBuilder中的setSingleChoice()方法\
                if (null != bankInfo && bankInfo.length > 0) {
                    DialogUtlis.towBtnLoopView(this, "选择提现账户", bankInfo, position, new MyDialogBuilder.LoopViewCallBack() {
                        @Override
                        public void callBack(int data) {
                            position = data;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if ("支付宝".equals(bankInfo[position])) {
                                    DialogUtlis.dismissDialog();
                                    startActivity(new Intent(ShouYiTiXiangActivity.this, AddAlipayActivity.class));
                                } else {
                                    bank = bankInfo[position];
                                    if (bank.contains("支付宝")) {
                                        selectedJson = zhifubaoJson;
                                    } else {
                                        selectedJson = bankJson;
                                    }
                                    bankId = bankIdList.get(position);
                                    Log.e(TAG, "onClick: " + bankId);
                                    tvTianjiaYinhangka.setText(bank);
                                    try {
                                        tixiangShuoming.setText(selectedJson.getString("备注").replace("/n", "\n"));
                                        tiXian_fanShi = selectedJson.getString("固定提现");
                                        gudinghongli = selectedJson.getString("固定提现额");
                                        if ("是".equals(tiXian_fanShi)) {
                                            tvTixiangJin.setVisibility(View.VISIBLE);
                                            etTixiangJine.setVisibility(View.GONE);
                                            tvTixiangJin.setText(gudinghongli);
                                        } else {
                                            tvTixiangJin.setVisibility(View.GONE);
                                            etTixiangJine.setVisibility(View.VISIBLE);
                                            etTixiangJine.setEnabled(true);
                                            etTixiangJine.setText("");
                                        }
                                    } catch (Exception e) {
                                        successInfo = false;
                                    }

                                    DialogUtlis.dismissDialog();
                                }
                            } catch (Exception e) {
                                DialogUtlis.dismissDialog();
                            }
                        }
                    });
                } else {
                    DialogUtlis.twoBtnNormal(mContext, "无银行卡是否添加?", "取消", "去添加", tianjiayinghangka);
                }
                break;

            case R.id.btn_lijitiqu:
                if (successInfo) {
                    check();
                } else {
                    requestHandleArrayList.add(requestAction.shop_scsy_tx_Version(ShouYiTiXiangActivity.this, phone));
                }
                break;
            default:
        }
    }

    public void check() {
        try {
            if ("是".equals(tiXian_fanShi)) {
                if (tvTianjiaYinhangka.getText().toString().equals("请选择提现账户")) {
                    DialogUtlis.oneBtnNormal(mContext, "请选择提现账户");
                } else if (Double.parseDouble(tvTixiangJin.getText().toString()) > Double.parseDouble(zongjine)) {
                    DialogUtlis.oneBtnNormal(mContext, "可兑换红利不足，不能进行提取");
                } else if (preferences.getString(ConstantUtils.SP_MYPWD, "否").equals("否")) {
                    CommonUtils.toSetPwd(this);
                } else {
                    DialogUtlis.oneBtnPwd(this, "¥" + tvTixiangJin.getText().toString(), new DialogUtlis.PasswordCallback() {
                        @Override
                        public void callback(String data) {
                            if (TextUtils.isEmpty(data) || data.length() < 6) {
                                MToast.showToast("请输入支付密码");
                            } else {
                                String weiYiID = preferences.getString(ConstantUtils.SP_ONLYID, "");

                                if (bank.contains("支付宝")) {
                                    requestHandleArrayList.add(requestAction.shop_profitExchange_Version(ShouYiTiXiangActivity.this, phone, bankId, tvTixiangJin.getText().toString(), weiYiID, MD5Utlis.Md5(data), true));
                                } else {
                                    requestHandleArrayList.add(requestAction.shop_profitExchange_Version(ShouYiTiXiangActivity.this, phone, bankId, tvTixiangJin.getText().toString(), weiYiID, MD5Utlis.Md5(data)));
                                }
                            }
                        }
                    });
                }
            } else {
                if (tvTianjiaYinhangka.getText().toString().equals("请选择提现账户")) {
                    DialogUtlis.oneBtnNormal(mContext, "请选择提现账户");
                } else if ("".equals(etTixiangJine.getText().toString())) {
                    MToast.showToast("请输入提现金额");
                } else if (Double.parseDouble(etTixiangJine.getText().toString()) > Double.parseDouble(zongjine)) {
                    DialogUtlis.oneBtnNormal(mContext, "可兑换红利不足，不能进行提取");
                } else if (preferences.getString(ConstantUtils.SP_MYPWD, "否").equals("否")) {
                    CommonUtils.toSetPwd(this);
                } else {
                    DialogUtlis.oneBtnPwd(this, "¥" + etTixiangJine.getText().toString(), new DialogUtlis.PasswordCallback() {
                        @Override
                        public void callback(String data) {
                            if (TextUtils.isEmpty(data) || data.length() < 6) {
                                MToast.showToast("请输入支付密码");
                            } else {
                                String weiYiID = preferences.getString(ConstantUtils.SP_ONLYID, "");

                                if (bank.contains("支付宝")) {
                                    requestHandleArrayList.add(requestAction.shop_profitExchange_Version(ShouYiTiXiangActivity.this, phone, bankId, etTixiangJine.getText().toString(), weiYiID, MD5Utlis.Md5(data), true));
                                } else {
                                    requestHandleArrayList.add(requestAction.shop_profitExchange_Version(ShouYiTiXiangActivity.this, phone, bankId, etTixiangJine.getText().toString(), weiYiID, MD5Utlis.Md5(data)));
                                }
                            }
                        }
                    });
                }
            }

        } catch (NumberFormatException e) {
            DialogUtlis.oneBtnNormal(mContext, "可兑换红利不足，不能进行提取");
        }
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {
        super.onCancel(requestTag, showLoad);
        if (requestTag == ConstantUtils.TAG_shop_scsy_tx_Version || requestTag == ConstantUtils.TAG_shop_profitExchange_Version) {
            LoadDialog.dismiss(mContext);
            finish();
        }
    }

    @Override
    public void requestErrorcallback(int requestTag, JSONObject response) throws Exception {
        switch (requestTag) {
            case ConstantUtils.TAG_shop_profitExchange_Version:
                if ("系统繁忙，请稍后重试".equals(response.get("状态").toString())) {
                    DialogUtlis.oneBtnNormal(mContext, "系统繁忙，请稍后重试", "确定", false, null);
                } else {
                    MToast.showToast(response.get("状态").toString());
                }
                break;
            default:
        }
    }
}
