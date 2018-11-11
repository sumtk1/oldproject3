package com.gloiot.hygoSupply.ui.activity.wode.tuihuodizhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退货地址.
 */

public class TuiHuoDiZhiActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rv_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.ll_shouhuodizhitubiao)
    LinearLayout llShouhuodizhitubiao;
    @Bind(R.id.btn_addition)
    Button btnAddition;
    private ShouhuoAddress shouhuoAddress;
    private List<ShouhuoAddress> addressBeanList = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int deletePosition = -1;//删除的位置
    private int previousPosition = -1;//之前默认地址的位置,用于局部刷新
    private int setPosition = -1;//当前改动的位置
    private String requestType;//请求类型，取值：show/del/edit/add（这里用到前三个）
    private String addressState;//地址类型：是否是默认地址
    private boolean isResetDefaultOK = true;//是否满足设置默认地址的条件（当默认地址状态为否时，只有条目不变时，才满足条件）

    @Override
    public int initResource() {
        return R.layout.activity_tuihuodizhi;
    }

    @Override
    public void onResume() {
        super.onResume();
        addressBeanList.clear();
        requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(TuiHuoDiZhiActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), shouhuoAddress, "show"));
        requestType = "show";
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "退货地址", "");
        shouhuoAddress = new ShouhuoAddress("", "", "", "", "", "", "", "", "");
    }

    @OnClick({R.id.btn_addition})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addition:
                Intent intent = new Intent(TuiHuoDiZhiActivity.this, TianJiaTuiHuoDiZhiActivity.class);
                intent.putExtra("type", "添加");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_send:
                if (response.getString("状态").equals("成功")) {
                    if (requestType.equals("show")) {   //查询发货地址
                        JSONArray jsonArray = response.getJSONArray("列表");
                        int num = jsonArray.length();
                        if (num != 0) {
                            btnAddition.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            llShouhuodizhitubiao.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (jsonObject.getString("状态").equals("默认地址")) {
                                    addressState = "是";
                                } else if (jsonObject.getString("状态").equals("显示")) {
                                    addressState = "否";
                                }
                                shouhuoAddress = new ShouhuoAddress(jsonObject.getString("id"),
                                        jsonObject.getString("收货人"),
                                        jsonObject.getString("手机号"),
                                        jsonObject.getString("省"),
                                        jsonObject.getString("市"),
                                        jsonObject.getString("区"),
                                        jsonObject.getString("地址"),
                                        addressState,
                                        jsonObject.getString("收货地区")
                                );
                                addressBeanList.add(shouhuoAddress);
                            }
                            setAdapter();
                        } else {
                            btnAddition.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            llShouhuodizhitubiao.setVisibility(View.VISIBLE);
                        }
                    } else if (requestType.equals("del")) {//删除收货地址
                        if (deletePosition != -1) {
                            addressBeanList.remove(deletePosition);
                            if (addressBeanList.size() == 0) {
                                mRecyclerView.setVisibility(View.GONE);
                                llShouhuodizhitubiao.setVisibility(View.VISIBLE);
                                btnAddition.setVisibility(View.VISIBLE);
                            } else {
                                commonAdapter.notifyDataSetChanged();
                            }
                        }
                        deletePosition = -1;
                    } else if (requestType.equals("edit")) {//更改默认地址
                        if (previousPosition != -1) {
                            if (previousPosition != setPosition) {
                                addressBeanList.get(previousPosition).setDefaultAddress("否");
                                commonAdapter.notifyItemChanged(previousPosition);
                                isResetDefaultOK = false;
                            } else {
                                isResetDefaultOK = true;
                            }
                        }
                        previousPosition = setPosition;
                    }
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                requestType = "";
                break;

        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<ShouhuoAddress>(mContext, R.layout.item_shouhuodizhi, addressBeanList) {
            @Override
            public void convert(final ViewHolder holder, final ShouhuoAddress addressBean) {
                holder.setText(R.id.tv_name, addressBean.getShouhuoren());
                holder.setText(R.id.tv_phonenum, addressBean.getPhoneNum());
                holder.setText(R.id.tv_address, addressBean.getShouhuoArea() + addressBean.getDetailedAddress());
//                if (addressBean.getDefaultAddress().equals("是")) {
//                    holder.setChecked(R.id.cb_set_default, true);
//                    holder.setText(R.id.tv_default, "默认地址");
//                    previousPosition = holder.getmPosition();
//                } else if (addressBean.getDefaultAddress().equals("否")) {
//                    holder.setChecked(R.id.cb_set_default, false);
//                    holder.setText(R.id.tv_default, "设为默认");
//                }
                TextView delete = holder.getView(R.id.tv_delete);
                TextView edit = holder.getView(R.id.tv_edit);
                RelativeLayout rlSetDefault = holder.getView(R.id.rl_checkbox_set_default);
                final CheckBox cbSetDefault = holder.getView(R.id.cb_set_default);
                rlSetDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cbSetDefault.isChecked()) {
                            isResetDefaultOK = true;//主动点击时，重置该状态，注意要先这样设置才能去更改checkbox勾选状态
                            cbSetDefault.setChecked(false);
                        } else {
                            cbSetDefault.setChecked(true);
                        }
                    }
                });
                cbSetDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setPosition = holder.getmPosition();
                        if (buttonView.isChecked()) {

                        } else {
                            isResetDefaultOK = true;//主动点击时，重置该状态，注意要先这样设置才能去更改checkbox勾选状态
                        }
                        if (isChecked) {
                            holder.setText(R.id.tv_default, "默认地址");
                            addressBean.setDefaultAddress("是");
                            requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(TuiHuoDiZhiActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), addressBean, "edit"));
                            requestType = "edit";
                        } else {
                            holder.setText(R.id.tv_default, "设为默认");
                            addressBean.setDefaultAddress("否");
                            if (previousPosition == setPosition && isResetDefaultOK) {
                                requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(TuiHuoDiZhiActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), addressBean, "edit"));
                                requestType = "edit";
                            }
                        }
                        Log.e("准备更改默认地址", "previousPosition==" + previousPosition + ",listsize=" + addressBeanList.size() +
                                ",更改的位置" + holder.getmPosition() + ",setPosition=" + setPosition);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.twoBtnNormal(mContext, "确认删除该地址", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                deletePosition = holder.getmPosition();
                                requestHandleArrayList.add(requestAction.getTuiHuoDiZhi(TuiHuoDiZhiActivity.this, preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), addressBean, "del"));
                                requestType = "del";
                                Log.e("准备删除", "deleteposition==" + deletePosition + ",listsize=" + addressBeanList.size());
                            }
                        });
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TuiHuoDiZhiActivity.this, TianJiaTuiHuoDiZhiActivity.class);
                        intent.putExtra("type", "编辑");
                        intent.putExtra("addressInfo", addressBean);
                        startActivity(intent);
                    }
                });

            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(commonAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
