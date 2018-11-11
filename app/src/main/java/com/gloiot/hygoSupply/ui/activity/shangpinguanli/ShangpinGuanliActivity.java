package com.gloiot.hygoSupply.ui.activity.shangpinguanli;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.ChuShouZhongFragment;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.ShenHeZhongFragment;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.YIXiaJiaFragment;
import com.gloiot.hygoSupply.ui.adapter.TabFragmentAdapter;
import com.gloiot.hygoSupply.utlis.BroadCastActionUtil;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShangpinGuanliActivity extends BaseActivity implements View.OnClickListener, YIXiaJiaFragment.YiXiaJiaFragmentBackCalled, ChuShouZhongFragment.ChuShouZhongFragmentBackCalled {
    @Bind(R.id.vp_shangpin_guanli_page)
    ViewPager vp_shangpin_guanli_page;
    @Bind(R.id.tv_twotile_1)
    TextView tv_twotile_1;
    @Bind(R.id.tv_twotile_2)
    TextView tv_twotile_2;
    @Bind(R.id.tv_twotile_3)
    TextView tv_twotile_3;
    @Bind(R.id.iv_twotitle_back)
    ImageView ivTwotitleBack;
    @Bind(R.id.iv_twotitle_right)
    ImageView ivTwotitleRight;
    @Bind(R.id.tv_twotile_right)
    TextView tvTwotileRight;

    private LinearLayout shangpin_guanli_bottom;
    private TextView bottom_right_txt;
    private ImageView selcted_img;
    private String[] titles = new String[]{"审核中", "出售中", "已下架"};

    public static boolean is_xiajia = true;
    public static boolean is_shangjia = true;
    private int currentFragment = 0;
    private TabFragmentAdapter fragmentAdapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_guanli;
    }

    @Override
    public void initData() {
        initComponent();
        currentFragment = 0;
        setTitleStyle();
        initFragment();
    }

    private void initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        Fragment f1 = new ShenHeZhongFragment();
        Fragment f2 = new ChuShouZhongFragment();
        Fragment f3 = new YIXiaJiaFragment();
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            Bundle bundle = new Bundle();
            bundle.putString("text", titles[i]);
            fragment.setArguments(bundle);
        }
        fragmentAdapter = new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this);
        vp_shangpin_guanli_page.setAdapter(fragmentAdapter);
        vp_shangpin_guanli_page.setOffscreenPageLimit(2);
        vp_shangpin_guanli_page.setCurrentItem(0);
    }

    public void initComponent() {
        bottom_right_txt = (TextView) findViewById(R.id.spgl_xiajia_txt);
        selcted_img = (ImageView) findViewById(R.id.spgl_quanxuan_img);
        shangpin_guanli_bottom = (LinearLayout) findViewById(R.id.shangpin_guanli_bottom);
        selcted_img.setOnClickListener(this);
        bottom_right_txt.setOnClickListener(this);
        tv_twotile_1.setText(titles[0]);
        tv_twotile_2.setText(titles[1]);
        tv_twotile_3.setText(titles[2]);
        shangpin_guanli_bottom.setVisibility(View.GONE);
        findViewById(R.id.rl_twotitle_all).setBackgroundColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
        ivTwotitleBack.setImageResource(R.mipmap.ic_titilebar_back);
    }

    @OnClick({R.id.tv_twotile_1, R.id.tv_twotile_2, R.id.tv_twotile_3, R.id.iv_twotitle_right, R.id.iv_twotitle_back, R.id.tv_twotile_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_twotitle_right: //界面头部右边按钮
            case R.id.tv_twotile_right: //界面头部右边按钮
                if (currentFragment == 2) {
                    if (is_shangjia) {
                        if (YIXiaJiaFragment.xiajiaNum <= 0) {
                            MToastUtils.showToast("暂无已下架商品");
                            return;
                        }
                        ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guangli_shangjia);
                        tvTwotileRight.setText("上架");
                        is_shangjia = false;
                        bottom_right_txt.setText("上架");
                        shangpin_guanli_bottom.setVisibility(View.VISIBLE);
                        sendBroast(BroadCastActionUtil.SHANGJIA_ACTION, "0");//"0"开始上架模式
                    } else {
                        ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guangli_shangjia);
                        tvTwotileRight.setText("上架");
                        is_shangjia = true;
                        shangpin_guanli_bottom.setVisibility(View.GONE);
                        sendBroast(BroadCastActionUtil.SHANGJIA_ACTION, "-1"); //"-1"取消上架模式
                    }
                } else if (currentFragment == 1) {
                    if (is_xiajia) {
                        if (ChuShouZhongFragment.xiaoshouNum <= 0) {
                            MToastUtils.showToast("请先上架商品");
                            return;
                        }
//                        ivTwotitleRight.setImageResource(R.mipmap.ic_product_cancle);
                        ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guanli_xiajia);
                        tvTwotileRight.setText("下架");
                        is_xiajia = false;
                        shangpin_guanli_bottom.setVisibility(View.VISIBLE);
                        bottom_right_txt.setText("下架");
                        sendBroast(BroadCastActionUtil.XIAJIA_ACTION, "0");//"0"开始下架模式
                    } else {
                        ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guanli_xiajia);
                        tvTwotileRight.setText("下架");
                        is_xiajia = true;
                        shangpin_guanli_bottom.setVisibility(View.GONE);
                        sendBroast(BroadCastActionUtil.XIAJIA_ACTION, "-1");//"-1"开始下架模式
                    }
                } else {
                    is_xiajia = true;
                    is_shangjia = true;
                }
                break;
            case R.id.spgl_quanxuan_img://界面底部左边图片按钮
                Intent intent = new Intent(BroadCastActionUtil.XUANZHONG_ACTION);
                intent.putExtra("fragment", currentFragment + "");
                sendBroadcast(intent);
                break;
            case R.id.spgl_xiajia_txt: //界面底部右边按钮
                if (currentFragment == 1) {
                    intent = new Intent(BroadCastActionUtil.XIAJIA_TIJIAO_ACTION);
                    intent.putExtra("fragment", currentFragment + "");
                    sendBroadcast(intent);
                } else if (currentFragment == 2) {
                    intent = new Intent(BroadCastActionUtil.SHANGJIA_TIJIAO_ACTION);
                    intent.putExtra("fragment", currentFragment + "");
                    sendBroadcast(intent);
                }
                break;
            case R.id.tv_twotile_1:
                currentFragment = 0;
                setTitleStyle();
                ivTwotitleRight.setVisibility(View.GONE);
                tvTwotileRight.setText("");
                shangpin_guanli_bottom.setVisibility(View.GONE);
                vp_shangpin_guanli_page.setCurrentItem(0);
                break;
            case R.id.tv_twotile_2:
                currentFragment = 1;
                setTitleStyle();
                ivTwotitleRight.setVisibility(View.VISIBLE);
                shangpin_guanli_bottom.setVisibility(View.GONE);
                tvTwotileRight.setText("下架");
                ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guanli_xiajia);
                vp_shangpin_guanli_page.setCurrentItem(1);
                break;
            case R.id.tv_twotile_3:
                currentFragment = 2;
                ivTwotitleRight.setVisibility(View.VISIBLE);
                shangpin_guanli_bottom.setVisibility(View.GONE);
                tvTwotileRight.setText("上架");
                ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guangli_shangjia);
                setTitleStyle();
                vp_shangpin_guanli_page.setCurrentItem(2);
                break;
            case R.id.iv_twotitle_back:
                finish();
                break;

        }
    }

    //点击title变色样式
    public void setTitleStyle() {
        switch (currentFragment) {
            case 0:
                tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
                tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_white3_shape_selected);
                tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_white3_shape_center);
                tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_white3_shape_right);
                break;
            case 1:
                tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
                tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_white3_shape);
                tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_white3_shape_center_selected);
                tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_white3_shape_right);
                break;
            case 2:
                tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_2.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
                tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
                tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_white3_shape);
                tv_twotile_2.setBackgroundResource(R.drawable.bg_tv_white3_shape_center);
                tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_white3_shape_right_selected);
                break;
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_toptitle_right: //界面头部右边按钮
//                if (currentFragment == 2) {
//                    if (is_shangjia) {
//                        if (YIXiaJiaFragment.xiajiaNum <= 0) {
//                            MToastUtils.showToast("暂无下架商品");
//                            return;
//                        }
//                        right_txt.setText("取消");
//                        is_shangjia = false;
//                        shangpin_guanli_bottom.setVisibility(View.VISIBLE);
//                        sendBroast(BroadCastActionUtil.SHANGJIA_ACTION, "0");//"0"开始上架模式
//                    } else {
//                        right_txt.setText("上架");
//                        is_shangjia = true;
//                        shangpin_guanli_bottom.setVisibility(View.GONE);
//                        sendBroast(BroadCastActionUtil.SHANGJIA_ACTION, "-1"); //"-1"取消上架模式
//                    }
//                } else if (currentFragment == 1) {
//                    if (is_xiajia) {
//                        if (ChuShouZhongFragment.xiaoshouNum <= 0) {
//                            MToastUtils.showToast("请先上架商品");
//                            return;
//                        }
//                        right_txt.setText("取消");
//                        is_xiajia = false;
//                        shangpin_guanli_bottom.setVisibility(View.VISIBLE);
//                        sendBroast(BroadCastActionUtil.XIAJIA_ACTION, "0");//"0"开始下架模式
//                    } else {
//                        right_txt.setText("下架");
//                        is_xiajia = true;
//                        shangpin_guanli_bottom.setVisibility(View.GONE);
//                        sendBroast(BroadCastActionUtil.XIAJIA_ACTION, "-1");//"-1"开始下架模式
//                    }
//                } else {
//                    is_xiajia = true;
//                    is_shangjia = true;
//                    right_txt.setText("");
//                }
//                break;
//            case R.id.spgl_quanxuan_img://界面底部左边图片按钮
////                if (allShangpinGuanli.size() == xuanzhongArray.size()) { //当选中的数目等于总数据就全部取消选中，否则全选
////                    xuan_zhuangtai(0);
////                } else {
////                    xuan_zhuangtai(1);
////                }
////                if (adapter == null) return;
////                adapter.notifyDataSetChanged();
//                Intent intent = new Intent(BroadCastActionUtil.XUANZHONG_ACTION);
//                intent.putExtra("fragment", currentFragment + "");
//                sendBroadcast(intent);
//                break;
//            case R.id.spgl_xiajia_txt: //界面底部右边按钮
//                if (currentFragment == 1) {
//                    intent = new Intent(BroadCastActionUtil.XIAJIA_TIJIAO_ACTION);
//                    intent.putExtra("fragment", currentFragment + "");
//                    sendBroadcast(intent);
////                    is_xiajia = true ;
////                    right_txt.setText("下架");
//                } else if (currentFragment == 2) {
//                    intent = new Intent(BroadCastActionUtil.SHANGJIA_TIJIAO_ACTION);
//                    intent.putExtra("fragment", currentFragment + "");
//                    sendBroadcast(intent);
////                    right_txt.setText("上架");
////                    is_shangjia = true;
//                }
//                break;
//        }
//    }


    public void initShangjiaShuxing() {
        ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guanli_xiajia);
        tvTwotileRight.setText("下架");
        is_xiajia = true;
        shangpin_guanli_bottom.setVisibility(View.GONE);
        bottom_right_txt.setText("下架");
        bottom_right_txt.setBackgroundColor(getResources().getColor(R.color.xiajia_default_bg));
    }

    @Override
    public void initXiajiaShuxing() {
        if (2 == currentFragment) {
            ivTwotitleRight.setImageResource(R.mipmap.ic_shangpin_guangli_shangjia);
            tvTwotileRight.setText("上架");
            is_shangjia = true;
            shangpin_guanli_bottom.setVisibility(View.GONE);
            bottom_right_txt.setText("上架");
            bottom_right_txt.setBackgroundColor(getResources().getColor(R.color.xiajia_default_bg));
        }
    }

    private void sendBroast(String action, String start_shangjia) {
        Intent intent = new Intent(action);
        intent.putExtra("start_shangjia", start_shangjia);
        intent.putExtra("fragment", currentFragment + "");
        sendBroadcast(intent);
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#ff7f29"));
    }


}
