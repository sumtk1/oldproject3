package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanXiangQingSPBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FasongShanagPingLianJieActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_shangpinglianjie)
    ListView lvShangpinglianjie;
    @Bind(R.id.but_fasong_lianjie)
    Button butFasongLianjie;
    @Bind(R.id.rl_no_product)
    RelativeLayout rlNoProduct;

    private CommonAdapter adapter;
    private List<DingdanXiangQingSPBean> shangpinList;
    private String phone;
    private int selectPosition = -1;

    @Override
    public int initResource() {
        return R.layout.activity_fasongshanagpinglianjie;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "选择商品链接", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        shangpinList = new ArrayList<>();
        setAdapter();

        requestHandleArrayList.add(requestAction.shop_s_ddgl_Info(this, phone, getIntent().getExtras().getBundle("data").getString("orderId"),
                getIntent().getExtras().getBundle("data").getString("orderState")));
    }

    @OnClick({R.id.but_fasong_lianjie})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_fasong_lianjie:
                if (selectPosition == -1) {
                    return;
                }
                DingdanXiangQingSPBean data = shangpinList.get(selectPosition);

                Bundle bundle = new Bundle();
                bundle.putString("id", data.getSp_id());
                bundle.putString("icon", data.getSp_tupian());
                bundle.putString("title", data.getSp_mingcheng());
                bundle.putString("money", data.getSp_danjia());
                bundle.putString("extra", "");

                ChatUiIM.getInstance().sendShangpin(bundle);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        L.e("-requestSuccess-", "response" + response.toString());
        JSONArray jsonArray = response.getJSONArray("商品");
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                DingdanXiangQingSPBean spBean = new DingdanXiangQingSPBean();
                spBean.setSp_type(jsonObject.getString("类型"));
                spBean.setSp_id(jsonObject.getString("商品id"));
                spBean.setSp_shuliang(jsonObject.getString("商品数量"));
                spBean.setSp_guige(jsonObject.getString("商品规格"));
                spBean.setSp_tupian(jsonObject.getString("缩略图"));
                spBean.setSp_mingcheng(jsonObject.getString("商品名称"));
                spBean.setSp_danjia(jsonObject.getString("价格"));
                spBean.setSp_status(jsonObject.getString("状态"));
                spBean.setId(jsonObject.getString("id"));

                shangpinList.add(spBean);
            }

            if (shangpinList.size() < 1) {
                rlNoProduct.setVisibility(View.VISIBLE);
            } else {
                rlNoProduct.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置数据
     */
    private void setAdapter() {
        adapter = new CommonAdapter<DingdanXiangQingSPBean>(this, R.layout.item_dynamic_product_choice, shangpinList) {
            @Override
            public void convert(ViewHolder holder, DingdanXiangQingSPBean dingdanXiangQingSPBean, final int position) {
                holder.setText(R.id.tv_dynamic_product_name, dingdanXiangQingSPBean.getSp_mingcheng());
                holder.setText(R.id.tv_dynamic_product_price, "￥ " + dingdanXiangQingSPBean.getSp_danjia());
                ImageView picView = holder.getView(R.id.iv_dynamic_product_picture);
                Glide.with(mContext).load(dingdanXiangQingSPBean.getSp_tupian()).into(picView);

                if (selectPosition == position) {
                    holder.setImageResource(R.id.iv_dynamic_product_choice, R.mipmap.icon_the_selected);
                } else {
                    holder.setImageResource(R.id.iv_dynamic_product_choice, R.mipmap.icon_not_to_choose);
                }

                holder.setOnClickListener(R.id.iv_dynamic_product_choice, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition = position;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lvShangpinglianjie.setAdapter(adapter);
    }

}
