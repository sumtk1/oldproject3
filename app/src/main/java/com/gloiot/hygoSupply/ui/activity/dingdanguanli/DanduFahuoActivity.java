package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanXiangQingSPBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.dingdanfragment.DingdanGuanliFragment;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.ImageSpanUtil;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 功能：单独发货类
 * Created by ZXL on 2017/7/26.
 */

public class DanduFahuoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.lv_dandu_fahuo_shangpin)
    ListView lvShangpin;
    @Bind(R.id.iv_dandu_fahuo_shangpin_img)
    ImageView ivShangpinImg;


    private CommonAdapter adapter;
    private String dingdan_id, phone;
    private boolean isNeedWuliu;
    private boolean isOffline;
    private List<DingdanXiangQingSPBean> spBeanList = new ArrayList<>();
    private List<DingdanXiangQingSPBean> xuanzhong_list = new ArrayList<>();


    @Override
    public int initResource() {
        return R.layout.activity_dandu_fahuo;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "单独发货", "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        dingdan_id = getIntent().getStringExtra("id");
        isOffline = getIntent().getBooleanExtra("isOffline", false);
        lvShangpin.setAdapter(adapter = new CommonAdapter<DingdanXiangQingSPBean>(this, R.layout.item_ddxq_shangpin, spBeanList) {
            @Override
            public void convert(ViewHolder holder, DingdanXiangQingSPBean spBean, final int position) {
                holder.setText(R.id.tv_item_ddxq_color, spBean.getSp_guige());
                holder.setText(R.id.tv_item_ddxq_num, "x" + spBean.getSp_shuliang());
                holder.setText(R.id.tv_item_ddxq_jiage, "￥" + spBean.getSp_danjia());
                ImageView ivSelected = holder.getView(R.id.iv_item_sp_selected);
                if (spBean.is_select_ed()) //是否选中
                    ivSelected.setImageResource(R.mipmap.icon_the_selected);
                else
                    ivSelected.setImageResource(R.mipmap.icon_not_to_choose);
                holder.setVisible2(R.id.iv_item_sp_selected, true);
                ivSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spBeanList.get(position).is_select_ed()) {
                            spBeanList.get(position).setIs_select_ed(false);
                            xuanzhong_list.remove(position);
                        } else {
                            spBeanList.get(position).setIs_select_ed(true);
                            xuanzhong_list.add(spBeanList.get(position));
                        }
                        if (xuanzhong_list.size() == spBeanList.size()) {
                            ivShangpinImg.setImageResource(R.mipmap.icon_the_selected);
                        } else {
                            ivShangpinImg.setImageResource(R.mipmap.icon_not_to_choose);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                if (!"全球购".equals(spBean.getSp_type())) {
                    holder.setText(R.id.tv_item_ddxq_shangpinming, spBean.getSp_mingcheng());
                } else {
                    TextView textView = holder.getView(R.id.tv_item_ddxq_shangpinming);
                    ImageSpanUtil.getInstance().setImage(DanduFahuoActivity.this, textView, R.mipmap.ic_quanqiugou, spBean.getSp_mingcheng());
                }
                CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_item_sp_pic), spBean.getSp_tupian(), 4);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isNeedWuliu = false;
        requestHandleArrayList.add(requestAction.shop_s_ddgl_Info(this, phone
                , dingdan_id, "买家已付款"));
    }

    @OnClick({R.id.iv_dandu_fahuo_shangpin_img, R.id.tv_dandu_fahuo_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dandu_fahuo_sure: //确认发货
                if (xuanzhong_list.size() <= 0) {
                    MToastUtils.showToast("请选择发货商品");
                    return;
                }
                for (DingdanXiangQingSPBean bean : xuanzhong_list) {
                    if ("实物商品".equals(bean.getSp_productType())) {
                        isNeedWuliu = true;
                    }
                }
                Intent intent = new Intent(mContext, QuerenFahuoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", dingdan_id);
                bundle.putSerializable("commodityList", (Serializable) xuanzhong_list);
                bundle.putBoolean("isNeedWuliu", isNeedWuliu);
                intent.putExtra("isOffline", isOffline);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.iv_dandu_fahuo_shangpin_img: //商品全选
                int selected_num = xuanzhong_list.size();

                for (int i = 0; i < spBeanList.size(); i++) {
                    if (selected_num == spBeanList.size()) {
                        spBeanList.get(i).setIs_select_ed(false);
                    } else {
                        spBeanList.get(i).setIs_select_ed(true);
                    }
                }
                if (selected_num == spBeanList.size()) {
                    xuanzhong_list.clear();
                    ivShangpinImg.setImageResource(R.mipmap.icon_not_to_choose);
                } else {
                    xuanzhong_list.clear();
                    xuanzhong_list.addAll(spBeanList);
                    ivShangpinImg.setImageResource(R.mipmap.icon_the_selected);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_s_ddgl_Info:
                spBeanList.clear();
                xuanzhong_list.clear();
                JSONArray jsonArray = response.getJSONArray("商品");
                response.getString("创建时间");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        DingdanXiangQingSPBean spBean = new DingdanXiangQingSPBean();
                        spBean.setSp_type(jsonObject.getString("类型"));
                        spBean.setSp_id(jsonObject.getString("商品id"));
                        spBean.setSp_shuliang(jsonObject.getString("商品数量"));
                        spBean.setSp_guige(jsonObject.getString("商品规格"));
                        spBean.setSp_tupian(jsonObject.getString("缩略图"));
                        spBean.setSp_status(jsonObject.getString("状态"));
                        spBean.setSp_productType(jsonObject.getString("商品类型"));
                        spBean.setSp_mingcheng(jsonObject.getString("商品名称"));
                        spBean.setSp_danjia(jsonObject.getString("价格"));
                        spBean.setId(jsonObject.getString("id"));
                        if ("买家已付款".equals(spBean.getSp_status())) {
                            spBeanList.add(spBean);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void statusUnusual(JSONObject response) throws JSONException {
//        super.statusUnusual(response);
        setResult(1);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();

    }
}
