package com.gloiot.hygoSupply.ui.activity.dingdanguanli;


import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 拒绝申请
 */
public class JuJueShenQingActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.te_shangpin_explain)
    EditText teShangpinExplain;
    @Bind(R.id.rl_shangpin_cause)
    RelativeLayout rl_shangpin_cause;
    @Bind(R.id.tv_suggestion_num)
    TextView tv_suggestion_num;
    @Bind(R.id.tv_suggestion_sum)
    TextView tv_suggestion_sum;
    private List<String> spinnerList = new ArrayList<>();
    private Spinner spinnerJujue;
    private ArrayAdapter<String> jujueAdapter;
    private String jujue_cause, dingdanID, zhanghao, type;
    private CharSequence tt;
    private int num = 200;
    private int selectionStart;
    private int selectionEnd;
    private String shangpinId;
    private String id;

    @Override
    public int initResource() {
        return R.layout.activity_jujueshenqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "拒绝申请", "");
        rl_shangpin_cause.setOnClickListener(this);
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        Intent intent = getIntent();
        dingdanID = intent.getStringExtra("dingdanID");
        shangpinId = intent.getStringExtra("shangpinId");
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        requestHandleArrayList.add(requestAction.DDoS(JuJueShenQingActivity.this, zhanghao));
        spinnerJujue = (Spinner) findViewById(R.id.spinner_jujue);
        teShangpinExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tt = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_suggestion_num.setText(editable.length() + "");
                selectionStart = teShangpinExplain.getSelectionStart();
                selectionEnd = teShangpinExplain.getSelectionEnd();
                if (tt.length() > num) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    teShangpinExplain.setText(editable);
                    teShangpinExplain.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @OnClick({R.id.but_yuanying_tijiao})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_yuanying_tijiao://提交
                String shuoming = teShangpinExplain.getText().toString().trim();
                if (TextUtils.isEmpty(shuoming)) {
                    MToast.showToast("请输入拒绝说明");
                } else {
                    if (type.equals("仅退款")) {
                        requestHandleArrayList.add(requestAction.JuJueYuanYinTiJiao(JuJueShenQingActivity.this, zhanghao, shuoming, jujue_cause, dingdanID, "仅退款", shangpinId,id));
                    } else if (type.equals("等待商家处理换货")) {
                        requestHandleArrayList.add(requestAction.JuJueYuanYinTiJiao(JuJueShenQingActivity.this, zhanghao, shuoming, jujue_cause, dingdanID, "换货", shangpinId,id));
                    } else {
                        requestHandleArrayList.add(requestAction.JuJueYuanYinTiJiao(JuJueShenQingActivity.this, zhanghao, shuoming, jujue_cause, dingdanID, "退款退货", shangpinId,id));
                    }
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_reason:
                if (response.getString("状态").equals("成功")) {
                    Log.e("--运行--", "到这里1");
                    Log.e("--TAG_shop_wl_reason--", response + "");
                    JSONArray jsonArray = response.getJSONArray("列表");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String cause = jsonObject.getString("拒绝原因");
                        spinnerList.add(cause);
                    }
                    loadDataForFreight();
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
            case ConstantUtils.TAG_shop_wl_Refuse:
                Intent intent = new Intent();
                String shuoming = teShangpinExplain.getText().toString().trim();
                intent.putExtra("type", response.getString("状态"));
                intent.putExtra("shuoming", shuoming);
                intent.putExtra("yuanyin", jujue_cause);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }


    /**
     * 拒绝原因适配器
     */
    private void loadDataForFreight() {
        jujueAdapter = new ArrayAdapter<>(this, R.layout.spinner_jujuejiemian, R.id.tv_shangpin_shangchuan1_txtvwSpinner1, spinnerList);
        jujueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style1);
        spinnerJujue.setAdapter(jujueAdapter);
        spinnerJujue.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jujue_cause = jujueAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
//                /*下拉菜单弹出的内容选项触屏事件处理*/
//        spinnerJujue.setOnTouchListener(new Spinner.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return false;
//            }
//
//        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        spinnerJujue.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

}
