package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.bean.DingdanXiangQingSPBean;
import com.gloiot.hygoSupply.bean.FaHuoBean;
import com.gloiot.hygoSupply.databinding.ActivityQuerenFahuoBinding;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.SelectOilCardPopupWindow;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.MyCommonAdapter;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuerenFahuoActivity extends BaseActivity implements View.OnClickListener {
    private FaHuoBean faHuoBean;
    private ObservableBoolean isShiwu;
    private boolean isNeedWuliu;
    private ObservableBoolean isOfflineObserva;
    private boolean isOffline;
    private String phone;
    private EditText et_queren_fahuo_kuaididanhao;//订单号
    private TextView et_queren_fahuo_xuanzegongsi;//公司
    private Button btn_queren_fahuo_queding;
    private String dingdanid;   //订单ID
    private SelectOilCardPopupWindow menuWindow;
    private List<String[]> KuaidigongsiSet = new ArrayList<>();
    ActivityQuerenFahuoBinding binding;
    private ListView listView;
    private List<DingdanGuanliShangpinBean> list;
    private List<DingdanXiangQingSPBean> commodityList;
    private List<String> shangpinID;
    private List<String> repairID;

    @Override

    public int initResource() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_queren_fahuo);
        return 0;
    }

    @Override
    public void initData() {
        initComponent();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        shangpinID = new ArrayList<>();
        repairID = new ArrayList<>();
        isShiwu = new ObservableBoolean();
        isOfflineObserva = new ObservableBoolean();
        faHuoBean = new FaHuoBean();

        CommonUtils.setTitleBar(this, true, "确认发货", "");
        dingdanid = (String) getIntent().getExtras().get("id");
        isOffline = getIntent().getBooleanExtra("isOffline", false);
        list = (List<DingdanGuanliShangpinBean>) getIntent().getExtras().getSerializable("list");
        if (null != list && list.size() > 0) {
            for (DingdanGuanliShangpinBean bean : list) {
                if ("买家已付款".equals(bean.getStatus())) {
                    if ("实物商品".equals(bean.get商品类型())) {
                        isNeedWuliu = true;
                    }
                    shangpinID.add(bean.get商品id());
                    repairID.add(bean.getId());
                }

            }
        }
        commodityList = (List<DingdanXiangQingSPBean>) getIntent().getExtras().getSerializable("commodityList");
        if (null != commodityList && commodityList.size() > 0) {
            for (DingdanXiangQingSPBean bean : commodityList) {
                if ("实物商品".equals(bean.getSp_productType())) {
                    isNeedWuliu = true;
                }
                shangpinID.add(bean.getSp_id());
                repairID.add(bean.getId());
            }
        }
        isOfflineObserva.set(isOffline);
//        isOfflineObserva.set(true);
        isShiwu.set(isNeedWuliu);
        faHuoBean.setIsOffline(isOfflineObserva);
        faHuoBean.setIsWuliu(isShiwu);
        binding.setFahuobean(faHuoBean);
        et_queren_fahuo_xuanzegongsi.setOnClickListener(this);
        btn_queren_fahuo_queding.setOnClickListener(this);
        requestHandleArrayList.add(requestAction.shop_ddgl_kd(QuerenFahuoActivity.this, phone, dingdanid));
        requestHandleArrayList.add(requestAction.shop_busExplain(QuerenFahuoActivity.this, phone));
    }

    public void initComponent() {
        et_queren_fahuo_xuanzegongsi = (TextView) findViewById(R.id.et_queren_fahuo_xuanzegongsi);
        et_queren_fahuo_kuaididanhao = (EditText) findViewById(R.id.et_queren_fahuo_kuaididanhao);
        btn_queren_fahuo_queding = (Button) findViewById(R.id.btn_queren_fahuo_queding);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_queren_fahuo_queding:
                if (isOffline) {
                    if (TextUtils.isEmpty(faHuoBean.getOfflineCode())) {
                        MToastUtils.showToast("请输入线下交易验证码");
                    } else {
                        requestHandleArrayList.add(requestAction.shop_wl_Linegoods(QuerenFahuoActivity.this, phone, repairID, faHuoBean.getOfflineCode()));
                    }
                } else if (isNeedWuliu) {      //需要物流运输
                    if (faHuoBean.getIsWuliu().get()) {//物流页面
                        if (!et_queren_fahuo_xuanzegongsi.getText().toString().equals("") && !et_queren_fahuo_kuaididanhao.getText().toString().equals("")) {
                            requestHandleArrayList.add(requestAction.shop_ddgl_fahuo(QuerenFahuoActivity.this, phone, et_queren_fahuo_xuanzegongsi.getText().toString(), et_queren_fahuo_kuaididanhao.getText().toString(), dingdanid, shangpinID, repairID));
                        } else {
                            MToast.showToast( "请完善发货信息");
                        }
                    } else {//无需物流页面
                        MToast.showToast( "实物商品需要物流发货");
                    }
                } else {
                    if (faHuoBean.getIsWuliu().get()) {
                        if (!et_queren_fahuo_xuanzegongsi.getText().toString().equals("") && !et_queren_fahuo_kuaididanhao.getText().toString().equals("")) {
                            requestHandleArrayList.add(requestAction.shop_ddgl_fahuo(QuerenFahuoActivity.this, phone, et_queren_fahuo_xuanzegongsi.getText().toString(), et_queren_fahuo_kuaididanhao.getText().toString(), dingdanid, shangpinID, repairID));
                        } else {
                            MToast.showToast( "请完善发货信息");
                        }
                    } else {
                        DialogUtlis.twoBtnNormal(mContext, "确定发货？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                requestHandleArrayList.add(requestAction.shop_ddgl_fahuoB(QuerenFahuoActivity.this, phone, dingdanid, repairID));
                            }
                        });
                    }
                }
                break;
            case R.id.et_queren_fahuo_xuanzegongsi:
                if (menuWindow != null) {
                    SelectKuaidi();
                }
                break;
        }
    }

    private View lastSelectView;//记录上次选择的Item
    private String gongsi;
    private int selectPosition = -10;

    private void SelectKuaidi() {
        menuWindow.showAtLocation(findViewById(R.id.ll_test), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        if (listView == null) {
            listView = menuWindow.setListViewSingle2(mContext);
            final MyCommonAdapter commonAdapter = new MyCommonAdapter<String[]>(mContext, R.layout.item_select_kuaidigongsi, KuaidigongsiSet) {
                @Override
                public void convert(ViewHolder holder, final String[] strings, int position) {
                    holder.setText(R.id.item_select_kuaidigongsi, strings[0]);
                    if (position == selectPosition) {
                        holder.setBackgroundColor(R.id.ll_select_kuaidi_gongsi, Color.rgb(128, 128, 128));
                    } else {
                        holder.setBackgroundColor(R.id.ll_select_kuaidi_gongsi, Color.rgb(255, 255, 255));
                    }
                }
            };
            listView.setAdapter(commonAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectPosition = position;
                    view.setBackgroundColor(Color.rgb(128, 128, 128));
                    gongsi = KuaidigongsiSet.get(position)[0];

                    if (lastSelectView != null) {
                        lastSelectView.setBackgroundColor(Color.rgb(255, 255, 255));
                    }
                    lastSelectView = view;
                }
            });
            menuWindow.setMaxHeight(listView);
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 取消
                case R.id.iv_cancel:

                    break;
                // 确认
                case R.id.tv_confirm:
                    et_queren_fahuo_xuanzegongsi.setText(gongsi);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_ddgl_fahuo:
                MToast.showToast( "发货成功");
                setResult(1);
                finish();
                break;
            case ConstantUtils.TAG_shop_ddgl_kd:
                JSONArray array = response.getJSONArray("列表");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    KuaidigongsiSet.add(new String[]{object.getString("名称")});
                }
                menuWindow = new SelectOilCardPopupWindow(mContext, itemsOnClick, KuaidigongsiSet);
                break;
            case ConstantUtils.TAG_shop_sj_fahuoB:
            case ConstantUtils.TAG_shop_wl_Linegoods:
                DialogUtlis.oneBtnNormal(mContext, "发货成功", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialogNoAnimator();
                        setResult(1);
                        finish();
                    }
                });
                break;
            case ConstantUtils.TAG_shop_busExplain:
                String offlineExplain = response.getString("explain");
                faHuoBean.setOfflineExplain(offlineExplain.replaceAll("\\\\n", "\n"));
                break;
        }
    }

}
