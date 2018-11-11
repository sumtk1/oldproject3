package com.gloiot.hygoSupply.ui.activity.wode.shoukuan;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 超级商家收款
 */

public class WoYaoShouKuanActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_shoukuan_icon)
    ImageView ivShoukuanIcon;
    @Bind(R.id.tv_shoukuan_nick)
    TextView tvShoukuanNick;
    @Bind(R.id.iv_shoukuan_qrcode)
    ImageView ivShoukuanQrcode;
    @Bind(R.id.rl_shoukuan_jilu)
    RelativeLayout rlShoukuanJilu;


    @Override
    public int initResource() {
        return R.layout.activity_woyaoshoukuan;
    }

    @Override
    public void initData() {
        //设置头像
        CommonUtils.setDisplayImageOptions(ivShoukuanIcon, preferences.getString(ConstantUtils.SP_USERIMG, ""), 20);
        //设置昵称
        tvShoukuanNick.setText(preferences.getString(ConstantUtils.SP_USERNICHENG, ""));
        //二维码
        ivShoukuanQrcode.setImageBitmap(CodeUtils.createImage(ConstantUtils.SUPERMERCHANT_URL + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "")+ "&AppType=Self", 400, 400, null));


    }

    @OnClick({R.id.toptitle_back, R.id.rl_shoukuan_jilu})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toptitle_back:
                finish();
                break;
            case R.id.rl_shoukuan_jilu:
                Intent intent = new Intent(WoYaoShouKuanActivity.this, ShouKuanJiLuActivity.class);
                startActivity(intent);
                break;
        }
    }

}
