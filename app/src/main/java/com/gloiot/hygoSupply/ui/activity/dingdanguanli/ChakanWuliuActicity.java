package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.WuliuBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChakanWuliuActicity extends BaseActivity {
    @Bind(R.id.iv_chakan_wuliu)
    ImageView iv_chakan_wuliu;
    @Bind(R.id.tv_chakan_wuliu_zhuangtai)
    TextView tv_chakan_wuliu_zhuangtai;
    @Bind(R.id.tv_chakan_wuliu_chengyun_laiyuan)
    TextView tv_chakan_wuliu_chengyun_laiyuan;
    @Bind(R.id.tv_chakan_wuliu_yundan_bianhao)
    TextView tv_chakan_wuliu_yundan_bianhao;
    @Bind(R.id.tv_chakan_wuliu_guanfang_dianhua)
    TextView tv_chakan_wuliu_guanfang_dianhua;
    @Bind(R.id.lv_chakan_wuliu)
    ListView lv_chakan_wuliu;
    @Bind(R.id.rl_chakan_wuliu)
    RelativeLayout rlChakanWuliu;
    @Bind(R.id.rl_chakan_wuliu_zanwu)
    RelativeLayout rlChakanWuliuZanwu;

    private String dingdanid, phone, suolvtu, wuliu, kuaidigongsi, kuaididanhao, shouhuoren, guanfangdianhua, shangpinID, saleId,state;
    private List<WuliuBean> allWuliu = new ArrayList<>();
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_chakan_wuliu;
    }

    @Override
    public void initData() {
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        CommonUtils.setTitleBar(this, true, "查看物流", "");
        dingdanid = (String) getIntent().getExtras().get("id");
        saleId = (String) getIntent().getExtras().get("saleId");
        suolvtu = (String) getIntent().getExtras().get("suolvtu");
        shangpinID = (String) getIntent().getExtras().get("shangpinId");
        state = (String) getIntent().getExtras().get("state");
        requestHandleArrayList.add(requestAction.shop_kuaidi_query(ChakanWuliuActicity.this, phone, dingdanid, shangpinID, saleId,state));
        CommonUtils.setDisplayImageOptions(iv_chakan_wuliu, suolvtu, 4);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_kuaidi_query:
                JSONObject obj = response.getJSONObject("列表");
                shouhuoren = obj.getString("收货人");
                kuaidigongsi = obj.getString("快递公司");
                kuaididanhao = obj.getString("快递单号");
                guanfangdianhua = obj.getString("联系方式");
//                wuliu = obj.getString("物流状态");
//                tv_chakan_wuliu_zhuangtai.setText(wuliu);
                tv_chakan_wuliu_chengyun_laiyuan.setText("承运公司: " + kuaidigongsi);
                tv_chakan_wuliu_yundan_bianhao.setText("订单编号: " + kuaididanhao);
                tv_chakan_wuliu_guanfang_dianhua.setText("承运电话: " + guanfangdianhua);

                JSONArray jsonArray = response.getJSONArray("快递");
                if (jsonArray.length() <= 0) {
                    rlChakanWuliu.setVisibility(View.GONE);
                    lv_chakan_wuliu.setVisibility(View.GONE);
                    rlChakanWuliuZanwu.setVisibility(View.VISIBLE);
                } else {
                    rlChakanWuliu.setVisibility(View.VISIBLE);
                    lv_chakan_wuliu.setVisibility(View.VISIBLE);
                    rlChakanWuliuZanwu.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject kuaidiObj = jsonArray.getJSONObject(i);
                        String time = kuaidiObj.getString("time");
                        String context = kuaidiObj.getString("context");
                        String ftime = kuaidiObj.getString("ftime");

                        WuliuBean wuliuBean = new WuliuBean(time, context, ftime);
                        allWuliu.add(wuliuBean);
                    }
                    allWuliu.get(0).setFirst(true);
                    adapter = new CommonAdapter<WuliuBean>(this, R.layout.item_dingdan_chakanwuliu, allWuliu) {
                        @Override
                        public void convert(ViewHolder holder, WuliuBean wuliuBean) {
                            Log.e("设置查看物流数据", wuliuBean.toString());
                            if (holder.getmPosition() == 0) {
                                ImageView view_dian = holder.getView(R.id.item_dingdan_chakanwuliu_dian);
                                View view_xian = holder.getView(R.id.item_dingdan_chakanwuliu_xian);
                                if (wuliuBean.getFirst()) {
                                    view_xian.setBackgroundColor(Color.rgb(255, 255, 255));
                                    view_dian.setImageResource(R.mipmap.wancheng);
                                } else {
                                    view_xian.setBackgroundColor(Color.rgb(238, 238, 238));
                                    view_dian.setImageResource(R.mipmap.quanquan);
                                }
                            } else {
                                holder.setBackgroundColor(R.id.item_dingdan_chakanwuliu_xian, Color.rgb(153, 153, 153));
                                holder.setImageResource(R.id.item_dingdan_chakanwuliu_dian, R.mipmap.quanquan);
                            }
                            holder.setText(R.id.tv_chakan_wuliu_context, wuliuBean.getContext());
                            holder.setText(R.id.tv_chakan_wuliu_time, wuliuBean.getTime());
                        }
                    };
                    lv_chakan_wuliu.setAdapter(adapter);
                }
                break;
        }
    }

    @Override
    public void statusUnusual(JSONObject response) throws JSONException {
        super.statusUnusual(response);
        rlChakanWuliu.setVisibility(View.GONE);
        lv_chakan_wuliu.setVisibility(View.GONE);
        rlChakanWuliuZanwu.setVisibility(View.VISIBLE);
    }
}
