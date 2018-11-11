package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;


public class ShouyiTiquActivity extends BaseActivity implements View.OnClickListener {
    private String phone, zong = "";  //账号 总收益
    private Button btn_shouyi_tiqu_lijitiqu;
    private TextView tv_shouyi_tiqu_zongshouyi;
    private EditText et_shouyi_tiqu_zhuanchushouyi, et_shouyi_tiqu_mima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shouyi_tiqu;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar(this, true, "提取红利", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        btn_shouyi_tiqu_lijitiqu.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            zong = getIntent().getExtras().getString("zong");
        }
        tv_shouyi_tiqu_zongshouyi.setText(zong);
    }

    public void initComponent() {
        tv_shouyi_tiqu_zongshouyi = (TextView) findViewById(R.id.tv_shouyi_tiqu_zongshouyi);
        et_shouyi_tiqu_zhuanchushouyi = (EditText) findViewById(R.id.et_shouyi_tiqu_zhuanchushouyi);
        et_shouyi_tiqu_mima = (EditText) findViewById(R.id.et_shouyi_tiqu_mima);
        btn_shouyi_tiqu_lijitiqu = (Button) findViewById(R.id.btn_shouyi_tiqu_lijitiqu);

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_sjtq:
                Log.e("收益请求成功", response.toString());
                DialogUtlis.oneBtnNormal(mContext, "提取成功", "知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialogNoAnimator();
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shouyi_tiqu_lijitiqu:
                if (pdshouyitiqu()) {
                    requestHandleArrayList.add(requestAction.shop_sj_sjtq(ShouyiTiquActivity.this,
                            phone,
                            et_shouyi_tiqu_zhuanchushouyi.getText().toString(),
                            et_shouyi_tiqu_mima.getText().toString()));
                }
                break;
        }
    }

    private Boolean pdshouyitiqu() {
        if (et_shouyi_tiqu_zhuanchushouyi.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "提取金额不能为空");
            return false;
        } else if (Integer.parseInt(et_shouyi_tiqu_zhuanchushouyi.getText().toString()) < 100 ||
                Integer.parseInt(et_shouyi_tiqu_zhuanchushouyi.getText().toString()) % 100 != 0) {
            DialogUtlis.oneBtnNormal(mContext, "提取金额非100的倍数");
            return false;
        } else if (et_shouyi_tiqu_mima.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请输入密码");
            return false;
        } else if (et_shouyi_tiqu_mima.getText().toString().length() < 6) {
            DialogUtlis.oneBtnNormal(mContext, "密码不能小于6位");
            return false;
        } else {
            return true;
        }
    }
}
