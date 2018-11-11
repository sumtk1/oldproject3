//package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.gloiot.hygoSupply.R;
//import com.gloiot.hygoSupply.bean.GuigeModel;
//import com.gloiot.hygoSupply.bean.MiaoShuBean;
//import com.gloiot.hygoSupply.ui.activity.BaseActivity;
//import com.gloiot.hygoSupply.ui.activity.dianpuguanli.PreviewPictureActivity;
//import com.gloiot.hygoSupply.utlis.CommonUtils;
//import com.gloiot.hygoSupply.utlis.ConstantUtils;
//import com.gloiot.hygoSupply.utlis.DialogUtlis;
//import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
//import com.zyd.wlwsdk.utlis.MToast;
//import com.zyd.wlwsdk.widge.swipe.NoSlipGridview;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class ShangPinShangChuanActivity1 extends BaseActivity implements View.OnClickListener {
//
//    @Bind(R.id.et_shangpin_shangchuan_minngcheng)
//    EditText et_shangpin_shangchuan_minngcheng;
//    @Bind(R.id.tv_shangpin_mingcheng_zishu)
//    TextView tv_shangpin_mingcheng_zishu;
//    @Bind(R.id.tv_shangpin_shangchuang1_leixing)
//    TextView tv_shangpin_shangchuang1_leixing;
//    @Bind(R.id.tv_shangpin_shangchuang1_leixing_xuanze)
//    TextView tv_shangpin_shangchuang1_leixing_xuanze;
//    @Bind(R.id.rl_shangpin_shangchuan_leixing)
//    RelativeLayout rl_shangpin_shangchuan_leixing;
//    @Bind(R.id.sp_shangpin_shangchuan1_sanjileixing)
//    Spinner sp_shangpin_shangchuan1_sanjileixing;
//    @Bind(R.id.tv_shangpin_shangchuan1_yijileixing)
//    TextView tv_shangpin_shangchuan1_yijileixing;
//    @Bind(R.id.sp_shangpin_shangchuan1_erjileixing)
//    Spinner sp_shangpin_shangchuan1_erjileixing;
//    @Bind(R.id.tv_shangpin_shangchuan_fahuodi_info)
//    TextView tv_shangpin_shangchuan_fahuodi_info;
//    @Bind(R.id.rl_shangpin_shangchuan_fahuodi)
//    RelativeLayout rl_shangpin_shangchuan_fahuodi;
//    @Bind(R.id.et_shangpin_shangchuan_yunfei)
//    EditText et_shangpin_shangchuan_yunfei;
//    @Bind(R.id.tv_shangpin_shangchuan_miaoshu_info)
//    TextView tv_shangpin_shangchuan_miaoshu_info;
//    @Bind(R.id.rl_shangpin_shangchuan_miaoshu)
//    RelativeLayout rl_shangpin_shangchuan_miaoshu;
//    @Bind(R.id.btn_shangpin_shangchuan_shangchuan)
//    Button btn_shangpin_shangchuan_shangchuan;
//    @Bind(R.id.gd_shangpin_sshangchuang1_pictures)
//    NoSlipGridview gd_shangpin_sshangchuang1_pictures;
//    @Bind(R.id.iv_shangpin_shangchuan_add)
//    ImageView iv_shangpin_shangchuan_add;
//
//    private MyGVAdapter adapter;
//    //店铺所卖商品的一级类型
//    private String zhuyingleimu = "";
//    private String zhuyingleimuid = "";
//    private static final int MAXPICTURES = 6;
//    private List<String> picUrlList = new ArrayList<>();
//    private String phone = "", erjileimu = "", sanjileimu = "";
//    private ArrayList<MiaoShuBean> allmiaoshu = new ArrayList<>();
//    private Intent intent;
//    private String fahuodiId;
//    private String fahuodiDizhi;
//    private List<String> picSrcList;
//    private List<String> spinnerList;
//    private List<String> spinnerList2;
//    private String[] dizhiInfoFinal;
//    private String shangchengleibie;
//    private String yunfei;
//    private Map<String, String> leimuInfo, sanjileimuInfo;
//    private String shangpin_xiangqing_id;
//    public static List<GuigeModel> guigeModels = new ArrayList<>();
//    private int erjileimuID = 0, sanjileimuID = 0;
//
//
//    @Override
//    public int initResource() {
//        return R.layout.activity_shangpin_shangchuan_new;
//    }
//
//    @Override
//    public void initData() {
//        //获取存储空间的手机号 请求查询发货地接口
//        CommonUtils.setTitleBar((Activity) mContext, true, "发布商品", "");
//        leimuInfo = new HashMap<>();
//        sanjileimuInfo = new HashMap<>();
//        dizhiInfoFinal = new String[4];
//        intent = getIntent();
//        zhuyingleimuid = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIEID, "");
//        zhuyingleimu = intent.getStringExtra("zhuyingleimu");
//        if (TextUtils.isEmpty(zhuyingleimu)) {
//            zhuyingleimu = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, "");
//        }
//        if (null != zhuyingleimu && (zhuyingleimu.equals("时尚服装") || zhuyingleimu.equals("鞋靴箱包"))) {
//            shangpin_xiangqing_id = "1";
//        } else {
//            shangpin_xiangqing_id = "0";
//        }
//        shangchengleibie = preferences.getString(ConstantUtils.SP_USERSCTYPE, "");
//        phone = preferences.getString(ConstantUtils.SP_MYID, "");
//        tv_shangpin_shangchuang1_leixing.setText(zhuyingleimu);
//        tv_shangpin_shangchuang1_leixing_xuanze.setOnClickListener(this);
//        rl_shangpin_shangchuan_fahuodi.setOnClickListener(this);
//        rl_shangpin_shangchuan_miaoshu.setOnClickListener(this);
//        btn_shangpin_shangchuan_shangchuan.setOnClickListener(this);
//        spinnerList = new ArrayList<>();
//        spinnerList2 = new ArrayList<>();
//        picSrcList = new ArrayList<>();
//        adapter = new MyGVAdapter(picSrcList);
//        gd_shangpin_sshangchuang1_pictures.setAdapter(adapter);
//        requestHandleArrayList.add(requestAction.getLeimuEr(this, zhuyingleimuid));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ConstantUtils.SP_ALLSHANGPINGUIGE = new ArrayList<>();
//        ConstantUtils.SP_ALLSHANGPINMIAOSHU = new ArrayList<>();
//        guigeModels.clear();
//
//        et_shangpin_shangchuan_minngcheng.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tv_shangpin_mingcheng_zishu.setText(s.toString().length() + "/30");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            //选择类型
//            case R.id.tv_shangpin_shangchuang1_leixing_xuanze:
//                Intent intent = new Intent(ShangPinShangChuanActivity1.this, XuanzeShangPinLeixingAvtivity.class);
//                intent.putExtra("类型", tv_shangpin_shangchuang1_leixing_xuanze.getText().toString());
//                startActivityForResult(intent, 5);
//                break;
//            //添加描述
//            case R.id.rl_shangpin_shangchuan_miaoshu:
//                intent = new Intent(mContext, ShangpinMiaoshuActivity.class);
//                startActivityForResult(intent, 3);
//                break;
//            //选择发货地
//            case R.id.rl_shangpin_shangchuan_fahuodi:
//                intent = new Intent(mContext, DizhiXuanzeActivity.class);
//                startActivityForResult(intent, 6);
//                break;
//            //商品上传
//            case R.id.btn_shangpin_shangchuan_shangchuan:
//                //商品上传 账号  商品名称   发货地址id  运费 商品描述  类目   商品规格   主营类目  商品类型 商城类别  图片列表
//                if (shangpinshangchuanpd()) {
//                    if (tv_shangpin_shangchuang1_leixing_xuanze.getText().toString().equals("实物商品")) {
//                        yunfei = et_shangpin_shangchuan_yunfei.getText().toString();
//                    } else {
//                        yunfei = "0";
//                    }
//                    //一级类目id
//                    int yijiID = Integer.parseInt(preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIEID, ""));
//                    requestHandleArrayList.add(requestAction.shop_sp_uploadA(this, phone, et_shangpin_shangchuan_minngcheng.getText().toString(), dizhiInfoFinal[3], yunfei
//                            , allmiaoshu, zhuyingleimu + ">" + erjileimu + ">" + sanjileimu, zhuyingleimu, tv_shangpin_shangchuang1_leixing_xuanze.getText().toString(), shangchengleibie, picUrlList, fahuodiDizhi, shangpin_xiangqing_id, dizhiInfoFinal, yijiID, erjileimuID, sanjileimuID));
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 2:
//                if (resultCode == RESULT_OK) {
//                    finish();
//                }
//                break;
//            case 3:
//                if (data != null) {
//                    if (data.getExtras() != null) {
//                        allmiaoshu = (ArrayList<MiaoShuBean>) data.getSerializableExtra("allmiaoshu");
//                        if (allmiaoshu.size() >= 1) {
//                            tv_shangpin_shangchuan_miaoshu_info.setText("已编辑");
//                        } else {
//                            tv_shangpin_shangchuan_miaoshu_info.setText("未编辑");
//                        }
//                        for (int i = 0; i < allmiaoshu.size(); i++) {
//                            Log.e("shangpinmiaoshubean" + i, allmiaoshu.get(i).toString());
//                        }
//                    }
//                }
//                break;
//            case 5:
//                if (data != null) {
//                    if (data.getExtras() != null) {
//                        boolean isShiwu = data.getBooleanExtra("isShiwu", true);
//                        if (isShiwu) {
//                            tv_shangpin_shangchuang1_leixing_xuanze.setText("实物商品");
//                            et_shangpin_shangchuan_yunfei.setText("");
//                            et_shangpin_shangchuan_yunfei.setFocusable(true);
//                            et_shangpin_shangchuan_yunfei.setFocusableInTouchMode(true);
//                        } else {
//                            tv_shangpin_shangchuang1_leixing_xuanze.setText("电子卡卷");
//                            et_shangpin_shangchuan_yunfei.setText("统一运费 0元");
//                            et_shangpin_shangchuan_yunfei.setFocusable(false);
//                            et_shangpin_shangchuan_yunfei.setFocusableInTouchMode(false);
//                        }
//                    }
//                }
//                break;
//            case 6:
//                if (data != null) {
//                    if (data.getExtras() != null) {
//                        dizhiInfoFinal = data.getStringArrayExtra("dizhiInfoFinal");
//                        if (dizhiInfoFinal.length > 1) {
//                            fahuodiId = dizhiInfoFinal[3];
//                            fahuodiDizhi = dizhiInfoFinal[0];
//                            shengID = dizhiInfoFinal[1];
//                            shiID = dizhiInfoFinal[2];
//                            quID = dizhiInfoFinal[3];
//                            tv_shangpin_shangchuan_fahuodi_info.setText(dizhiInfoFinal[0]);
//                        }
//                    }
//                }
//                break;
//        }
//    }
//
//    private String shengID;
//    private String shiID;
//    private String quID;
//
//    private boolean shangpinshangchuanpd() {
//        if (picUrlList == null || picUrlList.size() == 0) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置商品图片");
//            return false;
//        } else if (et_shangpin_shangchuan_minngcheng.getText().toString().equals("")) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置商品标题");
//            return false;
//        } else if (tv_shangpin_shangchuang1_leixing_xuanze.getText().toString().equals("请选择商品属性")) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置商品属性");
//            return false;
//        } else if (TextUtils.isEmpty(erjileimu)) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置商品类目");
//            return false;
//        } else if (TextUtils.isEmpty(tv_shangpin_shangchuan_fahuodi_info.getText().toString())) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置发货地");
//            return false;
//        } else if (TextUtils.isEmpty(et_shangpin_shangchuan_yunfei.getText().toString())) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置运费");
//            return false;
//        } else if (!tv_shangpin_shangchuan_miaoshu_info.getText().toString().equals("已编辑")) {
//            DialogUtlis.oneBtnNormal(mContext, "请设置图文详情");
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//    @Override
//    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
//        super.requestSuccess(requestTag, response, showLoad);
//        switch (requestTag) {
//            case ConstantUtils.TAG_shop_sp_uploadA:   //商品上传
//                String productId = response.getString("商品id");
//                Intent intent = new Intent(ShangPinShangChuanActivity1.this, TianjiaShangpinGuigeActivity_new.class);
//                intent.putExtra("zhuyingleimu", zhuyingleimu);
//                intent.putExtra("productId", productId);
//                startActivityForResult(intent, 2);
//                break;
//            case ConstantUtils.TAG_shop_s_uplm:
//                spinnerList.clear();
//                JSONArray array1 = response.getJSONArray("二级列表");
//                for (int i = 0; i < array1.length(); i++) {
//                    JSONObject object = array1.getJSONObject(i);
//                    String leimu = object.getString("二级");
//                    String id = object.getString("二级id");
//                    leimuInfo.put(leimu, id);
//                    spinnerList.add(leimu);
//                }
//                loadDataForSpinner();
//                break;
//            case ConstantUtils.TAG_shop_s_uplm3:
//                sanjileimuInfo.clear();
//                spinnerList2.clear();
//                JSONArray array2 = response.getJSONArray("三级列表");
//                for (int i = 0; i < array2.length(); i++) {
//                    JSONObject object = array2.getJSONObject(i);
//                    String leimu = object.getString("三级");
//                    sanjileimuInfo.put(leimu, object.getString("三级id"));
//                    spinnerList2.add(leimu);
//                }
//                loadDataForSpinner1();
//                break;
//        }
//    }
//
//    private List<String> changeList = new ArrayList<>();// 上传或删除的图片集合
//    private String stationPics;//所有图片的字符串拼接
//    private ArrayAdapter<String> myAdapter;
//
//    private void loadDataForSpinner() {
//        myAdapter = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style, R.id.tv_shangpin_shangchuan1_txtvwSpinner, spinnerList);
//        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
//        sp_shangpin_shangchuan1_erjileixing.setAdapter(myAdapter);
//        sp_shangpin_shangchuan1_erjileixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                spinnerList2.clear();
//                sanjileimu = "";
//                if (myAdapter1 != null) {
//                    myAdapter1.notifyDataSetChanged();
//                }
//                erjileimu = (String) sp_shangpin_shangchuan1_erjileixing.getSelectedItem();
//                erjileimuID = Integer.parseInt(leimuInfo.get(erjileimu));
//                requestHandleArrayList.add(requestAction.getLeimuSan(ShangPinShangChuanActivity1.this, leimuInfo.get(erjileimu)));
//                //TODO  联网获取三级类目
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//    }
//
//    private ArrayAdapter<String> myAdapter1;
//
//    /**
//     * 类目下拉列表适配器
//     */
//    private void loadDataForSpinner1() {
//        myAdapter1 = new ArrayAdapter<>(this, R.layout.spinner_chuanjiandianpu_display_style1, R.id.tv_shangpin_shangchuan1_txtvwSpinner1, spinnerList2);
//        myAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_style1);
//        sp_shangpin_shangchuan1_sanjileixing.setAdapter(myAdapter1);
//        sp_shangpin_shangchuan1_sanjileixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                sanjileimu = (String) sp_shangpin_shangchuan1_sanjileixing.getSelectedItem();
//                sanjileimuID = Integer.parseInt(sanjileimuInfo.get(sanjileimu));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
//
//    /**
//     * gridview适配器
//     */
//    class MyGVAdapter extends BaseAdapter {
//        private boolean isMax;
//        private List<String> list = new ArrayList<>();
//
//        public MyGVAdapter(List<String> list) {
//            this.list = list;
//        }
//
//        @Override
//        public int getCount() {
//            if (list.size() >= MAXPICTURES) {
//                isMax = true;
//                return MAXPICTURES;
//            } else {
//                isMax = false;
//                return list.size() + 1;
//            }
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, final ViewGroup parent) {
//
//            View view = View.inflate(ShangPinShangChuanActivity1.this, R.layout.item_shangpin_shanghcuan1_grid, null);
//
//            final ImageView mImageView = (ImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
//            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识
//            ShangPinShangChuanActivity1.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (list.size() == 0) {
//                        mSelect.setVisibility(View.GONE);
//                        mImageView.setBackgroundResource(R.mipmap.btn_leibie_xiangji);
//                    } else if (position == list.size() && list.size() < MAXPICTURES) {
//                        mSelect.setVisibility(View.GONE);
//                        mImageView.setBackgroundResource(R.mipmap.btn_leibie_xiangji);
//                    } else {
//                        mSelect.setVisibility(View.VISIBLE);
//                        CommonUtils.setDisplayImageOptions(mImageView, list.get(position), 0);
//                    }
//                }
//            });
//            if (list.size() > 0) {
//                mSelect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        list.remove(position);
//                        picUrlList.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//                mImageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, PreviewPictureActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putStringArrayList("imageList", (ArrayList<String>) list);
//                        bundle.putString("isLocationPicture", String.valueOf(true));
//                        bundle.putInt("position", position);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
//
//            }
//            if (!isMax) {
//                if (position == list.size()) {
//                    mImageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            setShangpin_tupian();
//                        }
//                    });
//                }
//            }
//            return view;
//        }
//    }
//
//    //选择图片
//    private void setShangpin_tupian() {
//        checkPermission(new CheckPermListener() {
//            @Override
//            public void superPermission() {
//                new ChoosePhoto(mContext) {
//                    @Override
//                    protected void setPicsSuccess(List<String> picsSrc, List<String> picsUrl) {
//                        picUrlList.addAll(picsUrl);
//                        picSrcList.addAll(picsSrc);
//                        ShangPinShangChuanActivity1.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected void setPicsFailure(List<String> picsSrc, List<String> picsUrl, List<Boolean> pics_upload_sf) {
//                        MToast.showToast("有部分图片上传失败");
//                    }
//                }.setPics(true, 6 - picSrcList.size());
//            }
//        }, R.string.perm_camera, Manifest.permission.CAMERA);
//    }
//
//}
