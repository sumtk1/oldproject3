package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

public class DianpuGuanliActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_dianpu_guanli_tubiao)
    ImageView iv_dianpu_guanli_tubiao;
    @Bind(R.id.tv_dianpu_guanli_mingcheng)
    TextView tv_dianpu_mingcheng;
    @Bind(R.id.iv_renzheng_go)
    ImageView iv_renzheng_go;
    @Bind(R.id.tv_dianpu_guanli_renzheng)
    TextView tv_renzheng_zhuangtai;
    @Bind(R.id.tv_dianpu_guanli_shuoming)
    TextView tv_dianpu_shuoming;
    private String phone;
    private String dianpumingcheng = "";
    private String renzhengzhuangtai = "";
    private String shuoming;
    private String iconPath;

    @Override
    public int initResource() {
        return R.layout.activity_dianpu_guanli;
    }


    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "店铺管理", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        iv_renzheng_go.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_s_storeM(DianpuGuanliActivity.this, phone));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_s_storeM:
                dianpumingcheng = response.getString("店铺名称");
                renzhengzhuangtai = response.getString("店铺验证");

                if (renzhengzhuangtai.equals("未认证") && !renzhengzhuangtai.equals("认证失败")) {
                    iv_renzheng_go.setVisibility(View.INVISIBLE);
                } else {
                    iv_renzheng_go.setVisibility(View.VISIBLE);
                }
                tv_dianpu_mingcheng.setText(dianpumingcheng);
                iconPath = response.getString("店铺图标");
                tv_renzheng_zhuangtai.setText(renzhengzhuangtai);

                CommonUtils.setDisplayImageOptions(iv_dianpu_guanli_tubiao, iconPath, 4);
                shuoming = response.getString("店铺说明");
                tv_dianpu_shuoming.setText(shuoming);
                break;
        }
    }


    @OnClick({R.id.rl_dianpurenzheng_zhuantai, R.id.iv_dianpu_guanli_tubiao})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_dianpurenzheng_zhuantai:
                if (renzhengzhuangtai.equals("未认证") || renzhengzhuangtai.equals("认证失败")) {
                    startActivity(new Intent(DianpuGuanliActivity.this, DianPuRenZhengActivity.class));
                }
                break;
            case R.id.iv_dianpu_guanli_tubiao:
                Intent intent = new Intent(DianpuGuanliActivity.this, WodeDianpuActivity.class);
                startActivity(intent);
                break;
        }
    }

}
