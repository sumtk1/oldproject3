package com.gloiot.hygoSupply.ui.activity.yunyinghuodong;


import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建优惠券
 */
public class ChuangJianYouHuiQuanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_coupon_name)
    EditText etCouponName;
    @Bind(R.id.et_coupon_mianzhi)
    EditText etCouponMianzhi;
    @Bind(R.id.et_coupon_shuliang)
    EditText etCouponShuliang;
    @Bind(R.id.et_coupon_xianling)
    TextView etCouponXianling;
    @Bind(R.id.et_qian)
    EditText etQian;
    @Bind(R.id.tv_coupon_shengxiaoshijian)
    TextView tvCouponShengxiaoshijian;
    @Bind(R.id.tv_coupon_shixiaoshijian)
    TextView tvCouponShixiaoshijian;
    @Bind(R.id.but_coupon)
    Button butCoupon;
    @Bind(R.id.im_ticket_maner)
    ImageView im_ticket_maner;
    @Bind(R.id.im_ticket_unconditional)
    ImageView im_ticket_unconditional;
    private String zhanghao, shangpingID = "", shiyongtiaojian = "0";//使用条件（无/金额）
    private int data1 = 0, data2 = 0;//时间（天）
    private int month1 = 0, month2 = 0;//时间（月）
    private String limit;

    @Override

    public int initResource() {
        return R.layout.activity_chuangjianyouhuiquan;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "创建优惠券", "");
        zhanghao = preferences.getString(ConstantUtils.SP_ZHANGHAO, "");
        //默认选金额
        im_ticket_maner.setImageResource(R.mipmap.ic_radio);
        im_ticket_unconditional.setImageResource(R.mipmap.ic_quan);
        etCouponName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        etQian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                shiyongtiaojian = editable.toString();
            }
        });

    }


    @OnClick({R.id.tv_coupon_shengxiaoshijian, R.id.tv_coupon_shixiaoshijian, R.id.but_coupon, R.id.im_ticket_unconditional, R.id.im_ticket_maner})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_coupon_shengxiaoshijian:
                DialogUtlis.towBtnDate(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                        month1 = datePicker.getMonth() + 1;
                        data1 = datePicker.getDayOfMonth();

                        String month;
                        String data;
                        if (month1 < 10) {
                            month = "0" + month1;
                        } else {
                            month = month1 + "";
                        }

                        if (data1 < 10) {
                            data = "0" + data1;
                        } else {
                            data = data1 + "";
                        }

                        tvCouponShengxiaoshijian.setText(datePicker.getYear() + "-" + month + "-" + data);
                        DialogUtlis.myDialogBuilder.dismiss();
                    }
                }, 1);
                break;
            case R.id.tv_coupon_shixiaoshijian:
                DialogUtlis.towBtnDate(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                        month2 = datePicker.getMonth() + 1;
                        data2 = datePicker.getDayOfMonth();
                        String month;
                        String data;
                        if (month2 < 10) {
                            month = "0" + month2;
                        } else {
                            month = month2 + "";
                        }

                        if (data2 < 10) {
                            data = "0" + data2;
                        } else {
                            data = data2 + "";
                        }
                        tvCouponShixiaoshijian.setText(datePicker.getYear() + "-" + month + "-" + data);
                        DialogUtlis.myDialogBuilder.dismiss();
                    }
                }, 1);
                break;
            case R.id.im_ticket_maner://满金额使用
                if ("无".equals(shiyongtiaojian)) {
                    shiyongtiaojian = "0";
                }
                etQian.setInputType(InputType.TYPE_CLASS_NUMBER);
                im_ticket_maner.setImageResource(R.mipmap.ic_radio);
                im_ticket_unconditional.setImageResource(R.mipmap.ic_quan);
                break;
            case R.id.im_ticket_unconditional://无条件使用
                etQian.getText().clear();
                etQian.setInputType(InputType.TYPE_NULL);
                shiyongtiaojian = "无";
                im_ticket_maner.setImageResource(R.mipmap.ic_quan);
                im_ticket_unconditional.setImageResource(R.mipmap.ic_radio);
                break;
            case R.id.but_coupon:
                int mianzhi = 0, shiyongjiner = 0, liang = 0;
                limit = etQian.getText().toString();
                String couponname = etCouponName.getText().toString().trim();
                String faxinglang = etCouponShuliang.getText().toString().trim();
                String shengxiaoshijian = tvCouponShengxiaoshijian.getText().toString();
                String shixiaoshijian = tvCouponShixiaoshijian.getText().toString();
                String shiyongmianzhi = etCouponMianzhi.getText().toString().trim();
                String moey = etQian.getText().toString().trim();
                //面值金额与使用金额转换
                try {
                    if (!TextUtils.isEmpty(faxinglang)) {
                        liang = Integer.parseInt(faxinglang);
//                        Log.e("--liang0--==", liang + "");
                    }
                    if (!TextUtils.isEmpty(moey) && !TextUtils.isEmpty(shiyongmianzhi)) {
                        mianzhi = Integer.parseInt(shiyongmianzhi);
                        shiyongjiner = Integer.parseInt(moey);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(couponname)) {
                    MToast.showToast("请输入优惠券名称");
                } else if (TextUtils.isEmpty(shiyongmianzhi) || shiyongmianzhi.equals("0")) {
                    MToast.showToast("面值范围为1至50");
                } else if (Double.parseDouble(shiyongmianzhi) > 50) {
                    MToast.showToast("面值范围为1至50");
                } else if (TextUtils.isEmpty(faxinglang) || faxinglang.equals("0")) {
                    MToast.showToast("发行量不能小于0");
                } else if (liang > 10000) {
                    etCouponShuliang.getText().clear();
                    MToast.showToast("发行量不能大于10000张");
                } else if (TextUtils.isEmpty(limit)) {
                    MToast.showToast("请输入使用条件");
                } else if (limit.equals("0")) {
                    MToast.showToast("使用金额不能为0");
                } else if (Integer.parseInt(limit) < getLimit(shiyongmianzhi)) {
                    MToast.showToast("使用条件金额不得低于" + getLimit(shiyongmianzhi));
                } else if (TextUtils.isEmpty(shengxiaoshijian)) {
                    MToast.showToast("请输入生效时间");
                } else if (TextUtils.isEmpty(shixiaoshijian)) {
                    MToast.showToast("请输入失效时间");
                } else {
                    requestHandleArrayList.add(requestAction.shop_establish_coupon(ChuangJianYouHuiQuanActivity.this, zhanghao, "店铺", couponname,
                            etCouponMianzhi.getText().toString().trim(), faxinglang, shiyongtiaojian,
                            shengxiaoshijian, shixiaoshijian, shangpingID));
                }
                break;
        }
    }

    public int getLimit(String mianzhi) {
        int a = Integer.parseInt(mianzhi);
        if (a >= 1 && a <= 3) {
            return 50;
        } else if (a >= 4 && a <= 5) {
            return 90;
        } else if (a >= 6 && a <= 10) {
            return 150;
        } else if (a >= 11 && a <= 20) {
            return 300;
        } else if (a >= 21 && a <= 30) {
            return 500;
        } else if (a >= 31 && a <= 50) {
            return 800;
        } else {
            return -1;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_establish_coupon:
                MToast.showToast(response.getString("状态"));
                finish();
                break;
        }
    }

}
