package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.FreightTemplateModel;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo03 on 2017/5/4.
 */

public class FreightTemplateActivity extends BaseActivity {

    private RecyclerView reclv_freight_list;
    private LinearLayout ll_freight_nodata;
    private List<FreightTemplateModel> freightTemplateModels;
    private FreightAdapter adapter;
    private RelativeLayout rl_freight_add;
    private String phone = "";
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private boolean isFromTrade;
    private String tradeName;
    private String tradeId;

    @Override
    public int initResource() {
        return R.layout.activity_freight_template;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar((Activity) mContext, true, "运费模板", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        freightTemplateModels = new ArrayList<>();
        adapter = new FreightAdapter();
        reclv_freight_list.setLayoutManager(new LinearLayoutManager(this));
        reclv_freight_list.setAdapter(adapter);
        colors.add(Color.rgb(255, 125, 86));//颜色集
        colors.add(Color.rgb(255, 170, 86));
        colors.add(Color.rgb(253, 212, 85));
        colors.add(Color.rgb(132, 233, 188));
        colors.add(Color.rgb(147, 210, 255));
        colors.add(Color.rgb(180, 228, 246));
        colors.add(Color.rgb(207, 241, 253));
        if ("trade".equals(getIntent().getStringExtra("type"))) {//从商品管理跳过来
            isFromTrade = true;
            tradeName = getIntent().getStringExtra("tradeName");
            tradeId = getIntent().getStringExtra("tradeId");
        }
    }


    public void initComponent() {
        reclv_freight_list = (RecyclerView) findViewById(R.id.rl_freight_list);
        rl_freight_add = (RelativeLayout) findViewById(R.id.rl_freight_add);
        ll_freight_nodata = (LinearLayout) findViewById(R.id.ll_freight_nodata);
        rl_freight_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreightTemplateActivity.this, FreightTemplateDetails.class);
                intent.putExtra("type", "new");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_exFeeModel:
                JSONArray array = response.getJSONArray("list");
                freightTemplateModels.clear();
                if (array.length() > 0) {
                    reclv_freight_list.setVisibility(View.VISIBLE);
                    ll_freight_nodata.setVisibility(View.GONE);
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
                            model.setPriceType("按" + object.getString("类型"));
                            model.setAdd(object.getString("加添"));
                            model.setAddNum(object.getString("加额"));
                            model.setLimiters(object.getString("限定条件"));
                            model.setLimitersType(object.getString("限定类型"));
                            model.setStrandard(object.getString("规格"));
                        }
                        model.setFreeExpress(isFreeExpress);
                        ObservableBoolean isLimitMoney = new ObservableBoolean();
                        ObservableBoolean isLimitNum = new ObservableBoolean();
                        if (!TextUtils.isEmpty(model.getLimitersType())) {
                            if ("最低条件".equals(model.getLimitersType())) {
                                isLimitNum.set(true);
                            } else {
                                isLimitMoney.set(true);
                            }
                        }
                        model.setIsLimitMoney(isLimitMoney);
                        model.setIsLimitNum(isLimitNum);
                        freightTemplateModels.add(model);
                    }
                } else {
                    reclv_freight_list.setVisibility(View.GONE);
                    ll_freight_nodata.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;
            case ConstantUtils.TAG_shop_wl_exFeeModel_delete:
                requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(this, phone));
                break;
            case ConstantUtils.TAG_shop_wl_shelfA:
                finish();
                MToast.showToast("该商品已恢复销售");
                break;
        }
    }

    private class FreightAdapter extends RecyclerView.Adapter<FreightAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_yunfei_liebiao, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv_freight_name.setText(freightTemplateModels.get(position).getName());
            holder.ll_freight_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtlis.twoBtnNormal(mContext, "确定删除运费模板:" + freightTemplateModels.get(position).getName(), "提示", true, "取消", "确定删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtlis.dismissDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = freightTemplateModels.get(position).getId();
                            requestHandleArrayList.add(requestAction.shop_wl_exFeeModel(FreightTemplateActivity.this, phone, id));
                            DialogUtlis.dismissDialog();
                        }
                    });

                }
            });
            holder.view_freight_color.setBackgroundColor(colors.get(position % colors.size()));
            holder.ll_freight_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FreightTemplateModel model = freightTemplateModels.get(position);
                    Intent intent = new Intent(FreightTemplateActivity.this, FreightTemplateDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.ll_freight_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = freightTemplateModels.get(position).getId();
                    requestHandleArrayList.add(requestAction.shop_wl_shelfA(FreightTemplateActivity.this, phone, tradeId, id));
                }
            });

            holder.tv_freight_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromTrade) {
                        FreightTemplateModel model = freightTemplateModels.get(position);
                        Intent intent = new Intent(FreightTemplateActivity.this, FreightTemplateDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("model", model);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
            holder.rl_freight_bottom.setVisibility(isFromTrade ? View.GONE : View.VISIBLE);
            holder.ll_freight_choice.setVisibility(isFromTrade ? View.VISIBLE : View.GONE);
            holder.ll_freight_delete.setVisibility(!isFromTrade ? View.VISIBLE : View.GONE);
            holder.ll_freight_edit.setVisibility(!isFromTrade ? View.VISIBLE : View.GONE);
        }


        @Override
        public int getItemCount() {
            return freightTemplateModels.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_freight_edit;
            LinearLayout ll_freight_delete;
            LinearLayout ll_freight_choice;
            RelativeLayout rl_freight_bottom;
            TextView tv_freight_name;
            View view_freight_color;

            public MyViewHolder(View itemView) {
                super(itemView);
                ll_freight_delete = (LinearLayout) itemView.findViewById(R.id.ll_freight_delete);
                ll_freight_edit = (LinearLayout) itemView.findViewById(R.id.ll_freight_edit);
                ll_freight_choice = (LinearLayout) itemView.findViewById(R.id.ll_freight_choice);
                tv_freight_name = (TextView) itemView.findViewById(R.id.tv_freight_name);
                view_freight_color = itemView.findViewById(R.id.view_freight_color);
                rl_freight_bottom = (RelativeLayout) itemView.findViewById(R.id.rl_freight_bottom);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

