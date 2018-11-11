package com.gloiot.hygoSupply.ui.activity.shangpinguanli;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.FreightTemplateModel;
import com.gloiot.hygoSupply.bean.GuigeModel;
import com.gloiot.hygoSupply.bean.MiaoShuBean;
import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.PreviewPictureActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.ChuShouZhongFragment;
import com.gloiot.hygoSupply.ui.activity.shangpinguanli.shangpinfragment.ShenHeZhongFragment;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.DizhiXuanzeActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.FreightTemplateActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.ShangPinShangChuanActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.TianjiaShangpinGuigeActivity_new;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.XuanzeShangPinLeixingAvtivity;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.InterceptRelativeLayout;
import com.gloiot.hygoSupply.widget.XiaJiaDialog;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProductActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_shangpin_shangchuan1_leixing;
    private EditText et_shangpin_shangchuan1_mingcheng;
    private TextView tv_shangpin_shangchuan1_yijileimu;
    private TextView tv_shangpin_shangchuan1_erjileimu;
    private TextView tv_shangpin_shangchuan1_sanjileimu;
    private TextView tv_shangpin_shangchuan_feilv;
    private EditText et_shangpin_shangchuan1_yunfei;
    private RelativeLayout rl_shangpin_shangchuan1_fahuodi;
    private RelativeLayout rl_shangpin_shangchuan1_miaoshu;
    private RelativeLayout rl_shangpin_shangchuan1_guige;
    private LinearLayout ll_shangpin_shangchuan_xuzhi;
    private LinearLayout ll_view_product_action;
    private Button bt_shangpin_shangchuan1_shangchuan;
    private GridView gv_shangpin_shangchuan_tupian;
    private MyGVAdapter adapter;
    //店铺所卖商品的一级类型
    private String zhuyingleimu = "";
    private String zhuyingleimuid = "";
    private static final int MAXPICTURES = 6;
    private List<String> picUrlList = new ArrayList<>();
    private Spinner sp_shangpin_shangchuan1_erjileimu;
    private Spinner sp_shangpin_shangchuan1_sanjileimu;
    private Spinner sp_shangpin_shangchuan1_freight;
    private String phone = "", imageUrl = "", yijileimu = "", erjileimu = "", sanjileimu = "";
    private List<MiaoShuBean> allmiaoshu = new ArrayList<>();
    private Intent intent;
    private String fahuodiId;
    private String fahuodiDizhi;
    private List<String> picSrcList;
    private List<String> stairSpinnerList;//一级类别
    private List<String> secondSpinnerList;//二级类别
    private List<String> thridSpinnerList;//三级类别
    private String[] dizhiInfoFinal;
    private TextView tv_shangpin_shangchuan_fahuodi_info;
    private TextView tv_shangpin_shangchuan_miaoshu_info;
    private String shangchengleibie;
    private String yunfei;
    private Map<String, String> yijileimuInfo, erjileimuInfo, sanjileimuInfo, feilvInfo;
    private TextView tv_shangpin_mingcheng_zishu;
    private String shangpin_xiangqing_id;
    public static List<GuigeModel> guigeModels = new ArrayList<>();
    private String yijileimuID = "0", erjileimuID = "0", sanjileimuID = "0";
    private List<FreightTemplateModel> freightTemplateModels;
    private List<String> freightTemplateNames;
    private FreightTemplateModel freightTemplateModel;
    private ImageView iv_freight_add;
    private Spinner sp_shangpin_shangchuan1_yijileixing;
    public static boolean isRefreshFreight;
    private int position = -1;
    private String productId = "";
    private boolean isEdit;//被编辑过
    private boolean isEditType;//编辑模式
    private boolean isDealer;//自营商家
    private String yunfeiId;
    //存储编辑模式下第一次加载的类目信息
    private String secondType;
    private String thirdType;
    private String productName;
    private CheckBox cb_shangchuan_xuzhi;
    private TextView tv_shangchuang_xuzhi;
    private TextView tv_shangchuang_xize;
    private TextView tv_view_product_bottom_right;
    private TextView tv_view_product_bottom_left;
    private TextView tv_view_product_bottom_middle;
    private InterceptRelativeLayout interceptRelativeLayout;
    public static boolean isFinish;
    public static List<MiaoShuBean> ALLSHANGPINMIAOSHU = new ArrayList<>();

    @Override
    public int initResource() {
        return R.layout.activity_view_product;
    }

    @Override
    public void initData() {
        isFinish = false;
        initComponent();
        erjileimuInfo = new HashMap<>();
        sanjileimuInfo = new HashMap<>();
        yijileimuInfo = new HashMap<>();
        feilvInfo = new HashMap<>();
        dizhiInfoFinal = new String[4];
        stairSpinnerList = new ArrayList<>();
        secondSpinnerList = new ArrayList<>();
        thridSpinnerList = new ArrayList<>();
        picSrcList = new ArrayList<>();
        freightTemplateNames = new ArrayList<>();
        freightTemplateModels = new ArrayList<>();
        zhuyingleimuid = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIEID, "");
        shangchengleibie = preferences.getString(ConstantUtils.SP_USERSCTYPE, "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        iv_freight_add.setOnClickListener(this);


        intent = getIntent();
        zhuyingleimu = intent.getStringExtra("zhuyingleimu");
        if (TextUtils.isEmpty(zhuyingleimu)) {
            zhuyingleimu = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, "");
        }
        if (null != zhuyingleimu && (zhuyingleimu.equals("时尚服装") || zhuyingleimu.equals("鞋靴箱包"))) {
            shangpin_xiangqing_id = "1";
        } else {
            shangpin_xiangqing_id = "0";
        }
        if ("是".equals(preferences.getString(ConstantUtils.SP_ZIYING, ""))) {
            isDealer = true;
            sp_shangpin_shangchuan1_yijileixing.setVisibility(View.VISIBLE);
            tv_shangpin_shangchuan1_yijileimu.setVisibility(View.GONE);
            requestHandleArrayList.add(requestAction.getLeimu(this));
        } else {
            sp_shangpin_shangchuan1_yijileixing.setVisibility(View.GONE);
            tv_shangpin_shangchuan1_yijileimu.setVisibility(View.VISIBLE);
            tv_shangpin_shangchuan1_yijileimu.setText(zhuyingleimu);
            yijileimuID = zhuyingleimuid;
            yijileimu = zhuyingleimu;
            requestHandleArrayList.add(requestAction.getLeimuEr(this, zhuyingleimuid));
        }
        if ("edit".equals(intent.getStringExtra("type"))) {
            sp_shangpin_shangchuan1_yijileixing.setVisibility(View.GONE);
            sp_shangpin_shangchuan1_erjileimu.setVisibility(View.GONE);
            sp_shangpin_shangchuan1_sanjileimu.setVisibility(View.GONE);
            tv_shangpin_shangchuan1_yijileimu.setVisibility(View.VISIBLE);
            tv_shangpin_shangchuan1_erjileimu.setVisibility(View.VISIBLE);
            tv_shangpin_shangchuan1_sanjileimu.setVisibility(View.VISIBLE);
            ll_shangpin_shangchuan_xuzhi.setVisibility(View.GONE);
            CommonUtils.setTitleBar((Activity) mContext, true, "商品信息", "");
            bt_shangpin_shangchuan1_shangchuan.setText("保存");
            rl_shangpin_shangchuan1_guige.setVisibility(View.VISIBLE);
            isEditType = true;
            productId = intent.getStringExtra("productId");
            requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone));
            requestHandleArrayList.add(requestAction.shop_wl_query(this, phone, productId));
        } else {
            CommonUtils.setTitleBar((Activity) mContext, true, "发布商品", "");
            rl_shangpin_shangchuan1_guige.setVisibility(View.GONE);
            requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone));
            adapter = new MyGVAdapter(picSrcList);
            gv_shangpin_shangchuan_tupian.setAdapter(adapter);
        }
        if ("refuse".equals(intent.getStringExtra("showbottom"))) {
            ll_view_product_action.setVisibility(View.VISIBLE);
            tv_view_product_bottom_middle.setVisibility(View.VISIBLE);
            tv_view_product_bottom_left.setText("失败原因");
            tv_view_product_bottom_middle.setText("去修改");
            tv_view_product_bottom_right.setText("删除");
        } else if ("sell".equals(intent.getStringExtra("showbottom"))) {
            tv_view_product_bottom_left.setText("下架");
            tv_view_product_bottom_right.setText("编辑");
        } else if ("soldout".equals(intent.getStringExtra("showbottom"))) {
            tv_view_product_bottom_middle.setVisibility(View.VISIBLE);
            tv_view_product_bottom_left.setText("上架");
            tv_view_product_bottom_right.setText("删除");
            tv_view_product_bottom_middle.setText("编辑");
        } else {
            ll_view_product_action.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFinish) {
            isFinish = false;
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConstantUtils.SP_ALLSHANGPINGUIGE = new ArrayList<>();
        ALLSHANGPINMIAOSHU = new ArrayList<>();
        guigeModels.clear();
    }


    public void initComponent() {
        tv_shangpin_shangchuan1_leixing = (TextView) findViewById(R.id.tv_shangpin_shangchuang1_leixing_xuanze);
        et_shangpin_shangchuan1_mingcheng = (EditText) findViewById(R.id.et_shangpin_shangchuan_minngcheng);
        et_shangpin_shangchuan1_yunfei = (EditText) findViewById(R.id.et_shangpin_shangchuan_yunfei);
        rl_shangpin_shangchuan1_miaoshu = (RelativeLayout) findViewById(R.id.rl_shangpin_shangchuan_miaoshu);
        rl_shangpin_shangchuan1_guige = (RelativeLayout) findViewById(R.id.rl_shangpin_shangchuan_guige);
        bt_shangpin_shangchuan1_shangchuan = (Button) findViewById(R.id.btn_shangpin_shangchuan_shangchuan);
        rl_shangpin_shangchuan1_fahuodi = (RelativeLayout) findViewById(R.id.rl_shangpin_shangchuan_fahuodi);
        sp_shangpin_shangchuan1_erjileimu = (Spinner) findViewById(R.id.sp_shangpin_shangchuan1_erjileixing);
        sp_shangpin_shangchuan1_sanjileimu = (Spinner) findViewById(R.id.sp_shangpin_shangchuan1_sanjileixing);
        sp_shangpin_shangchuan1_freight = (Spinner) findViewById(R.id.sp_shangpin_shangchuan1_freight);
        tv_shangpin_shangchuan1_yijileimu = (TextView) findViewById(R.id.tv_shangpin_shangchuan1_yijileixing);
        tv_shangpin_shangchuan1_erjileimu = (TextView) findViewById(R.id.tv_shangpin_shangchuan1_erjileixing);
        tv_shangpin_shangchuan1_sanjileimu = (TextView) findViewById(R.id.tv_shangpin_shangchuan1_sanjileixing);
        tv_shangpin_shangchuan_miaoshu_info = (TextView) findViewById(R.id.tv_shangpin_shangchuan_miaoshu_info);
        gv_shangpin_shangchuan_tupian = (GridView) findViewById(R.id.gd_shangpin_sshangchuang1_pictures);
        tv_shangpin_shangchuan_fahuodi_info = (TextView) findViewById(R.id.tv_shangpin_shangchuan_fahuodi_info);
        tv_shangpin_mingcheng_zishu = (TextView) findViewById(R.id.tv_shangpin_mingcheng_zishu);
        tv_shangpin_shangchuan_feilv = (TextView) findViewById(R.id.tv_shangpin_shangchuan_feilv);
        tv_view_product_bottom_right = (TextView) findViewById(R.id.tv_view_product_bottom_right);
        tv_view_product_bottom_middle = (TextView) findViewById(R.id.tv_view_product_bottom_middle);
        tv_view_product_bottom_left = (TextView) findViewById(R.id.tv_view_product_bottom_left);
        sp_shangpin_shangchuan1_yijileixing = (Spinner) findViewById(R.id.sp_shangpin_shangchuan1_yijileixing);
        ll_shangpin_shangchuan_xuzhi = (LinearLayout) findViewById(R.id.ll_shangpin_shangchuan_xuzhi);
        ll_view_product_action = (LinearLayout) findViewById(R.id.ll_view_product_action);
        cb_shangchuan_xuzhi = (CheckBox) findViewById(R.id.cb_shangchuan_xuzhi);
        tv_shangchuang_xuzhi = (TextView) findViewById(R.id.tv_shangchuang_xuzhi);
        tv_shangchuang_xize = (TextView) findViewById(R.id.tv_shangchuang_xize);
        tv_shangpin_shangchuan1_leixing.setOnClickListener(this);
        rl_shangpin_shangchuan1_fahuodi.setOnClickListener(this);
        rl_shangpin_shangchuan1_miaoshu.setOnClickListener(this);
        rl_shangpin_shangchuan1_guige.setOnClickListener(this);
        tv_shangpin_shangchuan1_yijileimu.setOnClickListener(this);
        tv_shangpin_shangchuan1_erjileimu.setOnClickListener(this);
        tv_shangpin_shangchuan1_sanjileimu.setOnClickListener(this);
        bt_shangpin_shangchuan1_shangchuan.setOnClickListener(this);
        tv_view_product_bottom_right.setOnClickListener(this);
        tv_view_product_bottom_left.setOnClickListener(this);
        tv_view_product_bottom_middle.setOnClickListener(this);
        tv_shangchuang_xuzhi.setOnClickListener(this);
        tv_shangchuang_xize.setOnClickListener(this);
        iv_freight_add = (ImageView) findViewById(R.id.iv_freight_add);
        interceptRelativeLayout = (InterceptRelativeLayout) findViewById(R.id.interceptRelativeLayout);
        interceptRelativeLayout.setAllowIds(new int[]{R.id.tv_shangpin_shangchuan_miaoshu_info, R.id.tv_shangpin_shangchuan_guige_info, R.id.imageView9, R.id.imageView10});
        et_shangpin_shangchuan1_mingcheng.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_shangpin_mingcheng_zishu.setText(s.toString().length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择类型
            case R.id.tv_shangpin_shangchuang1_leixing_xuanze:
                if (isEditType) {
                    Toast.makeText(this, "该信息不可修改", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ViewProductActivity.this, XuanzeShangPinLeixingAvtivity.class);
                    intent.putExtra("类型", tv_shangpin_shangchuan1_leixing.getText().toString());
                    startActivityForResult(intent, 5);
                }
                break;
            //添加描述
            case R.id.rl_shangpin_shangchuan_miaoshu:
                Intent miaoshu = new Intent(mContext, ViewShangpinMiaoshuActivity.class);
                if (isEditType) {
                    miaoshu.putExtra("type", "edit");
                }
                startActivityForResult(miaoshu, 3);
                break;
            //选择发货地
            case R.id.rl_shangpin_shangchuan_fahuodi:
                if (isEditType) {
                    Toast.makeText(this, "该信息不可修改", Toast.LENGTH_SHORT).show();
                } else {
                    miaoshu = new Intent(mContext, DizhiXuanzeActivity.class);
                    startActivityForResult(miaoshu, 6);
                }
                break;
            //商品上传
            case R.id.btn_shangpin_shangchuan_shangchuan:
                //商品上传 账号  商品名称   发货地址id  运费 商品描述  类目   商品规格   主营类目  商品类型 商城类别  图片列表
                if (shangpinshangchuanpd()) {
                    if (tv_shangpin_shangchuan1_leixing.getText().toString().equals("实物商品")) {
                        yunfei = freightTemplateModel.getFreight();
                    } else {
                        if (freightTemplateModel.getFreeExpress().get()) {
                            yunfei = "0";
                        } else {
                            MToast.showToast("电子卡卷商品请设置运费包邮");
                            break;
                        }
                    }
                    if (null != yijileimu && (yijileimu.equals("时尚服装") || yijileimu.equals("鞋靴箱包"))) {
                        shangpin_xiangqing_id = "1";
                    } else {
                        shangpin_xiangqing_id = "0";
                    }

                    if (isEditType) {
                        requestHandleArrayList.add(requestAction.shop_wl_editshop(this, phone, et_shangpin_shangchuan1_mingcheng.getText().toString(), fahuodiId, yunfei
                                , allmiaoshu, yijileimu + ">" + erjileimu + ">" + sanjileimu, zhuyingleimu, tv_shangpin_shangchuan1_leixing.getText().toString(), shangchengleibie, picUrlList, fahuodiDizhi, shangpin_xiangqing_id, dizhiInfoFinal, yijileimuID, erjileimuID, sanjileimuID, freightTemplateModel.getId(), productId, guigeModels));
                    } else {
                        if (!cb_shangchuan_xuzhi.isChecked()) {
                            MToast.showToast("请勾选发布商品须知");
                            break;
                        }
                        if (isEdit && !TextUtils.isEmpty(productId)) {
                            requestHandleArrayList.add(requestAction.shop_sp_uploadA(this, phone, et_shangpin_shangchuan1_mingcheng.getText().toString(), fahuodiId, yunfei
                                    , allmiaoshu, yijileimu + ">" + erjileimu + ">" + sanjileimu, zhuyingleimu, tv_shangpin_shangchuan1_leixing.getText().toString(), shangchengleibie, picUrlList, fahuodiDizhi, shangpin_xiangqing_id, dizhiInfoFinal, yijileimuID, erjileimuID, sanjileimuID, freightTemplateModel.getId(), productId));
                        } else {
                            requestHandleArrayList.add(requestAction.shop_sp_uploadA(this, phone, et_shangpin_shangchuan1_mingcheng.getText().toString(), fahuodiId, yunfei
                                    , allmiaoshu, yijileimu + ">" + erjileimu + ">" + sanjileimu, zhuyingleimu, tv_shangpin_shangchuan1_leixing.getText().toString(), shangchengleibie, picUrlList, fahuodiDizhi, shangpin_xiangqing_id, dizhiInfoFinal, yijileimuID, erjileimuID, sanjileimuID, freightTemplateModel.getId(), ""));
                        }
                    }
                }
                break;
            case R.id.iv_freight_add:
                startActivity(new Intent(ViewProductActivity.this, FreightTemplateActivity.class));
                break;
            case R.id.rl_shangpin_shangchuan_guige:
                Intent intent1 = new Intent(ViewProductActivity.this, ViewShangpinGuigeActivity.class);
                intent1.putExtra("zhuyingleimu", yijileimu);
                intent1.putExtra("productId", productId);
                intent1.putExtra("type", "edit");
                startActivityForResult(intent1, 2);
                break;
            case R.id.tv_shangpin_shangchuan1_yijileixing:
            case R.id.tv_shangpin_shangchuan1_erjileixing:
            case R.id.tv_shangpin_shangchuan1_sanjileixing:

                if (isEditType) {
                    Toast.makeText(this, "该信息不可修改", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_shangchuang_xuzhi:
                Intent intent2 = new Intent(mContext, WebActivity.class);
                intent2.putExtra("url", ConstantUtils.UPLOAD_INFORM_URL);
                startActivity(intent2);
                break;
            case R.id.tv_shangchuang_xize:
                Intent intent3 = new Intent(mContext, WebActivity.class);
                intent3.putExtra("url", ConstantUtils.UPLOAD_DETAIL_URL);
                startActivity(intent3);
                break;
            case R.id.tv_view_product_bottom_right:
//                if ("refuse".equals(intent.getStringExtra("showbottom"))) {
//                    Intent intent5 = new Intent(mContext, ShangPinShangChuanActivity.class);
//                    intent5.putExtra("type", "edit");
//                    intent5.putExtra("productId", productId);
//                    startActivity(intent5);
//                } else
                if ("sell".equals(intent.getStringExtra("showbottom"))) {


                    DialogUtlis.oneBtnSingleChoice(mContext, "选择编辑方式", new String[]{"快速编辑(不需后台审核)", "正常编辑(1-3工作日审核)"}, new MyDialogBuilder.SingleChoiceOnItemClickWithPosition() {
                        @Override
                        public void onItemClick(String data, int position) {
                            if (position == 0) {
                                Intent quickEdit = new Intent(ViewProductActivity.this, ViewShangpinGuigeActivity.class);
                                quickEdit.putExtra("zhuyingleimu", yijileimu);
                                quickEdit.putExtra("productId", productId);
                                quickEdit.putExtra("type", "edit");
                                quickEdit.putExtra("isQuicklyEdit", "yes");
                                startActivityForResult(quickEdit, 2);
                            } else {
                                Intent intent5 = new Intent(mContext, ShangPinShangChuanActivity.class);
                                intent5.putExtra("type", "edit");
                                intent5.putExtra("productId", productId);
                                startActivity(intent5);
                            }
                        }
                    });
                } else {
                    DialogUtlis.twoBtnNormal(mContext, "是否删除该商品信息？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestHandleArrayList.add(requestAction.shop_wl_delshops(ViewProductActivity.this, phone, productId));
                        }
                    });
                }
//                if ("soldout".equals(intent.getStringExtra("showbottom")))
                break;
            case R.id.tv_view_product_bottom_left:
                if ("refuse".equals(intent.getStringExtra("showbottom"))) {
                    Intent intent4 = new Intent(mContext, AuditResultActivity.class);
//                    intent4.putExtra("refuseProduct", getIntent().getSerializableExtra("refuseProduct"));
                    intent4.putExtra("productId", productId);
                    intent4.putExtra("productName", et_shangpin_shangchuan1_mingcheng.getText().toString());
                    startActivity(intent4);
                } else if ("sell".equals(intent.getStringExtra("showbottom"))) {
                    xiajia_dialog();
                } else if ("soldout".equals(intent.getStringExtra("showbottom"))) {
                    DialogUtlis.twoBtnNormal(mContext, "是否上架？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            spgl.clear();
                            ShangpinGuanliBean bean = new ShangpinGuanliBean();
                            bean.setId(productId);
                            bean.setShangpinmingcheng(productName);
                            spgl.add(bean);
                            requestHandleArrayList.add(requestAction.shop_wl_shelf(ViewProductActivity.this, phone, spgl));
                        }
                    });
                }
                break;
            case R.id.tv_view_product_bottom_middle:
                Intent intent7 = new Intent(mContext, ShangPinShangChuanActivity.class);
                intent7.putExtra("type", "edit");
                intent7.putExtra("productId", productId);
                startActivity(intent7);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone));
                    requestHandleArrayList.add(requestAction.shop_wl_query(this, phone, productId));
                } else {
                    isEdit = true;
                }
                break;
            case 3:
                if (data != null) {
                    if (data.getExtras() != null) {
                        allmiaoshu = (ArrayList<MiaoShuBean>) data.getSerializableExtra("allmiaoshu");
                        if (allmiaoshu.size() >= 1) {
                            tv_shangpin_shangchuan_miaoshu_info.setText("已编辑");
                        } else {
                            tv_shangpin_shangchuan_miaoshu_info.setText("未编辑");
                        }
                    }
                }
                break;
            case 5:
                if (data != null) {
                    if (data.getExtras() != null) {
                        boolean isShiwu = data.getBooleanExtra("isShiwu", true);
                        if (isShiwu) {
                            tv_shangpin_shangchuan1_leixing.setText("实物商品");
                        } else {
                            tv_shangpin_shangchuan1_leixing.setText("电子卡卷");
                        }
                    }
                }
                break;
            case 6:
                if (data != null) {
                    if (data.getExtras() != null) {
                        dizhiInfoFinal = data.getStringArrayExtra("dizhiInfoFinal");
                        if (dizhiInfoFinal.length > 1) {
                            try {
                                fahuodiId = dizhiInfoFinal[3];
                                quID = dizhiInfoFinal[3];
                                if (TextUtils.isEmpty(fahuodiId)) {
                                    fahuodiId = dizhiInfoFinal[2];
                                    dizhiInfoFinal[3] = "";
                                }
                                fahuodiDizhi = dizhiInfoFinal[0];
                                shengID = dizhiInfoFinal[1];
                                shiID = dizhiInfoFinal[2];

                                tv_shangpin_shangchuan_fahuodi_info.setText(dizhiInfoFinal[0]);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                break;
        }
    }

    XiaJiaDialog mXiaJiaDialog;
    String xiajiaContent;
    List<ShangpinGuanliBean> spgl = new ArrayList<>();

    private void xiajia_dialog() {
        mXiaJiaDialog = new XiaJiaDialog(mContext, R.style.dialogshow, new XiaJiaDialog.ClickListenerInterface() {
            @Override
            public void cancle() {
                mXiaJiaDialog.dismiss();
            }

            @Override
            public void doConfirm(String xiajia_yuanyin) {
                spgl.clear();
                mXiaJiaDialog.clearEdidtext();
                mXiaJiaDialog.dismiss();
                ShangpinGuanliBean bean = new ShangpinGuanliBean();
                bean.setId(productId);
                bean.setShangpinmingcheng(et_shangpin_shangchuan1_mingcheng.getText().toString());
                spgl.add(bean);
                String zhanghao = preferences.getString(ConstantUtils.SP_ZHANGHAO, "");
                requestHandleArrayList.add(requestAction.shop_wl_shelevs(ViewProductActivity.this, zhanghao, spgl, xiajia_yuanyin));
            }

            @Override
            public void addEditContentListten(String content, int length) {
                xiajiaContent = content;
            }
        });
        mXiaJiaDialog.show();
    }

    private String shengID;
    private String shiID;
    private String quID;

    private boolean shangpinshangchuanpd() {
        if (picUrlList == null || picUrlList.size() == 0) {
            DialogUtlis.oneBtnNormal(mContext, "请设置商品图片");
            return false;
        } else if (et_shangpin_shangchuan1_mingcheng.getText().toString().equals("")) {
            DialogUtlis.oneBtnNormal(mContext, "请设置商品标题");
            return false;
        } else if (tv_shangpin_shangchuan1_leixing.getText().toString().equals("请选择商品属性")) {
            DialogUtlis.oneBtnNormal(mContext, "请设置商品属性");
            return false;
        } else if (TextUtils.isEmpty(erjileimu)) {
            DialogUtlis.oneBtnNormal(mContext, "请设置商品类目");
            return false;
        } else if (TextUtils.isEmpty(tv_shangpin_shangchuan_fahuodi_info.getText().toString())) {
            DialogUtlis.oneBtnNormal(mContext, "请设置发货地");
            return false;
        } else if (freightTemplateModel == null) {
            DialogUtlis.oneBtnNormal(mContext, "请选择运费模板");
            return false;
        } else if (!tv_shangpin_shangchuan_miaoshu_info.getText().toString().equals("已编辑")) {
            DialogUtlis.oneBtnNormal(mContext, "请设置图文详情");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sp_uploadA:   //商品上传
                Log.e("商品上传请求成功", jsonObject.toString());
                productId = jsonObject.getString("商品id");
                Intent intent = new Intent(ViewProductActivity.this, TianjiaShangpinGuigeActivity_new.class);
                intent.putExtra("zhuyingleimu", yijileimu);
                intent.putExtra("productId", productId);
                startActivityForResult(intent, 2);
//              myDialogBuilder = MyDialogBuilder.getInstance(mContext);
//              myDialogBuilder
//                        .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
//                        .withTitie("提示")
//                        .withCenterContene("商品上传成功，请等待审核！", null)
//                        .setBtnClick("知道了", myDialogBuilder.BtnNormal, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                myDialogBuilder.dismissNoAnimator();
//                                finish();
//                            }
//                        }).show();
                break;
            case ConstantUtils.TAG_shop_s_uplm:
                if (jsonObject.getString("状态").equals("成功")) {
                    JSONArray array = jsonObject.getJSONArray("一级列表");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String id = object.getString("一级id");
                        String leimu = object.getString("一级");
                        yijileimuInfo.put(leimu, id);
                        stairSpinnerList.add(leimu);
                    }
                    loadDataForSpinner0();
                }
                break;
            case ConstantUtils.TAG_shop_s_uplm2:
                secondSpinnerList.clear();
                JSONArray array1 = jsonObject.getJSONArray("二级列表");
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject object = array1.getJSONObject(i);
                    String leimu = object.getString("二级");
                    String id = object.getString("二级id");
                    String feilv = object.getString("提点");
                    erjileimuInfo.put(leimu, id);
                    feilvInfo.put(leimu, feilv);
                    secondSpinnerList.add(leimu);
                }
                loadDataForSpinner();
                break;
            case ConstantUtils.TAG_shop_s_uplm3:
                sanjileimuInfo.clear();
                thridSpinnerList.clear();
                JSONArray array2 = jsonObject.getJSONArray("三级列表");
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject object = array2.getJSONObject(i);
                    String leimu = object.getString("三级");
                    sanjileimuInfo.put(leimu, object.getString("三级id"));
                    thridSpinnerList.add(leimu);
                }
                loadDataForSpinner1();
                break;

            case ConstantUtils.TAG_shop_wl_exFeeModel:
                JSONArray array = jsonObject.getJSONArray("list");
                freightTemplateNames.clear();
                freightTemplateModels.clear();
                if (array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        FreightTemplateModel model = new FreightTemplateModel();
                        ObservableBoolean isFreeExpress = new ObservableBoolean();
                        isFreeExpress.set("是".equals(object.getString("包邮")));
                        model.setId(object.getString("id"));
                        model.setName(object.getString("名称"));
                        if ("否".equals(object.getString("包邮"))) {
                            model.setFreight(object.getString("运费"));
                            model.setUnit(object.getString("单位"));
                            model.setPriceType(object.getString("类型"));
                            model.setAdd(object.getString("加添"));
                            model.setAddNum(object.getString("加额"));
                            model.setStrandard(object.getString("规格"));
                            model.setLimiters(object.getString("限定条件"));
                            model.setLimitersType(object.getString("限定类型"));
                        }
                        ObservableBoolean isLimitMoney = new ObservableBoolean();
                        ObservableBoolean isLimitNum = new ObservableBoolean();
                        model.setIsLimitMoney(isLimitMoney);
                        model.setIsLimitNum(isLimitNum);
                        model.setFreeExpress(isFreeExpress);
                        freightTemplateNames.add(model.getName());
                        freightTemplateModels.add(model);
                    }
                } else {
                    freightTemplateNames.add("暂无模板");
                }
                loadDataForFreight();
                break;
            case ConstantUtils.TAG_shop_wl_query:
                ALLSHANGPINMIAOSHU.clear();
                picUrlList.clear();
                guigeModels.clear();
                JSONArray infoarray = jsonObject.getJSONArray("商品信息");
                JSONObject info = infoarray.getJSONObject(0);
                productId = info.getString("id");
                productName = info.getString("商品名称");
                et_shangpin_shangchuan1_mingcheng.setText(info.getString("商品名称"));
                JSONArray content = info.getJSONArray("内容");
                if (content.length() > 0) {
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject jsonObject1 = content.getJSONObject(i);
                        MiaoShuBean miaoShuBean = new MiaoShuBean(jsonObject1.getString("描述"), jsonObject1.getString("图片"));
                        ALLSHANGPINMIAOSHU.add(miaoShuBean);
                    }
                    allmiaoshu = ALLSHANGPINMIAOSHU;
                    if (allmiaoshu.size() >= 1) {
                        tv_shangpin_shangchuan_miaoshu_info.setText("已编辑");
                    } else {
                        tv_shangpin_shangchuan_miaoshu_info.setText("未编辑");
                    }
                }
                JSONArray picture = info.getJSONArray("缩略图");
                if (picture.length() > 0) {
                    for (int i = 0; i < picture.length(); i++) {
                        JSONObject jsonObject1 = picture.getJSONObject(i);
                        String pictureUrl = jsonObject1.getString("图片");
                        picUrlList.add(pictureUrl);
                    }
                    adapter = new MyGVAdapter(picUrlList);
                    gv_shangpin_shangchuan_tupian.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                }
                yijileimu = info.getString("一级分类");
                erjileimu = info.getString("二级分类");
                secondType = info.getString("二级分类");
                sanjileimu = info.getString("三级分类");
                thirdType = info.getString("三级分类");
                try {
                    tv_shangpin_shangchuan_feilv.setText(info.getString("提点"));
                } catch (Exception e) {

                }
                tv_shangpin_shangchuan1_yijileimu.setText(yijileimu);
                tv_shangpin_shangchuan1_erjileimu.setText(erjileimu);
                tv_shangpin_shangchuan1_sanjileimu.setText(sanjileimu);
                yijileimuID = info.getString("一级分类id");
                erjileimuID = info.getString("二级分类id");
                sanjileimuID = info.getString("三级分类id");
//                if (isDealer) {//自营
//                    for (int i = 0; i < stairSpinnerList.size(); i++) {
//                        if (yijileimu.equals(stairSpinnerList.get(i))) {
//                            sp_shangpin_shangchuan1_yijileixing.setSelection(i);
//                        }
//                    }
//                } else {//非自营
//                    for (int i = 0; i < secondSpinnerList.size(); i++) {
//                        if (erjileimu.equals(secondSpinnerList.get(i))) {
//                            sp_shangpin_shangchuan1_erjileimu.setSelection(i);
//                        }
//                    }
//                }


                tv_shangpin_shangchuan1_leixing.setText(info.getString("商品类型"));
                dizhiInfoFinal[0] = "";
//                dizhiInfoFinal[1] = info.getString("省id");
                dizhiInfoFinal[1] = "0";
                dizhiInfoFinal[2] = "0";
                dizhiInfoFinal[3] = info.getString("区id");
                if (TextUtils.isEmpty(info.getString("区"))) {
                    tv_shangpin_shangchuan_fahuodi_info.setText(info.getString("市"));
                } else {
                    tv_shangpin_shangchuan_fahuodi_info.setText(info.getString("区"));
                }
                yunfeiId = info.getString("运费id");
                for (int j = 0; j < freightTemplateModels.size(); j++) {
                    if (yunfeiId.equals(freightTemplateModels.get(j).getId())) {
                        sp_shangpin_shangchuan1_freight.setSelection(j);
                    }
                }
                JSONArray property = jsonObject.getJSONArray("商品属性");
                if (property.length() > 0) {
                    for (int i = 0; i < property.length(); i++) {
                        JSONObject jsonObject1 = property.getJSONObject(i);
                        GuigeModel guigeModel;
                        if (TextUtils.isEmpty(jsonObject1.getString("颜色"))) {
                            guigeModel = new GuigeModel(true);
                        } else {
                            guigeModel = new GuigeModel(false);
                        }
                        if (property.length() == 1) {
                            guigeModel.setOnlyOne(true);
                        } else {
                            guigeModel.setOnlyOne(true);
                        }
                        guigeModel.setColor(jsonObject1.getString("颜色"));
                        guigeModel.setGuige(jsonObject1.getString("规格"));
                        guigeModel.setGonghuojia(jsonObject1.getString("供货价"));
                        guigeModel.setJianyijia(jsonObject1.getString("建议零售价"));
                        guigeModel.setKucun(jsonObject1.getString("库存"));
                        guigeModel.setSize(jsonObject1.getString("尺寸"));
                        guigeModel.setId(jsonObject1.getString("id"));
                        guigeModel.setMaxGonghuojia(Double.parseDouble(guigeModel.getGonghuojia()) * 1.1);
                        guigeModel.setMaxJianyijia(Double.parseDouble(guigeModel.getJianyijia()) * 1.1);
                        guigeModel.setIncreaseGonghuojia(!jsonObject1.getString("rise").equals("是"));
                        guigeModel.setIncreaseJianyijia(!jsonObject1.getString("rise").equals("是"));
                        guigeModels.add(guigeModel);
                    }
                }
                break;

            case ConstantUtils.TAG_shop_wl_editshop:
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                ChuShouZhongFragment.isRefush = true;
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_shelevs:
                MToastUtils.showToast("下架成功");
                ChuShouZhongFragment.isRefush = true;
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_shelf:
                MToastUtils.showToast("上架成功");
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_delshops:
                MToastUtils.showToast("删除成功");
                if ("refuse".equals(getIntent().getStringExtra("showbottom"))) {
                    ShenHeZhongFragment.isRefush = true;
                }
                finish();
                break;

        }
    }


    private ArrayAdapter<String> yijiAdapter;

    private void loadDataForSpinner0() {
        yijiAdapter = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, stairSpinnerList);
        yijiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_shangpin_shangchuan1_yijileixing.setAdapter(yijiAdapter);
        sp_shangpin_shangchuan1_yijileixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                secondSpinnerList.clear();
                erjileimu = "";
                if (erjiAdapter != null) {
                    erjiAdapter.notifyDataSetChanged();
                }
                yijileimu = (String) sp_shangpin_shangchuan1_yijileixing.getSelectedItem();
                yijileimuID = yijileimuInfo.get(yijileimu);
                requestHandleArrayList.add(requestAction.getLeimuEr(ViewProductActivity.this, yijileimuInfo.get(yijileimu)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private List<String> changeList = new ArrayList<>();// 上传或删除的图片集合
    private String stationPics;//所有图片的字符串拼接
    private ArrayAdapter<String> erjiAdapter;

    private void loadDataForSpinner() {
        erjiAdapter = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, secondSpinnerList);
        erjiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_shangpin_shangchuan1_erjileimu.setAdapter(erjiAdapter);
        sp_shangpin_shangchuan1_erjileimu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                thridSpinnerList.clear();
                sanjileimu = "";
                if (sanjiAdapter != null) {
                    sanjiAdapter.notifyDataSetChanged();
                }
                erjileimu = (String) sp_shangpin_shangchuan1_erjileimu.getSelectedItem();
                String feilv = feilvInfo.get(erjileimu);
                if (!isEditType) {
                    if (TextUtils.isEmpty(feilv)) {
                        tv_shangpin_shangchuan_feilv.setText("0");
                    } else {
                        tv_shangpin_shangchuan_feilv.setText(feilv);
                    }
                }
                erjileimuID = erjileimuInfo.get(erjileimu);
                requestHandleArrayList.add(requestAction.getLeimuSan(ViewProductActivity.this, erjileimuInfo.get(erjileimu)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        if (isEditType && !TextUtils.isEmpty(secondType)) {
            for (int i = 0; i < secondSpinnerList.size(); i++) {
                if (secondType.equals(secondSpinnerList.get(i))) {
                    sp_shangpin_shangchuan1_erjileimu.setSelection(i);
                }
            }
            secondType = "";
        }


    }

    private ArrayAdapter<String> sanjiAdapter;

    /**
     * 三级类目下拉列表适配器
     */
    private void loadDataForSpinner1() {
        sanjiAdapter = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style1, R.id.tv_shangpin_shangchuan1_txtvwSpinner1, thridSpinnerList);
        sanjiAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style1);
        sp_shangpin_shangchuan1_sanjileimu.setAdapter(sanjiAdapter);
        sp_shangpin_shangchuan1_sanjileimu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                sanjileimu = (String) sp_shangpin_shangchuan1_sanjileimu.getSelectedItem();
                sanjileimuID = sanjileimuInfo.get(sanjileimu);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        if (isEditType && !TextUtils.isEmpty(thirdType)) {
            for (int i = 0; i < thridSpinnerList.size(); i++) {
                if (thirdType.equals(thridSpinnerList.get(i))) {
                    sp_shangpin_shangchuan1_sanjileimu.setSelection(i);
                }
            }
            thirdType = "";
        }
    }

    private ArrayAdapter<String> greightAdapter;

    /**
     * 运费模板拉列表适配器
     */
    private void loadDataForFreight() {
        greightAdapter = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style1, R.id.tv_shangpin_shangchuan1_txtvwSpinner1, freightTemplateNames);
        greightAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style1);
        sp_shangpin_shangchuan1_freight.setAdapter(greightAdapter);
        sp_shangpin_shangchuan1_freight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (freightTemplateModels.size() > position) {
                    freightTemplateModel = freightTemplateModels.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * gridview适配器
     */
    class MyGVAdapter extends BaseAdapter {
        private boolean isMax;
        private List<String> list = new ArrayList<>();

        public MyGVAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() >= MAXPICTURES) {
                isMax = true;
                return MAXPICTURES;
            } else {
                isMax = false;
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View view = View.inflate(ViewProductActivity.this, R.layout.item_shangpin_shanghcuan1_grid, null);

            final ImageView mImageView = (ImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识
            ViewProductActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (list.size() == 0) {
                        mSelect.setVisibility(View.GONE);
//                        mImageView.setBackgroundResource(R.mipmap.btn_leibie_xiangji);
                        mImageView.setVisibility(View.GONE);
                    } else if (position == list.size() && list.size() < MAXPICTURES) {
                        mSelect.setVisibility(View.GONE);
//                        mImageView.setBackgroundResource(R.mipmap.btn_leibie_xiangji);
                        mImageView.setVisibility(View.GONE);
                    } else {
                        mSelect.setVisibility(View.VISIBLE);
                        CommonUtils.setDisplayImageOptions(mImageView, list.get(position), 0);
                    }
                }
            });
            if (list.size() > 0) {
                mSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        Log.e("TAG", "onClick--position: " + position);
                        Log.e("TAG", "onClick:--size: " + picUrlList.size());
                        if (!isEditType) {
                            picUrlList.remove(position);
                        }
                        notifyDataSetChanged();
                    }
                });
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imageList", (ArrayList<String>) list);
                        bundle.putString("isLocationPicture", String.valueOf(true));
                        bundle.putInt("position", position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
            if (!isMax) {
                if (position == list.size()) {
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setShangpin_tupian();
                        }
                    });
                }
            }
            return view;
        }
    }

    //选择图片
    private void setShangpin_tupian() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                new ChoosePhoto(mContext) {
                    @Override
                    protected void setPicsSuccess(List<String> picsSrc, List<String> picsUrl) {
                        picUrlList.addAll(picsUrl);
                        picSrcList.addAll(picsSrc);
                        ViewProductActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    protected void setPicsFailure(List<String> picsSrc, List<String> picsUrl, List<Boolean> pics_upload_sf) {
                        MToast.showToast("有部分图片上传失败");
                    }
                }.setPics(true, isEditType ? 6 - picUrlList.size() : 6 - picSrcList.size());
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA);
    }
}
