//package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//import com.gloiot.hygoSupply.R;
//import com.gloiot.hygoSupply.bean.ShangpinGuigeBean;
//import com.gloiot.hygoSupply.ui.activity.BaseActivity;
//import com.gloiot.hygoSupply.utlis.CommonUtils;
//import com.zyd.wlwsdk.utlis.MToast;
//
//public class ChangeShangpinGuigeActivity1 extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
//    private EditText et_change_shangpin_guige_yanse, et_change_shangpin_guige_chicun, et_change_shangpin_guige_jiage, et_change_shangpin_guige_kucun;
//    private EditText et_change_shangpin_guige_jianyijia;
//    private EditText et_change_shangpin_guige_xiangxi;
//    private ShangpinGuigeBean shangpinGuigeBean = new ShangpinGuigeBean();
//    private String position, change, jiesuanjia, beishu;
//    private boolean isClothes;
//    private Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public int initResource() {
//        intent = getIntent();
//        isClothes = intent.getBooleanExtra("isClothes", false);
//        if (isClothes) {
//            return R.layout.activity_change_shangpin_guige;
//        } else {
//            return R.layout.activity_change_shangpin_guige1;
//        }
//    }
//
//    @Override
//    public void initData() {
//        initComponent();
//        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);
//        change = intent.getStringExtra("添加修改类型");
//        if (intent.getStringExtra("添加修改类型").equals("添加")) { //添加发货地址
//            CommonUtils.setTitleBar((Activity) mContext, true, "添加商品规格", "保存");
//        } else { //修改商品规格
//            position = intent.getStringExtra("position");
//            shangpinGuigeBean = (ShangpinGuigeBean) intent.getSerializableExtra("商品规格");
//            CommonUtils.setTitleBar((Activity) mContext, true, "修改商品规格", "保存");
//        }
//        if (isClothes) {
//            et_change_shangpin_guige_yanse.setText(shangpinGuigeBean.getYanse());
//            et_change_shangpin_guige_chicun.setText(shangpinGuigeBean.getChicun());
//        } else {
//            et_change_shangpin_guige_xiangxi.setText(shangpinGuigeBean.getXiangxi());
//        }
//        et_change_shangpin_guige_jiage.setText(shangpinGuigeBean.getGonghuojia());
//        et_change_shangpin_guige_kucun.setText(shangpinGuigeBean.getKucun());
//        et_change_shangpin_guige_jianyijia.setText(shangpinGuigeBean.getJianyijia());
//    }
//
//    public void initComponent() {
//        if (isClothes) {
//            et_change_shangpin_guige_yanse = (EditText) findViewById(R.id.et_change_shangpin_guige_yanse);
//            et_change_shangpin_guige_chicun = (EditText) findViewById(R.id.et_change_shangpin_guige_chicun);
//            et_change_shangpin_guige_chicun.setOnFocusChangeListener(this);
//        } else {
//            et_change_shangpin_guige_xiangxi = (EditText) findViewById(R.id.et_change_shangpin_guige_xiangxi);
//        }
//        et_change_shangpin_guige_jiage = (EditText) findViewById(R.id.et_change_shangpin_guige_jiage);
//        et_change_shangpin_guige_kucun = (EditText) findViewById(R.id.et_change_shangpin_guige_kucun);
//        et_change_shangpin_guige_jianyijia = (EditText) findViewById(R.id.tv_change_shangpin_guige_jianyijia);
//
//        et_change_shangpin_guige_jiage.setOnFocusChangeListener(this);
//        et_change_shangpin_guige_jianyijia.setOnFocusChangeListener(this);
//        et_change_shangpin_guige_jianyijia.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                //小数点后位数控制
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
//                        et_change_shangpin_guige_jianyijia.setText(s);
//                        et_change_shangpin_guige_jianyijia.setSelection(s.length());
//                    }
//                }
//                //第一位为点
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    et_change_shangpin_guige_jianyijia.setText(s);
//                    et_change_shangpin_guige_jianyijia.setSelection(2);
//                }
//
//                //第一位为零，后面只能输入点
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        et_change_shangpin_guige_jianyijia.setText(s.subSequence(0, 1));
//                        et_change_shangpin_guige_jianyijia.setSelection(1);
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        et_change_shangpin_guige_jiage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                //小数点后位数控制
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
//                        et_change_shangpin_guige_jiage.setText(s);
//                        et_change_shangpin_guige_jiage.setSelection(s.length());
//                    }
//                }
//                //第一位为点
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    et_change_shangpin_guige_jiage.setText(s);
//                    et_change_shangpin_guige_jiage.setSelection(2);
//                }
//                //第一位为零，后面只能输入点
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        et_change_shangpin_guige_jiage.setText(s.subSequence(0, 1));
//                        et_change_shangpin_guige_jiage.setSelection(1);
//                        return;
//                    }
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_toptitle_right: //保存商品规格
//                if (change_shangpin_guige_yanzheng()) {
//                    if (isClothes) {
//                        shangpinGuigeBean.setYanse(et_change_shangpin_guige_yanse.getText().toString());
//                        shangpinGuigeBean.setChicun(et_change_shangpin_guige_chicun.getText().toString());
//                    } else {
//                        shangpinGuigeBean.setXiangxi(et_change_shangpin_guige_xiangxi.getText().toString());
//                    }
//                    shangpinGuigeBean.setJianyijia(et_change_shangpin_guige_jianyijia.getText().toString());
//                    shangpinGuigeBean.setGonghuojia(et_change_shangpin_guige_jiage.getText().toString());
//                    shangpinGuigeBean.setKucun(et_change_shangpin_guige_kucun.getText().toString());
//                    Intent intent = new Intent();
//                    if (change.equals("修改")) {
//                        Log.e("修改商品规格", "修改商品规格");
//                        intent.putExtra("添加修改类型", "修改");
//                        intent.putExtra("position", position);
//                    } else {
//                        Log.e("添加商品规格", "添加商品规格");
//                        intent.putExtra("添加修改类型", "添加");
//                    }
//                    intent.putExtra("商品规格", shangpinGuigeBean);
//                    //设置返回数据
//                    ChangeShangpinGuigeActivity1.this.setResult(1, intent);
//                    ChangeShangpinGuigeActivity1.this.finish();
//                }
//                break;
//        }
//    }
//
//
//    private boolean change_shangpin_guige_yanzheng() {
//        if (isClothes) {
//            if (et_change_shangpin_guige_yanse.getText().toString().equals("")) {
//                MToast.showToast(mContext, "请填写商品颜色");
//                return false;
//            }
//            if (et_change_shangpin_guige_chicun.getText().toString().equals("")) {
//                MToast.showToast(mContext, "请填写商品尺寸");
//                return false;
//            }
//        } else {
//            if (et_change_shangpin_guige_xiangxi.getText().toString().equals("")) {
//                MToast.showToast(mContext, "请填写商品规格");
//                return false;
//            }
//        }
//        if (et_change_shangpin_guige_jiage.getText().toString().equals("") || Double.parseDouble(et_change_shangpin_guige_jiage.getText().toString()) <= 0) {
//            MToast.showToast(mContext, "请填写供货价");
//            return false;
//        }
//        if (et_change_shangpin_guige_jianyijia.getText().toString().equals("") || Double.parseDouble(et_change_shangpin_guige_jiage.getText().toString()) <= 0) {
//            MToast.showToast(mContext, "请填写建议零售价");
//            return false;
//        }
//        if (et_change_shangpin_guige_kucun.getText().toString().equals("") || Double.parseDouble(et_change_shangpin_guige_kucun.getText().toString()) <= 0) {
//            MToast.showToast(mContext, "请填写商品库存");
//            return false;
//        }
//        if (et_change_shangpin_guige_kucun.getText().toString().substring(0, 1).equals("0")) {
//            MToast.showToast(mContext, "库存输入有误");
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        EditText editText1 = (EditText) v;
//        String s = editText1.getText().toString();
//        // 以小数点结尾，去掉小数点
//        if (!hasFocus && editText1.getText() != null && editText1.getText().toString().endsWith(".")) {
//            editText1.setText(s + "00");
//        } else if (!hasFocus && editText1.getText() != null && editText1.getText().toString().contains(".")) {
//            int num = s.indexOf(".");
//            if (s.length() - 1 - num < 2) {
//                editText1.setText(s + "0");
//            }
//        }
//    }
//
//}
