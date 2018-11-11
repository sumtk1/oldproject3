package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.app.Activity;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by hygo03 on 2016/12/2.
 */

public class RenZhengBiaoZhunXiZe_SonActivity extends BaseActivity {

    @Bind(R.id.tv_renzhengbiaozhun_xize)
    TextView tv_renzhengbiaozhun_xize;
    private String phone;

    @Override
    public int initResource() {
        return R.layout.activity_renzhengbiaozhunxize_son;
    }

    @Override
    public void initData() {
        String biaoti = getIntent().getStringExtra("标题");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        CommonUtils.setTitleBar((Activity) mContext, true, biaoti, "");
        requestHandleArrayList.add(requestAction.renZhengBiaoZhun(this, phone, biaoti));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_dianpu_renzheng_xize:
                if (response.getString("状态").equals("成功")) {
                    String neirong = response.getString("说明");
                    String n = neirong.replace("/n", "");
                    tv_renzhengbiaozhun_xize.setText(n);
                }
                break;
        }
    }

}
