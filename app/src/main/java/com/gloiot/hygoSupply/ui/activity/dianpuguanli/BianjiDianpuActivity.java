package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.yunyinghuodong.ManEBaoYouActivity;
import com.gloiot.hygoSupply.ui.activity.yunyinghuodong.YouHuiQuanActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.ViewUtil;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class BianjiDianpuActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_bianji_dianpu_icon)
    ImageView iv_bianji_dianpu_icon;
    @Bind(R.id.et_bianji_dianpu_name)
    EditText et_bianji_dianpu_name;
    @Bind(R.id.tv_bianji_dianpu_zhuyingleibie)
    TextView tv_bianji_dianpu_zhuyingleibie;
    @Bind(R.id.et_bianji_dianpu_jieshao)
    EditText et_bianji_dianpu_jieshao;
    @Bind(R.id.iv_bianji_dianpu_dianzhao)
    ImageView iv_bianji_dianpu_dianzhao;
    @Bind(R.id.btn_bianji_dianpu_wancheng)
    Button btn_bianji_dianpu_wancheng;
    @Bind(R.id.rl_bianji_dinapu_leimu)
    RelativeLayout rl_bianji_dianpu_leimu;
    @Bind(R.id.sp_bianji_dianpu_zhuyingleimu)
    Spinner sp_bianji_dianpu_leimu;


    private String icon = "", dianpu_mingcheng, dianzhao = "", phone, type, leimu, zhuiying_leibei;
    private List<String> spinnerList;
    private Map<String, String> leimuInfo;
    String title_type = "店铺编辑";

    @Override
    public int initResource() {
        return R.layout.activity_bianji_dianpu;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("类型");
        if (type.equals("修改")) {
            title_type = "店铺编辑";
            dianpu_mingcheng = intent.getStringExtra("dianpu_mingcheng");
            icon = intent.getStringExtra("dianpu_tubiao");
            dianzhao = intent.getStringExtra("dianzhao_tupian");
            zhuiying_leibei = intent.getStringExtra("zhuying_leibie");
            et_bianji_dianpu_name.setText(dianpu_mingcheng);
            if (intent.getStringExtra("dianpujieshao").equals("暂无数据")) {
                et_bianji_dianpu_jieshao.setText("");
            } else {
                et_bianji_dianpu_jieshao.setText(intent.getStringExtra("dianpujieshao"));
            }
            CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_icon, icon, 4);
            CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_dianzhao, dianzhao, 4);
            sp_bianji_dianpu_leimu.setVisibility(View.GONE);
            tv_bianji_dianpu_zhuyingleibie.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(zhuiying_leibei)) {
                zhuiying_leibei = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, "");
            }
            if ("是".equals(preferences.getString(ConstantUtils.SP_ZIYING, ""))) {
                tv_bianji_dianpu_zhuyingleibie.setText("全部类别");
            } else {
                tv_bianji_dianpu_zhuyingleibie.setText(zhuiying_leibei);
            }
            btn_bianji_dianpu_wancheng.setText("确认");
        } else if (type.equals("添加类目")) {
            title_type = "店铺编辑";
            spinnerList = new ArrayList<>();
            leimuInfo = new HashMap<>();
            dianpu_mingcheng = intent.getStringExtra("dianpu_mingcheng");
            icon = intent.getStringExtra("dianpu_tubiao");
            dianzhao = intent.getStringExtra("dianzhao_tupian");
            et_bianji_dianpu_name.setText(dianpu_mingcheng);
            et_bianji_dianpu_jieshao.setText(intent.getStringExtra("dianpujieshao"));
            CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_icon, icon, 4);
            CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_dianzhao, dianzhao, 4);
            sp_bianji_dianpu_leimu.setVisibility(View.VISIBLE);
            tv_bianji_dianpu_zhuyingleibie.setVisibility(View.GONE);
            requestHandleArrayList.add(requestAction.getLeimu(this));
            btn_bianji_dianpu_wancheng.setText("完成");
        } else {
            title_type = "创建店铺";
            spinnerList = new ArrayList<>();
            leimuInfo = new HashMap<>();
            sp_bianji_dianpu_leimu.setVisibility(View.VISIBLE);
            tv_bianji_dianpu_zhuyingleibie.setVisibility(View.GONE);
            requestHandleArrayList.add(requestAction.getLeimu(this));
            btn_bianji_dianpu_wancheng.setText("创建店铺");
        }
        CommonUtils.setTitleBar((Activity) mContext, true, title_type, "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        btn_bianji_dianpu_wancheng.setOnClickListener(this);
        iv_bianji_dianpu_icon.setOnClickListener(this);
        iv_bianji_dianpu_dianzhao.setOnClickListener(this);
        et_bianji_dianpu_jieshao.addTextChangedListener(new MaxLengthWatcher(20, et_bianji_dianpu_jieshao));
        et_bianji_dianpu_name.addTextChangedListener(new MaxLengthWatcher(10, et_bianji_dianpu_name));
    }


    @OnClick({R.id.btn_bianji_dianpu_wancheng, R.id.iv_bianji_dianpu_icon, R.id.iv_bianji_dianpu_dianzhao
             })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bianji_dianpu_wancheng:
                if (bianjichekc()) {
                    String dianpuID = preferences.getString(ConstantUtils.SP_DIANPUID, "");
                    if (type.equals("修改")) {
                        Log.e("修改店铺按钮", "修改店铺按钮");
                        requestHandleArrayList.add(requestAction.shop_sj_bianji(BianjiDianpuActivity.this, phone, et_bianji_dianpu_name.getText().toString(),
                                icon, dianzhao, zhuiying_leibei, et_bianji_dianpu_jieshao.getText().toString(), dianpuID));
                    } else if (type.equals("添加类目")) {
                        requestHandleArrayList.add(requestAction.shop_sj_bianji(BianjiDianpuActivity.this, phone, et_bianji_dianpu_name.getText().toString(),
                                icon, dianzhao, leimu, et_bianji_dianpu_jieshao.getText().toString(), dianpuID));
                    } else {
                        Log.e("编辑店铺按钮", "编辑店铺按钮," + "店招图片：" + dianzhao);
                        requestHandleArrayList.add(requestAction.shop_wl_newshop(BianjiDianpuActivity.this, phone, et_bianji_dianpu_name.getText().toString(), dianzhao, icon, leimu, et_bianji_dianpu_jieshao.getText().toString()));
                    }
                }
                break;
            case R.id.iv_bianji_dianpu_icon:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new ChoosePhoto(mContext) {
                                    @Override
                                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                                        BianjiDianpuActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_icon, myPicUrl, 4);
                                                icon = myPicUrl;
                                                Log.e("店铺图标添加成功", "图片添加成功" + icon);
                                            }
                                        });
                                    }

                                    @Override
                                    protected void setPicFailure() {
                                        //设置头像失败
                                        MToast.showToast("图片上传失败,请重新设置");
                                    }
                                }.setAspectXY(1, 1).setPic(false, true);
                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA);
                    }
                }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.iv_bianji_dianpu_dianzhao:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new ChoosePhoto(mContext) {
                                    @Override
                                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                                        BianjiDianpuActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CommonUtils.setDisplayImageOptions(iv_bianji_dianpu_dianzhao, myPicUrl, 4);
                                                dianzhao = myPicUrl;
                                                Log.e("店招图片添加成功", "图片添加成功" + dianzhao);
                                            }
                                        });
                                    }

                                    @Override
                                    protected void setPicFailure() {
                                        //设置图片失败
                                        MToast.showToast("图片上传失败,请重新设置");
                                    }
                                }.setPic(false, false); //是否是头像

                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA);
                    }
                }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_newshop:
                ConstantUtils.SP_CHANGEDIANPU = true;
                editor.putString(ConstantUtils.SP_ZHUYINGLEIBIE, leimu);
                editor.putString(ConstantUtils.SP_ZHUYINGLEIBIEID, leimuInfo.get(leimu));
                editor.putString(ConstantUtils.SP_YOUWUDIANPU, response.getString("店铺状态"));
                editor.putString(ConstantUtils.SP_DIANPUID, response.getString("店铺id"));
                editor.commit();
                DialogUtlis.twoBtnNormal(mContext, "店铺已创建成功，是否立即进行店铺认证？", "提示", false, "取消", "立即认证", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialogNoAnimator();
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialog();
                        startActivity(new Intent(mContext, DianPuRenZhengActivity.class));
                    }
                });
                break;
            case ConstantUtils.TAG_shop_sj_bianji:
                if (type.equals("添加类目")) {
                    editor.putString(ConstantUtils.SP_ZHUYINGLEIBIE, leimu);
                    editor.putString(ConstantUtils.SP_ZHUYINGLEIBIEID, leimuInfo.get(leimu));
                    editor.commit();
                }
                ConstantUtils.SP_CHANGEDIANPU = true;
                this.finish();
                break;
            case ConstantUtils.TAG_shop_s_uplm:
                JSONArray array = response.getJSONArray("一级列表");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String id = object.getString("一级id");
                    String leimu = object.getString("一级");
                    leimuInfo.put(leimu, id);
                    spinnerList.add(leimu);
                }
                loadDataForSpinner();
                break;
        }
    }

    private boolean bianjichekc() {
        if (TextUtils.isEmpty(icon)) {
            DialogUtlis.oneBtnNormal(mContext, "请设置店铺图标");
            return false;
        }
        if (TextUtils.isEmpty(et_bianji_dianpu_name.getText().toString())) {
            DialogUtlis.oneBtnNormal(mContext, "请设置店铺名称");
            return false;
        }
        if (et_bianji_dianpu_name.getText().toString().replace(" ", "").length() == 0) {
            DialogUtlis.oneBtnNormal(mContext, "请设置店铺名称");
            return false;
        }

        if (!type.equals("修改")) {
            if (rl_bianji_dianpu_leimu.getVisibility() == View.VISIBLE && TextUtils.isEmpty(leimu)) {
                DialogUtlis.oneBtnNormal(mContext, "请设置主营类目");
                return false;
            }
        }
        if (TextUtils.isEmpty(dianzhao)) {
            DialogUtlis.oneBtnNormal(mContext, "请设置店招图片");
            return false;
        }
        if (et_bianji_dianpu_name.getText().toString().length() > 10) {
            DialogUtlis.oneBtnNormal(mContext, "店铺名称不能超过十个字");
            return false;
        }
        if (et_bianji_dianpu_jieshao.getText().toString().isEmpty()) {
            DialogUtlis.oneBtnNormal(mContext, "店铺介绍不能为空");
            return false;
        }
        return true;
    }

    private void loadDataForSpinner() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, spinnerList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_bianji_dianpu_leimu.setAdapter(myAdapter);
        sp_bianji_dianpu_leimu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //拿到被选择项的值
                leimu = (String) sp_bianji_dianpu_leimu.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    public class MaxLengthWatcher implements TextWatcher {

        private int maxLen = 0;
        private EditText editText = null;


        public MaxLengthWatcher(int maxLen, EditText editText) {
            this.maxLen = maxLen;
            this.editText = editText;
        }

        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            Editable editable = editText.getText();
            int len = editable.length();

            if (len > maxLen) {
                MToastUtils.showToast(mContext, "超出字数，只能输入" + maxLen + "位");
                int selEndIndex = Selection.getSelectionEnd(editable);
                String str = editable.toString();
                //截取新字符串
                String newStr = str.substring(0, maxLen);
                editText.setText(newStr);
                editable = editText.getText();

                //新字符串的长度
                int newLen = editable.length();
                //旧光标位置超过字符串长度
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                //设置新光标所在的位置
                Selection.setSelection(editable, selEndIndex);

            }
        }
    }


}
