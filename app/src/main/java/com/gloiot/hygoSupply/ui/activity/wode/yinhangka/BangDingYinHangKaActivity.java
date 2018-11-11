package com.gloiot.hygoSupply.ui.activity.wode.yinhangka;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.serrver.network.RequestAction;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class BangDingYinHangKaActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_bank_02)
    TextView tvMydata02;
    @Bind(R.id.tv_getYZM)
    TextView tv_getYZM;
    @Bind(R.id.tv_bank_05)
    EditText tvMydata05;
    @Bind(R.id.tv_bank_06)
    TextView tvMydata06;
    @Bind(R.id.tv_bank_07)
    TextView tvMydata07;
    @Bind(R.id.tv_bank_10)
    TextView tvMydata10;
    @Bind(R.id.tv_bank_11)
    EditText tvMydata11;
    @Bind(R.id.btn_bindbankCard_next)
    Button btnBindbankCardNext;
    private String[] bank;
    private int i = 60;
    private String num, truename, phone;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tv_getYZM.setText(i + "秒后重试");
            } else if (msg.what == -8) {
                tv_getYZM.setText("获取验证码");
                tv_getYZM.setClickable(true);
                i = 60;
            }
        }
    };

    @Override
    public int initResource() {
        return R.layout.activity_my_bangdingyinhangka;
    }


    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "添加银行卡");
        num = preferences.getString(ConstantUtils.SP_USERPHONE, "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        tvMydata10.setText(num);
        requestHandleArrayList.add(requestAction.XuanZeYinHangKaLeiXing(BangDingYinHangKaActivity.this,phone));
        truename = preferences.getString(ConstantUtils.SP_USERNAME, "");
        tvMydata07.setText(truename);
    }

    int place, place1;

    @OnClick({R.id.btn_bindbankCard_next, R.id.rl_bankname, R.id.tv_getYZM, R.id.rl_bank_tv_cardtype})
    @Override
    public void onClick(View view) {
        if (onMoreClick(view)) return;
        switch (view.getId()) {
            case R.id.rl_bankname://选择银行
                if (bank != null) {
                    DialogUtlis.towBtnLoopView(mContext, "选择银行卡类型", bank, place, new MyDialogBuilder.LoopViewCallBack() {
                        @Override
                        public void callBack(int data) {
                            place = data;
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tvMydata02.setText(bank[place]);
                            DialogUtlis.dismissDialog();
                        }
                    });
                }
                break;
            case R.id.btn_bindbankCard_next:
                String name = tvMydata02.getText().toString();
                String kahao = tvMydata05.getText().toString();
                String type = tvMydata06.getText().toString();
                String xingming = tvMydata07.getText().toString();
                String shoujihao = tvMydata10.getText().toString();
                String yanzhengma = tvMydata11.getText().toString();
                String regx = "[0-9]{17}([0-9]|x|X)";
                if (TextUtils.isEmpty(name)) {
                    MToast.showToast("请选择银行种类");
                } else if (TextUtils.isEmpty(kahao)) {
                    MToast.showToast("请输入银行卡号");
                } else if (kahao.length() < 15 || kahao.length() > 21) {
                    MToast.showToast("银行卡号有误，请重新输入");
                } else if (TextUtils.isEmpty(xingming)) {
                    MToast.showToast("请填写持卡人姓名");
                } else if (TextUtils.isEmpty(yanzhengma)) {
                    MToast.showToast("请输入验证码");
                } else {
                    requestHandleArrayList.add(requestAction.TianJiaYinHangKa(BangDingYinHangKaActivity.this, phone, kahao, name, yanzhengma, shoujihao));
                }
                break;
            case R.id.tv_getYZM:
                shoujihao = tvMydata10.getText().toString();
                requestHandleArrayList.add(requestAction.shop_wl_monsms(BangDingYinHangKaActivity.this, shoujihao));
                break;
            default:
                break;

        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_p_bindingCard_three:
                MToast.showToast(response.getString("状态"));
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_monsms:
                MToast.showToast("验证码已发送");
                tv_getYZM.setClickable(false);
                tv_getYZM.setText(i + "秒后重试");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            SystemClock.sleep(1000);
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case ConstantUtils.TAG_p_bank_type_two:
                int num = Integer.parseInt(response.getString("条数"));
                if (num != 0) {
                    JSONArray jsonArray = response.getJSONArray("列表");
                    bank = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        bank[i] = jsonObject.getString("名称");
                    }
                }
                break;
            default:
                break;

        }
    }

}
