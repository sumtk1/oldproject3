package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.FreightTemplateModel;
import com.gloiot.hygoSupply.databinding.ActivityFreightTemplateDetailsBinding;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.YIXiaJiaFragment;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FreightTemplateDetails extends BaseActivity implements View.OnClickListener {

    private ActivityFreightTemplateDetailsBinding binding;
    private FreightTemplateModel model;
    private ObservableBoolean isFreeExpress;
    private ObservableBoolean isLimitMoney;
    private ObservableBoolean isLimitNum;
    private boolean isLimit;
    private List<String> priceTypeList;
    private String phone = "";
    private String tradeId;
    private String tradeName;
    private boolean isFromEdit;//从编辑页面过来

    @Override

    public int initResource() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_freight_template_details);
        return 0;
    }

    @Override
    public void initData() {
        initComponent();
        priceTypeList = new ArrayList<>();
        priceTypeList.add("按件数");
        priceTypeList.add("按重量");
        priceTypeList.add("按体积");
        loadDataForPriceType();
        if ("new".equals(getIntent().getStringExtra("type"))) {
            CommonUtils.setTitleBar((Activity) mContext, true, "新建模板", "");
            model = new FreightTemplateModel();
            isLimitMoney = new ObservableBoolean();
            isLimitNum = new ObservableBoolean();
            isFreeExpress = new ObservableBoolean(true);
            model.setFreeExpress(isFreeExpress);
            model.setIsLimitMoney(isLimitMoney);
            model.setIsLimitNum(isLimitNum);
            tradeId = getIntent().getStringExtra("tradeId");
            tradeName = getIntent().getStringExtra("tradeName");
            Log.e("TAG", "initData:商品信息 " + tradeName + "---" + tradeId);

        } else {
            isFromEdit = true;
            CommonUtils.setTitleBar((Activity) mContext, true, "模板详情", "");
            model = (FreightTemplateModel) getIntent().getSerializableExtra("model");
            isLimitMoney = model.getIsLimitMoney();
            isLimitNum = model.getIsLimitNum();
            isFreeExpress = model.getFreeExpress();
            if (isLimitNum.get()) {
                isLimit = true;
                isLimitNum.set(true);
                binding.etFreightLimitnum.setText(model.getLimiters());
            } else if (isLimitMoney.get()) {
                isLimit = true;
                isLimitMoney.set(true);
                binding.etFreightLimitmoney.setText(model.getLimiters());
            }
            if (!model.getFreeExpress().get()) {
                switch (model.getPriceType()) {
                    case "按件数":
                        binding.spFreightPriceType.setSelection(0);
                        break;
                    case "按重量":
                        binding.spFreightPriceType.setSelection(1);
                        break;
                    case "按体积":
                        binding.spFreightPriceType.setSelection(2);
                        break;
                }
            }
        }

        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        binding.setModel(model);
    }


    private void initComponent() {
        binding.ivFreightFreeFlag.setOnClickListener(this);
        binding.ivFreightNotfreeFlag.setOnClickListener(this);
        binding.ivFreightFreeMoney.setOnClickListener(this);
        binding.tvFreightFreeNum.setOnClickListener(this);
        binding.tvFreightFreeMoney.setOnClickListener(this);
        binding.ivFreightFreeNum.setOnClickListener(this);
        binding.btnFreightSave.setOnClickListener(this);
        setTextWatcher();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_freight_free_flag:
                isFreeExpress.set(true);
                model.setFreeExpress(isFreeExpress);
                break;
            case R.id.iv_freight_notfree_flag:
                isFreeExpress.set(false);
                model.setFreeExpress(isFreeExpress);
                break;
            case R.id.iv_freight_free_num:
            case R.id.tv_freight_free_num:

                if (isLimit) {
                    if (isLimitNum.get()) {
                        isLimitNum.set(false);
                        isLimitMoney.set(false);
                        isLimit = false;
                        model.setLimitersType("");
                        binding.etFreightLimitnum.setText("");
                    } else {
                        isLimitNum.set(true);
                        isLimitMoney.set(false);
                        model.setLimitersType("最低条件");
                        binding.etFreightLimitmoney.setText("");
                    }
                } else {
                    isLimitNum.set(true);
                    isLimitMoney.set(false);
                    model.setLimitersType("最低条件");
                    isLimit = true;
                }
                model.setIsLimitMoney(isLimitMoney);
                model.setIsLimitNum(isLimitNum);
                break;
            case R.id.iv_freight_free_money:
            case R.id.tv_freight_free_money:
                if (isLimit) {
                    if (isLimitMoney.get()) {
                        isLimitNum.set(false);
                        isLimitMoney.set(false);
                        isLimit = false;
                        model.setLimitersType("");
                        binding.etFreightLimitmoney.setText("");
                    } else {
                        isLimitNum.set(false);
                        isLimitMoney.set(true);
                        model.setLimitersType("最低金额");
                        binding.etFreightLimitnum.setText("");
                    }
                } else {
                    isLimitNum.set(false);
                    isLimitMoney.set(true);
                    isLimit = true;
                    model.setLimitersType("最低金额");
                }
                model.setIsLimitMoney(isLimitMoney);
                model.setIsLimitNum(isLimitNum);
                break;
            case R.id.btn_freight_save:
                if (isLimit) {
                    if (isLimitNum.get()) {//限制数量
                        model.setLimiters(binding.etFreightLimitnum.getText().toString());
                    } else {//限制金额
                        model.setLimiters(binding.etFreightLimitmoney.getText().toString());
                    }
                }
                if (check()) {
                    if ("new".equals(getIntent().getStringExtra("type"))) {
                        if (TextUtils.isEmpty(tradeId)) {
                            requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone, "new", model));
                        } else {
                            requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone, "new", tradeId, model));
                        }
                    } else {
                        requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone, "edit", model));
                    }
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(model.getName())) {
            MToast.showToast("请填写模板名称");
            return false;
        }
        if (!model.getFreeExpress().get()) {
            if (!"按件数".equals(model.getPriceType())) {
                if (TextUtils.isEmpty(model.getStrandard())) {
                    MToast.showToast("请填写正确运费详情");
                    return false;
                } else if ("0.".equals(model.getStrandard())) {
                    MToast.showToast("运费详情格式不正确");
                    return false;
                } else if (Double.parseDouble(model.getStrandard()) <= 0) {
                    MToast.showToast("运费详情数据不能为零");
                    return false;
                }
            }
            if (TextUtils.isEmpty(model.getUnit()) || TextUtils.isEmpty(model.getFreight()) || TextUtils.isEmpty(model.getAdd()) || TextUtils.isEmpty(model.getAddNum())) {
                MToast.showToast("请填写正确运费详情");
                return false;
            } else if ("0.".equals(model.getUnit()) || "0.".equals(model.getFreight()) || "0.".equals(model.getAdd()) || "0.".equals(model.getAddNum())) {
                MToast.showToast("运费详情格式不正确");
                return false;
            } else if (Double.parseDouble(model.getUnit()) <= 0) {
                binding.etFreightUnit.setText("");
                MToast.showToast("运费详情数据不能为零");
                return false;
            } else if (Double.parseDouble(model.getFreight()) <= 0) {
                binding.etFreightFreight.setText("");
                MToast.showToast("金额不能为零");
                return false;
            } else if (Double.parseDouble(model.getAdd()) <= 0) {
                binding.etFreightAdd.setText("");
                MToast.showToast("运费详情数据不能为零");
                return false;
            } else if (Double.parseDouble(model.getAddNum()) <= 0) {
                binding.etFreightAddNum.setText("");
                MToast.showToast("金额不能为零");
                return false;
            } else if (isLimit) {
                if (model.getIsLimitNum().get()) {
                    if (TextUtils.isEmpty(model.getLimiters())) {
                        MToast.showToast("请填写包邮最低" + binding.tvFreightFreeNum1.getText().toString() + "数");
                        return false;
                    } else if (Double.parseDouble(model.getLimiters()) <= 0) {
                        MToast.showToast("包邮最低" + binding.tvFreightFreeNum1.getText().toString() + "数不能为零");
                        return false;
                    }
                } else {
                    if (TextUtils.isEmpty(model.getLimiters())) {
                        MToast.showToast("请填写包邮最低消费额");
                        return false;
                    } else if (Double.parseDouble(model.getLimiters()) <= 0) {
                        MToast.showToast("包邮最低消费额不能为零");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_exFeeModel:
                if (TextUtils.isEmpty(tradeId)) {
                    ShangPinShangChuanActivity.isRefreshFreight = true;
                    finish();
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_shelf(this, phone, tradeId, tradeName));
                }
                break;
            case ConstantUtils.TAG_shop_wl_shelf:
                finish();
                MToast.showToast("该商品已恢复销售");
                break;
        }

    }

    private ArrayAdapter<String> priceType;

    /**
     * 运费模板拉列表适配器
     */
    private void loadDataForPriceType() {

        priceType = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style1, R.id.tv_shangpin_shangchuan1_txtvwSpinner1, priceTypeList);
        priceType.setDropDownViewResource(R.layout.spinner_dropdown_style1);
        binding.spFreightPriceType.setAdapter(priceType);
        binding.spFreightPriceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                model.setPriceType(binding.spFreightPriceType.getSelectedItem() + "");

                switch (model.getPriceType()) {
                    case "按件数":
                        binding.llFreightSetstandard.setVisibility(View.GONE);
                        binding.tvFreightUnitshow.setText("件");
                        binding.tvFreightUnitshow1.setText("件");
                        binding.tvFreightFreeNum1.setText("件");
                        binding.tvFreightFreeNum3.setText("件");
                        String unit = binding.etFreightUnit.getText().toString();
                        if (unit.contains(".")) {
                            try {
                                binding.etFreightUnit.setText(unit.toString().subSequence(0, unit.toString().indexOf(".")));
                                binding.etFreightUnit.setSelection(binding.etFreightUnit.getText().length());
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                        String add = binding.etFreightAdd.getText().toString();
                        if (add.contains(".")) {
                            try {
                                binding.etFreightAdd.setText(add.toString().subSequence(0, add.toString().indexOf(".")));
                                binding.etFreightAdd.setSelection(binding.etFreightUnit.getText().length());
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                        String limitnum = binding.etFreightLimitnum.getText().toString();
                        if (limitnum.contains(".")) {
                            try {
                                binding.etFreightLimitnum.setText(limitnum.toString().subSequence(0, limitnum.toString().indexOf(".")));
                                binding.etFreightLimitnum.setSelection(binding.etFreightUnit.getText().length());
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case "按重量":
                        binding.llFreightSetstandard.setVisibility(View.VISIBLE);
                        binding.tvFreightStandard.setText("kg");
                        binding.tvFreightUnitshow.setText("kg");
                        binding.tvFreightUnitshow1.setText("kg");
                        binding.tvFreightFreeNum1.setText("重量");
                        binding.tvFreightFreeNum3.setText("kg");
                        break;
                    case "按体积":
                        binding.llFreightSetstandard.setVisibility(View.VISIBLE);
                        binding.tvFreightStandard.setText("m³");
                        binding.tvFreightUnitshow.setText("m³");
                        binding.tvFreightUnitshow1.setText("m³");
                        binding.tvFreightFreeNum1.setText("体积");
                        binding.tvFreightFreeNum3.setText("m³");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setTextWatcher() {
        binding.etFreightUnit.addTextChangedListener(new DecimalTextWatcher(binding.etFreightUnit) {
            @Override
            public void afterTextChanged(Editable s) {
                if ("按件数".equals(model.getPriceType())) {
                    if (s.toString().contains(".")) {
                        try {
                            editText.setText(s.toString().subSequence(0, s.toString().indexOf(".")));
                            editText.setSelection(editText.getText().length());
                            if (!isFromEdit) {
                                MToast.showToast("件数必须为整数");
                            }
                            isFromEdit=false;
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        binding.etFreightFreight.addTextChangedListener(new DecimalTextWatcher(binding.etFreightFreight) {
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.etFreightAdd.addTextChangedListener(new DecimalTextWatcher(binding.etFreightAdd) {
            @Override
            public void afterTextChanged(Editable s) {
                if ("按件数".equals(model.getPriceType())) {
                    if (s.toString().contains(".")) {
                        try {
                            editText.setText(s.toString().subSequence(0, s.toString().indexOf(".")));
                            editText.setSelection(editText.getText().length());
                            if (!isFromEdit) {
                                MToast.showToast("件数必须为整数");
                            }
                            isFromEdit=false;
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        binding.etFreightAddNum.addTextChangedListener(new DecimalTextWatcher(binding.etFreightAddNum) {
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etFreightLimitnum.addTextChangedListener(new DecimalTextWatcher(binding.etFreightLimitnum) {
            @Override
            public void afterTextChanged(Editable s) {
                if ("按件数".equals(model.getPriceType())) {
                    if (s.toString().contains(".")) {
                        try {
                            editText.setText(s.toString().subSequence(0, s.toString().indexOf(".")));
                            editText.setSelection(editText.getText().length());
                            if (!isFromEdit) {
                                MToast.showToast("件数必须为整数");
                            }
                            isFromEdit=false;
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!model.getIsLimitNum().get() && !TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setText("");
                }
            }
        });
        binding.etFreightLimitmoney.addTextChangedListener(new DecimalTextWatcher(binding.etFreightLimitmoney) {
            @Override
            public void afterTextChanged(Editable s) {
                if (!model.getIsLimitMoney().get() && !TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setText("");
                }
            }
        });
        binding.etFreightStandard.addTextChangedListener(new DecimalTextWatcher(binding.etFreightStandard) {
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, boolean isChecked) {
        Log.e("TAG", "onClick:3" + isChecked);
        if (isChecked) {
            imageView.setImageResource(R.mipmap.ic_freight_type_check);
        } else {
            imageView.setImageResource(R.mipmap.ic_freight_type_normal);
        }
    }


    abstract class DecimalTextWatcher implements TextWatcher {
        EditText editText;

        public DecimalTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //小数点后位数控制
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }

            }
            //第一位为点
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

            //第一位为零，后面只能输入点
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }


        }
    }

}
