package com.gloiot.hygoSupply.ui.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.shopgrade.ShopGradeActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.QingxuanzeKefuActivity;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.ParticipateActivities;
import com.gloiot.hygoSupply.ui.activity.promotional.PromotionalAvtivities;
import com.gloiot.hygoSupply.ui.activity.promotional.ShopPromotionActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.ui.activity.wode.FenXiangEWeiMaActivity;
import com.gloiot.hygoSupply.ui.activity.wode.kefuzhongxin.KeFuZhongXinActivity;
import com.gloiot.hygoSupply.ui.activity.wode.setting.MySettingActivity;
import com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi.MyInfoActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shoukuan.WoYaoShouKuanActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shouyi.WoDeShouYiXiangQingActivity;
import com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi.SendSystemMessageActivity;
import com.gloiot.hygoSupply.ui.activity.wode.yinhangka.WoDeYinHangKaActivity;
import com.gloiot.hygoSupply.ui.activity.wode.zhifubao.WoDeZhiFuBaoActivity;
import com.gloiot.hygoSupply.ui.activity.yunyinghuodong.ChuangJianYouHuiQuanActivity;
import com.gloiot.hygoSupply.ui.activity.yunyinghuodong.YouHuiQuanActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.PopWindUtil;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main03Fragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.iv_fg3_touxiang)
    ImageView iv_fg3_touxiang;
    @Bind(R.id.rl_fg3_touxiang)
    RelativeLayout rl_fg3_touxiang;
    @Bind(R.id.tv_fg3_sctype)
    TextView tv_fg3_sctype;
    @Bind(R.id.tv_fg3_nicheng)
    TextView tv_fg3_nicheng;
    @Bind(R.id.tv_fg3_gerenxinxi)
    TextView tv_fg3_gerenxinxi;
    @Bind(R.id.tv_fg3_zongshouyi)
    TextView tv_fg3_zongshouyi;
    @Bind(R.id.rl_fg3_wodeshouyi)
    LinearLayout rl_fg3_wodeshouyi;
    @Bind(R.id.iv_fg3_shopgrade_icon)
    ImageView ivFg3ShopgradeIcon;
    @Bind(R.id.tv_fg3_shopgrade_name)
    TextView tvFg3ShopgradeName;
    @Bind(R.id.rl_fg3_shopgrade)
    LinearLayout rlFg3Shopgrade;
    //RecyclerView集合
    @Bind(R.id.rv_my_fragment_list)
    GridView rvMyFragmentList;

    private CommonAdapter commonAdapter;

    private MyFragmentBean[] myFragmentBeans = {
//            new MyFragmentBean(R.mipmap.super_merchant_collection, "收款"),
            new MyFragmentBean(R.mipmap.ic_shop_zhifubao, "关联支付宝"),
            new MyFragmentBean(R.mipmap.kefuzhongxin, "客服中心"),
            new MyFragmentBean(R.mipmap.my_bank_card, "我的银行卡"),
            new MyFragmentBean(R.mipmap.real_name_authentication, "实名认证"),
            new MyFragmentBean(R.mipmap.with_the_dynamic, "发动态"),
            new MyFragmentBean(R.mipmap.ic_activities, "参与活动"),
            new MyFragmentBean(R.mipmap.ic_chuangjianyouhuiquan, "创建优惠券"),
            new MyFragmentBean(R.mipmap.share_qr_codes, "分享二维码"),
            new MyFragmentBean(R.mipmap.setting_new, "设置"),
            new MyFragmentBean(R.mipmap.use_tutorials, "操作指导"),
//            new MyFragmentBean(R.mipmap.ic_shop_promotion, "商品推广")
            new MyFragmentBean(R.mipmap.ic_system_message, "系统消息")
    };

    public class MyFragmentBean {
        public MyFragmentBean(int imageResources, String title) {
            ImageResources = imageResources;
            this.title = title;
        }

        private int ImageResources;
        private String title;
    }

    private ArrayList<MyFragmentBean> listDatas1 = new ArrayList<>(Arrays.asList(myFragmentBeans));
    private List<MyFragmentBean> listDatas = new ArrayList<>(listDatas1);

    private SharedPreferences.Editor editor;
    private String phone;
    private String userImg, fg3_name;
    private String zong = null, nicheng;
    private String shangChengLeiBei, zhenshixingmin, shopGrade, activitiesExplain, deductionPercent;
    private boolean isJoinActivities;
    private String jiaonaState; // 缴纳状态


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main03_new, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {
        iv_fg3_touxiang.setOnClickListener(this);
        tv_fg3_gerenxinxi.setOnClickListener(this);
        rl_fg3_wodeshouyi.setOnClickListener(this);
        editor = preferences.edit();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        fg3_name = preferences.getString(ConstantUtils.SP_USERNAME, "");
        nicheng = preferences.getString(ConstantUtils.SP_USERNICHENG, "");
        shangChengLeiBei = preferences.getString(ConstantUtils.SP_USERSCTYPE, "");
        tv_fg3_nicheng.setText(nicheng);
        tv_fg3_sctype.setText(shangChengLeiBei);

        rvMyFragmentList.setFocusable(false);
        rvMyFragmentList.setNumColumns(4);
        if (!"是".equals(preferences.getString(ConstantUtils.SP_SENDMESSAGE, "否"))) {
            listDatas.remove(listDatas.size() - 1);//移除系统消息图标，注意系统消息位置
        }
        commonAdapter = new CommonAdapter<MyFragmentBean>(mContext, R.layout.item_myfragment_bean, listDatas) {
            @Override
            public void convert(ViewHolder holder, MyFragmentBean myFragmentBean) {
                holder.setText(R.id.tv_item_my_fragment_title, myFragmentBean.title);
                holder.getView(R.id.iv_item_my_fragment_img).setBackgroundResource(myFragmentBean.ImageResources);
            }
        };
        rvMyFragmentList.setAdapter(commonAdapter);
        rvMyFragmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) return;
                switch (listDatas.get(position).title) {
                    case "设置":
                        startActivity(new Intent(mContext, MySettingActivity.class));
                        break;
                    case "操作指导":
                        Intent intent3 = new Intent(mContext, WebActivity.class);
                        intent3.putExtra("url", ConstantUtils.INSTRUCTION_URL);
                        startActivity(intent3);
                        break;
                    case "实名认证":
                        if (!preferences.getString(ConstantUtils.SP_SHIMINGYANZHENG, "").equals("已认证")) {
                            startActivity(new Intent(mContext, ShiMingRenZhengActivity.class));
                        } else {
                            DialogUtlis.oneBtnNormal(mContext, "已实名认证");
                        }
                        break;
                    case "收款":
                        requestHandleArrayList.add(requestAction.ReturnAddress(Main03Fragment.this, phone));
                        break;
                    case "发动态":
                        PopWindUtil.getInstance().faBuDongTai(getActivity());
                        break;
                    case "分享二维码":
                        if (preferences.getString(ConstantUtils.SP_SHIFOUBANGDINGWX, "").equals("否")) {
                            DialogUtlis.oneBtnNormal(mContext, "请先在环游购客户版绑定微信");
                        } else {
                            startActivity(new Intent(getActivity(), FenXiangEWeiMaActivity.class));
                        }
                        break;
                    case "我的银行卡":
                        startActivity(new Intent(getActivity(), WoDeYinHangKaActivity.class));
                        break;
                    case "参与活动":
                        Intent activities;
                        if (isJoinActivities) {
                            //参与活动列表界面
                            activities = new Intent(mContext, ParticipateActivities.class);
                        } else {
                            //积分折扣活动界面
                            activities = new Intent(mContext, PromotionalAvtivities.class);
                            activities.putExtra("isFirst", "yes");
                        }
                        activities.putExtra("deductionPercent", deductionPercent);
                        activities.putExtra("activitiesExplain", activitiesExplain);
                        startActivity(activities);
                        break;
                    case "关联支付宝":
                        startActivity(new Intent(mContext, WoDeZhiFuBaoActivity.class));
                        break;
                    case "商品推广":
                        startActivity(new Intent(mContext, ShopPromotionActivity.class));
                        break;
                    case "创建优惠券":
                        startActivity(new Intent(mContext, YouHuiQuanActivity.class));
                        break;
                    case "客服中心":
                        startActivity(new Intent(mContext, KeFuZhongXinActivity.class));
                        break;
                    case "系统消息":
                        startActivity(new Intent(mContext, SendSystemMessageActivity.class));
                        break;
                    default:
                }
            }
        });

        CommonUtils.reMesureGridViewHeight(rvMyFragmentList);
    }


    @OnClick({R.id.iv_fg3_touxiang, R.id.rl_fg3_shopgrade, R.id.rl_fg3_wodeshouyi})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fg3_touxiang:
                startActivity(new Intent(mContext, MyInfoActivity.class));
                break;
            case R.id.rl_fg3_wodeshouyi:
                Intent intent = new Intent(mContext, WoDeShouYiXiangQingActivity.class);
                intent.putExtra("zong", tv_fg3_zongshouyi.getText().toString());
                intent.putExtra("jiaonaState", jiaonaState);
                startActivity(intent);
                break;
            case R.id.rl_fg3_shopgrade:
                if (!TextUtils.isEmpty(shopGrade)) {
                    Intent shopGrades = new Intent(mContext, ShopGradeActivity.class);
                    shopGrades.putExtra("shopGrade", shopGrade);
                    startActivity(shopGrades);
                }

                break;
            default:
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        userImg = preferences.getString(ConstantUtils.SP_USERIMG, "");
        fg3_name = preferences.getString(ConstantUtils.SP_USERNICHENG, "");
        zhenshixingmin = preferences.getString(ConstantUtils.SP_USERNAME, fg3_name);
        Log.e("XUtilsImageLoader", "显示头像" + userImg);
//        if (preferences.getBoolean(ConstantUtils.SP_CHANGEUSERIMG, false)) {
//            Log.e("XUtilsImageLoader", "显示头像" + userImg);
        if (!TextUtils.isEmpty(userImg)) {
            Log.e("头像", "onResume: " + userImg + "==" + "");
            CommonUtils.setDisplayImageOptions(iv_fg3_touxiang, userImg, 1000);
//                editor.putBoolean(ConstantUtils.SP_CHANGEUSERIMG, false);
//                editor.commit();
        }
//        }
        if (preferences.getBoolean(ConstantUtils.SP_CHANGEUSERNICHENG, false)) {
            editor.putBoolean(ConstantUtils.SP_CHANGEUSERNICHENG, false);
            editor.commit();
        }
        requestHandleArrayList.add(requestAction.shop_sj_sz(this, phone));
        requestHandleArrayList.add(requestAction.shop_sj_zong(this, phone));
        requestHandleArrayList.add(requestAction.shop_sh_honnr(this, phone));

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_dzong:
                Log.e("商家总收益请求成功", response.toString());
                if (response.getString("状态").equals("成功")) {
                    jiaonaState = JSONUtlis.getString(response, "缴纳状态", "");
                    zong = response.getString("总收益");
                    //截取小数点后两位以前的值
                    zong = zong.substring(0, zong.indexOf(".") + 3);
                    tv_fg3_zongshouyi.setText(zong);

                    if ("是".equals(response.getString("是否参与活动"))) {
                        isJoinActivities = true;
                    } else {
                        isJoinActivities = false;
                    }
//                    afterSaleModel.setNotice(response.getString("须知").replaceAll("\\/n", "\n"));
                    activitiesExplain = response.getString("说明").replaceAll("\\/n", "\n");
                    deductionPercent = response.getString("内部积分抵扣");
                    try {
                        deductionPercent = deductionPercent.substring(0, deductionPercent.indexOf("."));
                    } catch (Exception e) {
                        Log.e("TAG", "requestSuccess: 积分抵扣取整出错-----" + deductionPercent);
                    }
                }
                break;
            case ConstantUtils.TAG_shop_sj_sz:
                if (response.getString("状态").equals("成功")) {
                    editor.putString(ConstantUtils.SP_SHIMINGYANZHENG, response.getString("实名验证")).commit();
//                    tv_fg3_shiming.setText(response.getString("实名验证"));
                }
                break;
            case ConstantUtils.TAG_p_return_address:
                if (TextUtils.isEmpty(response.getString("地址"))) {
                    Intent intent;
                    intent = new Intent(mContext, WoYaoShouKuanActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    String url = response.getString("地址");
                    if (url.contains("?")) {
                        url = url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//                        url = url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business";
                    } else {
                        url = url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//                        url = url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business";
                    }
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                break;
            case ConstantUtils.TAG_p_get_QR:
                final String myQRcode = response.getString("二维码地址");
                if (TextUtils.isEmpty(myQRcode)) {
                    MToast.showToast("返回二维码信息错误");
                    return;
                }

                DialogUtlis.myErWeiMa(mContext, new DialogUtlis.MyERWeiMaCallback() {
                    @Override
                    public void callback(TextView tvName, TextView tvNum, ImageView pic, ImageView ewm, ImageView sex) {
                        // 头像
                        PictureUtlis.loadCircularImageViewHolder(mContext, preferences.getString(ConstantUtils.SP_USERIMG, ""), R.mipmap.ic_head, pic);

                        // 昵称
                        tvName.setText(preferences.getString(ConstantUtils.SP_USERNICHENG, ""));
                        // 账号
                        String my_phone = preferences.getString(ConstantUtils.SP_USERPHONE, "");
                        my_phone = my_phone.substring(0, 3) + "****" + my_phone.substring(my_phone.length() - 4, my_phone.length());
                        tvNum.setText(my_phone);
                        //性别
                        String gender = preferences.getString(ConstantUtils.SP_USERSEX, "");
                        if (gender.equals("男")) {
                            CommonUtils.setDisplayImage(sex, "", 0, R.mipmap.male_ic);
                        } else if (gender.equals("女")) {
                            CommonUtils.setDisplayImage(sex, "", 0, R.mipmap.female_ic);
                        } else {

                        }

                        CommonUtils.setDisplayImage(ewm, myQRcode, 0, R.mipmap.ic_default);
                    }
                });

                break;
            case ConstantUtils.TAG_shop_sh_honnr:
                try {
                    shopGrade = response.getString("等级");
                    tvFg3ShopgradeName.setText(shopGrade);
                    switch (shopGrade) {
                        case "见习商家":
                            CommonUtils.setDisplayImage(ivFg3ShopgradeIcon, "", 0, R.mipmap.ic_main_grade_a);
                            break;
                        case "高贵商家":
                            CommonUtils.setDisplayImage(ivFg3ShopgradeIcon, "", 0, R.mipmap.ic_main_grade_b);
                            break;
                        case "尊贵商家":
                            CommonUtils.setDisplayImage(ivFg3ShopgradeIcon, "", 0, R.mipmap.ic_main_grade_c);
                            break;
                        case "荣耀商家":
                            CommonUtils.setDisplayImage(ivFg3ShopgradeIcon, "", 0, R.mipmap.ic_main_grade_d);
                            break;
                        case "至尊商家":
                            CommonUtils.setDisplayImage(ivFg3ShopgradeIcon, "", 0, R.mipmap.ic_main_grade_e);
                            break;
                        default:
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            default:
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!"成功".equals(preferences.getString(ConstantUtils.SP_LOGINTYPE, ""))) {
                Toast.makeText(mContext, "您的帐号可能已在其他手机登录!", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onChanged: " + "03fragmeent退出");
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}