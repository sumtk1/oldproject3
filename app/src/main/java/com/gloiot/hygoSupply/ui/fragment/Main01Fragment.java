package com.gloiot.hygoSupply.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.BianjiDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.DianPuRenZhengActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.WodeDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DingdanGuanliActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleListActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.EvaluationActivity;
import com.gloiot.hygoSupply.ui.activity.remenzixun.ReMenZiXunActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.ShangpinGuanliActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.ShangPinShangChuanActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.ui.activity.zhexiantufragment.Fragment1;
import com.gloiot.hygoSupply.ui.activity.zhexiantufragment.Fragment2;
import com.gloiot.hygoSupply.ui.activity.zhexiantufragment.Fragment3;
import com.gloiot.hygoSupply.ui.activity.zhexiantufragment.Fragment4;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.widget.AutoTextView;
import com.gloiot.hygoSupply.widget.PercentTextView;
import com.jaeger.library.StatusBarUtil;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gloiot.hygoSupply.utlis.ConstantUtils.SP_ZHUYINGLEIBIEID;


public class Main01Fragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.main_backdrop3)
    ImageView main_backdrop3;
    @Bind(R.id.ll123)
    LinearLayout ll123;
    @Bind(R.id.img_homepage_shop_picture)
    ImageView img_homepage_shop_picture;
    @Bind(R.id.rl_homepage_shop_picture)
    RelativeLayout rl_homepage_shop_picture;
    @Bind(R.id.tv_homepage_shop_name)
    TextView tv_homepage_shop_name;
    @Bind(R.id.tv_homepage_shop_flag)
    TextView tv_homepage_shop_flag;
    @Bind(R.id.ptv_zhanshi)
    PercentTextView ptv_zhanshi;
    @Bind(R.id.tv_homepage_shop_description)
    TextView tv_homepage_shop_description;
    @Bind(R.id.ll_jieshao)
    RelativeLayout ll_jieshao;
    @Bind(R.id.rl_bianji)
    RelativeLayout rl_bianji;
    @Bind(R.id.img_chuangjiandianpu)
    ImageView img_chuangjiandianpu;
    @Bind(R.id.rl_biaoti)
    RelativeLayout rl_biaoti;
    @Bind(R.id.img_toutiao)
    ImageView img_toutiao;
    @Bind(R.id.fragment_autotextview)
    AutoTextView fragment_autotextview;
    @Bind(R.id.view00)
    View view00;
    @Bind(R.id.rl_01)
    RelativeLayout rl_01;
    @Bind(R.id.tv_jinri_shouyi)
    TextView tv_jinri_shouyi;
    @Bind(R.id.tv_jinri_fangke)
    TextView tv_jinri_fangke;
    @Bind(R.id.tv_jinri_dingdan)
    TextView tv_jinri_dingdan;
    @Bind(R.id.ll_02)
    LinearLayout ll_02;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.fl_home_container)
    RelativeLayout fl_home_container;
    @Bind(R.id.view_viewPage)
    View view_viewPage;
    @Bind(R.id.ll_fragment_tab)
    LinearLayout ll_fragment_tab;
    @Bind(R.id.tv_fabu_shangPing)
    TextView tv_fabu_shangPing;
    @Bind(R.id.tv_shangping_guanli)
    TextView tv_shangping_guanli;
    @Bind(R.id.tv_dingdanguanli)
    TextView tv_dingdanguanli;
    @Bind(R.id.ll_03)
    LinearLayout ll_03;
    @Bind(R.id.view_02)
    View view_02;
    @Bind(R.id.nested_scroll_view)
    NestedScrollView nested_scroll_view;
    @Bind(R.id.img_zhidao)
    ImageView img_zhidao;
    @Bind(R.id.rl_zhidao)
    RelativeLayout rl_zhidao;
    @Bind(R.id.iv_imageGuide)
    ImageView iv_imageGuide;
    @Bind(R.id.tv_zhidaole)
    TextView tv_zhidaole;
    @Bind(R.id.rl_guide)
    RelativeLayout rl_guide;
    @Bind(R.id.coordinatorLayout_toolbar)
    Toolbar coordinatorLayoutToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_pinglungunali)
    TextView tvPinglungunali;
    @Bind(R.id.bt_dingdan_daifukuan)
    Button btDingdanDaifukuan;
    @Bind(R.id.bt_dingdan_daifahuo)
    Button btDingdanDaifahuo;
    @Bind(R.id.bt_dingdan_yifahuo)
    Button btDingdanYifahuo;
    @Bind(R.id.bt_dingdan_yiwancheng)
    Button btDingdanYiwancheng;

    private DecimalFormat df;
    private String dianpu_zhuangtai, phone, dianpu_yanzheng, shiyongqi, zhuyingleimu, dianpujieshao, zhuyingleimuId, dianpu_tubiao, dianpu_mingcheng, dianzhao_tupian, dianPuType, zhanghao;
    private Rect rect = new Rect();
    private Fragment mContent;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private Handler handler = new Handler();
    private int count = 1;
    private List<String> arrList = new ArrayList<String>();
    private Context mContext;
    private String dianPuZhuantai = "", dianPuId;
    private String currentMessage;
    private Map<String, String> messageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), null);
        }
    }

    public void initData() {
        mContext = getActivity();
        messageUrl = new HashMap<>();
        if (!preferences.getString(ConstantUtils.SP_ZHIYIINGZHUANTAI, "").equals("1")) {
            measureGuideLocation();
        }
        df = new DecimalFormat("######0.00");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        setTab();
        handler.postDelayed(runnable, 0);
        //判断是否有店铺
        dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        //商家版今日头条(邱敬)shop_theadlines
        requestHandleArrayList.add(requestAction.shop_wl_theadlinesA(this, phone));
//        if (dianPuZhuantai.equals("是")) {
//            Log.e("dianPuZhuantai", dianPuZhuantai);
//            requestHandleArrayList.add(requestAction.shop_wl_top(this, zhanghao));
//        } else {
//            tv_homepage_shop_flag.setVisibility(View.GONE);
//            img_chuangjiandianpu.setVisibility(View.VISIBLE);
//            rl_bianji.setVisibility(View.GONE);
//        }
        tv_fabu_shangPing.setOnClickListener(this);
        tv_shangping_guanli.setOnClickListener(this);
        tv_dingdanguanli.setOnClickListener(this);
        tvPinglungunali.setOnClickListener(this);
        img_homepage_shop_picture.setOnClickListener(this);
        img_chuangjiandianpu.setOnClickListener(this);
        iv_imageGuide.setOnClickListener(this);
        rl_guide.setOnClickListener(this);
        rl_01.setOnClickListener(this);

        //折线图
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        getChildFragmentManager().beginTransaction().add(R.id.fl_home_container, fragment1).commit();
        mContent = fragment1;
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        switchFragment(fragment1);
                        break;
                    case 1:
                        switchFragment(fragment2);
                        break;
                    case 2:
                        switchFragment(fragment3);
                        break;
                    case 3:
                        switchFragment(fragment4);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否有店铺
        dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
        if (dianPuZhuantai.equals("是")) {
            requestHandleArrayList.add(requestAction.shop_wl_top(this, zhanghao));
        } else {
            img_homepage_shop_picture.setImageResource(R.mipmap.ic_dianpu_touxiang);
            tv_homepage_shop_description.setText("暂无介绍");
            tv_homepage_shop_name.setText("暂无数据");
            img_chuangjiandianpu.setVisibility(View.VISIBLE);
            rl_bianji.setVisibility(View.GONE);
            tv_homepage_shop_flag.setVisibility(View.GONE);
        }
        requestHandleArrayList.add(requestAction.shop_wl_prompt(this, zhanghao));
    }

    private void switchFragment(Fragment fragment) {
        if (mContent != fragment) {
            if (!fragment.isAdded()) {//判断fragment是否已经添加过
                //先把当前的fragment隐藏，把用到的fragment添加上去
                getChildFragmentManager().beginTransaction().hide(mContent).add(R.id.fl_home_container, fragment).commit();
            } else {
                //先把当前的fragment隐藏，把已经添加过的并需要用到的fragment显示出
                getChildFragmentManager().beginTransaction().hide(mContent).show(fragment).commit();
            }
            mContent = fragment;
        }
    }

    public void setTab() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("收益统计"));
        tabLayout.addTab(tabLayout.newTab().setText("访客统计"));
        tabLayout.addTab(tabLayout.newTab().setText("订单统计"));
        tabLayout.addTab(tabLayout.newTab().setText("商品统计"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //指引图
            case R.id.iv_imageGuide:
            case R.id.rl_guide:
                rl_guide.setVisibility(View.GONE);
                break;
            case R.id.img_homepage_shop_picture:
                if (dianPuZhuantai.equals("是")) {
                    Intent intent = new Intent(mContext, WodeDianpuActivity.class);
                    intent.putExtra("zhuyingleimu", zhuyingleimu);
                    intent.putExtra("shiyongqi", shiyongqi);
                    startActivity(intent);
                } else {
                    MToast.showToast("请先创建店铺");
                }
                break;
            case R.id.tv_homepage_shop_flag:
                if (dianPuType.equals("已认证")) {
                    ptv_zhanshi.setMaxValue("700");
                    ptv_zhanshi.setVisibility(View.VISIBLE);
                    ptv_zhanshi.start();
                } else if (dianPuType.equals("未认证") || dianPuType.equals("认证失败")) {
                    startActivity(new Intent(mContext, DianPuRenZhengActivity.class));
                }
                break;
            case R.id.img_chuangjiandianpu:
//                if ("是".equals(preferences.getString(ConstantUtils.SP_SUPERMERCHANT, ""))) {
                Bundle bundle = new Bundle();
                bundle.putString("类型", "添加");
                Intent intent02 = new Intent(mContext, BianjiDianpuActivity.class);
                intent02.putExtras(bundle);
                startActivity(intent02);
//                } else {
//                    DialogUtlis.oneBtnNormal(mContext, "请先在环游购app升级为超级商家");
//                }
                break;
            case R.id.tv_homepage_shop_name:
            case R.id.tv_homepage_shop_description:
            case R.id.rl_bianji:
                if (TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, ""))) {
                    Bundle bundle_02 = new Bundle();
                    bundle_02.putString("类型", "添加类目");
                    bundle_02.putString("dianpu_tubiao", dianpu_tubiao);
                    bundle_02.putString("dianpu_mingcheng", dianpu_mingcheng);
                    bundle_02.putString("dianzhao_tupian", dianzhao_tupian);
                    bundle_02.putString("dianpujieshao", dianpujieshao);
                    Intent intent_02 = new Intent(mContext, BianjiDianpuActivity.class);
                    intent_02.putExtras(bundle_02);
                    startActivity(intent_02);
                } else {
                    Bundle bundle_03 = new Bundle();
                    bundle_03.putString("类型", "修改");
                    bundle_03.putString("dianpu_tubiao", dianpu_tubiao);
                    bundle_03.putString("dianpu_mingcheng", dianpu_mingcheng);
                    bundle_03.putString("dianzhao_tupian", dianzhao_tupian);
                    bundle_03.putString("zhuying_leibie", zhuyingleimu);
                    bundle_03.putString("dianpujieshao", dianpujieshao);
                    Intent intent_03 = new Intent(mContext, BianjiDianpuActivity.class);
                    intent_03.putExtras(bundle_03);
                    startActivity(intent_03);
                }
                break;
            case R.id.tv_fabu_shangPing:
//                if (!"是".equals(preferences.getString(ConstantUtils.SP_SUPERMERCHANT, ""))) {
//                    DialogUtlis.oneBtnNormal(mContext, "请先创建店铺");
//                    break;
//                }
                requestHandleArrayList.add(requestAction.shop_sj_spec(Main01Fragment.this, phone));
                break;
            case R.id.tv_shangping_guanli://商品管理

//                if (!"是".equals(preferences.getString(ConstantUtils.SP_SUPERMERCHANT, ""))) {
//                    DialogUtlis.oneBtnNormal(mContext, "请先创建店铺");
//                    break;
//                }
                if (dianPuZhuantai.equals("是")) {
                    startActivity(new Intent(mContext, ShangpinGuanliActivity.class));
                } else {
                    MToast.showToast("请先创建店铺");
                }
                break;
            case R.id.tv_dingdanguanli://售后管理
                Intent afterSale = new Intent(mContext, AfterSaleListActivity.class);
                startActivity(afterSale);
                break;
            case R.id.tv_pinglungunali://评论管理
//                if (dianPuZhuantai.equals("是")) {
                Intent intent2 = new Intent(mContext, EvaluationActivity.class);
                startActivity(intent2);

//                } else {
//                    MToast.showToast("请先创建店铺");
//                }
                break;
            case R.id.rl_01:
                Intent intent = new Intent(mContext, ReMenZiXunActivity.class);
                intent.putExtra("url", messageUrl.get(currentMessage));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void statusUnusual(JSONObject response) throws JSONException {

        if ("请先设置退货地址再上传商品".equals(response.getString("状态"))) {
            DialogUtlis.oneBtnNormal(getActivity(), response.getString("状态"), "确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtlis.dismissDialog();
                }
            });
        } else {
            super.statusUnusual(response);
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_top:
                if (jsonObject.getString("状态").equals("成功")) {
                    tv_homepage_shop_name.setOnClickListener(this);
                    tv_homepage_shop_description.setOnClickListener(this);
                    editor.putString(ConstantUtils.SP_YOUWUDIANPU, jsonObject.getString("店铺状态"));
                    editor.commit();
                    if (jsonObject.getString("店铺状态").equals("是")) {
                        rl_bianji.setVisibility(View.VISIBLE);
                        rl_bianji.setOnClickListener(this);
                        tv_homepage_shop_flag.setVisibility(View.VISIBLE);
                        img_chuangjiandianpu.setVisibility(View.GONE);
                        JSONObject jsonObj = jsonObject.getJSONObject("列表");
                        zhuyingleimu = jsonObj.getString("主营类别");
                        dianpu_tubiao = jsonObj.getString("店铺图标");
                        dianpu_mingcheng = jsonObj.getString("店铺名称");
                        dianzhao_tupian = jsonObj.getString("店招图片");
                        dianpujieshao = jsonObj.getString("店铺介绍");
                        editor.putString(ConstantUtils.SP_ZHUYINGLEIBIEID, jsonObj.getString("主营类别id"));
                        CommonUtils.setDisplayImageOptions(img_homepage_shop_picture, dianpu_tubiao, 1000);
                        tv_homepage_shop_name.setText(dianpu_mingcheng);
                        if (dianpujieshao.equals("暂无数据")) {
                            tv_homepage_shop_description.setText("暂无介绍");
                        } else {
                            tv_homepage_shop_description.setText(dianpujieshao);
                        }
                        editor.putString(ConstantUtils.SP_ZHUYINGLEIBIE, zhuyingleimu);
                        try {
                            ConstantUtils.dianpuId = jsonObj.getString("店铺id");
                            editor.putString(SP_ZHUYINGLEIBIEID, jsonObj.getString("主营类别id"));
                        } catch (JSONException e) {
                            Log.e("TAG", "top接口字段");
                        }
                        dianPuType = jsonObj.getString("店铺验证");
                        tv_jinri_dingdan.setText(jsonObj.getString("今日订单") + "");
                        tv_jinri_fangke.setText(jsonObj.getString("今日访客") + "");
                        tv_jinri_shouyi.setText(df.format(jsonObj.getDouble("今日收益")) + "");
                        switch (dianPuType) {
                            case "认证失败":
                            case "未认证":
                                Drawable leftDrawable_00 = getResources().getDrawable(R.mipmap.ic_homepage_shop_unautherized);
                                leftDrawable_00.setBounds(0, 0, leftDrawable_00.getMinimumWidth(), leftDrawable_00.getMinimumHeight());
                                tv_homepage_shop_flag.setCompoundDrawables(null, null, leftDrawable_00, null);
                                tv_homepage_shop_flag.setOnClickListener(this);
                                break;
                            case "已认证":
                                Drawable leftDrawable_02 = getResources().getDrawable(R.mipmap.ic_homepage_shop_flag);
                                leftDrawable_02.setBounds(0, 0, leftDrawable_02.getMinimumWidth(), leftDrawable_02.getMinimumHeight());
                                tv_homepage_shop_flag.setCompoundDrawables(null, null, leftDrawable_02, null);
                                tv_homepage_shop_flag.setOnClickListener(this);
                                break;
                            case "审核中":
                                Drawable leftDrawable_03 = getResources().getDrawable(R.mipmap.ic_homepage_shop_check);
                                leftDrawable_03.setBounds(0, 0, leftDrawable_03.getMinimumWidth(), leftDrawable_03.getMinimumHeight());
                                tv_homepage_shop_flag.setCompoundDrawables(null, null, leftDrawable_03, null);
                                tv_homepage_shop_flag.setOnClickListener(this);
                                break;
                        }
                    } else {
                        dianPuZhuantai = "否";
                        editor.putString(ConstantUtils.SP_DIANPUID, "");
                        img_homepage_shop_picture.setImageResource(R.mipmap.ic_dianpu_touxiang);
                        tv_homepage_shop_description.setText("暂无介绍");
                        tv_homepage_shop_name.setText("暂无数据");
                        img_chuangjiandianpu.setVisibility(View.VISIBLE);
                        rl_bianji.setVisibility(View.GONE);
                        tv_homepage_shop_flag.setVisibility(View.GONE);
                    }
                    editor.commit();
                }
                break;
            case ConstantUtils.TAG_shop_sj_spec:
                dianpu_zhuangtai = jsonObject.getString("店铺状态");
                String shiming_yanzheng = jsonObject.getString("实名验证");
                if (shiming_yanzheng.equals("未认证") || shiming_yanzheng.equals("认证失败")) {

                    DialogUtlis.twoBtnNormal(mContext, "您还没有进行实名认证，是否立即认证？", "提示", false, "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();

                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();
                            Intent intent = new Intent(mContext, ShiMingRenZhengActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                } else if (shiming_yanzheng.equals("审核中")) {
                    DialogUtlis.oneBtnNormal(mContext, "您的实名认证处于审核中，请待认证通过后，即可上传商品");
                    break;
                }
                if (dianpu_zhuangtai.equals("是")) {
                    if (TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, ""))) {
                        DialogUtlis.twoBtnNormal(mContext, "请先给店铺添加“主营类目”，是否立即添加？", "提示", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString("类型", "添加类目");
                                bundle.putString("dianpu_tubiao", dianpu_tubiao);
                                bundle.putString("dianpu_mingcheng", dianpu_mingcheng);
                                bundle.putString("dianzhao_tupian", dianzhao_tupian);
                                bundle.putString("dianpujieshao", dianpujieshao);
                                Intent intent = new Intent(mContext, BianjiDianpuActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                    }
                    dianpu_yanzheng = jsonObject.getString("店铺验证");
                    if (dianpu_yanzheng.equals("已认证")) {
                        Intent intent = new Intent(mContext, ShangPinShangChuanActivity.class);
                        intent.putExtra("zhuyingleimu", zhuyingleimu);
                        startActivity(intent);
                    } else {
                        shiyongqi = jsonObject.getString("试用期");
                        if (shiyongqi.equals("未过试用期")) {
                            Intent intent = new Intent(mContext, ShangPinShangChuanActivity.class);
                            intent.putExtra("zhuyingleimu", zhuyingleimu);
                            startActivity(intent);
                        } else if (shiyongqi.equals("")) {
                            if (dianpu_yanzheng.equals("未认证")) {
                                DialogUtlis.oneBtnNormal(mContext, "店铺未认证，认证过后即可上传商品");
                            } else if (dianpu_yanzheng.equals("认证失败")) {
                                DialogUtlis.oneBtnNormal(mContext, "店铺认证失败，认证过后即可上传商品");
                            } else if (dianpu_yanzheng.equals("审核中")) {
                                DialogUtlis.oneBtnNormal(mContext, "店铺认证正在审核中，审核通过后即可上传商品");
                            }
                        } else {
                            if (dianpu_yanzheng.equals("审核中")) {
                                DialogUtlis.oneBtnNormal(mContext, "店铺认证正在审核中，审核通过后即可上传商品");
                            } else {
                                DialogUtlis.twoBtnNormal(mContext, "您的店铺已过试用期，进行店铺认证后方可继续使用，是否立即认证", "提示", false, "取消", "确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialog();

                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialog();
                                        startActivity(new Intent(mContext, DianPuRenZhengActivity.class));
                                    }
                                });
                            }
                        }
                    }
                } else {
                    DialogUtlis.twoBtnNormal(mContext, "您还未创建店铺，是否立即创建", "提示", false, "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString("类型", "添加");
                            Intent intent = new Intent(mContext, BianjiDianpuActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                break;
            case ConstantUtils.TAG_shop_wl_theadlinesA:
                Log.e("今日头条", jsonObject.toString());
                if (jsonObject.getString("状态").equals("成功")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("列表");
                    if (Integer.parseInt(jsonObject.getString("条数")) > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            arrList.add(jsonObj.getString("内容"));
                            messageUrl.put(jsonObj.getString("内容"), jsonObj.getString("链接"));
                        }
                        fragment_autotextview.setText(arrList.get(0));
                    }
                }
                break;

            case ConstantUtils.TAG_shop_wl_prompt:
                btDingdanDaifahuo.setVisibility(View.GONE);
                btDingdanDaifukuan.setVisibility(View.GONE);
                btDingdanYifahuo.setVisibility(View.GONE);
                btDingdanYiwancheng.setVisibility(View.GONE);

                btDingdanDaifukuan.setText(jsonObject.getInt("待付款数量") + "");
                btDingdanDaifahuo.setText(jsonObject.getInt("待发货数量") + "");
                btDingdanYifahuo.setText(jsonObject.getInt("已发货数量") + "");
                btDingdanYiwancheng.setText(jsonObject.getInt("已完成数量") + "");

                if (jsonObject.getInt("待付款数量") != 0)
                    btDingdanDaifukuan.setVisibility(View.VISIBLE);
                if (jsonObject.getInt("待发货数量") != 0)
                    btDingdanDaifahuo.setVisibility(View.VISIBLE);
                if (jsonObject.getInt("已发货数量") != 0)
                    btDingdanYifahuo.setVisibility(View.VISIBLE);
                if (jsonObject.getInt("已完成数量") != 0)
                    btDingdanYiwancheng.setVisibility(View.VISIBLE);
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 4000);
                fragment_autotextview.next();
                if (arrList.size() > 0) {
                    currentMessage = arrList.get(count % arrList.size());
                    fragment_autotextview.setText(currentMessage);
                    count++;
                }
            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
            }
        }
    };


    private void measureGuideLocation() {
        //注意：此处需要开辟线程，延迟1s左右来做，以防测量不到坐标
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    img_homepage_shop_picture.getLocationOnScreen(location);
                    rect.left = img_homepage_shop_picture.getLeft();
                    rect.top = img_homepage_shop_picture.getTop();
                    rect.right = img_homepage_shop_picture.getRight();
                    rect.bottom = img_homepage_shop_picture.getHeight();
                    xLocation = location[0];
                    yLocation = location[1];
                    Log.e("hxy:", "xLocation:" + xLocation + "yLocation:" + yLocation);
                    Log.e("hxy:", "rect.left:" + rect.left + "rect.top:" + rect.top + "rect.right" + rect.right + "rect.bottom" + rect.bottom);
                    //更新控件的位置，放在UI线程中做：
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setGuideLocation();
                        }
                    });
                } catch (Exception e) {

                }

            }
        }).start();
    }

    private int[] location = new int[2];
    private float xLocation;
    private float yLocation;

    private void setGuideLocation() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_imageGuide.getLayoutParams();
        layoutParams.setMargins(rect.left, (int) yLocation, 0, 0);
        iv_imageGuide.setLayoutParams(layoutParams);
        rl_guide.setVisibility(View.VISIBLE);
        iv_imageGuide.setVisibility(View.VISIBLE);
    }

    //    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            requestHandleArrayList.add(requestAction.shop_sp_sjm(this,preferences.getString(ConstantUtils.SP_ZHANGHAO,""),preferences.getString(ConstantUtils.SP_RANDOMCODE,"")));
//            if (!"成功".equals(preferences.getString(ConstantUtils.SP_LOGINTYPE, ""))) {
//                Toast.makeText(getActivity(), "您的帐号可能已在其他手机登录!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_daifukuan, R.id.rl_daifahuo, R.id.rl_yifahuo, R.id.rl_yiwancheng, R.id.rl_wodedingdan})
    public void onViewClicked(View view) {
        if (dianPuZhuantai.equals("是")) {
            Intent intent = new Intent(mContext, DingdanGuanliActivity.class);
            switch (view.getId()) {
                case R.id.rl_daifukuan:
                    intent.putExtra("select", 1);
                    break;
                case R.id.rl_daifahuo:
                    intent.putExtra("select", 2);
                    break;
                case R.id.rl_yifahuo:
                    intent.putExtra("select", 3);
                    break;
                case R.id.rl_yiwancheng:
                    intent.putExtra("select", 4);
                    break;
                case R.id.rl_wodedingdan:
                    intent.putExtra("select", 0);
                    break;
            }
            startActivity(intent);
        } else {
            MToast.showToast("请先创建店铺");
        }
    }
}