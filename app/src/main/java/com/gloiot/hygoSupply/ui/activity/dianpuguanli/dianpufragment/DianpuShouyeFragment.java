package com.gloiot.hygoSupply.ui.activity.dianpuguanli.dianpufragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DianpuShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class DianpuShouyeFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.gv_dianpu_shouye)
    GridView gvDianpuShouye;
    private String title = "首页", phone = "", hengfu = "";
    private CommonAdapter commonAdapter;
    private ArrayList<DianpuShangpinBean> allDianpuShangpin = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dianpu_shouye, container, false);
        ButterKnife.bind(this, view);

        if (phone.equals("")) {
            phone = preferences.getString(ConstantUtils.SP_MYID, "");
        }
        requestHandleArrayList.add(requestAction.shop_sj_sy(DianpuShouyeFragment.this, phone, title, hengfu));
        return view;
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_sy:
                Log.e("首页状态请求成功", response.toString());
                if (response.getString("状态").equals("成功")) {
                    if (!response.getString("条数").equals("0")) {
                        allDianpuShangpin.clear();
                        JSONArray jsonArray = response.getJSONArray("列表");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            response = (JSONObject) jsonArray.get(i);

                            DianpuShangpinBean dianpuShangpin = new DianpuShangpinBean(response.getString("id"),
                                    response.getString("缩略图"),
                                    response.getString("商品名称"),
                                    response.getString("建议零售价"));
                            if ("全球购".equals(response.getString("类型")) || "全球购-自营".equals(response.getString("类型"))) {
                                dianpuShangpin.setQuanqiugou(true);
                            }
                            allDianpuShangpin.add(dianpuShangpin);
                        }
                        commonAdapter = new CommonAdapter<DianpuShangpinBean>(this.getActivity(), R.layout.item_dianpu_shangpin, allDianpuShangpin) {
                            @Override
                            public void convert(final ViewHolder holder, final DianpuShangpinBean dianpuShangpinBean) {
                                ImageView iv_item_dingdan_guanli_suolvetu = holder.getView(R.id.iv_item_dianpu_shangpin_tupian);
                                CommonUtils.setDisplayImageOptions(iv_item_dingdan_guanli_suolvetu, dianpuShangpinBean.getSuolvetu(), 4);
                                holder.setText(R.id.tv_item_dianpu_shangpin_mingcheng, dianpuShangpinBean.getShangpinmingcheng());
                                holder.setText(R.id.tv_item_dianpu_shangpin_danjia, dianpuShangpinBean.getDanjia());
                                holder.setVisible2(R.id.iv_item_dianpu_shangpin_quanqiugou, dianpuShangpinBean.isQuanqiugou());
                            }
                        };
                        gvDianpuShouye.setAdapter(commonAdapter);
                    }
                }
                break;
        }
    }


    @OnItemClick({R.id.gv_dianpu_shouye})
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getActivity(), ShangpinDianpuActivity.class);
        intent.putExtra("id", allDianpuShangpin.get(position).getId());
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

