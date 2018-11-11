package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DianpuShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.dianpufragment.DianpuFenleiFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.dianpufragment.DianpuQuanbuFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.dianpufragment.DianpuShouyeFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.ShopGradeActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.DingdanGuanliActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AfterSaleListActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.EvaluationActivity;
import com.gloiot.hygoSupply.ui.activity.postproduct.PostProductActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.ShangpinGuanliActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.ShangPinShangChuanActivity;
import com.gloiot.hygoSupply.ui.adapter.TabFragmentAdapter;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.jaeger.library.StatusBarUtil;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WodeDianpuActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TabLayout tl_wode_dianpu;
    private ViewPager vp_wode_dianpu;
    private EditText et_sousuo;
    private String[] titles = new String[]{"首页", "全部", "分类"};
    private String dianpu_tubiao, dianpu_mingcheng, dianzhao_tupian, phone;
    private LinearLayout ll_fabushangpin, ll_shangpinguanli, ll_dingdanguanli, ll_pinglunguanli, ll_shouhouguanli;
    private TextView tv_wode_dianpu_dianpu_mingcheng, tv_wode_dianpu_dianpu_xiaoliang, tv_wode_dianpu_dianpu_shoucang, tv_fabu_shangPing, tv_shangping_guanli, tv_dingdanguanli;
    private ImageView iv_wode_dianpu_dianzhao_tupian, iv_wode_dianpu_bianji, iv_wode_dianpu_back, iv_wode_dianpu_dianpu_tubiao, iv_dianpu_grade;
    private GridView gv_dianpu_sousuo;
    private Boolean huifu = false;  // 用于恢复原来页面
    private ArrayList<DianpuShangpinBean> alldianpuShangpinBeen = new ArrayList<>();
    private CommonAdapter adapter;
    private RelativeLayout rl_sousuo;
    private RelativeLayout rl_dianpu_title;
    private String zhuying_leibie, shangchengleibie, dianpu_zhuangtai, dianpu_yanzheng, shiyongqi;
    private String dianpujieshao;
    private String shopGrade;
    @Bind(R.id.view_status_bar)
    View viewStatusBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_my_shop;
    }

    @Override
    public void initData() {
        tl_wode_dianpu = (TabLayout) findViewById(R.id.tl_wode_dianpu);
        vp_wode_dianpu = (ViewPager) findViewById(R.id.vp_wode_dianpu);
        iv_wode_dianpu_dianzhao_tupian = (ImageView) findViewById(R.id.iv_dianpu_title);
        iv_wode_dianpu_dianpu_tubiao = (ImageView) findViewById(R.id.iv_dianpu_tubiao);
        tv_wode_dianpu_dianpu_mingcheng = (TextView) findViewById(R.id.tv_dianpu_ming);
        tv_wode_dianpu_dianpu_xiaoliang = (TextView) findViewById(R.id.tv_dianpu_xiaoliangNum);
        tv_wode_dianpu_dianpu_shoucang = (TextView) findViewById(R.id.tv_dianpu_guanzhu);
        iv_wode_dianpu_back = (ImageView) findViewById(R.id.iv_toptitle_back);
        et_sousuo = (EditText) findViewById(R.id.et_sousuo);
        rl_dianpu_title = (RelativeLayout) findViewById(R.id.rl_dianpu_title);
        gv_dianpu_sousuo = (GridView) findViewById(R.id.gv_dianpu_sousuo);
        tv_dingdanguanli = (TextView) findViewById(R.id.tv_dingdanguanli);
        tv_shangping_guanli = (TextView) findViewById(R.id.tv_shangping_guanli);
        tv_fabu_shangPing = (TextView) findViewById(R.id.tv_fabu_shangPing);
        ll_fabushangpin = (LinearLayout) findViewById(R.id.ll_fabushangpin);
        ll_shangpinguanli = (LinearLayout) findViewById(R.id.ll_shangpinguanli);
        ll_dingdanguanli = (LinearLayout) findViewById(R.id.ll_dingdanguanli);
        ll_pinglunguanli = (LinearLayout) findViewById(R.id.ll_pinglunguanli);
        ll_shouhouguanli = (LinearLayout) findViewById(R.id.ll_shouhouguanli);
        iv_dianpu_grade = (ImageView) findViewById(R.id.iv_dianpu_grade);
        rl_sousuo = (RelativeLayout) findViewById(R.id.rl_sousuo);

        iv_wode_dianpu_back.setOnClickListener(this);
        iv_dianpu_grade.setOnClickListener(this);
        ll_dingdanguanli.setOnClickListener(this);
        ll_fabushangpin.setOnClickListener(this);
        ll_shangpinguanli.setOnClickListener(this);
        ll_pinglunguanli.setOnClickListener(this);
        ll_shouhouguanli.setOnClickListener(this);
        tv_wode_dianpu_dianpu_mingcheng.setText(dianpu_mingcheng);
        shiyongqi = getIntent().getStringExtra("shiyongqi");
        CommonUtils.setDisplayImageOptions(iv_wode_dianpu_dianpu_tubiao, dianpu_tubiao, 4);
        CommonUtils.setDisplayImageOptions(iv_wode_dianpu_dianzhao_tupian, dianzhao_tupian, 4);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        shangchengleibie = preferences.getString(ConstantUtils.SP_USERSCTYPE, "");
        TabLayout();
        gv_dianpu_sousuo.setOnItemClickListener(this);


        et_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!"".equals(et_sousuo.getText().toString().trim())) {
                        hide();
                        huifu = true;
                        alldianpuShangpinBeen.clear();
                        requestHandleArrayList.add(requestAction.shop_sj_sou(WodeDianpuActivity.this, phone, et_sousuo.getText().toString()));
                    } else {
                        MToast.showToast("请输入搜索内容");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_sj_dp(WodeDianpuActivity.this, phone));
        requestHandleArrayList.add(requestAction.shop_sh_honnr(WodeDianpuActivity.this, phone));
    }


    //成功回调
    @Override
    public void onSuccess(int requestTag, JSONObject jsonObject, int showLoad) {
        super.onSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_dp: //我的店铺
                Log.e("我的店铺", "我的店铺编辑完重新加载请求成功:" + jsonObject.toString());
                try {
                    if (jsonObject.getString("状态").equals("成功")) {
                        if (jsonObject.getString("商家状态").equals("是")) {
                            dianpu_mingcheng = jsonObject.getString("店铺名称");
                            dianpu_tubiao = jsonObject.getString("店铺图标");
                            dianzhao_tupian = jsonObject.getString("店招图片");
                            zhuying_leibie = jsonObject.getString("主营类别");
                            dianpujieshao = jsonObject.getString("店铺介绍");
                            tv_wode_dianpu_dianpu_xiaoliang.setText("销量 " + jsonObject.getString("销量"));
                            tv_wode_dianpu_dianpu_mingcheng.setText(dianpu_mingcheng);
                            tv_wode_dianpu_dianpu_shoucang.setText("关注 " + jsonObject.getString("收藏"));
                            CommonUtils.setDisplayImageOptions(iv_wode_dianpu_dianpu_tubiao, dianpu_tubiao, 4);
                            CommonUtils.setDisplayImageOptions(iv_wode_dianpu_dianzhao_tupian, dianzhao_tupian, 4);

                            editor.putString(ConstantUtils.SP_ZHUYINGLEIBIE, zhuying_leibie);
                            editor.commit();
                            Log.e("我的店铺", "我的店铺有数据");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("类型", "添加");
                            Intent intent = new Intent(mContext, BianjiDianpuActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        Log.e("状态", jsonObject.getString("状态"));
                        DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("状态"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ConstantUtils.TAG_shop_sj_sou:
                Log.e("我的店铺搜索", "搜索结果:" + jsonObject.toString());
                try {
                    if (jsonObject.getString("状态").equals("成功")) {
                        String tiaoshu;             //条数
                        String id;                  //id
                        String shangpinmingcheng;   //商品名称
                        String jiesuanjia;          //单价
                        String suoluetu;            //缩略图
                        JSONArray jsonArray = new JSONArray();
                        if (!jsonObject.getString("条数").equals("0")) {
                            jsonArray = jsonObject.getJSONArray("商城搜索类别");
                            Log.e("jsonArraySize", jsonArray.length() + "");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                id = jsonObject.getString("id");
                                shangpinmingcheng = jsonObject.getString("商品名称");
                                jiesuanjia = jsonObject.getString("供货价");
                                suoluetu = jsonObject.getString("缩略图");
                                DianpuShangpinBean bean = new DianpuShangpinBean(id, suoluetu, shangpinmingcheng, jiesuanjia);
                                if ("全球购".equals(jsonObject.getString("类型"))) {
                                    bean.setQuanqiugou(true);
                                }
                                alldianpuShangpinBeen.add(bean);
                            }
                        }
                        Log.e("我的店铺搜索请求bean数据", alldianpuShangpinBeen.size() + "````" + alldianpuShangpinBeen.toString());
                        adapter = new CommonAdapter<DianpuShangpinBean>(WodeDianpuActivity.this, R.layout.item_dianpu_shangpin, alldianpuShangpinBeen) {
                            @Override
                            public void convert(final ViewHolder holder, final DianpuShangpinBean dianpuShangpinBean) {
                                ImageView iv_item_dingdan_guanli_suolvetu = holder.getView(R.id.iv_item_dianpu_shangpin_tupian);
                                CommonUtils.setDisplayImageOptions(iv_item_dingdan_guanli_suolvetu, dianpuShangpinBean.getSuolvetu(), 4);
                                holder.setText(R.id.tv_item_dianpu_shangpin_mingcheng, dianpuShangpinBean.getShangpinmingcheng());
                                holder.setText(R.id.tv_item_dianpu_shangpin_danjia, dianpuShangpinBean.getDanjia());
                                holder.setVisible2(R.id.iv_item_dianpu_shangpin_quanqiugou, dianpuShangpinBean.isQuanqiugou());
                            }
                        };
                        gv_dianpu_sousuo.setAdapter(adapter);
                        MToast.showToast("加载成功");
                    } else if (jsonObject.getString("状态").equals("无商品")) {
                        MToast.showToast("无商品");
                        if (adapter != null) {
                            alldianpuShangpinBeen.clear();
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        DialogUtlis.oneBtnNormal(mContext, jsonObject.getString("状态"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case ConstantUtils.TAG_shop_sj_spec:
                try {
                    if (jsonObject.getString("状态").equals("成功")) {
                        dianpu_zhuangtai = jsonObject.getString("店铺状态");
                        String shiming_yanzheng = jsonObject.getString("实名验证");
                        if (shiming_yanzheng.equals("未认证") || shiming_yanzheng.equals("认证失败")) {
                            DialogUtlis.twoBtnNormal(mContext, "您还没有进行实名认证，请进行实名认证", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO: 2017/6/15   这里增加跳转
                                    DialogUtlis.dismissDialog();
                                }
                            });
                            break;
                        } else if (shiming_yanzheng.equals("审核中")) {
                            DialogUtlis.oneBtnNormal(mContext, "您的实名认证处于审核中，请待认证通过后，即可上传商品");
                            break;
                        }
                        if (dianpu_zhuangtai.equals("是")) {
                            if (TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, ""))) {
                                DialogUtlis.twoBtnNormal(mContext, "请先给店铺添加“主营类目”，是否立即添加？", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialog();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("类型", "添加类目");
                                        bundle.putString("dianpu_tubiao", dianpu_tubiao);
                                        bundle.putString("dianpu_mingcheng", dianpu_mingcheng);
                                        bundle.putString("dianzhao_tupian", dianzhao_tupian);
                                        Intent intent = new Intent(mContext, BianjiDianpuActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            }
                            dianpu_yanzheng = jsonObject.getString("店铺验证");
                            //                  jiaona_zhuangtai = jsonObject.getString("缴纳状态");
                            if (dianpu_yanzheng.equals("已认证")) {
                                Intent intent = new Intent(mContext, PostProductActivity.class);
                                intent.putExtra("zhuyingleimu", zhuying_leibie);
                                intent.putExtra("type", "add");
                                startActivity(intent);
                            } else {
                                shiyongqi = jsonObject.getString("试用期");
                                if (shiyongqi.equals("未过试用期")) {
                                    Intent intent = new Intent(mContext, PostProductActivity.class);
                                    intent.putExtra("zhuyingleimu", zhuying_leibie);
                                    intent.putExtra("type", "add");
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
                                        DialogUtlis.twoBtnNormal(mContext, "您的店铺已过试用期，进行店铺认证后方可继续使用，是否立即认证", new View.OnClickListener() {
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
                            DialogUtlis.twoBtnNormal(mContext, "你还没有创建店铺，是否立即创建", new View.OnClickListener() {
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
                            break;
                        }
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ConstantUtils.TAG_shop_sh_honnr:
                try {
                    shopGrade = jsonObject.getString("等级");
                    switch (shopGrade) {
                        case "见习商家":
                            CommonUtils.setDisplayImage(iv_dianpu_grade, "", 0, R.drawable.ic_myshop_grade_a);
                            break;
                        case "高贵商家":
                            CommonUtils.setDisplayImage(iv_dianpu_grade, "", 0, R.drawable.ic_myshop_grade_b);
                            break;
                        case "尊贵商家":
                            CommonUtils.setDisplayImage(iv_dianpu_grade, "", 0, R.drawable.ic_myshop_grade_c);
                            break;
                        case "荣耀商家":
                            CommonUtils.setDisplayImage(iv_dianpu_grade, "", 0, R.drawable.ic_myshop_grade_d);
                            break;
                        case "至尊商家":
                            CommonUtils.setDisplayImage(iv_dianpu_grade, "", 0, R.drawable.ic_myshop_grade_e);
                            break;
                        default:
                            iv_dianpu_grade.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
    }


    private void TabLayout() {
        //设置TabLayout的模式
        tl_wode_dianpu.setTabMode(TabLayout.MODE_FIXED);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DianpuShouyeFragment());
        fragments.add(new DianpuQuanbuFragment());
        fragments.add(new DianpuFenleiFragment());
        for (int i = 0; i < titles.length; i++) {
            tl_wode_dianpu.addTab(tl_wode_dianpu.newTab().setText(titles[i]));
        }
        vp_wode_dianpu.setAdapter(new TabFragmentAdapter(fragments, titles, getSupportFragmentManager(), this));
        vp_wode_dianpu.setOffscreenPageLimit(2);
        tl_wode_dianpu.setupWithViewPager(vp_wode_dianpu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toptitle_back:
                if (huifu) {
                    //恢复原始界面
                    tl_wode_dianpu.setVisibility(View.VISIBLE);
                    vp_wode_dianpu.setVisibility(View.VISIBLE);
                    rl_sousuo.setVisibility(View.GONE);
                    huifu = false;
                } else {
                    this.finish();
                }
                break;
            case R.id.btn_wode_dianpu_sousuo:
                Log.e("搜索点击", "点击了搜索");
                if (!et_sousuo.getText().toString().equals("")) {
                    hide();
                    huifu = true;
                    alldianpuShangpinBeen.clear();
                    requestHandleArrayList.add(requestAction.shop_sj_sou(this, phone, et_sousuo.getText().toString()));
                } else {
                    MToast.showToast("请输入搜索内容");
                }
                break;
            case R.id.ll_fabushangpin:
                requestHandleArrayList.add(requestAction.shop_sj_spec(this, phone));
                break;
            case R.id.ll_shangpinguanli://商品管理
                startActivity(new Intent(mContext, ShangpinGuanliActivity.class));
                break;
            case R.id.ll_dingdanguanli://订单管理
                startActivity(new Intent(mContext, DingdanGuanliActivity.class));
                break;
            case R.id.ll_pinglunguanli://评论管理
                startActivity(new Intent(mContext, EvaluationActivity.class));
                break;
            case R.id.ll_shouhouguanli://售后管理
                startActivity(new Intent(mContext, AfterSaleListActivity.class));
                break;
            case R.id.iv_dianpu_grade://店铺等级
                Intent shopGrades = new Intent(mContext, ShopGradeActivity.class);
                shopGrades.putExtra("shopGrade", shopGrade);
                startActivity(shopGrades);
                break;
        }
    }

    private void hide() {  //隐藏所有
        tl_wode_dianpu.setVisibility(View.GONE);
        vp_wode_dianpu.setVisibility(View.GONE);
        rl_sousuo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WodeDianpuActivity.this, ShangpinDianpuActivity.class);
        intent.putExtra("id", alldianpuShangpinBeen.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (huifu) {
            //恢复原始界面
            tl_wode_dianpu.setVisibility(View.VISIBLE);
            vp_wode_dianpu.setVisibility(View.VISIBLE);
            rl_sousuo.setVisibility(View.GONE);
            huifu = false;
        } else {
            finish();
        }
    }

    @Override
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setTranslucentForImageView(this, null);
            viewStatusBar.setVisibility(View.VISIBLE);
        } else {
            StatusBarUtil.setColor(this, Color.parseColor("#ffffff"));
        }
    }
}
