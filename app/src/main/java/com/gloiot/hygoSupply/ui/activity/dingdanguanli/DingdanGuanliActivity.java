package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.dingdanfragment.DingdanGuanliFragment;
import com.gloiot.hygoSupply.ui.adapter.TabFragmentAdapter;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.zyd.wlwsdk.widge.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DingdanGuanliActivity extends BaseActivity {
    @Bind(R.id.tl_dingdan_gunli_head)
    TabLayout tl_dingdan_gunli_head;
    @Bind(R.id.vp_dingdan_gunli_page)
    NoScrollViewPager vp_dingdan_gunli_page;
    @Bind(R.id.tv_toptitle_right)
    TextView tvToptitleRight;

    //    private String[] titles = new String[]{"全部", "待付款", "待发货", "已发货", "已完成"};
    private String tuikuan = "退款/售后";
    private String yifahuo = "已发货";
    private String yiwancheng = "已完成";
    private String[] titles = new String[]{"全部", "待付款", "待发货", yifahuo, yiwancheng};

    @Override
    public int initResource() {
        return R.layout.activity_dingdan_guanli;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "订单管理", "筛选");
        tvToptitleRight.setVisibility(View.GONE);
        TabLayout();
        int select = getIntent().getIntExtra("select", 0);
        vp_dingdan_gunli_page.setCurrentItem(select);
        tl_dingdan_gunli_head.getTabAt(select).select();

    }

    private void TabLayout() {
        //设置TabLayout的模式
        tl_dingdan_gunli_head.setTabMode(TabLayout.MODE_SCROLLABLE);
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            DingdanGuanliFragment fragment = new DingdanGuanliFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text", titles[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
            tl_dingdan_gunli_head.addTab(tl_dingdan_gunli_head.newTab().setText(titles[i]));
        }
        vp_dingdan_gunli_page.setAdapter(new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this));
        vp_dingdan_gunli_page.setOffscreenPageLimit(titles.length - 1);
        tl_dingdan_gunli_head.setupWithViewPager(vp_dingdan_gunli_page);
        vp_dingdan_gunli_page.setNoScroll(true);
        tl_dingdan_gunli_head.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("待发货".equals(tab.getText().toString())) {
                    tvToptitleRight.setVisibility(View.VISIBLE);

                } else {
                    tvToptitleRight.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tvToptitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DingdanGuanliFragment fragment = (DingdanGuanliFragment) fragments.get(2);
                fragment.showTimeList();
            }
        });

    }

}
