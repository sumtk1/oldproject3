package com.gloiot.hygoSupply.ui.activity.wode;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.PopWindUtil;
import com.gloiot.hygoSupply.utlis.ToolUtil;
import com.gloiot.hygoSupply.utlis.VersionManagerUtil;
import com.tencent.bugly.beta.Beta;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 关于
 * <p>
 * Created by zhaoxl on 2017/2/17.
 */

public class GuanYuWoMenActivity extends BaseActivity implements View.OnClickListener {
    private WebView rmzx_webview;
    private String zhanghao;
    private ImageView gywm_2dcode_img;
    private ImageView icon_img;
    private TextView gywm_banben_tv;
    String local_version;
    String server_version = "1.0.0";
    String update_content;
    Dialog updateDialog;
    PopWindUtil mPopWindUtil = null;
    String erweima = "";

    @Override
    public int initResource() {
        return R.layout.activity_guanyu_women;
    }

    @Override
    public void initData() {
        initComponent();
//      CommonUtils.setTitleBarAndRightImg((Activity) mContext, true, "关于", "", R.mipmap.ic_launcher);
        CommonUtils.setTitleBar((Activity) mContext, true, "关于我们", "");
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        local_version = VersionManagerUtil.getVersion(this);
//      ShareSDK.initSDK(this);
        requestHandleArrayList.add(requestAction.shop_wl_my(GuanYuWoMenActivity.this, zhanghao));
        String iconUri = "drawable://" + R.mipmap.ic_app_icon;
        CommonUtils.setDisplayImageOptions(icon_img, iconUri, 8);
    }

    public void initComponent() {
        findViewById(R.id.gywm_gengxin).setOnClickListener(this);
        findViewById(R.id.gywm_shiyongbangzhu).setOnClickListener(this);
        gywm_2dcode_img = (ImageView) findViewById(R.id.gywm_2dcode_img);
        gywm_banben_tv = (TextView) findViewById(R.id.gywm_banben_tv);
        icon_img = (ImageView) findViewById(R.id.gywm_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toptitle_right_img: //标题右边图片的点击事件
                Log.e("onclick", "标题右边图片的点击事件");
                mPopWindUtil = PopWindUtil.getInstance();
                mPopWindUtil.showSharePopWind(this);
                break;
            case R.id.gywm_gengxin: //检测更新
//                int versionComparison = VersionManagerUtil.VersionComparison(server_version, local_version);
//                if (versionComparison == 1) {
//                    updateDialog = ToolUtil.showUpdateVersion(this, server_version, update_content);
//                } else {
//                    Display display = getWindowManager().getDefaultDisplay();
//                    // 获取屏幕高度
//                    int height = display.getHeight();
//                    MToastUtils.showToast("您使用的已是最新版本", Gravity.CENTER, height / 10);
//                }
                Beta.checkUpgrade(true, false);
                break;
            case R.id.version_cancle_txt: //稍后更新
                if (updateDialog != null) {
                    updateDialog.dismiss();
                }
                break;
            case R.id.version_sure_txt: //立即更新
                if (updateDialog != null) {
                    updateDialog.dismiss();
                }
                break;
            case R.id.gywm_shiyongbangzhu: //使用帮助
                MToastUtils.showToast("敬请期待");
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_my: //{"状态":"成功","版本号":"","二维码":"","使用帮助":""}
                server_version = response.getString("版本号");
                erweima = response.getString("二维码");
                update_content = response.getString("使用帮助");
                CommonUtils.setDisplayImageOptions(gywm_2dcode_img, erweima, 0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPopWindUtil != null) {
            mPopWindUtil.closePopWind();
        }
    }
}
