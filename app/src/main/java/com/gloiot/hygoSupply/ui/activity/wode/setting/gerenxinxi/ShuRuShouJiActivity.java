package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.MToast;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;


/**
 * Created by moxx on 2017/2/15.
 */

public class ShuRuShouJiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_zhanghao)
    EditText et_zhanghao;
    @Bind(R.id.btn_queren)
    Button btn_queren;

    private String shoujihao;

    @Override
    public int initResource() {
        return R.layout.activity_shuru_shouji;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "设置新手机号", "");
        btn_queren.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_queren:
                shoujihao = et_zhanghao.getText().toString();
                if (shoujihao.isEmpty()) {
                    MToast.showToast("请输入新手机号");
                } else if (shoujihao.length() != 11) {
                    MToast.showToast("输入手机号有误");
                } else {
                    requestHandleArrayList.add(requestAction.shop_set_tel(this, preferences.getString(ConstantUtils.SP_MYID, ""), shoujihao));
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_set_tel:
                if ("成功".equals(response.getString("状态"))) {
                    MToast.showToast("成功设置新手机号");
                    editor.putString(ConstantUtils.SP_USERPHONE, shoujihao);
                    editor.commit();
                    finish();
                }
                break;
        }
    }
}
