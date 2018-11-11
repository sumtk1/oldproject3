package com.gloiot.hygoSupply.ui.activity.wode.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.ui.activity.login.ZhifumimaActivity;
import com.gloiot.hygoSupply.ui.activity.wode.GuanYuWoMenActivity;
import com.gloiot.hygoSupply.ui.activity.wode.setting.zhanghuyuanquan.AnquanShezhiActivity1;
import com.gloiot.hygoSupply.ui.activity.wode.tuihuodizhi.TuiHuoDiZhiActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.CacheManagerUtils;
import com.zyd.wlwsdk.utlis.MToast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MySettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rl_setting_gerenxinxi)
    RelativeLayout rl_setting_gerenxinxi;
    @Bind(R.id.rl_setting_anquanshezhi)
    RelativeLayout rl_setting_anquanshezhi;
    @Bind(R.id.tv_setting_huancun)
    TextView tv_setting_huancun;
    @Bind(R.id.rl_setting_qingchuhuancun)
    RelativeLayout rl_setting_qingchuhuancun;
    @Bind(R.id.btn_setting_loginout)
    Button btn_setting_loginout;
    @Bind(R.id.btn_setting_SwitchAccount)
    Button btn_setting_SwitchAccount;
    @Bind(R.id.rl_setting_zhifumima)
    RelativeLayout rlSettingZhifumima;
    @Bind(R.id.rl_setting_guanyuwomen)
    RelativeLayout rlSettingGuanyuwomen;


    @Override
    public int initResource() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {

        CommonUtils.setTitleBar((Activity) mContext, true, "设置", "");
        if ("是".equals(preferences.getString(ConstantUtils.SP_MYPWD, ""))) {
            rlSettingZhifumima.setVisibility(View.GONE);
        } else {
            rlSettingZhifumima.setVisibility(View.VISIBLE);
        }
        rl_setting_gerenxinxi.setOnClickListener(this);
        rl_setting_anquanshezhi.setOnClickListener(this);
        rl_setting_qingchuhuancun.setOnClickListener(this);
        btn_setting_loginout.setOnClickListener(this);
        btn_setting_SwitchAccount.setOnClickListener(this);
        rlSettingZhifumima.setOnClickListener(this);
        rlSettingGuanyuwomen.setOnClickListener(this);
        try {
            Log.e("缓存大小", CacheManagerUtils.getTotalCacheSize(mContext));
            tv_setting_huancun.setText(CacheManagerUtils.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_gerenxinxi: //设置退货地址
                startActivity(new Intent(mContext, TuiHuoDiZhiActivity.class));
                break;
            case R.id.rl_setting_zhifumima: //设置支付密码
                startActivity(new Intent(mContext, ZhifumimaActivity.class));
                break;
            case R.id.rl_setting_anquanshezhi: //进入账户与安全
                startActivity(new Intent(mContext, AnquanShezhiActivity1.class));
                break;
            case R.id.rl_setting_qingchuhuancun: //清除缓存
                Log.e("清理缓存", "清理缓存");
                CacheManagerUtils.clearAllCache(mContext);
                try {
                    Log.e("清理缓存后大小", CacheManagerUtils.getTotalCacheSize(mContext));
                    tv_setting_huancun.setText(CacheManagerUtils.getTotalCacheSize(mContext));
                    MToast.showToast("缓存清理完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_setting_loginout: //退出登录
                Log.e("TAG", "onClick:点击事件触发 ");
                try {
                    DialogUtlis.twoBtnNormal(mContext, "确定退出登录？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            SocketListener.getInstance().signoutRenZheng();
                            IMDBManager.getInstance(mContext).ClearnData();
                            editor.putString(ConstantUtils.SP_LOGINTYPE, "退出");
                            editor.commit();
//                          App.getInstance().setHasLogin(false);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    SocketListener.getInstance().signoutRenZheng();
                    IMDBManager.getInstance(mContext).ClearnData();
                    editor.putString(ConstantUtils.SP_LOGINTYPE, "退出");
                    editor.commit();
                }
                break;
            case R.id.rl_setting_guanyuwomen: //关于我们
                startActivity(new Intent(mContext, GuanYuWoMenActivity.class));
                break;
            case R.id.btn_setting_SwitchAccount: //切换账号
                startActivity(new Intent(MySettingActivity.this, ZhangHaoQieHuanActivity.class));
                finish();
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
