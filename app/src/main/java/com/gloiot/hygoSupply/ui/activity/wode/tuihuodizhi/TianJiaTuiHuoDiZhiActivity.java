package com.gloiot.hygoSupply.ui.activity.wode.tuihuodizhi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加收货地址.
 */

public class TianJiaTuiHuoDiZhiActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.et_shouhuoren_name)
    EditText mEtShouhuorenName;
    @Bind(R.id.et_shouhuoren_phonenum)
    EditText mEtShouhuorenPhoneNum;
    @Bind(R.id.tv_shouhuoren_area)
    TextView mTvShouhuorenArea;
    @Bind(R.id.et_address_detail)
    EditText mEtAddressDetail;
    @Bind(R.id.rl_set_default_address)
    RelativeLayout mRlSetDefaultAddress;
    @Bind(R.id.cb_set_default)
    CheckBox mCbSetDefault;
    @Bind(R.id.btn_save)
    Button mBtnSave;
    private String typeFlag;//类型：修改/添加
    private ShouhuoAddress addressInfo;
    private String mShouhuorenName, mShouhuorenPhoneNum, mShouhuorenArea, mShouhuorenProvince, mShuohuorenCity,
            mShohuorenDistrict, mShouhuorenAddressDetail, mAddressState;


    @Override
    public int initResource() {
        return R.layout.activity_xiugaishouhuodizhi;
    }

    @Override
    public void initData() {
        typeFlag = getIntent().getStringExtra("type");
        if (typeFlag.equals("编辑")) {
            addressInfo = (ShouhuoAddress) getIntent().getSerializableExtra("addressInfo");
            CommonUtils.setTitleBar((Activity) mContext, true, "修改退货地址", "");
            mEtShouhuorenName.setText(addressInfo.getShouhuoren());
            mEtShouhuorenName.setSelection(addressInfo.getShouhuoren().length());//光标移到最后
            mEtShouhuorenPhoneNum.setText(addressInfo.getPhoneNum());
            mEtAddressDetail.setText(addressInfo.getDetailedAddress());
            mShouhuorenArea = addressInfo.getShouhuoArea();
            mShouhuorenProvince = addressInfo.getProvince();
            mShuohuorenCity = addressInfo.getCity();
            mShohuorenDistrict = addressInfo.getDistrict();
            mTvShouhuorenArea.setText(mShouhuorenArea);

            if (addressInfo.getDefaultAddress().equals("是")) {
                mCbSetDefault.setChecked(true);
            } else {
                mCbSetDefault.setChecked(false);
            }
        } else if (typeFlag.equals("添加")) {
            CommonUtils.setTitleBar((Activity) mContext, true, "添加退货地址", "");
        }

    }

    /**
     * 校验数据是否符合规则
     *
     * @return
     */
    private boolean verifyData() {
        mShouhuorenName = mEtShouhuorenName.getText().toString();
        mShouhuorenPhoneNum = mEtShouhuorenPhoneNum.getText().toString();
        mShouhuorenArea = mTvShouhuorenArea.getText().toString();
        mShouhuorenAddressDetail = mEtAddressDetail.getText().toString();
        if (TextUtils.isEmpty(mShouhuorenName)) {
            MToast.showToast("请输入退货人姓名");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenPhoneNum)) {
            MToast.showToast("请输入手机号");
            return false;
        } else if (mShouhuorenPhoneNum.length() != 11) {
            MToast.showToast("请输入有效手机号");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenArea)) {
            MToast.showToast("请选择退货地区");
            return false;
        } else if (TextUtils.isEmpty(mShouhuorenAddressDetail)) {
            MToast.showToast("请填写详细地址");
            return false;
        } else {
            return true;
        }
    }


    @OnClick({R.id.tv_shouhuoren_area, R.id.rl_set_default_address, R.id.btn_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_default_address:
                if (mCbSetDefault.isChecked()) {
                    mCbSetDefault.setChecked(false);
                } else {
                    mCbSetDefault.setChecked(true);
                }
                break;
            case R.id.tv_shouhuoren_area:
                hideInput(mContext, mTvShouhuorenArea);//隐藏软键盘
                CityPicker cityPicker = new CityPicker.Builder(TianJiaTuiHuoDiZhiActivity.this)
                        .textSize(20)
                        .title("退货地区")
                        .backgroundPop(0xa0000000)
                        .titleBackgroundColor("#ff7f29")
                        .titleTextColor("#ffffff")
                        .backgroundPop(0xa0000000)
                        .confirTextColor("#ffffff")
                        .cancelTextColor("#ffffff")
                        .province(mShouhuorenProvince)
                        .city(mShuohuorenCity)
                        .district(mShohuorenDistrict)
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .onlyShowProvinceAndCity(false)
                        .build();
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        //省份
                        mShouhuorenProvince = citySelected[0];
                        //城市
                        mShuohuorenCity = citySelected[1];
                        //区县（如果设定了两级联动，那么该项返回空）
                        mShohuorenDistrict = citySelected[2];
                        //邮编
                        String code = citySelected[3];

                        mTvShouhuorenArea.setText(mShouhuorenProvince + mShuohuorenCity + mShohuorenDistrict);
                    }

                    @Override
                    public void onCancel() {
//                        Toast.makeText(TianJiaTuiHuoDiZhiActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    }
                });

                break;
            case R.id.btn_save:
                if (verifyData()) {
//                    if (mCbSetDefault.isChecked()) {
//                        mAddressState = "是";
//                    } else {
//                        mAddressState = "否";
//                    }
                    mAddressState = "是";
                    if (typeFlag.equals("编辑")) {
                        addressInfo = new ShouhuoAddress(addressInfo.getId(),
                                mShouhuorenName,
                                mShouhuorenPhoneNum,
                                mShouhuorenProvince,
                                mShuohuorenCity,
                                mShohuorenDistrict,
                                mShouhuorenAddressDetail,
                                mAddressState,
                                mShouhuorenArea);
                        requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), addressInfo, "edit"));
                    } else {//添加新的退货地址
                        addressInfo = new ShouhuoAddress("",
                                mShouhuorenName,
                                mShouhuorenPhoneNum,
                                mShouhuorenProvince,
                                mShuohuorenCity,
                                mShohuorenDistrict,
                                mShouhuorenAddressDetail,
                                mAddressState,
                                mShouhuorenArea);
                        requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), addressInfo, "add"));
                    }

                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_send://添加/修改
                if (response.getString("状态").equals("成功")) {
                    if (typeFlag.equals("编辑")) {
                        MToast.showToast("修改成功");
                    } else {
                        MToast.showToast("添加成功");
                    }
                    Intent intent = new Intent();
                    intent.putExtra("addressInfo", addressInfo);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    public void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
