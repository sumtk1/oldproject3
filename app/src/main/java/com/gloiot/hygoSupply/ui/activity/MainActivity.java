package com.gloiot.hygoSupply.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.socket.SocketServer;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.App;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.ShangpinGuanliActivity;
import com.gloiot.hygoSupply.ui.adapter.MyPagerAdapter;
import com.gloiot.hygoSupply.ui.fragment.LifeFragment;
import com.gloiot.hygoSupply.ui.fragment.Main01Fragment;
import com.gloiot.hygoSupply.ui.fragment.Main03Fragment;
import com.gloiot.hygoSupply.ui.fragment.SocialFragment;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.NetBroadcastReceiver;
import com.gloiot.hygoSupply.utlis.NetEvent;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity implements View.OnClickListener, NetEvent {
    @Bind(R.id.id_indicator_one_iv)
    ImageView id_indicator_one_iv;
    @Bind(R.id.id_indicator_one_tv)
    TextView id_indicator_one_tv;
    @Bind(R.id.id_indicator_one)
    LinearLayout id_indicator_one;
    @Bind(R.id.id_indicator_two_iv)
    ImageView id_indicator_two_iv;
    @Bind(R.id.id_indicator_two_tv)
    TextView id_indicator_two_tv;
    @Bind(R.id.id_indicator_two)
    LinearLayout id_indicator_two;
    @Bind(R.id.id_indicator_three_iv)
    ImageView id_indicator_three_iv;
    @Bind(R.id.id_indicator_three_tv)
    TextView id_indicator_three_tv;
    @Bind(R.id.id_indicator_three)
    LinearLayout id_indicator_three;
    @Bind(R.id.ll_main_bottom)
    LinearLayout ll_main_bottom;
    @Bind(R.id.id_viewpager)
    NoScrollViewPager id_viewpager;
    @Bind(R.id.id_indicator_social)
    LinearLayout idIndicatorSocial;
    @Bind(R.id.id_indicator_social_iv)
    ImageView idIndicatorSocialIv;
    @Bind(R.id.id_indicator_social_tv)
    TextView idIndicatorSocialTv;
    @Bind(R.id.rl_main_message)
    RelativeLayout rlMainMessage;
    @Bind(R.id.rl_main_message_num)
    RelativeLayout rlMainMessageNum;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private Boolean isExit = false;
    private SocialFragment socialFragment;
    private static boolean[] fragmentsUpdateFlag = {false, false, false, false};
    public static Handler mainHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //刷新界面
                    fragmentsUpdateFlag[1] = true;
                    fragmentsUpdateFlag[0] = true;
                    break;
            }
        }
    };

    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            IMDBManager.getInstance(mContext, preferences.getString(ConstantUtils.SP_MYID, ""));
            if (!TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_MYID, "")) &&
                    !TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_RANDOMCODE, ""))) {
                SocketListener.getInstance().staredData(preferences.getString(ConstantUtils.SP_MYID, "") + "_商家", preferences.getString(ConstantUtils.SP_RANDOMCODE, ""));
            }
            // 连接
            SocketServer.socketOnConnect();
        }catch (Exception e){
            Log.e("chat", e+"-");
        }

        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netBroadcastReceiver = new NetBroadcastReceiver();
        mContext.registerReceiver(netBroadcastReceiver, filter);
        //设置监听
        netBroadcastReceiver.setNetEvent(this);
        badge();
        BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                badge.setBadgeNumber(IMDBManager.getInstance(mContext).GetAllReadNum());
            }
        });
    }

    @Override
    public int initResource() {
        return R.layout.activity_main;
    }


    @Override
    public void initData() {

        setOverflowShowingAlways();
        mContext = this;
        Main01Fragment fragment01 = new Main01Fragment();
        LifeFragment fragment02 = new LifeFragment();
        Main03Fragment fragment03 = new Main03Fragment();
        socialFragment = new SocialFragment();
        mTabs.add(fragment01);
        mTabs.add(fragment02);
        mTabs.add(socialFragment);
        mTabs.add(fragment03);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mTabs, fragmentsUpdateFlag);
        id_viewpager.setAdapter(mAdapter);
        id_viewpager.setCurrentItem(0, false);
        id_viewpager.setOffscreenPageLimit(3);
        reSetIcon(1);
        id_indicator_one.setOnClickListener(this);
        id_indicator_two.setOnClickListener(this);
        id_indicator_three.setOnClickListener(this);
        idIndicatorSocial.setOnClickListener(this);
        requestHandleArrayList.add(requestAction.shop_wl_notice(MainActivity.this, preferences.getString(ConstantUtils.SP_MYID, "")));


    }

    private Badge badge;

    private void badge() {
        badge = new QBadgeView(mContext).bindTarget(rlMainMessageNum);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//        badge.setGravityOffset()
            badge.setGravityOffset(0, 0, false);
        badge.setBadgeBackgroundColor(Color.parseColor("#FF6D63"));
        badge.setBadgeTextSize(10, true);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, final Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                    IMDBManager.getInstance(mContext).CleanAllReadNum(new DataBaseCallBack() {
                        @Override
                        public void operationState(boolean flag) {
                            //将未读消息的条数置零
                            badge.setBadgeNumber(0);
                            socialFragment.refresh(); // 刷新聊天界面
                        }
                    });
                }
            }
        });
        badge.setBadgeNumber(IMDBManager.getInstance(mContext).GetAllReadNum());
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            MToast.showToast("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            App.getInstance().mActivityStack.AppExit();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    public void onClick(View v) {
//        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_one:
                reSetIcon(1);
                id_viewpager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two://生活
                reSetIcon(2);
                id_viewpager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_social://社交
                reSetIcon(3);
                id_viewpager.setCurrentItem(2, false);
                break;

            case R.id.id_indicator_three:
                reSetIcon(4);
                id_viewpager.setCurrentItem(3, false);
                break;

        }
    }

    private void reSetIcon(int i) {
        id_indicator_one_iv.setImageResource(R.mipmap.ic_shangjia);
        id_indicator_two_iv.setImageResource(R.mipmap.ic_life_normal);
        id_indicator_three_iv.setImageResource(R.mipmap.ic_wode);
        idIndicatorSocialIv.setImageResource(R.mipmap.ic_xiaoxi);
        id_indicator_one_tv.setTextColor(getResources().getColor(R.color.cl_999));
        id_indicator_two_tv.setTextColor(getResources().getColor(R.color.cl_999));
        id_indicator_three_tv.setTextColor(getResources().getColor(R.color.cl_999));
        idIndicatorSocialTv.setTextColor(getResources().getColor(R.color.cl_999));
        switch (i) {
            case 1:
                id_indicator_one_iv.setImageResource(R.mipmap.ic_shangjia_dianji);
                id_indicator_one_tv.setTextColor(getResources().getColor(R.color.cl_ff7f29));
                break;
            case 3:
                idIndicatorSocialIv.setImageResource(R.mipmap.ic_xiaoxi_diannji);
                idIndicatorSocialTv.setTextColor(getResources().getColor(R.color.cl_ff7f29));
                break;
            case 4:
                id_indicator_three_iv.setImageResource(R.mipmap.ic_wode_dianji);
                id_indicator_three_tv.setTextColor(getResources().getColor(R.color.cl_ff7f29));
                break;
            case 2:
                id_indicator_two_iv.setImageResource(R.mipmap.ic_life_click);
                id_indicator_two_tv.setTextColor(getResources().getColor(R.color.cl_ff7f29));
                break;

        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_notice:
                if (response.getString("状态").equals("成功")) {
                    if ("否".equals(response.getString("模版"))) {
                        DialogUtlis.InForm(mContext, response.getString("通知"), "立即设置", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogUtlis.dismissDialog();
                                Intent intent = new Intent(MainActivity.this, ShangpinGuanliActivity.class);
                                intent.putExtra("type", "soldOut");
                                startActivity(intent);
                            }
                        });

                    }
                } else {
                    MToast.showToast(response.getString("状态"));
                }

                break;
        }
    }

    @Override
    public void onNetChange(int netMobile) {
        switch (netMobile) {
            case 1://wifi
            case 0://移动数据
            case -1://没有网络
                BroadcastManager.getInstance(this).sendBroadcast("判断网络", netMobile + "");
                break;
        }
    }

    public void getBadge(){
        badge.setBadgeNumber(IMDBManager.getInstance(mContext).GetAllReadNum()); // 获取未读条数
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netBroadcastReceiver != null) {
            mContext.unregisterReceiver(netBroadcastReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int p = getIntent().getIntExtra("chatSelectFlag", -1);
        if (p != -1){
            reSetIcon(3);
            id_viewpager.setCurrentItem(2, false);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        badge.setBadgeNumber(IMDBManager.getInstance(mContext, preferences.getString(ConstantUtils.SP_USERPHONE, "")).GetAllReadNum()); // 获取未读条数
//        Log.e("main--onRestart", "21313");
    }
}

